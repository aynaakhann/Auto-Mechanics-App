package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class custBillByMech extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    TextView mech_name,mech_id,total1,total2,time,distanceinKM,address,services,chargesofServices,chargesperKM,infoLine;
    //    TextView address,chargesofServices,services;
    String duration;
    double charges,totalCharges;
    String mechId,availedAddress,availedService,distance;
    Button custBill;
    ImageView mech_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_bill_by_mech);


        mech_name=findViewById(R.id.mech_name);

//        total1=findViewById(R.id.totalChargesatBill1M);
        total2=findViewById(R.id.totalBill2M);
        services=findViewById(R.id.servicesAvailedAtBillM);
        chargesofServices=findViewById(R.id.chargesofServicesAtBillM);
        distanceinKM=findViewById(R.id.distanceatBillM);
        chargesperKM=findViewById(R.id.perKmatBillM);
        time=findViewById(R.id.timeinMinatBillM);
        address=findViewById(R.id.addressofAvailedServiceM);
        infoLine=findViewById(R.id.infoLineM);
        custBill=findViewById(R.id.custbillPaidM);

        Intent intent=getIntent();
        duration=intent.getStringExtra("timeBillM");
        mechId=intent.getStringExtra("mechIdM");
        distance=intent.getStringExtra("distanceBillM");
        availedAddress=intent.getStringExtra("addressBillM");
        availedService=intent.getStringExtra("serviceBillM");
        charges=intent.getDoubleExtra("chargesBillM",0);
        totalCharges=intent.getDoubleExtra("tchargesBillM",0);


        myRef.child("mechanic").child(mechId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String mech_namee=snapshot.child("mname").getValue(String.class);
                    String mech_image=snapshot.child("mimage").getValue(String.class);

                    distanceinKM.setText(distance+" meters");
                    time.setText(duration+"");
                    address.setText(availedAddress);
                    chargesofServices.setText(charges+" Rs");
                    services.setText(availedService);
                    mech_name.setText(mech_namee);
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

                Intent intent=new Intent(custBillByMech.this,custFeedbackforMech.class);
                intent.putExtra("mId",mechId);
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