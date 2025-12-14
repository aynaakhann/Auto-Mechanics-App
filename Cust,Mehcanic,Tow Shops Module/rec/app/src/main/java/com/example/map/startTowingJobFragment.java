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
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class startTowingJobFragment extends Fragment implements OnMapReadyCallback {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    private GoogleMap mMap;
    List<Address> listGeoCoder;
    private static final int Location_permission_code = 101;
    FloatingActionButton shareloc;
    FloatingActionButton alertCall;
    Button confirmtowingreachedcustomer;
    public double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;
    String cID;
    FirebaseAuth auth;
    String currentLoginUser;
    long startTime = 0;
    long endTime = 0;
    String servicesToSend,timeString;
    double chargesToSend;
    TextView timer;
    long timeDifference,timeInMinutes,seconds;
    public startTowingJobFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_start_towing_job, container, false);
        shareloc = view.findViewById(R.id.shareLocation);
        alertCall= view.findViewById(R.id.alertcall);
        confirmtowingreachedcustomer=view.findViewById(R.id.confirmTowReachedCustbtn);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        currentLoginUser = auth.getInstance().getCurrentUser().getUid();
        Bundle bundle=this.getArguments();
        cID =bundle.getString("cIdd");

        timer=view.findViewById(R.id.timer);
        startTime = System.currentTimeMillis();
        // startTimer();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (startTime != 0) {
                    // Calculate the elapsed time since the timer started
                    endTime = System.currentTimeMillis();
                    timeDifference = endTime - startTime;
                    timeInMinutes = timeDifference / 60000;

                    // Convert the time difference to minutes and seconds
                    int minutes = (int) (timeInMinutes % 60);
                    int seconds = (int) ((timeDifference / 1000) % 60);

                    // Format the time and update the UI
                    timeString = String.format("%02d:%02d", minutes, seconds);
                    timer.setText("Running time: " + timeString);
                }
                handler.postDelayed(this, 1000); // Update every second
            }
        });
        confirmtowingreachedcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("History").child(cID).child(currentLoginUser).child("tow_end_latitude").setValue(latitudee);
                myRef.child("History").child(cID).child(currentLoginUser).child("tow_end_longitude").setValue(longitudee);
                myRef.child("History").child(cID).child(currentLoginUser).child("time").setValue(timeString);
                myRef.child("History_Tow").child(cID).child(currentLoginUser).child("time").setValue(timeString);

                AppCompatActivity activity=(AppCompatActivity)getContext();
                TowEndJob endJob=new TowEndJob();
                Bundle bundle11=new Bundle();
                bundle11.putString("cIdd",cID);
//                bundle11.putLong("timeinMin",timeInMinutes);
                bundle11.putString("timeinMin",timeString);
               endJob.setArguments(bundle11);

               activity.getSupportFragmentManager().beginTransaction().replace(R.id.containertow,endJob).addToBackStack(null).commit();
            }
        });

        // share button
        shareloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                shareLocation(location);
                                // Get the latitude and longitude
//                        latitudee = location.getLatitude();
//                        longitudee = location.getLongitude();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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
                }
            });

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.cofirmTowReachedCustmap);
        //assert mapFragment != null;
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
                        myRef.child("requests").child(currentLoginUser).child(cID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Loop through the data and add markers to the map

//                                custLat = snapshot.child("latitude").getValue(double.class);
//                                custLng = snapshot.child("longitude").getValue(double.class);

                                LatLng Customer = new LatLng(32.5101, 74.5431); //model town lat, lng
//
                                mMap.addMarker(new MarkerOptions().position(Customer).title("Customer"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Customer,16));

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
                builder.setNegativeButton("OK", null);
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}