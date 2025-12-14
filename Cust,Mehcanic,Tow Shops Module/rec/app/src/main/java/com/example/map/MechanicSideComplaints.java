package com.example.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MechanicSideComplaints extends Fragment {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    FirebaseAuth auth;
    String currUser;
    String phone,complainn;
    SwitchCompat switchCompat;
    public double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;
    int n=1;

    public MechanicSideComplaints() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mechanic_side_complaints, container, false);


        EditText phonee= view.findViewById(R.id.phoneofComplaintsByMech);
        EditText complain= view.findViewById(R.id.complaintByMech);
        Button submit=view.findViewById(R.id.submit_complainbyMech);

        switchCompat=getActivity().findViewById(R.id.switchbtn);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        currUser=auth.getInstance().getCurrentUser().getUid();

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
                    myRef.child("mechanic").child(currUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String mech_name=snapshot.child("mname").getValue().toString();
                            String mech_phone=snapshot.child("mphone").getValue().toString();
                            String mech_email=snapshot.child("memail").getValue().toString();
                            String mech_id=snapshot.child("mid").getValue().toString();
                            String mech_age=snapshot.child("mage").getValue().toString();
                            String mech_img=snapshot.child("mimage").getValue().toString();
                            String mech_cat=snapshot.child("category").getValue().toString();

                            myRef.child("mech_locations").child(currUser).child("mech_name").setValue(mech_name);
                            myRef.child("mech_locations").child(currUser).child("mech_phone").setValue(mech_phone);
                            myRef.child("mech_locations").child(currUser).child("mech_email").setValue(mech_email);
                            myRef.child("mech_locations").child(currUser).child("mech_id").setValue(mech_id);
                            myRef.child("mech_locations").child(currUser).child("mech_age").setValue(mech_age);
                            myRef.child("mech_locations").child(currUser).child("mech_img").setValue(mech_img);
                            myRef.child("mech_locations").child(currUser).child("mech_categories").setValue(mech_cat);
                            myRef.child("mech_locations").child(currUser).child("latitude").setValue(latitudee);
                            myRef.child("mech_locations").child(currUser).child("longitude").setValue(longitudee);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else
                    myRef.child("mech_locations").child(currUser).removeValue();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = phonee.getText().toString();
                complainn = complain.getText().toString();
                if (!phone.isEmpty() && !complainn.isEmpty()){
                    if(Patterns.PHONE.matcher(phone).matches()) {
                        myRef.child("Mech_complains").child(currUser).child(phone).child("complain "+n).setValue(complainn);
                        n++;
//                        myRef.child("Towing_complains").child(curr).child("Email").setValue(emaill);
//                        myRef.child("Towing_complains").child(curr).child("complain").setValue(complainn);
                        Toast.makeText(requireContext(), "complain submitted", Toast.LENGTH_SHORT).show();
                        phonee.setText(" ");
                        complain.setText(" ");
                    }
                    else
                        phonee.setError("Enter Valid Phone Number upto 11 digits");
                }
                else {
                    Toast.makeText(requireContext(), "fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}