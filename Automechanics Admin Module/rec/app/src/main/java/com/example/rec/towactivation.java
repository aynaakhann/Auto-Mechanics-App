package com.example.rec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class towactivation extends AppCompatActivity {
    TextView emailt,passt,cnict,phonet,shopnamet,namet,onamet,idt,shopregnot,tdate;
    ImageView img,recimg,registrationimgt,regimgpic;
    Button acceptbtn;
    String emails,passwords,cnics,phones,shopnames,names,regimages,ids,recimgs,shopregnos,date,charges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesttowstatus);
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

        //Toast.makeText(requesttowstatus.this, " em  "+emails+" sn  "+shopnames+" p  "+passwords+"  nm  "+names+" sr  "+shopregnos+" cn  "+cnics+" id  "+ids+"   "+phones, Toast.LENGTH_SHORT).show();

       acceptbtn=findViewById(R.id.acceptrequest);
        RadioGroup mechstatus=findViewById(R.id.status);
        RadioButton unpaid=findViewById(R.id.unpaid);
        RadioButton paid=findViewById(R.id.paid);

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
                DatabaseReference re= FirebaseDatabase.getInstance().getReference("/Admin/towing_activation_requests");
                Query cu = re.orderByChild("shopid").equalTo(ids);
                cu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //Toast.makeText(requesttowstatus.this, "entereeeedd", Toast.LENGTH_SHORT).show();
                        /*String cnic=snapshot.child(tid).child("owner_cnic").getValue().toString();
                        String email=snapshot.child(tid).child("owner_email").getValue().toString();
                        String password=snapshot.child(tid).child("password").getValue().toString();
                        String username=snapshot.child(tid).child("owner_name").getValue().toString();
                        String phone=snapshot.child(tid).child("owner_phone").getValue().toString();
                        String uid=snapshot.child(tid).child("shopid").getValue().toString();
                        String ushopname=snapshot.child(tid).child("shop_name").getValue().toString();
                        String uimage=snapshot.child(tid).child("reg_img").getValue().toString();
                        String ushopregno=snapshot.child(tid).child("shop_regno").getValue().toString();
                        String paymentreceipt=snapshot.child(tid).child("rpayment").getValue().toString();*/

                    //    HelperTow helper= new HelperTow(ids,names,cnics,shopnames,shopregnos,phones,emails,passwords,regimages,recimgs,date,"",charges);
                      //  FirebaseDatabase.getInstance().getReference("Towing_Shop").child(ids).setValue(helper);
                        FirebaseDatabase.getInstance().getReference("Towing_Shop").child(ids).child("charges").setValue("3000");
                        snapshot.child(ids).getRef().removeValue();
                        Toast.makeText(towactivation.this,"Account Activated!", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(towactivation.this,MainActivity3.class);
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