package com.example.map;

import static android.location.LocationManager.*;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map.databinding.ActivityMapsBinding;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    int n;
    String reqRoot;
    String reqRootChild;
    String cuname,cuID; //cust info for showing req
    String cust_distance;
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
    myMechAdpter adapter;
    FirebaseAuth auth;
    String currentLoginUser;

    String a;
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        switchCompat = getActivity().findViewById(R.id.switchbtn);
        recyclerView = view.findViewById(R.id.requestRecViewforMech);
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myRef.child("mechanic").child(currentLoginUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String mech_name = snapshot.child("mname").getValue(String.class);
                                String mech_phone = snapshot.child("mphone").getValue(String.class);
                                String mech_email = snapshot.child("memail").getValue(String.class);
                                String mech_id = snapshot.child("mid").getValue(String.class);
                                String mech_age = snapshot.child("mage").getValue(String.class);
                                String mech_img = snapshot.child("mimage").getValue(String.class);
                                String mech_cat = snapshot.child("category").getValue(String.class);

                                Map<String, Object> mechData = new HashMap<>();
                                mechData.put("mech_name", mech_name);
                                mechData.put("mech_phone", mech_phone);
                                mechData.put("mech_email", mech_email);
                                mechData.put("mech_id", mech_id);
                                mechData.put("mech_age", mech_age);
                                mechData.put("mech_img", mech_img);
                                mechData.put("mech_categories", mech_cat);
                                mechData.put("latitude", latitudee);
                                mechData.put("longitude", longitudee);

                                myRef.child("mech_locations").child(currentLoginUser).setValue(mechData);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    myRef.child("mech_requests").child(currentLoginUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(requireContext(), "No request exists", Toast.LENGTH_SHORT).show();
                                recyclerView.setVisibility(View.INVISIBLE);
                            } else {
                                ArrayList<MechModel> models = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    String reqRootChild = dataSnapshot1.getKey();
                                    String cuname = dataSnapshot1.child("username").getValue(String.class);
                                    String cuID = dataSnapshot1.child("uid").getValue(String.class);
                                    String cust_distance = dataSnapshot1.child("distance").getValue(String.class);
//                                    String ratings=dataSnapshot1.child("ratings").getValue(String.class);

                                    MechModel modell = new MechModel();
                                    modell.setCust_nameforMech(cuname);
                                    modell.setCustidforMech(cuID);
                                    modell.setCustdistanceforMech(cust_distance);
//                                    modell.setCustRatings(ratings);
                                    models.add(modell);
                                }
                                // create and set the adapter for the RecyclerView
                                adapter = new myMechAdpter(models, getContext());
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView.setVisibility(View.VISIBLE);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {

                    myRef.child("mech_locations").child(currentLoginUser).removeValue();
                    recyclerView.setVisibility(View.INVISIBLE);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.detach(MapFragment.this).attach(MapFragment.this).commit();
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
            return true;
        } else {
            return false;
        }
    }
    private void requestLocationPermission()
    {

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