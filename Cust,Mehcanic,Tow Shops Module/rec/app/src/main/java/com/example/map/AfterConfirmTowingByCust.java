package com.example.map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.map.databinding.ActivityAfterConfirmTowingByCustBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AfterConfirmTowingByCust extends FragmentActivity implements OnMapReadyCallback {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    double latitudee, longitudee;
    String namee, phone, cnic;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    String latt, lang;
    String latt_m, lang_m;
    LocationManager locationManager;
    public double i, j,startLat,startLong;
    public double k,endLat,endLong,distanceInMeters;
    private ActivityAfterConfirmTowingByCustBinding binding;
    List<Address> listGeoCoder;
    private ValueEventListener valueEventListener;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    Location location;
    String keyRoot;
    String categoryName;
    int n;
    long duration;
    double chargesforServices;
    String distanceinKM,formattedDistanceInMeters;
    String shopId,availedAddress,servicesAvailed;
    FirebaseAuth auth;
    String currentLoginUser;
    Button serviceDone;
    String services,distanceBill;
    double charges;
    String time;
    FloatingActionButton shareloc;
    FloatingActionButton alertCall;

    public static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
    private static final int Location_permission_code = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterConfirmTowingByCustBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        shareloc = findViewById(R.id.shareLocationT);
        alertCall= findViewById(R.id.alertcallT);
        serviceDone=findViewById(R.id.service_donebyCust);

        currentLoginUser = auth.getInstance().getCurrentUser().getUid();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        Intent intent=getIntent();
//        duration=intent.getLongExtra("durationinMin",0);
        shopId=intent.getStringExtra("shopId");


        serviceDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();

                myRef.child("History").child(currentLoginUser).child(shopId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("Services Availed")) {
                            dismissProgressDialog();
                            services = snapshot.child("Services Availed").getValue().toString();
                            charges = Double.parseDouble(snapshot.child("Service Charges").getValue().toString());
                            distanceBill = snapshot.child("distance").getValue().toString();
                            time = snapshot.child("time").getValue().toString();
                            String address=snapshot.child("Address").getValue().toString();
                            double totalCharges=Double.parseDouble(snapshot.child("Total Charges").getValue().toString());


                            Intent intent1 = new Intent(AfterConfirmTowingByCust.this, cust_BIll.class);
                            intent1.putExtra("shopId", shopId);
                            intent1.putExtra("chargesBill",charges);
                            intent1.putExtra("tchargesBill",totalCharges);
                            intent1.putExtra("serviceBill",services);
                            intent1.putExtra("distanceBill",distanceBill);
                            intent1.putExtra("timeBill",time);
                            intent1.putExtra("addressBill",address);
                            startActivity(intent1);
                            finish();

                          //  Toast.makeText(AfterConfirmTowingByCust.this, "Alhamdulilah" + services + "charges" + charges + "d" + distanceBill + "tiume" + time, Toast.LENGTH_LONG).show();
                        }
                        else
                            showProgressDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();
        }

// share button
        shareloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AfterConfirmTowingByCust.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                shareLocation(location);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AfterConfirmTowingByCust.this);
                                builder.setTitle("Location Alert")
                                        .setIcon(R.drawable.ic_alert_error_msg)
                                        .setMessage("please on the location");
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
            }
        });

        alertCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "15";
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.afterconfirmmapforcust);
        mapFragment.getMapAsync(this);
        //    return View;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        // Remove the value event listener if it's still active
//        if (myRef != null && valueEventListener != null) {
//            myRef.removeEventListener(valueEventListener);
//        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Get the latitude and longitude
                        latitudee = location.getLatitude();
                        longitudee = location.getLongitude();
                        LatLng current = new LatLng(latitudee, longitudee);

                        mMap.addMarker(new MarkerOptions().position(current).title("Current"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,17));

                        myRef.child("History").child(currentLoginUser).child(shopId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                String shopLatt = snapshot.child("tow_end_latitude").getValue().toString();

                                String  shoplangg =  snapshot.child("tow_end_longitude").getValue().toString();
                                double l1 = Double.parseDouble(shopLatt);
                                double l2= Double.parseDouble(shoplangg);
                                LatLng Shop = new LatLng(l1, l2); //model town lat, lng
                                mMap.addMarker(new MarkerOptions().position(Shop).title("Towing_Shop").icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_tow_truck_tow_away_svgrepo_com__2_)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Shop,13));

                PolylineOptions options = new PolylineOptions().add(current).add(Shop).width(7).color(Color.RED).geodesic(true);
                mMap.addPolyline(options);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database error here
                            }
                        });
                    }
                }
            });
        }


        //to set current location
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {            requestLocationPermission();
            //  requestLocationPermission();
            mMap.setMyLocationEnabled(true);
        }
    }
    private void shareLocation(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        String uri = "http://maps.google.com/maps?q=" +latitude+","+longitude;

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String ShareSub = "Here is my location";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, ShareSub);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share Location via"));
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Location_permission_code);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Location_permission_code);
            }
            return false;
        } else {

            return true;
        }
    }
    private boolean isLocationPermissionGranted(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else{
            return false;
        }
    }
    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Location_permission_code);
        //  locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId)
    {
        Drawable vectorDrawable=ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Exit Alert")
                .setIcon(R.drawable.ic_alert_error_msg)
                .setMessage("You cannot exit!")  ;

        builder.setNegativeButton("ok", null);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}