package com.example.map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login_tow extends AppCompatActivity {
    EditText emailaddress,emailpass;
    FirebaseAuth fauth;
    Button login_btn;
    TextView forgetpass;
    DatabaseReference myRef;
    TextView signup;
    String status="no";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);
        signup=findViewById(R.id.signup);
        login_btn=findViewById(R.id.login);
        ImageView showpassbtn=findViewById(R.id.show_hide_pass);
        emailaddress=findViewById(R.id.emailaddress);
        emailpass=findViewById(R.id.epass);
        showpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailpass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    showpassbtn.setImageResource(R.drawable.hide_pass);
                    emailpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    showpassbtn.setImageResource(R.drawable.show_pass);
                    //Hide Password
                    emailpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                emailpass.setSelection(emailpass.getText().length());
            }
        });

        forgetpass=findViewById(R.id.resetpass);
        login_btn=findViewById(R.id.login);
        myRef= FirebaseDatabase.getInstance().getReference("Towing_Shop");
        fauth=FirebaseAuth.getInstance();

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail=new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your Email to receive reset link");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        String mail=resetMail.getText().toString();
                        fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(login_tow.this,"Reset Link sent to your email address.", Toast.LENGTH_LONG);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login_tow.this,"Error! Reset Link is not Sent."+e.getMessage(),Toast.LENGTH_LONG);
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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login_tow.this, registerall.class);
                startActivity(intent);
                finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get all values
                String em=emailaddress.getText().toString();
                String pass=emailpass.getText().toString();

                if(em.isEmpty()||pass.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"fill all fields",Toast.LENGTH_LONG).show();
                }
                else {
                    fauth.signInWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // if(fauth.getCurrentUser().isEmailVerified()){
                                DatabaseReference re=FirebaseDatabase.getInstance().getReference("Towing_Shop");
                                Query cu = re.orderByChild("shopid");
                                cu.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot childs : snapshot.getChildren()){
                                            String chrg=childs.child("charges").getValue().toString();
                                            int inn=Integer.valueOf(chrg);
                                            if(childs.child("owner_email").getValue().toString().equals(em) && inn>1){
                                                status ="yes";
                                            }
                                            if((inn<=1)){
                                                String oid=childs.child("shopid").getValue().toString();
                                                String sn=childs.child("shop_name").getValue().toString();
                                                String on=childs.child("owner_name").getValue().toString();
                                                String oc=childs.child("owner_cnic").getValue().toString();
                                                String op=childs.child("owner_phone").getValue().toString();
                                                String oe=childs.child("owner_email").getValue().toString();
                                                String ops=childs.child("password").getValue().toString();
                                                String orn=childs.child("shop_regno").getValue().toString();
                                                String oi=childs.child("reg_img").getValue().toString();
                                                Intent intent=new Intent(login_tow.this,account_deactivate_towing.class);
                                                intent.putExtra("oname",on);
                                                intent.putExtra("ouserid",oid);
                                                intent.putExtra("ocnic",oc);
                                                intent.putExtra("ophone",op);
                                                intent.putExtra("oemail",oe);
                                                intent.putExtra("opassword",ops);
                                                intent.putExtra("oregno",orn);
                                                intent.putExtra("oimage",oi);
                                                intent.putExtra("oshop",sn);
                                                startActivity(intent);
                                                finish();
                                                Toast.makeText(login_tow.this,sn+" Your Account is deactivated!",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        if(status=="yes") {
                                            Toast.makeText(login_tow.this,"Login Successfully!",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(login_tow.this,Towing_home.class));
                                            finish();
                                        }
                                        else if(status!="yes") {
                                            Toast.makeText(login_tow.this,"Invalid Towing Account!",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(login_tow.this,"Try Again!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            /*else {
                                Toast.makeText(login_mechanic.this,"Email not verified!",Toast.LENGTH_LONG).show();
                            }}*/
                            else {
                                Toast.makeText(login_tow.this,"Email/Password are incorrect",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });

       /* login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get all values
                String em=emailaddress.getText().toString();
                String pass=emailpass.getText().toString();

                if(em.isEmpty()||pass.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"fill all fields",Toast.LENGTH_LONG).show();
                }
                else {
                    DatabaseReference re=FirebaseDatabase.getInstance().getReference("mechanic");
                    Query query = re.orderByChild("mid");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            fauth.signInWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        //is email verified?
                                        if(fauth.getCurrentUser().isEmailVerified()){
                                            Toast.makeText(login_mechanic.this,"email verified!",Toast.LENGTH_LONG).show();
                                            }else{
                                            Toast.makeText(login_mechanic.this,"email is not verified!",Toast.LENGTH_LONG).show();
                                        }
                                        for (DataSnapshot childs : dataSnapshot.getChildren()){
                                            if(childs.child("memail").getValue().equals(em)){
                                                Toast.makeText(login_mechanic.this,"matched Successfully!",Toast.LENGTH_LONG).show();
                                                */
        /*startActivity(new Intent(login_mechanic.this, cust_mainHome.class));
                                                finish();*/
        /*
                                            }
                                        }
                                        }
                                    else {
                                        Toast.makeText(login_mechanic.this,"Email/Password are incorrect",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });*/
        /*.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(login_mechanic.this,"Email/Password are incorrect",Toast.LENGTH_LONG).show();
                                }
                            });*/
        /*
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });*/
    }
}