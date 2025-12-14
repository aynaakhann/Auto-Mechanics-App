package com.example.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.map.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    double latitudee, longitudee;
    String namee, phone, cnic;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    String latt, lang;
    String latt_m, lang_m;
    LocationManager locationManager;
    public double i, j;
    public double k, l;
    private ActivityMapsBinding binding;
    List<Address> listGeoCoder;
    Location location;
    String keyRoot;
    String categoryName;
    int n;
    private static final int Location_permission_code = 101;
    public static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        n = 0;
//
//        Intent intent=getIntent();
//        categoryName=intent.getStringExtra("categoryName");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //    return View;
    }
    public static double calculateDistance(double startLat, double startLong, double endLat, double endLong) {
        double latDistance = Math.toRadians(endLat - startLat);
        double lonDistance = Math.toRadians(endLong - startLong);
        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2));
        double c = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));

        return (AVERAGE_RADIUS_OF_EARTH_KM * c);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng Pakistan = new LatLng(30.3894, 69.3532);
//        mMap.addMarker(new MarkerOptions().position(Pakistan).title(" Pakistan"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pakistan));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {

                        myRef.child("towing_locations").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Loop through the data and add markers to the map
                                mMap.clear();
                                // Get the latitude and longitude
                                latitudee = location.getLatitude();
                                longitudee = location.getLongitude();
                                LatLng current = new LatLng(latitudee, longitudee);
                                mMap.addMarker(new MarkerOptions().position(current).title("Current"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,10));
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    // Retrieve the data for this marker
                                    keyRoot=childSnapshot.getKey();
                                    String markerTitle = childSnapshot.child("owner_name").getValue(String.class);
                                    double markerLat = childSnapshot.child("latitude").getValue(Double.class);
                                    double markerLng = childSnapshot.child("longitude").getValue(Double.class);

                                    double distance= calculateDistance(markerLat, markerLng,latitudee, longitudee);
//                                        String distanceinKM=String.format("%.2f", distance);
//                                        double distanceInMeters = Double.parseDouble(distanceinKM) * 1000;
                                    double distanceInMeters = distance * 1000;
                                    String distanceinM=String.format("%.2f", distanceInMeters);

                                    if (Double.parseDouble(distanceinM) <= 3000){ //Radius 3km

                                        // Add the marker to the map and set its tag to the Firebase data key
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(markerLat, markerLng))
                                                .title(keyRoot).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_tow_truck_tow_away_svgrepo_com__2_)));
                                        marker.setTag(childSnapshot.getKey());
                                        Toast.makeText(MapsActivity.this, "within your 3km , mechanic is available", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (Double.parseDouble(distanceinM) <= 5000){ //Radius 5km

                                        // Add the marker to the map and set its tag to the Firebase data key
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(markerLat, markerLng))
                                                .title(keyRoot).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_tow_truck_tow_away_svgrepo_com__2_)));
                                        marker.setTag(childSnapshot.getKey());
                                        Toast.makeText(MapsActivity.this, "within your 5km , Tow service provider is available", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (Double.parseDouble(distanceinM) <= 10000){ //Radius 10km

                                        // Add the marker to the map and set its tag to the Firebase data key
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(markerLat, markerLng))
                                                .title(keyRoot).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_tow_truck_tow_away_svgrepo_com__2_)));
                                        marker.setTag(childSnapshot.getKey());
                                        Toast.makeText(MapsActivity.this, "within your 10km , Tow service provider is available", Toast.LENGTH_SHORT).show();
                                    }
                                    else {

                                        // Add the marker to the map and set its tag to the Firebase data key
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(markerLat, markerLng))
                                                .title(keyRoot).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_tow_truck_tow_away_svgrepo_com__2_)));
                                        marker.setTag(childSnapshot.getKey());
                                        Toast.makeText(MapsActivity.this, " Tow service provider is available but is far", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database error here
                            }
                        });

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                String markerKey = (String) marker.getTag();
                                if (markerKey != null) {


                                    myRef.child("towing_locations").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            // Display the data to the user
                                            String Phone = snapshot.child(markerKey).child("owner_phone").getValue(String.class);
                                            String shopID = snapshot.child(markerKey).child("shopid").getValue(String.class);
                                            String ownerName = snapshot.child(markerKey).child("owner_name").getValue(String.class);
                                            String email = snapshot.child(markerKey).child("owner_email").getValue(String.class);
                                            String shopName = snapshot.child(markerKey).child("shop_name").getValue(String.class);

                                            //lat,lng to measure distance
                                            double towlat = snapshot.child(markerKey).child("latitude").getValue(Double.class);
                                            double towlng = snapshot.child(markerKey).child("longitude").getValue(Double.class);


//                                            //to measure distance
//                                            float[] results = new float[1];
//                                            Location.distanceBetween(latitudee, longitudee, towlat, towlng, results);
//                                            float distance = results[0];
//                                            // String distanceString = Float.toString(distance) + " meters";
//                                            float distanceInKilometers = distance / 1000;
//                                            String distanceString =(distanceInKilometers) + " KM";

                                            double distance= calculateDistance(towlat, towlng,latitudee, longitudee);
                                            String distanceinKM=String.format("%.2f", distance);

                                            AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this);
                                            View c_alert=getLayoutInflater().inflate(R.layout.alertmain,null);
                                            builder.setView(c_alert);
                                            Button okk=c_alert.findViewById(R.id.button);
                                            TextView namee=c_alert.findViewById(R.id.name);
                                            TextView phonee=c_alert.findViewById(R.id.phone);
                                           // TextView cnicc=c_alert.findViewById(R.id.cnic);
                                            //TextView emaill=c_alert.findViewById(R.id.email);

                                            namee.setText(shopName);
                                            phonee.setText(distanceinKM);
                                             
                                            okk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
//                                  Intent intent=new Intent(MapsActivity.this,current_tow_profile_fromMap.class);
//                                        intent.putExtra("id", shopID);
                                                    Intent intent= new Intent(MapsActivity.this,towingProfileForCust.class);
                                                    intent.putExtra("shop_id",shopID);
                                                    intent.putExtra("distance",distanceinKM);
                                                    intent.putExtra("shopEmail",email);
                                                    intent.putExtra("shopPhone",Phone);
                                                    intent.putExtra("owner_name",ownerName);
                                                    intent.putExtra("shop_name",shopName);
                                                    startActivity(intent);
                                                }
                                            });

                                            AlertDialog alertDialog= builder.create();
                                            alertDialog.show();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle database error here
                                        }
                                    });
                                }
                                return false;
                            }
                        });



                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
//                        LatLng Pakistan = new LatLng(30.3894, 69.3532);
//                        mMap.addMarker(new MarkerOptions().position(Pakistan).title(" Pakistan"));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pakistan));
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

}