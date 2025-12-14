package com.example.rec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class block_mech_details extends AppCompatActivity {
    TextView emailt,passt,cnict,phonet,shopnamet,namet,onamet,idt,shopregnot,tdate,chargess;
    ImageView img,recimg,registrationimgt,regimgpic;
    String emails,passwords,cnics,phones,shopnames,names,regimages,ids,shopregnos,date,charges;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_mech_details);
        Intent intent= getIntent();
        emails = intent.getStringExtra("memail");
        passwords = intent.getStringExtra("mpasswd");
        shopnames = intent.getStringExtra("mshopname");
        names = intent.getStringExtra("mname");
        shopregnos = intent.getStringExtra("mshopregg");
        cnics = intent.getStringExtra("mcnic");
        ids = intent.getStringExtra("mid");
        phones = intent.getStringExtra("mphone");
        regimages = intent.getStringExtra("mimage");
        date = intent.getStringExtra("mdate");
        charges = intent.getStringExtra("mcharges");
        namet=findViewById(R.id.namem);
        onamet=findViewById(R.id.mechname);
        phonet=findViewById(R.id.mechphone);
        cnict=findViewById(R.id.mechcnic);
        idt=findViewById(R.id.mechid);
        emailt=findViewById(R.id.mechemail);
        tdate=findViewById(R.id.datee);
        chargess=findViewById(R.id.charg);
        namet.setText(names);
        onamet.setText(names);
        phonet.setText(phones);
        cnict.setText(cnics);
        idt.setText(ids);
        emailt.setText(emails);
        tdate.setText(date);
       chargess.setText(charges);



    }
}