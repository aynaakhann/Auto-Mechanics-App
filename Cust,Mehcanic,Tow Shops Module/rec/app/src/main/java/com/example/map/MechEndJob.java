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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class MechEndJob extends Fragment implements OnMapReadyCallback {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    private GoogleMap mMap;
    List<Address> listGeoCoder;
    private static final int Location_permission_code = 101;
    FloatingActionButton shareloc;
    FloatingActionButton alertCall;
    Button jobDone;
    public double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;
    String cID;
    FirebaseAuth auth;
    String currentLoginUser;
    long startTime = 0;
    long endTime = 0;
    String duration;
    double startLat,startLong,distance,distanceInMeters,chargesforServices;
    String servicesToSend,availedAddress,distanceinKM,formattedDistanceInMeters,servicesAvailed;
    public static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
    double chargesToSend,custLng,custLat;
    public MechEndJob() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mech_end_job, container, false);
        shareloc = view.findViewById(R.id.shareLocationM);
        alertCall= view.findViewById(R.id.alertcallM);
        jobDone=view.findViewById(R.id.endJobM);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        currentLoginUser = auth.getInstance().getCurrentUser().getUid();
        Bundle bundle=this.getArguments();
        cID =bundle.getString("cIddM");
        duration=bundle.getString("timeMin");


        myRef.child("HistoryM").child(cID).child(currentLoginUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//              Toast.makeText(AfterConfirmTowingByCust.this, ""+currentLoginUser+shopId, Toast.LENGTH_SHORT).show();
                String mechLatt = snapshot.child("mech_start_latitude").getValue().toString();
                String mechlangg =  snapshot.child("mech_start_longitude").getValue().toString();

                startLat= Double.parseDouble(mechLatt);
                startLong= Double.parseDouble(mechlangg);
                availedAddress= snapshot.child("Address").getValue().toString();
                // Calculate the distance between the two points using the calculateDistance method:
                distance = calculateDistance(startLat, startLong,latitudee, longitudee);
                distanceinKM=String.format("%.2f", distance);
                // Check if the distance is less than 1 kilometer:
                if (distance < 1) {
                    // Convert distance to meters:
                    distanceInMeters = distance * 1000;
                    // Format the distance in meters:
                    formattedDistanceInMeters = String.format("%.2f", distanceInMeters);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error here
            }
        });


        jobDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(distance<1){
                    myRef.child("HistoryM").child(cID).child(currentLoginUser).child("distance").setValue(formattedDistanceInMeters);
                    myRef.child("History_Mech").child(cID).child(currentLoginUser).child("distance").setValue(formattedDistanceInMeters);
                }
                else {
                    myRef.child("HistoryM").child(cID).child(currentLoginUser).child("distance").setValue(distanceinKM);
                    myRef.child("History_Mech").child(cID).child(currentLoginUser).child("distance").setValue(distanceinKM);
                }

                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                //  builder.setIcon(R.drawable.ic_today).setTitle("Select your DOB");
                View PriceView= getLayoutInflater().inflate(R.layout.alertcharges, null);
                builder.setView(PriceView);
                EditText service = PriceView.findViewById(R.id.service_name);
                EditText charges = PriceView.findViewById(R.id.charges);

                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String servicesAvailed = service.getText().toString();
                        double chargesforServices = Double.parseDouble(charges.getText().toString());

                        myRef.child("HistoryM").child(cID).child(currentLoginUser).child("Services Availed").setValue(servicesAvailed);
                        myRef.child("HistoryM").child(cID).child(currentLoginUser).child("Service Charges").setValue(chargesforServices);

                        myRef.child("History_Mech").child(cID).child(currentLoginUser).child("Services Availed").setValue(servicesAvailed);
                        myRef.child("History_Mech").child(cID).child(currentLoginUser).child("Service Charges").setValue(chargesforServices);

                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        mBillFragment mBillFragment = new mBillFragment();
                        Bundle bundle11 = new Bundle();
                        bundle11.putString("cIDM", cID);
                        bundle11.putString("availedAddressM", availedAddress);
                        bundle11.putString("availedServicesM", servicesAvailed);
                        bundle11.putString("timeBillM", duration);
                        bundle11.putDouble("chargesM", chargesforServices);
                        if (distance < 1) {
                            bundle11.putString("distanceM", formattedDistanceInMeters);
                        } else {
                            bundle11.putString("distanceM", distanceinKM);
                        }
                        mBillFragment.setArguments(bundle11);

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, mBillFragment).addToBackStack(null).commit();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();

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
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.Mechlastmap);
        //assert mapFragment != null;
        mapFragment.getMapAsync(this);
        return view;

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
                        myRef.child("mech_requests").child(currentLoginUser).child(cID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    // Loop through the data and add markers to the map
                                    String mlatt=snapshot.child("latitude").getValue().toString();
                                    String mlong=snapshot.child("longitude").getValue().toString();
                                    custLat=Double.parseDouble(mlatt);
                                    custLng=Double.parseDouble(mlong);
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
        fragmentTransaction.replace(R.id.container,fragment);
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