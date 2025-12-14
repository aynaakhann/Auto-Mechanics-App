package com.example.rec;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class blocked_tow_details extends AppCompatActivity {
    TextView
            emailt,passt,cnict,phonet,shopnamet,namet,onamet,idt,shopregnot,tdate;
    ImageView img,recimg,registrationimgt,regimgpic;
    String emails,passwords,cnics,phones,shopnames,names,regimages,ids,shopregnos,date,charges;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_tow_details);
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



    }
}