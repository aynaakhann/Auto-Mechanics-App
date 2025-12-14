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


public class EditMechanicProfile extends Fragment {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    Uri imagepath;
    Bitmap bitmapp;
    ImageView pi,showpassbtn; //profile image
    TextView editServices,editCnic,forgetpass;
    EditText editName,editPh,editAge,editEmail;
    Button saveChanges;
    FirebaseAuth auth;
    String currUser;
    String name,phone,age,email,cnic,services,image;
    String newName, newPhone, newEmail, newAge,newServices,newImage;
    NavigationView navigationView;
    ImageView profile_img;
    TextView mech_name,mech_ph;
    SwitchCompat switchCompat;
    public double latitudee, longitudee;
    private FusedLocationProviderClient fusedLocationClient;


    ArrayList<Integer> services_list = new ArrayList<>(); //2
    boolean[] selectedServices; //3

    String[] servicesProvided = {"Oil/filter changed", "New tires", "Battery replacement", "Replace Air-filter", "Brake work",
            "Testing electrical & mechanical system", "Engine tune-up", "Wheels balance & alignment"}; //4
    StringBuilder stringBuilder = new StringBuilder();


    public EditMechanicProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_mechanic_profile, container, false);
        pi=view.findViewById(R.id.editProfileImgofMech);
        editName=view.findViewById(R.id.editProfileNameofMech);
        editPh=view.findViewById(R.id.editProfilePhoneofMech);
        editCnic=view.findViewById(R.id.editProfileCnicofMech);
        editAge=view.findViewById(R.id.editProfileAgeofMech);
        forgetpass=view.findViewById(R.id.resetpasswd);
        editEmail=view.findViewById(R.id.editProfileEmailofMech);
        editServices=view.findViewById(R.id.editPrfoileServicesofMech);
        saveChanges=view.findViewById(R.id.saveChangesofMech);


        switchCompat=getActivity().findViewById(R.id.switchbtn);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        navigationView =getActivity().findViewById(R.id.navigationView);

        View header = navigationView.getHeaderView(0);

        profile_img=header.findViewById(R.id.mech_profile_img);
        mech_name=header.findViewById(R.id.mech_profile_name);
        mech_ph=header.findViewById(R.id.mech_profile_phone);


        currUser=auth.getInstance().getCurrentUser().getUid();

       selectedServices = new boolean[servicesProvided.length];

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

     switchCompat.setChecked(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("mechanic");
        Query checkUser = reference.orderByChild("mid").equalTo(currUser);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child(currUser).child("mname").getValue().toString();
                phone = snapshot.child(currUser).child("mphone").getValue().toString();
                email = snapshot.child(currUser).child("memail").getValue().toString();
                age = snapshot.child(currUser).child("mage").getValue().toString();
                cnic = snapshot.child(currUser).child("mcnic").getValue().toString();
                services = snapshot.child(currUser).child("category").getValue().toString();
                image=  snapshot.child(currUser).child("mimage").getValue(String.class);

                editName.setText(name);
                editAge.setText(age);
                editCnic.setText(cnic);
                editPh.setText(phone);
                editEmail.setText(email);
                editServices.setText(services);
              Picasso.get().load(image).into(pi); //to retireve image
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Can't Edit Email ID", Toast.LENGTH_SHORT).show();
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

//        //to upload image
//        pi.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse response)
//                            {
//                                Intent intent=new Intent(Intent.ACTION_PICK);
//                                intent.setType("image/*");
//                                startActivityForResult(Intent.createChooser(intent,"select an Image File"),1);
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse response) {
//
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        }).check();
//
//            }
//        });

        //for eye btn

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName=editName.getText().toString();
                newPhone=editPh.getText().toString();
                newEmail=editEmail.getText().toString();
                newAge=editAge.getText().toString();
                newServices=editServices.getText().toString();
                newImage=pi.toString();


                if(!newName.equals(name) || !newPhone.equals(phone)  || !newEmail.equals(email) || !newAge.equals(age) || !newServices.equals(services)){

                    myRef.child("mechanic").child(currUser).child("mname").setValue(newName);
                    myRef.child("mechanic").child(currUser).child("mphone").setValue(newPhone);
                    myRef.child("mechanic").child(currUser).child("memail").setValue(newEmail);
                    myRef.child("mechanic").child(currUser).child("mage").setValue(newAge);
                    myRef.child("mechanic").child(currUser).child("category").setValue(newServices);

//                    FirebaseStorage storage=FirebaseStorage.getInstance();
//                    StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(100));
//                    uploader.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    myRef.child("mechanic").child(currUser).child("mimage").setValue(uri.toString());
//                                    Picasso.get().load(uri).into(profile_img);
//                                }
//                            });
//                        }
//                    });


                    Toast.makeText(getContext(), "Data has been Updated", Toast.LENGTH_SHORT).show();

                    mech_name.setText(newName);
                    mech_ph.setText(newPhone);

                }
                else
                    Toast.makeText(getContext(), "Data is same and cannot be Updated", Toast.LENGTH_SHORT).show();
            }
        });
        editCnic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Can't Edit CNIC", Toast.LENGTH_SHORT).show();
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final androidx.appcompat.app.AlertDialog.Builder passwordResetDialog= new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
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

        //selection of service categories
        editServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initializing alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Select services").setIcon(R.drawable.ic_services);
                builder.setCancelable(false);
                builder.setMultiChoiceItems(servicesProvided, selectedServices, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            services_list.add(which);
                        }
                        else{
                            services_list.remove(Integer.valueOf(which));
                        }
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder=new StringBuilder();

                        for (int j = 0; j < services_list.size(); j++) {
                            //concat array values
                            stringBuilder.append(servicesProvided[services_list.get(j)]);

                            if (j != services_list.size() - 1)
                                stringBuilder.append(",");
                        }
                        editServices.setText(stringBuilder.toString());
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ArrayList<Integer> tempList = new ArrayList<>(services_list);

                        for (int item : tempList) {
                            selectedServices[item] = false;
                        }
                        services_list.clear();
                        editServices.setText("");

                    }
                });
                builder.show();
            }
        });
        return view;
    }

    private boolean isImageChanged() {
        if(!imagepath.equals(image)){
            FirebaseStorage storage=FirebaseStorage.getInstance();
            StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(100));
            uploader.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            myRef.child("mechanic").child(currUser).child("mimage").setValue(uri.toString());
                            Picasso.get().load(uri).into(profile_img);
                        }
                    });
                }
            });
            return true;
        }
        else
            return false;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1)
//        {
//            imagepath=data.getData();
//
//            try {
//                InputStream inputStream=getActivity().getContentResolver().openInputStream(imagepath);
//                bitmapp= BitmapFactory.decodeStream(inputStream);
//                pi.setImageBitmap(bitmapp);
//            }
//            catch (Exception e){
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }
}