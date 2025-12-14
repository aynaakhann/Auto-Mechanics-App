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
import android.graphics.Color;
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
import com.google.android.gms.maps.model.PolylineOptions;
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

public class after_accept_request_of_towing_for_customer extends FragmentActivity implements OnMapReadyCallback {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    double latitudee, longitudee;
    String namee, phone, cnic;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    double latt_Tow, lang_tow;
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

    FirebaseAuth auth;
    String currentLoginUser;
    String ShopID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
       Intent intent=getIntent();
     ShopID= intent.getStringExtra("shopID");

//
//        Intent intent=getIntent();
//        categoryName=intent.getStringExtra("categoryName");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // isLocationPermissionGranted();
            checkLocationPermission();
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
//                         Get the latitude and longitude
//                        latitudee = location.getLatitude();
//                        longitudee = location.getLongitude();

                    }
                    else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("Location Alert")
                                .setIcon(R.drawable.ic_alert_error_msg)
                                .setMessage("please on the location");
                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        builder.create();
                        builder.show();

                    }
                }
            });

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.journeyStartedCust);
        mapFragment.getMapAsync(this);
        //    return View;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                        myRef.child("History").child(currentLoginUser).child(ShopID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Loop through the data and add markers to the map

                              latt_Tow = snapshot.child("latitude").getValue(double.class);
                               lang_tow = snapshot.child("longitude").getValue(double.class);

                                LatLng Towing = new LatLng(latt_Tow, lang_tow); //model town lat, lng
//                                Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
//                                List<Address> addresses = null;
//
//                                try {
//                                    addresses = geocoder.getFromLocation(32.5101, 74.5431, 1);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                if (addresses != null && addresses.size() > 0) {
//                                    Address address = addresses.get(0);
//                                    String addressLine = address.getAddressLine(0);
//                                    String city = address.getLocality();
//                                    String state = address.getAdminArea();
//                                    String country = address.getCountryName();
//                                    String postalCode = address.getPostalCode();
//                                    String knownName = address.getFeatureName();
//                                    Toast.makeText(requireContext(), ""+addressLine, Toast.LENGTH_SHORT).show();
//                                    // Do something with the address information
//                                }
                                mMap.addMarker(new MarkerOptions().position(Towing).title("Customer"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Towing,13));

                                PolylineOptions options = new PolylineOptions().add(current).add(Towing).width(7).color(Color.RED).geodesic(true);
                                mMap.addPolyline(options);
                                // Add the marker to the map and set its tag to the Firebase data key


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
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
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