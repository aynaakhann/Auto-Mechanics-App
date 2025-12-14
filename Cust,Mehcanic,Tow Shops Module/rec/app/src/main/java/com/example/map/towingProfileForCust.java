package com.example.map;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class towingProfileForCust extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    FirebaseAuth auth;
    String currentState="nothing_happen";
    Button sendRequestBtn;
    String ShopId,ShopDistance,ShopEmail,ShopPhone,ownerName,ShopName;
    Location location;
    double latitude,longitude;
    private FusedLocationProviderClient fusedLocationClient;
    FloatingActionButton callTowing;
    String currentLoginUser;
    long startTime = 0;
    long endTime = 0;
    public static final Integer NF_ID =01 ;
    public static final String REPLY ="Text_message" ;//name for remote input
    private static final String CHANNEL_ID ="Channel_01" ;
    private CountDownTimer timer;
    public static final Integer NF_ID2 =02 ;
    //public static final String REPLY ="Text_message" ;//name for remote input
    private static final String CHANNEL_ID2 ="Channel_02" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towing_profile_for_cust);

        TextView shop_name=findViewById(R.id.shop_name);
        TextView owner_name=findViewById(R.id.ownerNameForReqProfile);
        //   ImageView mechimg=findViewById(R.id.mech_image);
        TextView shop_distance=findViewById(R.id.shopDistanceForReqProfile);
        TextView shop_phone=findViewById(R.id.ownerPhoneForReqProfile);
        TextView shop_email=findViewById(R.id.ownerEmailForReqProfile);
        TextView rating=findViewById(R.id.rating);
        sendRequestBtn=findViewById(R.id.sendrequestbtn);
        callTowing=findViewById(R.id.callTowing);

        currentLoginUser = auth.getInstance().getCurrentUser().getUid();

        Intent intent=getIntent();

        ShopId = intent.getStringExtra("shop_id");
        ShopDistance = intent.getStringExtra("distance");
        ShopEmail = intent.getStringExtra("shopEmail");
        ShopPhone = intent.getStringExtra("shopPhone");
        ownerName = intent.getStringExtra("owner_name");
        ShopName = intent.getStringExtra("shop_name");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Get the latitude and longitude
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                    }

                }
            });
        }
        Query q=FirebaseDatabase.getInstance().getReference("Ratings");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("final_tow_rating").child(ShopId).exists()) {
                    String ratings=snapshot.child("final_tow_rating").child(ShopId).child("rating").getValue().toString();
                    if(TextUtils.isEmpty(ratings)){
                        rating.setText("N/A");
                    }else{
                        rating.setText(ratings);
                    }
                }
                else{
                    rating.setText("N/A");
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        shop_name.setText(ShopName);
        shop_distance.setText("Distance\n"+ShopDistance);
        owner_name.setText("Owner Name\n"+ownerName);
        shop_email.setText("Shop Email\n"+ShopEmail);
        shop_phone.setText("PhoneNumber\n"+ShopPhone);



        callTowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + ShopPhone));
                startActivity(callIntent);
            }
        });

        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer = new CountDownTimer(120000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // This method will be called every second until the timer is finished
                     //   currentLoginUser = auth.getInstance().getCurrentUser().getUid();

                    }

                    @Override
                    public void onFinish() {
                        // Perform the particular action here

                        myRef.child("TowingDecline").child(currentLoginUser).child(ShopId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild("status")){
                                    addNotification2();
                                    sendRequestBtn.setText("Send Request");
                                    myRef.child("TowingDecline").child(currentLoginUser).child(ShopId).child("status").removeValue();
                                    Intent intent=new Intent(towingProfileForCust.this,MapsActivity.class);
                                    timer.cancel();
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef.child("History").child(currentLoginUser).child(ShopId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("request_status")) {
                                    addNotification();
                                    sendRequestBtn.setText("Send Request");
                                    timer.cancel();
                                    Intent intent=new Intent(towingProfileForCust.this,cust_confirm_towing.class);
                                    intent.putExtra("shopID",ShopId);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(towingProfileForCust.this, "no req accept", Toast.LENGTH_SHORT).show();
                                    myRef.child("requests").child(ShopId).child(currentLoginUser).removeValue();
                                    sendRequestBtn.setText("Send Request");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                };

                sendReq(ShopId);
                timer.start();

            }
        });//sendReqbtn end


        myRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
      //  for(DataSnapshot dataSnapshot:snapshot.getChildren()){
           // String table=dataSnapshot.getKey();
         //   if(table.equals("requests")){
        if(snapshot.hasChild("requests")){
                myRef.child("requests").child(ShopId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                      //  for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                       //     String keyRoot=dataSnapshot.getKey();
                         //   if(keyRoot.equals(currentLoginUser)){
                        if(snapshot1.hasChild(currentLoginUser)){
                                sendRequestBtn.setText("Cancel Request");
                            }
//                    else
//                    {
//                        Toast.makeText(towingProfileForCust.this, "No req exists", Toast.LENGTH_SHORT).show();
//                    }
                      //  }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
//        else{
//            Toast.makeText(towingProfileForCust.this, "no request table", Toast.LENGTH_SHORT).show();
//        }
       // }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

    }//onCreate


    private void sendReq(String shopId) {

        if (sendRequestBtn.getText().toString().equals("Cancel Request")) {
            myRef.child("requests").child(shopId).child(currentLoginUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(towingProfileForCust.this, "You have cancelled a Request", Toast.LENGTH_SHORT).show();
//                        currentState="nothing_happen";
                        timer.cancel();
                        sendRequestBtn.setText("Send Request");
                    } else {
                        Toast.makeText(towingProfileForCust.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            myRef.child("user").child(currentLoginUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child("username").getValue().toString();
                    String uid = snapshot.child("uid").getValue().toString();
                    String ratings=snapshot.child("Trating").getValue(String.class);

                    myRef.child("requests").child(ShopId).child(currentLoginUser).child("username").setValue(username);
                    myRef.child("requests").child(ShopId).child(currentLoginUser).child("uid").setValue(uid);
                    myRef.child("requests").child(ShopId).child(currentLoginUser).child("distance").setValue(ShopDistance);
                    myRef.child("requests").child(ShopId).child(currentLoginUser).child("latitude").setValue(latitude);
                    myRef.child("requests").child(ShopId).child(currentLoginUser).child("longitude").setValue(longitude);
                    myRef.child("requests").child(ShopId).child(currentLoginUser).child("ratings").setValue(ratings);
//                    HashMap hashMap = new HashMap();
////            hashMap.put("status", "pending");
//                    hashMap.put("distance", ShopDistance);
//                    hashMap.put("latitude", latitude);
//                    hashMap.put("longitude", longitude);

                    Toast.makeText(towingProfileForCust.this, "You have sent a Request", Toast.LENGTH_SHORT).show();
                    sendRequestBtn.setText("Cancel Request");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
//            HashMap hashMap = new HashMap();
////            hashMap.put("status", "pending");
//            hashMap.put("distance", ShopDistance);
//            hashMap.put("latitude", latitude);
//            hashMap.put("longitude", longitude);

////            currentLoginUser = auth.getInstance().getCurrentUser().getUid();

//                myRef.child("requests").child(shopId).child(currentLoginUser).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(towingProfileForCust.this, "You have sent a Request", Toast.LENGTH_SHORT).show();
//                         //  timer.start();
////                        currentState="i_sent_pending";
//                            sendRequestBtn.setText("Cancel Request");
//
//                        } else {
//                            Toast.makeText(towingProfileForCust.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        }
    }
    private void addNotification() {
        notificationchannel();
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_tow_truck_tow_away_svgrepo_com__2_) //set icon for notification
                .setContentTitle("Notifications") //set title of notification
                .setContentText(""+ShopName+" Accepted your request\uD83E\uDD17")//this is notification message
                .setAutoCancel(true) // makes auto cancel of notification
                .setPriority(Notification.PRIORITY_DEFAULT);

// Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NF_ID, builder.build());
    }
    private void notificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name= "my_channel";
//channel description for developers understanding
            // String description = getString();
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("accept request notification");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void addNotification2() {
        notificationchannel2();
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this,CHANNEL_ID2)
                .setSmallIcon(R.drawable.ic_tow_truck_tow_away_svgrepo_com__2_) //set icon for notification
                .setContentTitle("Notifications") //set title of notification
                .setContentText(""+ShopName+" Declined your request\uD83D\uDE1F")//this is notification message
                .setAutoCancel(true) // makes auto cancel of notification
                .setPriority(Notification.PRIORITY_DEFAULT);
// Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NF_ID2, builder.build());
    }

    private void notificationchannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name= "my_channell";
//channel description for developers understanding
            // String description = getString();
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID2, name, importance);
            channel.setDescription("decline request notification");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
