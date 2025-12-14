package com.example.map;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Random;

public class account_deactivation_mechanic extends AppCompatActivity {
    TextView accountnumber,username;
    ImageView copytoclipicon;
    Button requestbtn,backbtn;
    TextView  add;
    FirebaseAuth auth;
    ImageView pu;//picUpload
    Uri filepath;
    Bitmap bitmap;
    String mid,mname,categg,charges,mage,mcnic,memail,cnicback,cnicfront,mimage,mpass,mphone,recimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_deactivation_mechanic);
        username=findViewById(R.id.username);
        requestbtn=findViewById(R.id.reqbtn);
        backbtn=findViewById(R.id.backk);
        pu = findViewById(R.id.teventBrowse);
        add=findViewById(R.id.add);
        Intent intent = getIntent();
        /*intent.putExtra("id",mid);
        intent.putExtra("name",mname);
        intent.putExtra("categ",categ);
        intent.putExtra("age",mage);
        intent.putExtra("cnic",mcnic);
        intent.putExtra("email",memail);
        intent.putExtra("cnicb",cnicback);
        intent.putExtra("cnicf",cnicfront);
        intent.putExtra("pass",mpass);
        intent.putExtra("phone",mphone);
        intent.putExtra("rimg",recimg);
        intent.putExtra("charges",chargess);
        intent.putExtra("mimg",mimage);*/
        mid= intent.getStringExtra("id");
        mname= intent.getStringExtra("name");
        categg= intent.getStringExtra("categ");
        charges= intent.getStringExtra("charges");  //shop_name
        mage= intent.getStringExtra("age");
        mcnic= intent.getStringExtra("cnic");
        memail= intent.getStringExtra("email");
        cnicback= intent.getStringExtra("cnicb");
        cnicfront= intent.getStringExtra("cnicf");
        mimage= intent.getStringExtra("mimage");
                mpass=intent.getStringExtra("pass");
                mphone=intent.getStringExtra("phone");
                recimg=intent.getStringExtra("rimg");


        String msg= "Dear, "+mname;
        username.setText(msg);
        accountnumber=findViewById(R.id.accnum);
        copytoclipicon=findViewById(R.id.copytoclipboard);
        copytoclipicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("label",accountnumber.getText().toString());
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(account_deactivation_mechanic.this, "copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getInstance().signOut();
                Intent i=new Intent(account_deactivation_mechanic.this, language_user.class);
                startActivity(i);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Dexter.withActivity(account_deactivation_mechanic.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"select an Image File"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        requestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(account_deactivation_mechanic.this);
                dialog.setTitle("Sending Registration Request");
                dialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference uploader = storage.getReference("Image1" + new Random().nextInt(70));
                uploader.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Calendar calendar = Calendar.getInstance();
                                        LocalTime time = null;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                             time = LocalTime.now();
                                        }
                                        LocalDate date = null;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            date = LocalDate.now();
                                        }

                                        HelperMechanicRegistration towing = new HelperMechanicRegistration(mid, mname, mcnic, mage, mphone, memail, mpass, mimage, categg,uri.toString(),"3000",cnicback,cnicfront);
                                        FirebaseDatabase.getInstance().getReference("Admin").child("mechanic_activation_requests")
                                                .child(mid)
                                                .setValue(towing).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(account_deactivation_mechanic.this, "request sent successfully!", Toast.LENGTH_LONG).show();
                                                            auth.getInstance().signOut();
                                                            Intent i=new Intent(account_deactivation_mechanic.this, done.class);
                                                            startActivity(i);
                                                            finish();


                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(account_deactivation_mechanic.this, "not stored in db", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                });

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                dialog.setMessage((int) percent + "%");
                            }
                        });

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK && data !=null)
        {
            filepath=data.getData();
            try {
                InputStream inputStream=getApplication().getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                pu.setImageBitmap(bitmap);
                add.setText("Re-upload");

            }
            catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}