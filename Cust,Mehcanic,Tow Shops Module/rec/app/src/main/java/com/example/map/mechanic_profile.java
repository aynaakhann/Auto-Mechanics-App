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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class mechanic_profile extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    FirebaseAuth auth;
    String currentState="nothing_happen";
    Button sendRequestBtn;
    String mech_Id,mech_Distance,mech_Email,mech_Phone,mech_Name,mech_Age,mech_Img;
    Location location;
    Double latitude,longitude;
    private FusedLocationProviderClient fusedLocationClient;
    String currentLoginUser;
    FloatingActionButton call_mech;
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
        setContentView(R.layout.activity_mechanic_profile);

        TextView mech_name=findViewById(R.id.mech_name);
        TextView mech_phone=findViewById(R.id.mechPhoneForReqProfile);
        ImageView mechimg=findViewById(R.id.mech_image);
        TextView mech_distance=findViewById(R.id.mechDistanceForReqProfile);
        TextView mech_email=findViewById(R.id.mechEmailForReqProfile);
        TextView mech_age=findViewById(R.id.mechAgeForReqProfile);
        TextView rating=findViewById(R.id.rating);

        call_mech=findViewById(R.id.callMechanic);

       sendRequestBtn=findViewById(R.id.sendReqToMechbtn);

       currentLoginUser = auth.getInstance().getCurrentUser().getUid();

        Intent intent=getIntent();
        mech_Id = intent.getStringExtra("mech_Id");
        mech_Distance = intent.getStringExtra("mech_Distance");

        mech_Email = intent.getStringExtra("mech_Email");
        mech_Phone = intent.getStringExtra("mech_Phone");
        mech_Name = intent.getStringExtra("mech_Name");
        mech_Age = intent.getStringExtra("mech_Age");
        mech_Img= intent.getStringExtra("mech_Img");

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


        //ratings

        Query q=FirebaseDatabase.getInstance().getReference("Ratings");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("final_mech_rating").child(mech_Id).exists()) {
                    String ratings=snapshot.child("final_mech_rating").child(mech_Id).child("rating").getValue().toString();
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

        mech_name.setText(mech_Name);
        mech_distance.setText("Distance\n"+mech_Distance);
        mech_phone.setText("PhoneNumber\n"+mech_Phone);
        mech_email.setText("Mech Email\n"+mech_Email);
        mech_age.setText("Mech Age\n"+mech_Age);
        Picasso.get().load(mech_Img).into(mechimg);

        call_mech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + mech_Phone));
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
                    }

                    @Override
                    public void onFinish() {
                        // Perform the particular action here

                        myRef.child("MechDecline").child(currentLoginUser).child(mech_Id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild("status")){
                                    addNotification2();
                                    timer.cancel();
                                    sendRequestBtn.setText("Send Request");
                                    Intent intent=new Intent(mechanic_profile.this,MapsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef.child("HistoryM").child(currentLoginUser).child(mech_Id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("request_status")) {
                                    addNotification();
                                    timer.cancel();
                                    sendRequestBtn.setText("Send Request");
                                    Intent intent=new Intent(mechanic_profile.this,cust_confirm_mechanic.class);
                                    intent.putExtra("mechID",mech_Id);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(mechanic_profile.this, "no req accept", Toast.LENGTH_SHORT).show();
                                    //  myRef.child("requests").child(ShopId).child(currentLoginUser).removeValue();
                                    sendRequestBtn.setText("Send Request");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                };
                sendReq(mech_Id);
                timer.start();
            }
        });//sendReqbtn end

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("mech_requests")){
                    myRef.child("mech_requests").child(mech_Id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            if(snapshot1.hasChild(currentLoginUser)){
                                sendRequestBtn.setText("Cancel Request");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
//                else{
//                    Toast.makeText(mechanic_profile.this, "no request table", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }//onCreate

    private void sendReq(String mechId) {

        if (sendRequestBtn.getText().toString().equals("Cancel Request")) {
            myRef.child("mech_requests").child(mechId).child(currentLoginUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(mechanic_profile.this, "You have cancelled a Request", Toast.LENGTH_SHORT).show();
//                        currentState="nothing_happen";
                        timer.cancel();
                        sendRequestBtn.setText("Send Request");
                    } else {
                        Toast.makeText(mechanic_profile.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
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

                    myRef.child("mech_requests").child(mech_Id).child(currentLoginUser).child("username").setValue(username);
                    myRef.child("mech_requests").child(mech_Id).child(currentLoginUser).child("uid").setValue(uid);
                    myRef.child("mech_requests").child(mech_Id).child(currentLoginUser).child("distance").setValue(mech_Distance);
                    myRef.child("mech_requests").child(mech_Id).child(currentLoginUser).child("latitude").setValue(latitude);
                    myRef.child("mech_requests").child(mech_Id).child(currentLoginUser).child("longitude").setValue(longitude);
                    myRef.child("mech_requests").child(mech_Id).child(currentLoginUser).child("ratings").setValue(ratings);

                    Toast.makeText(mechanic_profile.this, "You have sent a Request", Toast.LENGTH_SHORT).show();
                    sendRequestBtn.setText("Cancel Request");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void addNotification() {
        notificationchannel();
        //image converter
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.mechanic_icon) //set icon for notification
                .setContentTitle("Notifications") //set title of notification
                .setContentText(""+mech_Name+" Accepted your request\uD83E\uDD17")//this is notification message
                .setAutoCancel(true) // makes auto cancel of notification
                .setPriority(Notification.PRIORITY_DEFAULT);
// Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NF_ID, builder.build());
    }

    private void notificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name= "my_channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("accept request by mech");
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
                .setSmallIcon(R.drawable.mechanic_icon) //set icon for notification
                .setContentTitle("Notifications") //set title of notification
                .setContentText(""+mech_Name+" Declined your request\uD83D\uDE1F")//this is notification message
                .setAutoCancel(true) // makes auto cancel of notification
                .setPriority(Notification.PRIORITY_DEFAULT);
// Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NF_ID2, builder.build());
    }

    private void notificationchannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name= "my_channell";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID2, name, importance);
            channel.setDescription("declined req by mech");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}










//float[] results = new float[1];
//Location.distanceBetween(lat1, lng1, lat2, lng2, results);
//float distance = results[0];
//
//String distanceString = Float.toString(distance) + " meters";
//float distanceInKilometers = distance / 1000;
//    String distanceString = Float.toString(distanceInKilometers) + " kilometers";
