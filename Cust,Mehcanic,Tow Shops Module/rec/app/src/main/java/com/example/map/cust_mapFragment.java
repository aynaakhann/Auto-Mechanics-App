package com.example.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.map.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class cust_mapFragment extends Fragment implements OnMapReadyCallback {
   //DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    double latitudee,longitudee;
    //  String lat,longi;
    public GoogleMap mMap;
    public ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    String latt,lang;
    List<Address> listGeoCoder;
    public static final int Location_permission_code=101;
    public double i,j;
    public cust_mapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        if (isLocationPermissionGranted()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            try {
                listGeoCoder = new Geocoder(getContext()).getFromLocationName("Sialkot International Airport, Sialkot Airport Road, Sialkot, Pakistan", 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            double longitude = listGeoCoder.get(0).getLongitude();
            double latitude = listGeoCoder.get(0).getLatitude();

            Log.i("GOOGLE_MAP_TAG", "Address has Longitude:::" + String.valueOf(longitude) + "Latitude" + String.valueOf(latitude));
        } else {
            requestLocationPermission();
        }
        return view;

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        myRef.child("Towing_Shop_location").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //  locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
//                latt= snapshot.child("1").child("latitude").getValue().toString();
//                //    lang= snapshot.child("long").getValue().toString();
//                latt= snapshot.child("1").child("longitude").getValue().toString();
//
//                i=Double.parseDouble(latt);
//                j=Double.parseDouble(lang);
//
//                // LatLng Sialkot = new LatLng(32.517960, 74.500916);
////                        a=i;
////                        b=j;
//                //       j=Float.parseFloat(lang);
//              //  res.setText("latitude is: "+ i+"longitude is :"+j);
//
//                LatLng Pakistan = new LatLng(i,j);
//                mMap.addMarker(new MarkerOptions().position(Pakistan).title(" current loc"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(Pakistan));
//                //     res.setText(String.valueOf(lang));
//                //Toast.makeText(getApplicationContext(), usernameFromDB, Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
        // Add a marker in Sydney and move the camera
        //  LatLng sydney = new LatLng(-34, 151);
        //   mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //to set current location
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            mMap.setMyLocationEnabled(true);
        }
    }
    private boolean isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_permission_code);

    }
}