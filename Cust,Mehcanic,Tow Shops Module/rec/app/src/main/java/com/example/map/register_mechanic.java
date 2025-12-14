package com.example.map;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;


public class register_mechanic extends Fragment {
    private String title;
    boolean isImageUploaded,isfrontImageUploaded,isbackImageUploaded;
    Uri filepath, fpath,bpath;
    Bitmap bitmap,fbitmap,bbitmap;
    ImageView pu,pr;//picUpload, picRetrieve
    //for cnic
    ImageView frontsidecnic,backsidecnic;
    String capname;//name in capital
    TextView addback,addfront,submitback,submitfront;

    String mechname, mechcnic, mechphone, password, emailaddress, mechage;
    ImageView mpu; //mech_pic_upload
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth mechauth;
    String status = "no";
    //For spinner
    TextView show_servicesTextView; //1
    EditText name,cnic,phone,age,pass,email;
    ArrayList<Integer> services_list = new ArrayList<>(); //2
    boolean[] selectedServices; //3
    String[] services = {"Oil/filter changed", "New tires", "Battery replacement", "Replace Air-filter", "Brake work",
            "Testing electrical & mechanical system", "Engine tune-up", "Wheels balance & alignment"}; //4
    StringBuilder stringBuilder;

    public register_mechanic(String title) {
        this.title = title;
    }
    public register_mechanic() {
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
        View view = inflater.inflate(R.layout.fragment_register_mechanic, container, false);
        show_servicesTextView = view.findViewById(R.id.mservices);
        mechauth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        isImageUploaded = false;
        isfrontImageUploaded=false;
        isbackImageUploaded=false;

        mpu = view.findViewById(R.id.picUpload); //image
        addback=view.findViewById(R.id.addb);
        addfront= view.findViewById(R.id.addf);
        submitback=view.findViewById(R.id.submit_picb);
        submitfront=view.findViewById(R.id.submit_picf);
        frontsidecnic=view.findViewById(R.id.frontside);
        backsidecnic=view.findViewById(R.id.backside);

        name = view.findViewById(R.id.username);
        cnic = view.findViewById(R.id.cnic);
        phone = view.findViewById(R.id.phone);
        age = view.findViewById(R.id.age);
        pass = view.findViewById(R.id.pass);
        email=view.findViewById(R.id.email);
        TextView signin = view. findViewById(R.id.signin);
        Button mechanicregister = view.findViewById(R.id.mechregister);
        ImageView showpassbtn = view.findViewById(R.id.show_hide_pass); //eye btn

        //show_services = findViewById(R.id.mservices);
        //Initializing selected services array
        selectedServices = new boolean[services.length]; //5
//        stringBuilder= new StringBuilder();

        String unregx = "[a-zA-Z]{4,}";
        String phregx = "[0-9]{4}+-+[0-9]{7}";
        String passregx = ".{8,}";
        String cnicregx = "[0-9]{5}+-+[0-9]{7}+-+[0-9]{1}";

        //For going to login page
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), login_mechanic.class);
                startActivity(intent);
            }
        });

        //For image
        mpu.setOnClickListener(new View.OnClickListener() {
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

        //eye btn for pass
        showpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    showpassbtn.setImageResource(R.drawable.hide_pass);
                    //Show Password
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    showpassbtn.setImageResource(R.drawable.show_pass);
                    //Hide Password
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                pass.setSelection(pass.getText().length());
            }
        });
        //on click of register button
        mechanicregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()==true && isImageUploaded==true && isfrontImageUploaded==true && isbackImageUploaded==true){
                    mechname = name.getText().toString();
                    mechcnic = cnic.getText().toString();
                    mechphone = phone.getText().toString();
                    password = pass.getText().toString();
                    emailaddress=email.getText().toString();
                    mechage=age.getText().toString();

                    DatabaseReference re=FirebaseDatabase.getInstance().getReference("Admin").child("deleted_mechanic_accounts");
                    Query cu = re.orderByChild("mid");
                    cu.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childs : dataSnapshot.getChildren()){
                                if(childs.child("memail").getValue().equals(emailaddress)){
                                    Toast.makeText(getContext(),"Account is Blocked!",Toast.LENGTH_LONG).show();
                                    status ="yes";
                                }
                            }
                            if(status!="yes"){
                                mechauth.createUserWithEmailAndPassword(emailaddress,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            ProgressDialog dialog=new ProgressDialog(getContext());
                                            dialog.setTitle("Mechanic Registration");
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

                                                                    //for front side
                                                                    FirebaseStorage storage=FirebaseStorage.getInstance();
                                                                    StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(70));
                                                                    uploader.putFile(fpath)
                                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                                                {
                                                                                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                        @Override
                                                                                        public void onSuccess(Uri furi) {
                                                                                            //for back side
                                                                                            FirebaseStorage storage=FirebaseStorage.getInstance();
                                                                                            StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(70));
                                                                                            uploader.putFile(bpath)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                                                                        {
                                                                                                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Uri buri) {

                                                                                                                    FirebaseUser user= mechauth.getCurrentUser();
                                                                                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            if(task.isSuccessful()){
                                                                                                                                Toast.makeText(getContext(), "Mechanic Registered Successfully. Please check your email for verification.", Toast.LENGTH_LONG).show();
                                                                                                                                name.setText("");
                                                                                                                                cnic.setText("");
                                                                                                                                phone.setText("");
                                                                                                                                pass.setText("");
                                                                                                                                email.setText("");
                                                                                                                                age.setText("");
                                                                                                                                dialog.dismiss();
                                                                                                                                // clear service list
                                                                                                                                services_list.clear();
                                                                                                                                //clear txt view value
                                                                                                                                show_servicesTextView.setText("");
                                                                                                                            }
                                                                                                                            else{
                                                                                                                                Toast.makeText(getContext(), "email verification not sent", Toast.LENGTH_LONG).show();
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                                    String mechuserid=mechauth.getInstance().getCurrentUser().getUid();
                                                                                                                    Intent intent=new Intent(getContext(), payment_mech.class);
                                                                                                                    intent.putExtra("mechanicname",capname);
                                                                                                                    intent.putExtra("mechanicuserid",mechuserid);
                                                                                                                    intent.putExtra("mechaniccnic",cnic.getText().toString());
                                                                                                                    intent.putExtra("mechanicphone",phone.getText().toString());
                                                                                                                    intent.putExtra("mechanicemail",email.getText().toString());
                                                                                                                    intent.putExtra("mechanicpassword",pass.getText().toString());
                                                                                                                    intent.putExtra("mechanicage",age.getText().toString());
                                                                                                                    intent.putExtra("mechanicimage",uri.toString());
                                                                                                                    intent.putExtra("frontcnic",furi.toString());
                                                                                                                    intent.putExtra("backcnic",buri.toString());
                                                                                                                    intent.putExtra("mechaniccategory",stringBuilder.toString());

                                                                                                                    startActivity(intent);
                                                                                                                    dialog.dismiss();
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
                else{
                    Toast.makeText(getContext(), "wrong validation", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //selection of service categories
        show_servicesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initializing alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Select services").setIcon(R.drawable.ic_services);
                builder.setCancelable(false);
                builder.setMultiChoiceItems(services, selectedServices, new DialogInterface.OnMultiChoiceClickListener() {
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
//                        StringBuilder
                                stringBuilder=new StringBuilder();

                        for (int j = 0; j < services_list.size(); j++) {
                            //concat array values
                            stringBuilder.append(services[services_list.get(j)]);

                            if (j != services_list.size() - 1)
                                stringBuilder.append(",");
                        }
                        show_servicesTextView.setText(stringBuilder.toString());
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
                        show_servicesTextView.setText("");

                    }
                });
                builder.show();
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
        addfront.setOnClickListener(new View.OnClickListener() {
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
                                startActivityForResult(Intent.createChooser(intent,"select an Image File"),2);

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
        addback.setOnClickListener(new View.OnClickListener() {
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
                                startActivityForResult(Intent.createChooser(intent,"select an Image File"),3);

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
        return view;
    }
    Boolean validate(){
        mechname = name.getText().toString();
        mechcnic = cnic.getText().toString();
        mechphone = phone.getText().toString();
        password = pass.getText().toString();
        emailaddress=email.getText().toString();
        mechage=age.getText().toString();
        //Toast.makeText(getContext(), ""+mechname+mechcnic+mechphone+pass+emailaddress+mechage, Toast.LENGTH_SHORT).show();
//        f=custImage.toString();

        String nameregex = "[a-zA-Z ]{4,}";
        String phregx = "[0-9]{4}+-+[0-9]{7}";
        String passregx = ".{8,}";
        String cnicregx = "[0-9]{5}+-+[0-9]{7}+-+[0-9]{1}";
        // e = show_services.getText().toString();
        //  if(!a.isEmpty() && !b.isEmpty() && !c.isEmpty() && !d.isEmpty())
        if (!mechname.isEmpty() && !mechcnic.isEmpty() && !mechphone.isEmpty() && !password.isEmpty() && !emailaddress.isEmpty() && !mechage.isEmpty() && isImageUploaded==true && isbackImageUploaded==true && isfrontImageUploaded==true)
        {
            if(!mechname.matches(nameregex)){
                name.setError("Username must be charecters only");
                return false;
            }
            if(mechname.matches(nameregex)){
                StringTokenizer tokenizer = new StringTokenizer(mechname);

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
                mechname= String.valueOf(result.deleteCharAt(result.length() - 1));
                capname=mechname;
                return true;
            }

            if (!mechphone.matches(phregx) && mechphone.length() != 12) {
                phone.setError("PhoneNumber must be 11 digits long");
                return false;
            }
            if (!password.matches(passregx)) {
                pass.setError("Password must be 8 characters long");
                return false;
            }
            if (!mechcnic.matches(cnicregx)) {
                cnic.setError("Enter Valid cnic with dashes");
                return false;
            }
            if (!emailaddress.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()) {
                return true;
            } else {
                email.setError("Enter valid Email address!");
                return false;
            }
        }
        else
        {
            if (mechname.isEmpty())
                name.setError("Fill this field!");
            if (mechcnic.isEmpty())
                cnic.setError("Fill this field!");
            if (mechphone.isEmpty())
                phone.setError("Fill this field!");
            if (password.isEmpty()) {
                pass.setError("Fill this field!");
            }
            if(emailaddress.isEmpty()){
                email.setError("Fill this field!");
            }
            if (isImageUploaded==false) {
                Toast.makeText(getContext(), "Add Mechanic Picture!", Toast.LENGTH_SHORT).show();
            }
            if (isfrontImageUploaded==false) {
                Toast.makeText(getContext(), "Add Front CNIC Picture!", Toast.LENGTH_SHORT).show();
            }
            if (isbackImageUploaded==false) {
                Toast.makeText(getContext(), "Add Back CNIC Picture!", Toast.LENGTH_SHORT).show();
            }
            if(mechage.isEmpty()){
                age.setError("Fill this field!");
            }
//            if(f.isEmpty()){
//                Toast.makeText(getContext(), "Upload Profile Image", Toast.LENGTH_SHORT).show();
//            }
            return false;
        }
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
                mpu.setImageBitmap(bitmap);
                isImageUploaded = true;
            }
            catch (Exception e){

            }
        }
        if(requestCode==2 && resultCode==RESULT_OK && data !=null)
        {
            fpath=data.getData();
            try {
                InputStream inputStream=getActivity().getContentResolver().openInputStream(fpath);
                fbitmap= BitmapFactory.decodeStream(inputStream);
                frontsidecnic.setImageBitmap(fbitmap);
                isfrontImageUploaded = true;
            }
            catch (Exception e){

            }
        }
        if(requestCode==3 && resultCode==RESULT_OK && data !=null)
        {
            bpath=data.getData();
            try {
                InputStream inputStream=getActivity().getContentResolver().openInputStream(bpath);
                bbitmap= BitmapFactory.decodeStream(inputStream);
                backsidecnic.setImageBitmap(bbitmap);
                isbackImageUploaded = true;
            }
            catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}