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
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;


public class towAcceptReqFragment extends Fragment implements OnMapReadyCallback {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    List<Address> listGeoCoder;
    Button getStarted;
    double Lat,custLat,custLng;
    double Lng;
    double latitudee,longitudee;
    String cId,address;
    private static final int Location_permission_code = 101;
    FirebaseAuth auth;
    String currentLoginUser;
    public towAcceptReqFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tow_accept_req, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();

        getStarted=view.findViewById(R.id.tgetStarted);

        Bundle bundle=this.getArguments();
        cId =bundle.getString("cIdT");
        address=bundle.getString("addressLineT");
//        Toast.makeText(getContext(), "cId "+cId, Toast.LENGTH_SHORT).show();

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Towing_Shop").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                            String keyRoot=dataSnapshot.getKey();
                            if(snapshot.hasChild(currentLoginUser)){
                                String Shop_name=snapshot.child(currentLoginUser).child("shop_name").getValue().toString();
                                String shop_phone=snapshot.child(currentLoginUser).child("owner_phone").getValue().toString();

                                myRef.child("History").child(cId).child(currentLoginUser).child("Shop_name").setValue(Shop_name);
                                myRef.child("History").child(cId).child(currentLoginUser).child("Address").setValue(address);
                                myRef.child("History").child(cId).child(currentLoginUser).child("shop_phone").setValue(shop_phone);
                                myRef.child("History").child(cId).child(currentLoginUser).child("request_status").setValue("accept");
                                myRef.child("History").child(cId).child(currentLoginUser).child("tow_start_latitude").setValue(latitudee);
                                myRef.child("History").child(cId).child(currentLoginUser).child("tow_start_longitude").setValue(longitudee);


                                /*String Shop_id=snapshot.child(currentLoginUser).child("shopid").getValue().toString();
                                HelperTow_complains helperr= new HelperTow_complains(Shop_id,Shop_name,shop_phone,address,cId,"");
                                myRef.child("History_Tow").child(cId).child(Shop_id).setValue(helperr);*/
                            }
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String keyRoot=dataSnapshot.getKey();
                            if(keyRoot.equals(currentLoginUser)){
                                String Shop_name=snapshot.child(currentLoginUser).child("shop_name").getValue().toString();
                                String Shop_id=snapshot.child(currentLoginUser).child("shopid").getValue().toString();
                                String oname=snapshot.child(currentLoginUser).child("owner_name").getValue().toString();
                                String ocnic=snapshot.child(currentLoginUser).child("owner_cnic").getValue().toString();
                                String sreg=snapshot.child(currentLoginUser).child("shop_regno").getValue().toString();
                                String ophone=snapshot.child(currentLoginUser).child("owner_phone").getValue().toString();
                                String oemail=snapshot.child(currentLoginUser).child("owner_email").getValue().toString();
                                String opass=snapshot.child(currentLoginUser).child("password").getValue().toString();
                                String regimg=snapshot.child(currentLoginUser).child("reg_img").getValue().toString();
                                String rpayment=snapshot.child(currentLoginUser).child("rpayment").getValue().toString();
                                String key =FirebaseDatabase.getInstance().getReference("History_Tow").push().getKey();
                                LocalTime currentTime = null;
                                LocalDate currentDate=null;
                                String time,date;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    currentTime = LocalTime.now();
                                }
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    currentDate = LocalDate.now();
                                }
                                HelperTow_complains helperr= new HelperTow_complains(Shop_id,cId,Shop_name,oname,ocnic,sreg,ophone,address,"",oemail,opass,regimg,rpayment,currentTime.toString(),currentDate.toString(),"Charges in process");
                                myRef.child("History_Tow").child(key).setValue(helperr);
//                                String dist=snapshot.child(custId).child("distance").getValue().toString();
                                myRef.child("History").child(cId).child(currentLoginUser).child("Shop_name").setValue(Shop_name);
                                myRef.child("History").child(cId).child(currentLoginUser).child("Address").setValue(address);
                                myRef.child("History").child(cId).child(currentLoginUser).child("shop_phone").setValue(ophone);
                                myRef.child("History").child(cId).child(currentLoginUser).child("request_status").setValue("accept");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                myRef.child("requests").child(currentLoginUser).child(cId).removeValue();
                AppCompatActivity activity=(AppCompatActivity)getContext();
                startTowingJobFragment startjob=new startTowingJobFragment();
                Bundle bundle11=new Bundle();
                bundle11.putString("cIdd",cId);

                startjob.setArguments(bundle11);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containertow,startjob).addToBackStack(null).commit();
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.startjourney);
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
                        mMap.clear();
                        latitudee = location.getLatitude();
                        longitudee = location.getLongitude();
                       LatLng current = new LatLng(latitudee, longitudee);

                        mMap.addMarker(new MarkerOptions().position(current).title("Current"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,13));
                        myRef.child("requests").child(currentLoginUser).child(cId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Loop through the data and add markers to the map

//                                custLat = snapshot.child("latitude").getValue(double.class);
//                                custLng = snapshot.child("longitude").getValue(double.class);

                                LatLng Customer = new LatLng(32.5101, 74.5431); //model town lat, lng
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
                                mMap.addMarker(new MarkerOptions().position(Customer).title("Customer"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Customer,10));

                                PolylineOptions options = new PolylineOptions().add(current).add(Customer).width(7).color(Color.RED).geodesic(true);
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
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentmanager =requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentmanager.beginTransaction();
        fragmentTransaction.replace(R.id.containertow,fragment);
        fragmentTransaction.commit();
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
                builder.setNegativeButton("No", null);
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}