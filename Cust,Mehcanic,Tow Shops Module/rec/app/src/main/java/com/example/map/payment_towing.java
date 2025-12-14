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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Random;

public class payment_towing extends AppCompatActivity {
    TextView accountnumber,username;
    ImageView copytoclipicon;
    Button requestbtn;
    TextView  add;
    ImageView pu;//picUpload
    Boolean isImageUploaded;
    Uri filepath;
    Bitmap bitmap;
    String oname,ocnic,oshop,shopreg,ophone,oemail,opass,oimage,oid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_towing);
        username=findViewById(R.id.username);
        requestbtn=findViewById(R.id.reqbtn);
        isImageUploaded = false;
        pu = findViewById(R.id.teventBrowse);
        add=findViewById(R.id.add);
        Intent intent = getIntent();
        oname= intent.getStringExtra("oname");
        oid= intent.getStringExtra("ouserid");
        ocnic= intent.getStringExtra("ocnic");
        oshop= intent.getStringExtra("oshop");  //shop_name
        shopreg= intent.getStringExtra("oregno");
        ophone= intent.getStringExtra("ophone");
        oemail= intent.getStringExtra("oemail");
        opass= intent.getStringExtra("opassword");
        oimage= intent.getStringExtra("oimage");

        String msg= "Dear, "+oname;
        username.setText(msg);
        accountnumber=findViewById(R.id.accnum);
        copytoclipicon=findViewById(R.id.copytoclipboard);
        copytoclipicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("label",accountnumber.getText().toString());
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(payment_towing.this, "copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Dexter.withActivity(payment_towing.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    ProgressDialog dialog = new ProgressDialog(payment_towing.this);
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

                                            HelperTow towing = new HelperTow(oid, oname, ocnic, oshop, shopreg, ophone, oemail, opass, oimage,uri.toString(),date.toString(),time.toString(),"3000");
                                            FirebaseDatabase.getInstance().getReference("Admin").child("towing_requests")
                                                    .child(oid)
                                                    .setValue(towing).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                FirebaseAuth auth;
                                                                auth=FirebaseAuth.getInstance();
                                                                auth.signOut();
                                                                Toast.makeText(payment_towing.this, "Request sent successfully!", Toast.LENGTH_LONG).show();
                                                                Intent intentt=new Intent(payment_towing.this, done.class);
                                                                dialog.dismiss();
                                                                startActivity(intentt);
                                                                finish();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(payment_towing.this, "not stored in db", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(payment_towing.this, "Please Attach Payment Receipt!", Toast.LENGTH_SHORT).show();
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
                .setMessage("You cannot exit!")  ;

        builder.setNegativeButton("ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}