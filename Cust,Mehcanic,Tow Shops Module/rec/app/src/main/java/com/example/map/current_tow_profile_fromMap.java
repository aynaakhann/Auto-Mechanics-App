package com.example.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class current_tow_profile_fromMap extends AppCompatActivity {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_tow_profile_from_map);
        String uiid = auth.getInstance().getCurrentUser().getUid();

        TextView name=findViewById(R.id.name);
        TextView email=findViewById(R.id.name);
        TextView phone=findViewById(R.id.name);
        Button send_req=findViewById(R.id.send_request);

        Intent intent=getIntent();
        String id=  intent.getStringExtra("id").toString();

        myRef.child("Towing_Shop").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                             String namee = snapshot.child("owner_name").getValue(String.class);
                             name.setText(namee);
//                            Toast.makeText(MapsActivity.this, Phone , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error here
                        }
                    });
        send_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Towing_Shop").child(id).child("request").setValue(uiid);

                Toast.makeText(current_tow_profile_fromMap.this, id, Toast.LENGTH_SHORT).show();
            }
        });

    }
}