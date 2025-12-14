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
import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.Random;

public class payment_mech extends AppCompatActivity {
    TextView accountnumber,username;
    ImageView copytoclipicon, logoimage;
    TextView  add;
    ImageView pu;//picUpload
    Boolean isImageUploaded;
    Button requestbtn;
    Uri filepath;
    Bitmap bitmap;
    ArrayList<String> mechservices = new ArrayList<>();
    String mechname, mechid,mechphone,mechcnic,mechemail,mechpassword,mechage,mechcategories,mechimage,cnicfront,cnicback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mech);
        username=findViewById(R.id.username);
        requestbtn=findViewById(R.id.reqbtn);
        isImageUploaded = false;
        Intent intent = getIntent();
        pu = findViewById(R.id.teventBrowse);
        add=findViewById(R.id.add);
        mechname= intent.getStringExtra("mechanicname");
        mechid= intent.getStringExtra("mechanicuserid");
        mechcnic= intent.getStringExtra("mechaniccnic");
        mechphone= intent.getStringExtra("mechanicphone");
        mechemail= intent.getStringExtra("mechanicemail");
        mechpassword= intent.getStringExtra("mechanicpassword");
        mechage= intent.getStringExtra("mechanicage");
        mechcategories= intent.getStringExtra("mechaniccategory");
        mechimage= intent.getStringExtra("mechanicimage");
        cnicfront= intent.getStringExtra("frontcnic");
        cnicback= intent.getStringExtra("backcnic");
        String msg= "Dear, "+mechname;

        username.setText(msg);
        accountnumber=findViewById(R.id.accnum);
        copytoclipicon=findViewById(R.id.copytoclipboard);
        copytoclipicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("label",accountnumber.getText().toString());
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(payment_mech.this, "copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Dexter.withActivity(payment_mech.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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

                        if(isImageUploaded==true){
                        ProgressDialog dialog = new ProgressDialog(payment_mech.this);
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
                                                HelperMechanicRegistration mechanic = new HelperMechanicRegistration(mechid, mechname, mechcnic, mechage, mechphone, mechemail, mechpassword, mechimage, mechcategories, uri.toString(),"3000",cnicfront,cnicback);
                                                FirebaseDatabase.getInstance().getReference("Admin").child("mechanic_requests")
                                                        .child(mechid)
                                                        .setValue(mechanic).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    FirebaseAuth auth;
                                                                    auth=FirebaseAuth.getInstance();
                                                                    auth.signOut();
                                                                    Toast.makeText(payment_mech.this, "Request sent", Toast.LENGTH_LONG).show();
                                                                    Intent intentt=new Intent(payment_mech.this, done.class);
                                                                    dialog.dismiss();
                                                                    startActivity(intentt);
                                                                    finish();
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(payment_mech.this, "not stored in db", Toast.LENGTH_LONG).show();
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
                        else if(isImageUploaded==false){
                            Toast.makeText(payment_mech.this, "Please Attach Payment Receipt!", Toast.LENGTH_SHORT).show();
                        }
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
                isImageUploaded = true;
            }
            catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Alert")
                .setIcon(R.drawable.ic_alert_error_msg)
                .setMessage("You cannot exit!");
        builder.setNegativeButton("ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}