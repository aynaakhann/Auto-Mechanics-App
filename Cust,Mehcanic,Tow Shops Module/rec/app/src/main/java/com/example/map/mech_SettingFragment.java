package com.example.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mech_SettingFragment extends Fragment {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    LinearLayout editProfile, changeLanguage;
    SwitchCompat switchCompat;
    String currentLoginuser;
    FirebaseAuth auth;
    public double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;

    public mech_SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mech__setting, container, false);
        editProfile = view.findViewById(R.id.editProfileforMech);
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
                    myRef.child("mechanic").child(currentLoginuser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String mech_name=snapshot.child("mname").getValue().toString();
                            String mech_phone=snapshot.child("mphone").getValue().toString();
                            String mech_email=snapshot.child("memail").getValue().toString();
                            String mech_id=snapshot.child("mid").getValue().toString();
                            String mech_age=snapshot.child("mage").getValue().toString();
                            String mech_img=snapshot.child("mimage").getValue().toString();
                            String mech_cat=snapshot.child("category").getValue().toString();

                            myRef.child("mech_locations").child(currentLoginuser).child("mech_name").setValue(mech_name);
                            myRef.child("mech_locations").child(currentLoginuser).child("mech_phone").setValue(mech_phone);
                            myRef.child("mech_locations").child(currentLoginuser).child("mech_email").setValue(mech_email);
                            myRef.child("mech_locations").child(currentLoginuser).child("mech_id").setValue(mech_id);
                            myRef.child("mech_locations").child(currentLoginuser).child("mech_age").setValue(mech_age);
                            myRef.child("mech_locations").child(currentLoginuser).child("mech_img").setValue(mech_img);
                            myRef.child("mech_locations").child(currentLoginuser).child("mech_categories").setValue(mech_cat);
                            myRef.child("mech_locations").child(currentLoginuser).child("latitude").setValue(latitudee);
                            myRef.child("mech_locations").child(currentLoginuser).child("longitude").setValue(longitudee);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else
                    myRef.child("mech_locations").child(currentLoginuser).removeValue();
            }
        });
        changeLanguage = view.findViewById(R.id.changeLanguagefromSettingsforMech);
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditMechanicProfile();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}