package com.example.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class EditTowProfile extends Fragment {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    TextView editOwnerCnic,tow_name,tow_ph,forgetpass;
    EditText editOwnerName,editShopName,editPh,editEmail;
    Button saveChanges;
    FirebaseAuth auth;
    String currUser;
    String Oname,Shopname,Sphone,Semail,Ocnic,pass;
    String newName, newPhone, newEmail, newShopName,newPass,newCnic;
    NavigationView navigationView;

    SwitchCompat switchCompat;
    public double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;


    public EditTowProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_tow_profile, container, false);
        editOwnerName=view.findViewById(R.id.editProfileOwnerNameofTow);
        forgetpass=view.findViewById(R.id.resetpasswd);
        editPh=view.findViewById(R.id.editProfilePhoneofTow);
        editOwnerCnic=view.findViewById(R.id.editProfileOwnerCnicofTow);
        editShopName=view.findViewById(R.id.editProfileShopNameofTow);
        editEmail=view.findViewById(R.id.editProfileEmailofTow);
        saveChanges=view.findViewById(R.id.save_data);


        navigationView = getActivity().findViewById(R.id.towNavigationView);

        View header= navigationView.getHeaderView(0);

        tow_name=header.findViewById(R.id.tow_profile_name);
        tow_ph=header.findViewById(R.id.tow_profile_phone);

        currUser=auth.getInstance().getCurrentUser().getUid();

        editOwnerCnic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Can't Edit CNIC", Toast.LENGTH_SHORT).show();
            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Can't Edit Email ID", Toast.LENGTH_SHORT).show();
            }
        });
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
                    myRef.child("Towing_Shop").child(currUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String owner_name = snapshot.child("owner_name").getValue().toString();
                            String owner_phone = snapshot.child("owner_phone").getValue().toString();
                            String owner_email = snapshot.child("owner_email").getValue().toString();
                            String shopid = snapshot.child("shopid").getValue().toString();
                            String shopName = snapshot.child("shop_name").getValue().toString();

                            myRef.child("towing_locations").child(currUser).child("owner_name").setValue(owner_name);
                            myRef.child("towing_locations").child(currUser).child("owner_phone").setValue(owner_phone);
                            myRef.child("towing_locations").child(currUser).child("owner_email").setValue(owner_email);
                            myRef.child("towing_locations").child(currUser).child("shopid").setValue(shopid);
                            myRef.child("towing_locations").child(currUser).child("shop_name").setValue(shopName);
                            myRef.child("towing_locations").child(currUser).child("latitude").setValue(latitudee);
                            myRef.child("towing_locations").child(currUser).child("longitude").setValue(longitudee);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else
                    myRef.child("towing_locations").child(currUser).removeValue();
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Towing_Shop");
        Query checkUser = reference.orderByChild("shopid").equalTo(currUser);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Oname = snapshot.child(currUser).child("owner_name").getValue().toString();
                Sphone = snapshot.child(currUser).child("owner_phone").getValue().toString();
                Semail = snapshot.child(currUser).child("owner_email").getValue().toString();
                Shopname = snapshot.child(currUser).child("shop_name").getValue().toString();
                Ocnic = snapshot.child(currUser).child("owner_cnic").getValue().toString();

                editOwnerName.setText(Oname);
                editShopName.setText(Shopname);
                editOwnerCnic.setText(Ocnic);
                editPh.setText(Sphone);
                editEmail.setText(Semail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final androidx.appcompat.app.AlertDialog.Builder passwordResetDialog= new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("You will receive reset password link on your email address '"+Semail+"'");

                passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        auth=FirebaseAuth.getInstance();
                        auth.sendPasswordResetEmail(Semail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),"Reset Link sent to your email address.", Toast.LENGTH_LONG);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Error! Reset Link is not Sent."+e.getMessage(),Toast.LENGTH_LONG);
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                passwordResetDialog.create().show();
            }
        });

        //auto format phone
        editPh.addTextChangedListener(new TextWatcher() {
            int len=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = editPh.getText().toString();
                len = str.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = editPh.getText().toString();
                if((str.length()==4 && len <str.length()))
                {
                    //checking length  for backspace.
                    editPh.append("-");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //for eye btn


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName=editOwnerName.getText().toString();
                newPhone=editPh.getText().toString();
                newEmail=editEmail.getText().toString();
                newShopName=editShopName.getText().toString();
                newCnic=editOwnerCnic.getText().toString();


                if(!newName.equals(Oname) || !newPhone.equals(Sphone) || !newEmail.equals(Semail) || !newShopName.equals(Shopname) || !newCnic.equals(Ocnic)){

                    myRef.child("Towing_Shop").child(currUser).child("owner_name").setValue(newName);
                    myRef.child("Towing_Shop").child(currUser).child("owner_phone").setValue(newPhone);
                    myRef.child("Towing_Shop").child(currUser).child("owner_email").setValue(newEmail);
                    myRef.child("Towing_Shop").child(currUser).child("shop_name").setValue(newShopName);
                    myRef.child("Towing_Shop").child(currUser).child("password").setValue(newPass);
                    myRef.child("Towing_Shop").child(currUser).child("owner_cnic").setValue(newCnic);
                    Toast.makeText(getContext(), "Data has been Updated", Toast.LENGTH_SHORT).show();

                    tow_name.setText(newName);
                    tow_ph.setText(newPhone);

                }
                else
                    Toast.makeText(getContext(), "Data is same and cannot be Updated", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}