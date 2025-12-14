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

public class register_towing extends Fragment {
    private String title;
    String oname,ocnic,oshop,shopreg,ophone,oemail,opass;
    Uri filepath;
    Bitmap bitmap;
    ImageView pu;//picUpload
    TextView add;
    TextView submit_successfully;
    FirebaseAuth auth;
    String status = "no",capname;
    boolean isImageUploaded;
    EditText owner_name,owner_cnic,shop_name,reg_no,phone,email,password;
    public register_towing() {
        // Required empty public constructor
    }
    public register_towing(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_register_towing, container, false);
        isImageUploaded=false;
        auth= FirebaseAuth.getInstance();
        pu = view.findViewById(R.id.teventBrowse);
        add=view.findViewById(R.id.add);
        submit_successfully=view.findViewById(R.id.submit_pic);
         owner_name=view.findViewById(R.id.owner_name);
         owner_cnic=view.findViewById(R.id.ownercnic);
         shop_name=view.findViewById(R.id.Shop_name);
         reg_no = view.findViewById(R.id.reg_no);
         phone = view.findViewById(R.id.tphone);
         email=view.findViewById(R.id.email);
         password = view.findViewById(R.id.pass);
        TextView signin = view.findViewById(R.id.signin);
        Button register = view.findViewById(R.id.register);

