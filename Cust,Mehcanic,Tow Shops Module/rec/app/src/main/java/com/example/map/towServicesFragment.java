package com.example.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class towServicesFragment extends Fragment {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    SwitchCompat switchCompat;
    String currentLoginuser;
    FirebaseAuth auth;
    public double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tow_services, container, false);
        switchCompat = getActivity().findViewById(R.id.switchbtn);
        currentLoginuser = auth.getInstance().getCurrentUser().getUid();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCompat.isChecked()) {
                    myRef.child("Towing_Shop").child(currentLoginuser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String owner_name = snapshot.child("owner_name").getValue().toString();
                            String owner_phone = snapshot.child("owner_phone").getValue().toString();
                            String owner_email = snapshot.child("owner_email").getValue().toString();
                            String shopid = snapshot.child("shopid").getValue().toString();
                            String shopName = snapshot.child("shop_name").getValue().toString();

                            myRef.child("towing_locations").child(currentLoginuser).child("owner_name").setValue(owner_name);
                            myRef.child("towing_locations").child(currentLoginuser).child("owner_phone").setValue(owner_phone);
                            myRef.child("towing_locations").child(currentLoginuser).child("owner_email").setValue(owner_email);
                            myRef.child("towing_locations").child(currentLoginuser).child("shopid").setValue(shopid);
                            myRef.child("towing_locations").child(currentLoginuser).child("shop_name").setValue(shopName);
                            myRef.child("towing_locations").child(currentLoginuser).child("latitude").setValue(latitudee);
                            myRef.child("towing_locations").child(currentLoginuser).child("longitude").setValue(longitudee);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else
                    myRef.child("towing_locations").child(currentLoginuser).removeValue();
            }
        });



        return view;
    }
}