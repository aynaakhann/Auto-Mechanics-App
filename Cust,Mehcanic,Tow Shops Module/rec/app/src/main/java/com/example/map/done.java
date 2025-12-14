package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class done extends AppCompatActivity {
Button btn;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        btn=findViewById(R.id.okbtn);
        auth=FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(done.this,language_user.class);
                startActivity(intent);
                auth.signOut();
                finish();
            }
        });
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