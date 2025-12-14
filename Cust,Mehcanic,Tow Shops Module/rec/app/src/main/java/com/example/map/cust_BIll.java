package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class cust_BIll extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    TextView tow_name,tow_id,total1,total2,time,distanceinKM,address,services,chargesofServices,chargesperKM,infoLine;
//    TextView address,chargesofServices,services;
    String duration;
    double charges,totalCharges;
    String shopId,availedAddress,availedService,distance;
    Button custBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_bill);


//        total1=findViewById(R.id.totalChargesatBill1);
        tow_name=findViewById(R.id.tow_name);
        total2=findViewById(R.id.totalBill2);
        services=findViewById(R.id.servicesAvailedAtBill);
        chargesofServices=findViewById(R.id.chargesofServicesAtBill);
        distanceinKM=findViewById(R.id.distanceatBill);
        chargesperKM=findViewById(R.id.perKmatBill);
        time=findViewById(R.id.timeinMinatBill);
        address=findViewById(R.id.addressofAvailedService);
        infoLine=findViewById(R.id.infoLine);
        custBill=findViewById(R.id.custbillPaid);

        Intent intent=getIntent();
        duration=intent.getStringExtra("timeBill");
        shopId=intent.getStringExtra("shopId");
        distance=intent.getStringExtra("distanceBill");
        availedAddress=intent.getStringExtra("addressBill");
        availedService=intent.getStringExtra("serviceBill");
        charges=intent.getDoubleExtra("chargesBill",0);
        totalCharges=intent.getDoubleExtra("tchargesBill",0);

        myRef.child("Towing_Shop").child(shopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shop_namee=snapshot.child("shop_name").getValue(String.class);

                    distanceinKM.setText(distance+" meters");
                    time.setText(duration+"");
                    address.setText(availedAddress);
                    chargesofServices.setText(charges+" Rs");
                    services.setText(availedService);
                  //  tow_id.setText(shopId);
                  tow_name.setText(shop_namee);
//
//        long totalcharge= (long) ((distance*20)+charges);
                  //  total1.setText(totalCharges+"");
                    total2.setText(totalCharges+"");
                    infoLine.setText("You travelled " +distance+" meters in "+duration+" min");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        custBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(cust_BIll.this, "Alhamdulilah", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(cust_BIll.this,custFeedbackforTowing.class);
                intent.putExtra("sId",shopId);
                startActivity(intent);
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