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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.map.databinding.ActivityMapsBinding;
import com.example.map.databinding.ActivityMechMapsBinding;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MechMapsActivity extends FragmentActivity implements OnMapReadyCallback {

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
    private ActivityMechMapsBinding binding;
    List<Address> listGeoCoder;
    Location location;
    String keyRoot;
    String categoryName;
    String provided_category;
    int n;
    private static final int Location_permission_code = 101;
    public static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMechMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
          fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        n = 0;

        Intent intent=getIntent();
        categoryName=intent.getStringExtra("categoryName");
//        Toast.makeText(this, "CN "+categoryName, Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mech_map);
        mapFragment.getMapAsync(this);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        myRef.child("mech_locations").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                mMap.clear();
                                latitudee = location.getLatitude();
                                longitudee = location.getLongitude();
                                LatLng current = new LatLng(latitudee, longitudee);
                                mMap.addMarker(new MarkerOptions().position(current).title("Current"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,10));
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    // Retrieve the data for this marker
                                    keyRoot=childSnapshot.getKey();
                                    provided_category=childSnapshot.child("mech_categories").getValue(String.class);
                                    if(provided_category.contains(categoryName))
                                    {
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
                                                    .title(keyRoot).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.mechanic_icon)));
                                            marker.setTag(childSnapshot.getKey());
                                            Toast.makeText(MechMapsActivity.this, "within your 3km , mechanic is available", Toast.LENGTH_SHORT).show();
                                        }
                                        else if (Double.parseDouble(distanceinM) <= 5000){ //Radius 5km
                                            // Add the marker to the map and set its tag to the Firebase data key
                                            Marker marker = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(markerLat, markerLng))
                                                    .title(keyRoot).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.mechanic_icon)));
                                            marker.setTag(childSnapshot.getKey());
                                            Toast.makeText(MechMapsActivity.this, "within your 5km , mechanic is available", Toast.LENGTH_SHORT).show();
                                        }
                                        else if (Double.parseDouble(distanceinM) <= 10000){ //Radius 10km
                                            // Add the marker to the map and set its tag to the Firebase data key
                                            Marker marker = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(markerLat, markerLng))
                                                    .title(keyRoot).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.mechanic_icon)));
                                            marker.setTag(childSnapshot.getKey());
                                            Toast.makeText(MechMapsActivity.this, "within your 10km , mechanic is available", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            // Add the marker to the map and set its tag to the Firebase data key
                                            Marker marker = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(markerLat, markerLng))
                                                    .title(keyRoot).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.mechanic_icon)));
                                            marker.setTag(childSnapshot.getKey());
                                            Toast.makeText(MechMapsActivity.this, " mechanic is available but is far", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                    else
                                    {Toast.makeText(MechMapsActivity.this, "No current Mechanic Available", Toast.LENGTH_SHORT).show();}
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
                                    myRef.child("mech_locations").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            // Display the data to the user
                                            String mech_Phone = snapshot.child(markerKey).child("mech_phone").getValue(String.class);
                                            String mech_ID = snapshot.child(markerKey).child("mech_id").getValue(String.class);
                                            String mech_Name = snapshot.child(markerKey).child("mech_name").getValue(String.class);
                                            String mech_Email = snapshot.child(markerKey).child("mech_email").getValue(String.class);
                                            String mech_age = snapshot.child(markerKey).child("mech_age").getValue(String.class);
                                            String mech_Img = snapshot.child(markerKey).child("mech_img").getValue(String.class);

                                            //lat,lng to measure distance
                                            Double towlat = snapshot.child(markerKey).child("latitude").getValue(Double.class);
                                            Double towlng = snapshot.child(markerKey).child("longitude").getValue(Double.class);


//                                            //to measure distance
//                                            float[] results = new float[1];
//                                            Location.distanceBetween(latitudee, longitudee, towlat, towlng, results);
//                                            float distance = results[0];
//                                            // String distanceString = Float.toString(distance) + " meters";
//                                            float distanceInKilometers = distance / 1000;
//                                            String distanceString =(distanceInKilometers) + " meters";

                                            double distance= calculateDistance(towlat, towlng,latitudee, longitudee);
                                            String distanceinKM=String.format("%.2f", distance);
//                                            double distanceInMeters = Double.parseDouble(distanceinKM) * 1000;
//                                            double distanceInMeters = distance * 1000;
//                                            String distanceinM=String.format("%.2f", distanceInMeters/100)+ " meters";


                                            AlertDialog.Builder builder=new AlertDialog.Builder(MechMapsActivity.this);
                                            View c_alert=getLayoutInflater().inflate(R.layout.alert_for_mech,null);
                                            builder.setView(c_alert);
                                            Button okk=c_alert.findViewById(R.id.btnProfile);
                                            TextView namee=c_alert.findViewById(R.id.mech_name);
                                            TextView dist=c_alert.findViewById(R.id.mech_dist);
                                            ImageView img=c_alert.findViewById(R.id.pictureid);

                                            namee.setText(mech_Name);
//                                            dist.setText(distanceString);
                                            dist.setText(distanceinKM);
                                            Picasso.get().load(mech_Img).into(img);


                                            okk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
//                                  Intent intent=new Intent(MapsActivity.this,current_tow_profile_fromMap.class);
//                                        intent.putExtra("id", shopID);
                                                    Intent intent= new Intent(MechMapsActivity.this,mechanic_profile.class);
                                                    intent.putExtra("mech_Id",mech_ID);
//                                                    intent.putExtra("mech_Distance",distanceString);
                                                    intent.putExtra("mech_Distance",distanceinKM);
                                                    intent.putExtra("mech_Email",mech_Email);
                                                    intent.putExtra("mech_Phone",mech_Phone);
                                                    intent.putExtra("mech_Name",mech_Name);
                                                    intent.putExtra("mech_Age",mech_age);
                                                    intent.putExtra("mech_Img",mech_Img);
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