package com.example.map;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.map.databinding.ActivityCustConfirmTowingBinding;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class cust_confirm_towing extends FragmentActivity implements OnMapReadyCallback {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    FirebaseAuth auth;
    String currentLoginUser;
    private ActivityCustConfirmTowingBinding binding;
    String shopId;
    Button confirmTow;
    public ProgressDialog progressDialog;
    private static final int Location_permission_code = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCustConfirmTowingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        Intent intent=getIntent();
        shopId=intent.getStringExtra("shopID");
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();
        confirmTow=findViewById(R.id.confirmTow);

        confirmTow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Toast.makeText(cust_confirm_towing.this, ""+seconds, Toast.LENGTH_SHORT).show();
        Intent intent1=new Intent(cust_confirm_towing.this,AfterConfirmTowingByCust.class);
//        intent1.putExtra("durationinMin",timeInMinutes);
        intent1.putExtra("shopId",shopId);
        startActivity(intent1);
        finish();

    }
});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.polyLinecustTow);
        mapFragment.getMapAsync(this);
        //    return View;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng Pakistan = new LatLng(30.3894, 69.3532);
//
//        mMap.addMarker(new MarkerOptions().position(Pakistan).title(" Pakistan"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pakistan));
//
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
                           showProgressDialog();
                                if (snapshot.hasChild("tow_end_latitude")){
                                    dismissProgressDialog();
                                    //  Toast.makeText(cust_confirm_towing.this, ""+currentLoginUser+shopId, Toast.LENGTH_SHORT).show();
                                    String shopLatt = snapshot.child("tow_start_latitude").getValue().toString();

                                String shoplangg = snapshot.child("tow_start_longitude").getValue().toString();
                                double l1 = Double.parseDouble(shopLatt);
                                double l2 = Double.parseDouble(shoplangg);
                                LatLng Shop = new LatLng(l1, l2); //model town lat, lng
                                mMap.addMarker(new MarkerOptions().position(Shop).title("Towing_Shop").icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_tow_truck_tow_away_svgrepo_com__2_)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Shop, 13));

                                PolylineOptions options = new PolylineOptions().add(current).add(Shop).width(7).color(Color.RED).geodesic(true);
                                mMap.addPolyline(options);
                                // Add the marker to the map and set its tag to the Firebase data key


                            }
                                else{
                                    Toast.makeText(cust_confirm_towing.this, "not reached", Toast.LENGTH_SHORT).show();
                                }
                        }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database error here
                            }
                        });

               }
//
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
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Alert")
                .setIcon(R.drawable.ic_alert_error_msg)
                .setMessage("You cannot exit!")  ;

        builder.setNegativeButton("ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}