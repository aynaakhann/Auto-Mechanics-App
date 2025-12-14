package com.example.rec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class requestmechanicstatus extends AppCompatActivity {
    TextView username,mechname,phone,mcnic,age,id,email,categories;
    ImageView img,recimg,imcnicf,imcnicb;
    Button acceptbtn;
    String emails,passwords,cnic,mphone,mage,mname,mimage,mid,recimgs,mcategories,cnicfront,cnicback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requsetmechanicstatus);
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
        cnicfront = intent.getStringExtra("cnicfrontp");
        cnicback = intent.getStringExtra("cnicbackp");

        imcnicf=findViewById(R.id.cnicfront);
        imcnicb=findViewById(R.id.cnicback);

        acceptbtn=findViewById(R.id.acceptrequest);
        RadioGroup mechstatus=findViewById(R.id.status);
        RadioButton unpaid=findViewById(R.id.unpaid);
        RadioButton paid=findViewById(R.id.paid);

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

        Picasso.get().load(cnicfront).into(imcnicf);
        Picasso.get().load(cnicback).into(imcnicb);

        mechstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);
                if(paid.isChecked()){
                    acceptbtn.setEnabled(true);
                }
                if(unpaid.isChecked()){
                    acceptbtn.setEnabled(false);
                }
            }
        });

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference re= FirebaseDatabase.getInstance().getReference("Admin").child("mechanic_requests");
                Query cu = re.orderByChild("mid").equalTo(mid);
                cu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        HelperMechanicRegistration helper= new HelperMechanicRegistration(mid,mname,cnic,mage,mphone,emails,passwords,mimage,mcategories,recimgs,"3000",cnicfront,cnicback);
                        FirebaseDatabase.getInstance().getReference("mechanic").child(mid).setValue(helper);
                        snapshot.child(mid).getRef().removeValue();
                        Toast.makeText(requestmechanicstatus.this,"request accepted!", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(requestmechanicstatus.this,MainActivity3.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}