package com.example.rec;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class towdetact extends AppCompatActivity {
    TextView emailt,passt,cnict,phonet,shopnamet,namet,onamet,idt,shopregnot,tdate;
    ImageView img,recimg,registrationimgt,regimgpic;
    Button acceptbtn;
    String emails,passwords,cnics,phones,shopnames,names,regimages,ids,recimgs,shopregnos,date,charges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towdetact);
        getSupportActionBar().hide();
        Intent intent= getIntent();
        emails = intent.getStringExtra("temail");
        passwords = intent.getStringExtra("tpasswd");
        shopnames = intent.getStringExtra("tshopname");
        names = intent.getStringExtra("tname");
        shopregnos = intent.getStringExtra("tshopregg");
        cnics = intent.getStringExtra("tcnic");
        ids = intent.getStringExtra("tid");
        phones = intent.getStringExtra("tphone");
        regimages = intent.getStringExtra("timage");
        recimgs = intent.getStringExtra("receiptimage");
        date = intent.getStringExtra("tdate");
        charges = intent.getStringExtra("tcharges");

        namet=findViewById(R.id.namem);
        onamet=findViewById(R.id.mechname);
        img=findViewById(R.id.pictureid);
        shopnamet=findViewById(R.id.shopname);
        shopregnot=findViewById(R.id.shopregnum);
        phonet=findViewById(R.id.mechphone);
        cnict=findViewById(R.id.mechcnic);
        idt=findViewById(R.id.mechid);
        emailt=findViewById(R.id.mechemail);
        recimg=findViewById(R.id.receipt);
        tdate=findViewById(R.id.datee);
        registrationimgt=findViewById(R.id.registrationimg);
        namet.setText(names);
        onamet.setText(shopnames);
        phonet.setText(phones);
        cnict.setText(cnics);
        idt.setText(ids);
        emailt.setText(emails);
        shopnamet.setText(shopnames);
        shopregnot.setText(shopregnos);
        tdate.setText(date);
        Picasso.get().load(regimages).into(img);
        Picasso.get().load(regimages).into(registrationimgt);
        Picasso.get().load(recimgs).into(recimg);

        //rating
        TextView rating=findViewById(R.id.rating);
        Query q=FirebaseDatabase.getInstance().getReference("Ratings");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("final_tow_rating").child(ids).exists()) {
                    String ratings=snapshot.child("final_tow_rating").child(ids).child("rating").getValue().toString();
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