package com.example.map;

import static com.example.map.cust_mapFragment.Location_permission_code;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cust_mainHome extends AppCompatActivity {
    public double latitudee, longitudee;
    private static final int Location_permission_code = 101;
    // GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient fusedLocationClient;
    public static final Integer NF_ID =01 ;
    public static final String REPLY ="Text_message" ;//name for remote input
    private static final String CHANNEL_ID ="Channel_01" ;
    FirebaseAuth auth;
    String currentLoginUser;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_main_home);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(cust_mainHome.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // isLocationPermissionGranted();
            checkLocationPermission();
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Get the latitude and longitude
                        latitudee = location.getLatitude();
                        longitudee = location.getLongitude();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(cust_mainHome.this);
                        builder.setTitle("Location Alert")
                                .setIcon(R.drawable.ic_alert_error_msg)
                                .setMessage("please on the location to get better services!");
                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        builder.create();
                        builder.show();

                    }
                }
            });
        }
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();
//        myRef.child("user").child(currentLoginUser).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//        //    String a=    snapshot.child(currentLoginUser).child("request").getValue().toString();
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    String keyRoot=dataSnapshot.getKey();
//                    if(keyRoot.equals("request")){
//
//                    addNotification();
//
//
//                }
//                else{
//                    Toast.makeText(cust_mainHome.this, "no req accept", Toast.LENGTH_SHORT).show();
//                }
//            }}
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);

        replaceFragment(new homeFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home)
                {
                    replaceFragment(new homeFragment());
                }
                else if (id == R.id.Activities)
                {
                    replaceFragment(new actFragment());
                }
                else if (id == R.id.Profile)
                {
                    replaceFragment(new profileFragment());
                }
                else if (id == R.id.Setting)
                {
                    replaceFragment(new settingsFragment());
                }
                return true;
            }
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentmanager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentmanager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
//    private void addNotification() {
//        notificationchannel();
////        Intent notificationIntent = new Intent(this, notification_Layout.class);
////        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        //notification message will get at Notificationlayout
////        notificationIntent.putExtra("message", "This is a notification message");
////        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
////
////        //Adding button to your notification
////        Intent noIntent = new Intent(this, notification_Layout.class);
////        noIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        //notification message will get at Notificationlayout
////        PendingIntent nopendingIntent = PendingIntent.getActivity(this, 0, noIntent, PendingIntent.FLAG_ONE_SHOT);
////        //Adding button to your notification
////        Intent yesIntent = new Intent(this, notification_Layout.class);
////        noIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        //notification message will get at Notificationlayout
////        PendingIntent yespendingIntent = PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_ONE_SHOT);
//
//        //image converter
//        Drawable drawable= ResourcesCompat.getDrawable(getResources(),R.drawable.regpic2,null);
//        BitmapDrawable bitmapDrawable=(BitmapDrawable) drawable;
//        Bitmap largeicon=bitmapDrawable.getBitmap();
//        NotificationCompat.Builder builder;
//        builder = new NotificationCompat.Builder(this,CHANNEL_ID)
//                .setSmallIcon(R.drawable.car) //set icon for notification
//                .setLargeIcon(largeicon)
//                .setContentTitle("Notifications Example") //set title of notification
//                .setContentText("This is a notification message")//this is notification message
//                .setAutoCancel(true) // makes auto cancel of notification
//                .setPriority(Notification.PRIORITY_DEFAULT);
////        builder.setContentIntent(pendingIntent);
////        builder.addAction(R.drawable.car,"yes",yespendingIntent);
////        builder.addAction(R.drawable.car,"No",nopendingIntent);
//        //for adding quick reply with notification
//      /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//           RemoteInput remoteInput=new RemoteInput.Builder(REPLY).setLabel("Reply").build();
//
//Intent replyintent=new Intent(this,MainActivity2.class);
//replyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//PendingIntent rpendingintent=PendingIntent.getActivity(this,0,replyintent,PendingIntent.FLAG_ONE_SHOT);
//
//NotificationCompat.Action action;
//            action = new NotificationCompat.Action.Builder(R.drawable.ic_del,
//                    "Reply",rpendingintent).addRemoteInput(remoteInput).build();
//            builder.addAction(action);}*/
//
//// Add as notification
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(NF_ID, builder.build());
//    }
//
//    private void notificationchannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String name= "my_channel";
////channel description for developers understanding
//           // String description = getString();
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription("jgf");
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(cust_mainHome.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(cust_mainHome.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(cust_mainHome.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Location_permission_code);
            } else {
                ActivityCompat.requestPermissions(cust_mainHome.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Location_permission_code);
            }
            return false;
        } else {

            return true;
        }
    }
}