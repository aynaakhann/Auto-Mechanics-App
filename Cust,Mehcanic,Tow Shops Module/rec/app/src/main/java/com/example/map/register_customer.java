package com.example.map;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Random;
import java.util.StringTokenizer;

public class register_customer extends Fragment {

    String a, b, c, d, e,imagepath;
    EditText name,cnic,phone,pass,email;
    private String title;
    FirebaseAuth cauth;
    ImageView custImage;
    Uri filepath;
    Bitmap bitmap;
    boolean isImageUploaded;
    String status = "no",capname;
    ImageView pu,pr;//picUpload, picRetrieve
    String cname,ccnic,cphone,cpass,cemail;
    public register_customer(String title) {
        this.title = title;
    }

    public register_customer() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_register_customer, container, false);
        View view=inflater.inflate(R.layout.fragment_register_customer, container, false);
        cauth= FirebaseAuth.getInstance();
        isImageUploaded=false;
        ImageView showpassbtn=view.findViewById(R.id.show_hide_pass);
        //for customer info
        name = view.findViewById(R.id.username);
        cnic = view.findViewById(R.id.cnic);
        phone = view.findViewById(R.id.phone);
        pass = view.findViewById(R.id.pass);
        email= view.findViewById(R.id.emailid);
        TextView signin = view.findViewById(R.id.signin);
        Button register = view.findViewById(R.id.register);

        custImage = view.findViewById(R.id.custpicUpload); //image


        //For going to login page
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), login_customer.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()==true && isImageUploaded==true){
                    cname = name.getText().toString();
                    ccnic=cnic.getText().toString();
                    cphone=phone.getText().toString();
                    cpass=pass.getText().toString();
                    cemail=email.getText().toString();

                    DatabaseReference re=FirebaseDatabase.getInstance().getReference("Admin").child("deleted_customer_accounts");
                    Query cu = re.orderByChild("uid");
                    cu.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childs : dataSnapshot.getChildren()){
                                if(childs.child("email").getValue().equals(cemail)){
                                    Toast.makeText(getContext(),"Account is Blocked!",Toast.LENGTH_LONG).show();
                                    status ="yes";
                                }
                            }
                            if(status!="yes"){
                                cauth.createUserWithEmailAndPassword(cemail,cpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            ProgressDialog dialog=new ProgressDialog(getContext());
                                            dialog.setTitle("Customer Registration");
                                            dialog.show();
                                            FirebaseStorage storage=FirebaseStorage.getInstance();
                                            StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(70));
                                            uploader.putFile(filepath)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                        {
                                                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {

                                                                    FirebaseUser user= cauth.getCurrentUser();
                                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                Toast.makeText(getContext(), "Customer Registered Successfully. Please check your email for verification.", Toast.LENGTH_LONG).show();
                                                                                name.setText("");
                                                                                cnic.setText("");
                                                                                phone.setText("");
                                                                                pass.setText("");
                                                                                email.setText("");
                                                                                Intent i=new Intent(getContext(),login_customer.class);
                                                                                dialog.dismiss();
                                                                                startActivity(i);
                                                                                cauth.signOut();
                                                                            }
                                                                            else{
                                                                                Toast.makeText(getContext(), "email verification not sent", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                                    String cid=cauth.getInstance().getCurrentUser().getUid();
                                                                    HelperCustomerRegistration customer=new HelperCustomerRegistration(cid,capname,cemail,cphone,ccnic,cpass,uri.toString());
                                                                    FirebaseDatabase.getInstance().getReference("user").child(cid).setValue(customer);

                                                                }
                                                            });

                                                        }
                                                    })
                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                            float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                                            dialog.setMessage("Uploaded: "+(int)percent+" %");
                                                        }
                                                    });
                                        }
                                        else {
                                            Toast.makeText(getContext(),"Email already exists or invalid email",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        //For image
        custImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
        //auto format cnic
        cnic.addTextChangedListener(new TextWatcher() {
            int len=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = cnic.getText().toString();
                len = str.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = cnic.getText().toString();

                if ((str.length() == 5 && len < str.length()) || (str.length() == 13 && len < str.length())) {
                    //checking length  for backspace.
                    cnic.append("-");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //auto format phone
        phone.addTextChangedListener(new TextWatcher() {
            int len=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = phone.getText().toString();
                len = str.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = phone.getText().toString();
                if((str.length()==4 && len <str.length()))
                {
                    //checking length  for backspace.
                    phone.append("-");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //pass eye btn
        showpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    showpassbtn.setImageResource(R.drawable.hide_pass);
                    //Show Password
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    showpassbtn.setImageResource(R.drawable.show_pass);
                    //Hide Password
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                pass.setSelection(pass.getText().length());
            }
        });
        return view;
    }

    Boolean validate(){
        if (isImageUploaded==false) {
            Toast.makeText(getContext(), "Add Shop Registration Picture!", Toast.LENGTH_SHORT).show();
        }
        a = name.getText().toString();
        b = cnic.getText().toString();
        c = phone.getText().toString();
        d = pass.getText().toString();
        e= email.getText().toString();
//        f=custImage.toString();

        String nameregex = "[a-zA-Z ]{4,}";
        String phregx = "[0-9]{4}+-+[0-9]{7}";
        String passregx = ".{8,}";
        String cnicregx = "[0-9]{5}+-+[0-9]{7}+-+[0-9]{1}";
        // e = show_services.getText().toString();
        //  if(!a.isEmpty() && !b.isEm() && !c.isEmpty() && !d.isEmpty())
        if (!a.isEmpty() && !b.isEmpty() && !c.isEmpty() && !d.isEmpty() && !e.isEmpty())
        {
            if(!a.matches(nameregex)){
                name.setError("Username must be charecters only");
                return false;
            }
            if(a.matches(nameregex)){
                StringTokenizer tokenizer = new StringTokenizer(a);

                // Create a StringBuilder to build the modified sentence
                StringBuilder result = new StringBuilder();

                // Iterate through each word in the sentence
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();

                    // Capitalize the first letter of the word
                    String capitalizedWord = Character.toUpperCase(word.charAt(0)) + word.substring(1);
                    // Append the capitalized word to the result
                    result.append(capitalizedWord).append(" ");
                }
                // Remove the extra space at the end of the result
                a= String.valueOf(result.deleteCharAt(result.length() - 1));
                capname=a;
                return true;
            }
            if (!c.matches(phregx) && c.length() != 12) {
                phone.setError("PhoneNumber must be 11 digits long");
                return false;
            }
            if (!d.matches(passregx)) {
                pass.setError("Password must be 8 characters long");
                return false;
            }
            if (!b.matches(cnicregx)) {
                cnic.setError("Enter Valid cnic with dashes");
                return false;
            }
            if (!e.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                //Toast.makeText(this, "Email Verified !", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                email.setError("Enter valid Email address!");
                return false;
            }
        }
        else
        {
            if (a.isEmpty())
                name.setError("Fill this field!");
            if (b.isEmpty())
                cnic.setError("Fill this field!");
            if (c.isEmpty())
                phone.setError("Fill this field!");
            if (d.isEmpty()) {
                pass.setError("Fill this field!");
            }
            if(e.isEmpty()){
                email.setError("Fill this field!");
            }
            return false;
        }
    }
    private void uploadToFirebase() {
        ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setTitle("File Uploader");
        dialog.show();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(50));
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();
                                imagepath=uri.toString();
                                /*FirebaseDatabase.getInstance().getReference("user")
                                        .child(mauth.getCurrentUser().getUid()).child("image").setValue(uri.toString());*/
                                Toast.makeText(getContext(), "Uploaded Successfully"+imagepath, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded: "+(int)percent+"%");
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data !=null)
        {
            filepath=data.getData();
            try {
                InputStream inputStream=getActivity().getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                custImage.setImageBitmap(bitmap);
                isImageUploaded=true;
            }
            catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}