package com.example.rec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class adminlogin extends AppCompatActivity {
EditText adminusername,adminpassword;
TextView resetpass;
    String username,password;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        getSupportActionBar().hide();

        Button loginbutton=findViewById(R.id.loginbtn);
        resetpass=findViewById(R.id.resetpass);
        fauth= FirebaseAuth.getInstance();
        adminusername=findViewById(R.id.username);
        adminpassword=findViewById(R.id.password);

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Are you sure you want to reset your password?");
                passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        fauth.sendPasswordResetEmail("jaweriajalil9@gmail.com").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(adminlogin.this,"Reset Link sent to your email address.", Toast.LENGTH_LONG);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(adminlogin.this,"Error! Reset Link is not Sent."+e.getMessage(),Toast.LENGTH_LONG);
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
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = adminusername.getText().toString();
                password = adminpassword.getText().toString();
                if(username.isEmpty()||password.isEmpty()) {
                    adminusername.setError("Please Enter Username!");
                    adminpassword.setError("Please Enter Password!");
                    Toast.makeText(getApplicationContext(),"Fill all fields",Toast.LENGTH_LONG).show();
                }
                else {
                    fauth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(adminlogin.this,"Admin Login Successfully!👍", Toast.LENGTH_LONG);
                                Intent intent = new Intent(adminlogin.this, MainActivity3.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(adminlogin.this,"Email/Password are incorrect",Toast.LENGTH_LONG).show();
                                fauth.signOut();
                            }
                        }
                    });

                }


            }
        });


    }
}