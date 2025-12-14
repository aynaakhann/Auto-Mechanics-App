package com.example.rec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
public class mechanicdetact extends AppCompatActivity {
    TextView username,mechname,phone,mcnic,age,id,email,categories;
    ImageView img,recimg,imcnicf,imcnicb;
    String emails,passwords,cnic,mphone,mage,mname,mimage,mid,recimgs,mcategories,cnicf,cnicb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanicdetact);
        Intent intent= getIntent();

        emails = intent.getStringExtra("memail");
        passwords = intent.getStringExtra("mpasswd");
        cnic = intent.getStringExtra("mcnic");
        mphone = intent.getStringExtra("mphone");
        mage = intent.getStringExtra("mage");
        mname = intent.getStringExtra("mname");
        mimage = intent.getStringExtra("mimage");
        mid = intent.getStringExtra("mid");
        recimgs = intent.getStringExtra("receiptimage");
        mcategories = intent.getStringExtra("mcatgr");

        cnicf=intent.getStringExtra("cnicfrontpic");
        cnicb=intent.getStringExtra("cnicbackpic");

        imcnicf=findViewById(R.id.cnicfront);
        imcnicb=findViewById(R.id.cnicback);

        username=findViewById(R.id.namem);
        mechname=findViewById(R.id.mechname);
        img=findViewById(R.id.pictureid);
        phone=findViewById(R.id.mechphone);
        mcnic=findViewById(R.id.mechcnic);
        age=findViewById(R.id.mechAge);
        id=findViewById(R.id.mechid);
        email=findViewById(R.id.mechemail);
        recimg=findViewById(R.id.receipt);
        categories=findViewById(R.id.mcateg);
        username.setText(mname);
        mechname.setText(mname);
        phone.setText(mphone);
        mcnic.setText(cnic);
        age.setText(mage);
        id.setText(mid);
        categories.setText(mcategories);
        email.setText(emails);
        Picasso.get().load(mimage).into(img);
        Picasso.get().load(recimgs).into(recimg);
        Picasso.get().load(cnicf).into(imcnicf);
        Picasso.get().load(cnicb).into(imcnicb);

        TextView rating=findViewById(R.id.rating);
        Query q= FirebaseDatabase.getInstance().getReference("Ratings");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("final_mech_rating").child(mid).exists()) {
                    String ratings=snapshot.child("final_mech_rating").child(mid).child("rating").getValue().toString();
                    if(TextUtils.isEmpty(ratings)){
                        rating.setText("N/A");
                    }else{
                        rating.setText(ratings);
                    }
                }
                else{
                    rating.setText("N/A");
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}