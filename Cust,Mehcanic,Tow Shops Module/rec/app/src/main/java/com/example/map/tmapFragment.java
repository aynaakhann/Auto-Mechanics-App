package com.example.map;

import static android.location.LocationManager.*;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class tmapFragment extends Fragment implements OnMapReadyCallback {
    public static final Integer NF_ID =01 ;
    public static final String REPLY ="Text_message" ;//name for remote input
    private static final String CHANNEL_ID ="Channel_01";
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    int n;
    String reqRoot;
    String reqRootChild;
    String cuname, cuID; //cust info for showing req
    String cust_distance;
    double cust_latitude, cust_longitude;
    Location location;
    public double latitudee, longitudee;
    // GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    List<Address> listGeoCoder;
    private FragmentActivity frs;
    private static final int Location_permission_code = 101;

    SwitchCompat switchCompat;
    RecyclerView recyclerView;
    myAdapter adapter;
    FirebaseAuth auth;
    String currentLoginUser;

    String a;

    public tmapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tmap, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        switchCompat = getActivity().findViewById(R.id.switchbtn);
        recyclerView = view.findViewById(R.id.requestRecView);
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myRef.child("Towing_Shop").child(currentLoginUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String owner_name = snapshot.child("owner_name").getValue(String.class);
                                String owner_phone = snapshot.child("owner_phone").getValue(String.class);
                                String owner_email = snapshot.child("owner_email").getValue(String.class);
                                String shopid = snapshot.child("shopid").getValue(String.class);
                                String shopName = snapshot.child("shop_name").getValue(String.class);

                                Map<String, Object> towingData = new HashMap<>();
                                towingData.put("owner_name", owner_name);
                                towingData.put("owner_phone", owner_phone);
                                towingData.put("owner_email", owner_email);
                                towingData.put("shopid", shopid);
                                towingData.put("shop_name", shopName);
                                towingData.put("latitude", latitudee);
                                towingData.put("longitude", longitudee);

                                myRef.child("towing_locations").child(currentLoginUser).setValue(towingData);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error if necessary
                        }
                    });

                    myRef.child("requests").child(currentLoginUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(requireContext(), "No request exists", Toast.LENGTH_SHORT).show();
                                recyclerView.setVisibility(View.INVISIBLE);
                            } else {
                                ArrayList<Model> models = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    String reqRootChild = dataSnapshot1.getKey();
                                    String cuname = dataSnapshot1.child("username").getValue(String.class);
                                    String cuID = dataSnapshot1.child("uid").getValue(String.class);
                                    String cust_distance = dataSnapshot1.child("distance").getValue(String.class);
//                                    String ratings=dataSnapshot1.child("ratings").getValue(String.class);

                                    Model model = new Model();
                                    model.setCust_name(cuname);
                                    model.setId(cuID);
                                    model.setDistance(cust_distance);
//                                    model.setRatings(ratings);
                                    models.add(model);
                                }
addNotification();
                                // Create and set the adapter for the RecyclerView
                                adapter = new myAdapter(models, getContext());
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error if necessary
                        }
                    });
                } else {
                    myRef.child("towing_locations").child(currentLoginUser).removeValue();
                    recyclerView.setVisibility(View.INVISIBLE);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.detach(tmapFragment.this).attach(tmapFragment.this).commit();
                }
            }
        });


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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.towMap);
        mapFragment.getMapAsync(this);
        return view;

    }


    @Override
    public void onMapReady(GoogleMap mMap) {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mMap.clear();
                        // Get the latitude and longitude
                        latitudee = location.getLatitude();
                        longitudee = location.getLongitude();
                        LatLng current = new LatLng(latitudee, longitudee);

                        mMap.addMarker(new MarkerOptions().position(current).title("Current"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,17));

                    }
                    else{
                        LatLng Pakistan = new LatLng(30.3894, 69.3532);
        mMap.addMarker(new MarkerOptions().position(Pakistan).title(" Pakistan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pakistan));
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
                Context context = requireContext();
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Get the latitude and longitude
                                latitudee = location.getLatitude();
                                longitudee = location.getLongitude();

                            }

                        }
                    });
//
                    return true;
                } else {
                    return false;
                }
            }

            private void requestLocationPermission() {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_permission_code);
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

    private void addNotification() {
        notificationchannel();
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(requireContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_profile) //set icon for notification
                .setContentTitle("Notifications") //set title of notification
                .setContentText(" You have a request\uD83E\uDD17")//this is notification message
                .setAutoCancel(true) // makes auto cancel of notification
                .setPriority(Notification.PRIORITY_DEFAULT);

// Add as notification
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NF_ID, builder.build());
    }
    private void notificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name= "my_channel";
//channel description for developers understanding
            // String description = getString();
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("accept request notification");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
//                builder.setTitle("Exit Alert")
//                        .setIcon(R.drawable.ic_alert_error_msg)
//                        .setMessage("Are you sure you want to exit?")  ;
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Perform any necessary actions or navigate back
//                        requireActivity().onBackPressed();
//                    }
//                });
//                builder.setNegativeButton("NO", null);
//                androidx.appcompat.app.AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });
//    }
}