        ImageView showpassbtn = view.findViewById(R.id.tshow_hide_pass); //eye btn

//for eye btn
        showpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    showpassbtn.setImageResource(R.drawable.hide_pass);
                    //Show Password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    showpassbtn.setImageResource(R.drawable.show_pass);
                    //Hide Password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                password.setSelection(password.getText().length());
            }
        });

        //For going to login page
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), login_tow.class);
                startActivity(intent);
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

    //auto format cnic
        owner_cnic.addTextChangedListener(new TextWatcher() {
            int len=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = owner_cnic.getText().toString();
                len = str.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = owner_cnic.getText().toString();

                if ((str.length() == 5 && len < str.length()) || (str.length() == 13 && len < str.length())) {
                    //checking length  for backspace.
                   owner_cnic.append("-");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
});

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()==true && isImageUploaded==true){
                    auth.createUserWithEmailAndPassword(oemail, opass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                oname = owner_name.getText().toString();
                                ocnic = owner_cnic.getText().toString();
                                oshop = shop_name.getText().toString();
                                shopreg = reg_no.getText().toString();
                                ophone = phone.getText().toString();
                                oemail = email.getText().toString();
                                opass = password.getText().toString();
                                DatabaseReference re=FirebaseDatabase.getInstance().getReference("/Admin/deleted_towing_accounts");
                                Query cu = re.orderByChild("shopid");
                                cu.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childs : snapshot.getChildren()){
                                            if(childs.child("owner_email").getValue().equals(oemail)){
                                                Toast.makeText(getContext(),"Account is Blocked!",Toast.LENGTH_LONG).show();
                                                auth.getCurrentUser().delete();
                                                status ="yes";
                                            }
                                        }
                                        if(status!="yes"){
                                            String towingid = auth.getInstance().getCurrentUser().getUid();
                                            ProgressDialog dialog=new ProgressDialog(getContext());
                                            dialog.setTitle("Towing Registration");
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
                                                                    FirebaseUser user= auth.getCurrentUser();
                                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                Toast.makeText(getContext(), "Towing Shop Registered Successfully. Please check your email for verification.", Toast.LENGTH_LONG).show();
                                                                                owner_name.setText("");
                                                                                owner_cnic.setText("");
                                                                                shop_name.setText("");
                                                                                reg_no.setText("");
                                                                                phone.setText("");
                                                                                email.setText("");
                                                                                password.setText("");
                                                                            }
                                                                            else{
                                                                                Toast.makeText(getContext(), "email verification not sent", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                                    Intent intent=new Intent(getContext(), payment_towing.class);
                                                                    intent.putExtra("oname",capname);
                                                                    intent.putExtra("ouserid",towingid);
                                                                    intent.putExtra("ocnic",owner_cnic.getText().toString());
                                                                    intent.putExtra("ophone",phone.getText().toString());
                                                                    intent.putExtra("oemail",email.getText().toString());
                                                                    intent.putExtra("opassword",password.getText().toString());
                                                                    intent.putExtra("oregno",reg_no.getText().toString());
                                                                    intent.putExtra("oimage",uri.toString());
                                                                    intent.putExtra("oshop",shop_name.getText().toString());
                                                                    startActivity(intent);
                                                                        /*
                                                                    //HelperTowingShop towing = new HelperTowingShop(towingid, oname, ocnic, oshop, shopreg, ophone, oemail, opass, uri.toString());
                                                                    FirebaseDatabase.getInstance().getReference("Towing_Shop")
                                                                            .child(towingid)
                                                                            .setValue(towing).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        dialog.dismiss();
                                                                                        Intent intent=new Intent(getContext(),Towing_home.class);
                                                                                        startActivity(intent);
                                                                                        Toast.makeText(getContext(), "Towing Shop Registered Successfully👍", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(getContext(), "Fail To Register", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });
                                                                    Toast.makeText(getContext(), "Image Uploaded to firebase Successfully👍", Toast.LENGTH_LONG).show();*/

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
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else {
                                Toast.makeText(getContext(),"Email already exists or invalid email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "wrong validation", Toast.LENGTH_SHORT).show();
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }
    Boolean validate(){
        if (isImageUploaded==false) {
            Toast.makeText(getContext(), "Add Shop Registration Picture!", Toast.LENGTH_SHORT).show();
        }
        oname = owner_name.getText().toString();
        ocnic = owner_cnic.getText().toString();
        oshop = shop_name.getText().toString();
        shopreg = reg_no.getText().toString();
        ophone = phone.getText().toString();
        oemail = email.getText().toString();
        opass = password.getText().toString();

        String nameregex = "[a-zA-Z ]{4,}";
        String phregx = "[0-9]{4}+-+[0-9]{7}";
        String passregx = ".{8,}";
        String cnicregx = "[0-9]{5}+-+[0-9]{7}+-+[0-9]{1}";
        // e = show_services.getText().toString();
        //  if(!a.isEmpty() && !b.isEmpty() && !c.isEmpty() && !d.isEmpty())
        if (!oname.isEmpty() && !ocnic.isEmpty() && !oshop.isEmpty() && !oemail.isEmpty() && !oemail.isEmpty() && !shopreg.isEmpty() && !ophone.isEmpty() && !opass.isEmpty() && isImageUploaded==true)
        {
            if(!oname.matches(nameregex)){
                owner_name.setError("Username must be charecters only");
                return false;
            }
            if (!ophone.matches(phregx) && ophone.length() != 12) {
                phone.setError("PhoneNumber must be 11 digits long");
                return false;
            }
            if(oname.matches(nameregex)){
                StringTokenizer tokenizer = new StringTokenizer(oname);

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
                oname= String.valueOf(result.deleteCharAt(result.length() - 1));
                capname=oname;
                return true;
            }

            if (!opass.matches(passregx)) {
                password.setError("Password must be 8 characters long");
                return false;
            }
            if (!ocnic.matches(cnicregx)) {
                owner_cnic.setError("Enter Valid cnic with dashes");
                return false;
            }
            if (isImageUploaded==false) {
                Toast.makeText(getContext(), "Add Shop Registration Picture!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!oemail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(oemail).matches()) {
                //Toast.makeText(this, "Email Verified !", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                email.setError("Enter valid Email address!");
                return false;
            }
        }
        else
        {
            if (oname.isEmpty())
                owner_name.setError("Fill this field!");
            if (ocnic.isEmpty())
                owner_cnic.setError("Fill this field!");
            if (ophone.isEmpty())
                phone.setError("Fill this field!");
            if (opass.isEmpty()) {
                password.setError("Fill this field!");
            }
            if(oemail.isEmpty()){
                email.setError("Fill this field!");
            }
            if(oshop.isEmpty()){
                shop_name.setError("Fill this field!");
            }
            if (isImageUploaded==false) {
                Toast.makeText(getContext(), "Add Shop Registration Picture!", Toast.LENGTH_SHORT).show();
            }
            if(shopreg.isEmpty()){
                reg_no.setError("Fill this field!");
            }
//            if(f.isEmpty()){
//                Toast.makeText(getContext(), "Upload Profile Image", Toast.LENGTH_SHORT).show();
//            }
            return false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK && data !=null)
        {
            filepath=data.getData();
            try {
                InputStream inputStream=getActivity().getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                pu.setImageBitmap(bitmap);
                add.setText("Re-upload");
                submit_successfully.setText("Submitted");
                isImageUploaded=true;
            }
            catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}