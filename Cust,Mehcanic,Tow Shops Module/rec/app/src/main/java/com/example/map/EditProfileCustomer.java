package com.example.map;

import android.Manifest;
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
import androidx.appcompat.app.AlertDialog;
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


public class EditProfileCustomer extends Fragment {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    TextView editCnic,tow_name,tow_ph,forgetpass;
    EditText editName,editPh;
    TextView passreset, editEmail;
    Button saveChanges;
    ImageView pi;
    FirebaseAuth auth;
    String currUser,image;
    String name,phone,email,cnic;
    String newName, newPhone;
    NavigationView navigationView;
    ImageView showpassbtn;

    public EditProfileCustomer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_profile_customer, container, false);
        editName=view.findViewById(R.id.editProfileName);
        forgetpass=view.findViewById(R.id.resetpasswd);
        editPh=view.findViewById(R.id.editProfilePhone);
        editCnic=view.findViewById(R.id.editProfileCnic);
        pi=view.findViewById(R.id.editProfileImgofCust);

        editEmail=view.findViewById(R.id.editProfileEmail);
        saveChanges=view.findViewById(R.id.Csave_data);


        currUser=auth.getInstance().getCurrentUser().getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUser = reference.orderByChild("uid").equalTo(currUser);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child(currUser).child("username").getValue().toString();
                phone = snapshot.child(currUser).child("phone").getValue().toString();
                email = snapshot.child(currUser).child("email").getValue().toString();
                cnic = snapshot.child(currUser).child("cnic").getValue().toString();
                image=  snapshot.child(currUser).child("image").getValue(String.class);

                editName.setText(name);
                editCnic.setText(cnic);
                editPh.setText(phone);
                editEmail.setText(email);
                Picasso.get().load(image).into(pi); //to retireve image
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("You will receive reset password link on your email address '"+email+"'");

                passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        auth=FirebaseAuth.getInstance();
                        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
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


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName=editName.getText().toString();
                newPhone=editPh.getText().toString();
               //**// newEmail=editEmail.getText().toString();



                if(!newName.equals(name) || !newPhone.equals(phone)){

                    myRef.child("user").child(currUser).child("username").setValue(newName);
                    myRef.child("user").child(currUser).child("phone").setValue(newPhone);
              /*      myRef.child("user").child(currUser).child("email").setValue(newEmail);
                    myRef.child("user").child(currUser).child("password").setValue(newPass);*/
//                    myRef.child("user").child(currUser).child("cnic").setValue(newCnic);
                    Toast.makeText(getContext(), "Data has been Updated", Toast.LENGTH_SHORT).show();


                }
                else
                    Toast.makeText(getContext(), "Data is same and cannot be Updated", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}