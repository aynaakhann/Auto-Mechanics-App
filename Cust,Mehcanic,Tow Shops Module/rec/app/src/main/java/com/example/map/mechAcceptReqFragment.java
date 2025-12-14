package com.example.map;

import android.Manifest;
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
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class mechAcceptReqFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Address> listGeoCoder;
    Button getStarted;
    private static final int Location_permission_code = 101;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    private FusedLocationProviderClient fusedLocationClient;
    double Lat,custLat,custLng;
    double Lng;
    double latitudee,longitudee;
    String cIdd,address;
    FirebaseAuth auth;
    String currentLoginUser;
    public mechAcceptReqFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mech_accept_req, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();
        getStarted=view.findViewById(R.id.getStarted);
        Bundle bundle=this.getArguments();
//        cId =bundle.getString("cIdm");
        cIdd= bundle.getString("cIdm");
        address=bundle.getString("addressLinem");


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("mechanic").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.hasChild(currentLoginUser)){
                            String mech_name=snapshot.child(currentLoginUser).child("mname").getValue().toString();
                            String mech_phone=snapshot.child(currentLoginUser).child("mphone").getValue().toString();

                            myRef.child("HistoryM").child(cIdd).child(currentLoginUser).child("mname").setValue(mech_name);
                            myRef.child("HistoryM").child(cIdd).child(currentLoginUser).child("Address").setValue(address);
                            myRef.child("HistoryM").child(cIdd).child(currentLoginUser).child("mphone").setValue(mech_phone);
                            myRef.child("HistoryM").child(cIdd).child(currentLoginUser).child("request_status").setValue("accept");
                            myRef.child("HistoryM").child(cIdd).child(currentLoginUser).child("mech_start_latitude").setValue(latitudee);
                            myRef.child("HistoryM").child(cIdd).child(currentLoginUser).child("mech_start_longitude").setValue(longitudee);


                         /*   String mid=snapshot.child(currentLoginUser).child("mid").getValue().toString();
                            HelperMech_complains helperr= new HelperMech_complains(mid,mech_name,address,cIdd,"",mech_phone);
                            myRef.child("History_Mech").child(cIdd).child(mid).setValue(helperr);*/
                        }
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String keyRoot=dataSnapshot.getKey();
                            if(keyRoot.equals(currentLoginUser)){
                                String mname=snapshot.child(currentLoginUser).child("mname").getValue().toString();
                                String mid=snapshot.child(currentLoginUser).child("mid").getValue().toString();
                                String mcnic=snapshot.child(currentLoginUser).child("mcnic").getValue().toString();
                                String mphone=snapshot.child(currentLoginUser).child("mphone").getValue().toString();
                                String memail=snapshot.child(currentLoginUser).child("memail").getValue().toString();
                                String mpass=snapshot.child(currentLoginUser).child("mpass").getValue().toString();
                                String recimg=snapshot.child(currentLoginUser).child("recimg").getValue().toString();
                                String mimg=snapshot.child(currentLoginUser).child("mimage").getValue().toString();
                                String key =FirebaseDatabase.getInstance().getReference("History_Mech").push().getKey();
                                LocalTime currentTime = null;
                                LocalDate currentDate=null;
                                String time,date;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    currentTime = LocalTime.now();
                                }
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    currentDate = LocalDate.now();
                                }
//                                String mid,cid,mname,mcnic,mphone,address,statuscomplain,memail,mpass,mimage,date,time,charges;

                                HelperMech_complains hlper= new HelperMech_complains(mid,cIdd,mname,mcnic,mphone,address,"",memail,mpass,mimg,currentTime.toString(),currentDate.toString(),"Charges in process");
                                myRef.child("History_Mech").child(key).setValue(hlper);
                                //myRef.child("History_Mech").child(cIdd).child(currentLoginUser).child("mname").setValue(mname);
                                //myRef.child("History_Mech").child(cIdd).child(currentLoginUser).child("address").setValue(address);
                                //myRef.child("History_Mech").child(cIdd).child(currentLoginUser).child("mphone").setValue(mphone);
                                myRef.child("History_Mech").child(cIdd).child(currentLoginUser).child("request_status").setValue("accept");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                myRef.child("mech_requests").child(currentLoginUser).child(cIdd).removeValue();
                AppCompatActivity activity=(AppCompatActivity)getContext();
                startMechJobFragment startjob=new startMechJobFragment();
                Bundle bundle11=new Bundle();
                bundle11.putString("cIdM",cIdd);

                startjob.setArguments(bundle11);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,startjob).addToBackStack(null).commit();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // isLocationPermissionGranted();
            checkLocationPermission();
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        //   Get the latitude and longitude
                        latitudee = location.getLatitude();
                        longitudee = location.getLongitude();

                    }
                    else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.startmap);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                        myRef.child("mech_requests").child(currentLoginUser).child(cIdd).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    // Loop through the data and add markers to the map
                                    String slatt=snapshot.child("latitude").getValue().toString();
                                    String slong=snapshot.child("longitude").getValue().toString();
                                    custLat=Double.parseDouble(slatt);
                                    custLng=Double.parseDouble(slong);
                                    LatLng Customer = new LatLng(custLat, custLng);
//                                    LatLng Customer = new LatLng(32.5101, 74.5431); //model town lat, lng
                                    mMap.addMarker(new MarkerOptions().position(Customer).title("Customer"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Customer, 17));

                                    PolylineOptions options = new PolylineOptions().add(current).add(Customer).width(7).color(Color.RED).geodesic(true);
                                    mMap.addPolyline(options);
                                }
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
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            mMap.setMyLocationEnabled(true);
        }
    }

    private boolean isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_permission_code);
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

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Location_permission_code);
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Location_permission_code);
            }
            return false;
        } else {

            return true;
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentmanager =requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentmanager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
                builder.setTitle("Exit Alert")
                        .setIcon(R.drawable.ic_alert_error_msg)
                        .setMessage("You cannot exit!")  ;
                builder.setNegativeButton("OK", null);
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}