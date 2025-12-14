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

public class login_customer extends AppCompatActivity {
    EditText emailaddress,emailpass;
    FirebaseAuth fauth;
    Button login_btn;
    TextView forgetpass;
    DatabaseReference myRef,m;
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

        forgetpass=findViewById(R.id.resetpass);
        login_btn=findViewById(R.id.login);
        myRef= FirebaseDatabase.getInstance().getReference("users");
        //m= FirebaseDatabase.getInstance().getReference("History_Tow");
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
                                Toast.makeText(login_customer.this,"Reset Link sent to your email address.", Toast.LENGTH_LONG);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login_customer.this,"Error! Reset Link is not Sent."+e.getMessage(),Toast.LENGTH_LONG);
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
                Intent intent=new Intent(login_customer.this, registerall.class);
                startActivity(intent);
                finish();
            }
        });
        showpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailpass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    showpassbtn.setImageResource(R.drawable.hide_pass);
                    //Show Password
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
                                //if(fauth.getCurrentUser().isEmailVerified()){
                                DatabaseReference re=FirebaseDatabase.getInstance().getReference("user");
                                Query cu = re.orderByChild("uid");
                                cu.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childs : snapshot.getChildren()){
                                            if(childs.child("email").getValue().toString().equals(em)){
                                                status ="yes";
                                            }
                                        }
                                        if(status=="yes") {
                                            Toast.makeText(login_customer.this,"Login Successfully!",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(login_customer.this, cust_mainHome.class));
                                            finish();
                                        }
                                        else if(status!="yes") {
                                            Toast.makeText(login_customer.this,"Invalid Customer Account!",Toast.LENGTH_LONG).show();
                                            fauth.signOut();
                                        }
                                        else{
                                            Toast.makeText(login_customer.this,"Try Again!",Toast.LENGTH_LONG).show();
                                            fauth.signOut();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                               /* }
                                else{
                                    Toast.makeText(login_customer.this,"Login Failed! verify your account",Toast.LENGTH_LONG).show();
                                }*/
                            }
                            else {
                                Toast.makeText(login_customer.this,"Email/Password are incorrect",Toast.LENGTH_LONG).show();
                                fauth.signOut();
                            }
                        }
                    });

                }
            }
        });
    }
}