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
import android.widget.LinearLayout;
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


public class towingSideComplaints extends Fragment {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    FirebaseAuth auth;
    String phone,complainn;
    int n;
    SwitchCompat switchCompat;
    String curr;
    public double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;

    public towingSideComplaints() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_towing_side_complaints, container, false);

        curr = auth.getInstance().getCurrentUser().getUid();

        EditText phonee= view.findViewById(R.id.phoneForComplaints);
        EditText complain= view.findViewById(R.id.complain_multiline);
        Button submit=view.findViewById(R.id.submit_complain);

        switchCompat = getActivity().findViewById(R.id.switchbtn);
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
                    myRef.child("Towing_Shop").child(curr).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String owner_name = snapshot.child("owner_name").getValue().toString();
                            String owner_phone = snapshot.child("owner_phone").getValue().toString();
                            String owner_email = snapshot.child("owner_email").getValue().toString();
                            String shopid = snapshot.child("shopid").getValue().toString();
                            String shopName = snapshot.child("shop_name").getValue().toString();
                            myRef.child("towing_locations").child(curr).child("owner_name").setValue(owner_name);
                            myRef.child("towing_locations").child(curr).child("owner_phone").setValue(owner_phone);
                            myRef.child("towing_locations").child(curr).child("owner_email").setValue(owner_email);
                            myRef.child("towing_locations").child(curr).child("shopid").setValue(shopid);
                            myRef.child("towing_locations").child(curr).child("shop_name").setValue(shopName);
                            myRef.child("towing_locations").child(curr).child("latitude").setValue(latitudee);
                            myRef.child("towing_locations").child(curr).child("longitude").setValue(longitudee);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else
                    myRef.child("towing_locations").child(curr).removeValue();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phonee.getText().toString();
                complainn = complain.getText().toString();
                if (!phone.isEmpty() && !complainn.isEmpty()){
                    if(Patterns.PHONE.matcher(phone).matches())
                    {
                        myRef.child("Towing_Complaints").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                n=1;
                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    String root=dataSnapshot.getKey();
                                    Toast.makeText(getContext(), ""+root, Toast.LENGTH_SHORT).show();

                                    if(root.equals(curr)){
                                        Toast.makeText(getContext(), "id match", Toast.LENGTH_SHORT).show();

                                        myRef.child("Towing_Complaints").child(curr).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                for (DataSnapshot dataSnapshot1:snapshot1.getChildren()){
                                                    String rootPh= dataSnapshot1.getKey();
                                                    if(rootPh.equals(phonee)){
                                                        Toast.makeText(getContext(), "phone match", Toast.LENGTH_SHORT).show();
                                                        myRef.child("Towing_Complaints").child(root).child(rootPh).child("complain "+ n+":").setValue(complainn);
                                                        n++;
                                                        Toast.makeText(getContext(), "Already exist phone Complain submitted", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        myRef.child("Towing_Complaints").child(root).child(phone).child("complain "+ n+":").setValue(complainn);
                                                        n++;
                                                        Toast.makeText(getContext(), "ID exist phone doesn't Complain submitted", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    else {
                                        myRef.child("Towing_Complaints").child(curr).child(phone).child("complain "+ n+":").setValue(complainn);
                                        n++;
                                        Toast.makeText(getContext(), "Nohthing exist Complain submitted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
//                        myRef.child("Towing_complains").child(curr).child(phone).child("complain "+n).setValue(complainn);
//                        n++;
//                        Toast.makeText(requireContext(), "complain submitted", Toast.LENGTH_SHORT).show();
                    }
                    else
                        phonee.setError("Enter Valid Phone Number upto 11 digits");

                }
                else
                {
                    Toast.makeText(requireContext(), "fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return view;
    }
}
/*

 String root=snapshot.getKey();


                               myRef.child("Towing_complains").child(curr).child(phone).child("complain "+n).setValue(complainn);
                                n++;
                              Toast.makeText(requireContext(), "complain submitted", Toast.LENGTH_SHORT).show();
 */