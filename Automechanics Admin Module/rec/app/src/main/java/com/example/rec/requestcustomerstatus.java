package com.example.rec;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class requestcustomerstatus extends AppCompatActivity {
    TextView username,mechname,phone,mcnic,id,email;
    Button acceptbtn;
    ImageView img,recimg;
    LinearLayout l;

    String emails,passwords,cnic,mphone,mname,mimage,uid,rimg;
    private boolean zoomOut =  false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestcustomerstatus);
        Intent intent= getIntent();
        l=findViewById(R.id.ll);
         emails = intent.getStringExtra("cemail");
         passwords = intent.getStringExtra("cpasswd");
         cnic = intent.getStringExtra("ccnic");
         mphone = intent.getStringExtra("cphone");
         mname = intent.getStringExtra("cname");
         mimage = intent.getStringExtra("cimage");
         uid = intent.getStringExtra("cid");
        rimg = intent.getStringExtra("creceiptimage");
        recimg=findViewById(R.id.receipt);
        acceptbtn=findViewById(R.id.acceptrequest);
        RadioGroup mechstatus=findViewById(R.id.status);
        RadioButton unpaid=findViewById(R.id.unpaid);
        RadioButton paid=findViewById(R.id.paid);
        username=findViewById(R.id.namem);
        mechname=findViewById(R.id.mechname);
        img=findViewById(R.id.pictureid);
        phone=findViewById(R.id.mechphone);
        mcnic=findViewById(R.id.mechcnic);
        id=findViewById(R.id.mechid);
        email=findViewById(R.id.mechemail);

        username.setText(mname);
        mechname.setText(mname);
        phone.setText(mphone);
        mcnic.setText(cnic);
        id.setText(uid);
        email.setText(emails);
        Picasso.get().load(mimage).into(img);
        Picasso.get().load(rimg).into(recimg);
        mechstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);
                if(paid.isChecked()){
                    acceptbtn.setEnabled(true);
                    acceptbtn.setTextColor(Color.WHITE);
                }
                if(unpaid.isChecked()){
                    acceptbtn.setEnabled(false);
                    acceptbtn.setTextColor(Color.GRAY);
                }
            }
        });

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference re= FirebaseDatabase.getInstance().getReference("Admin").child("customer_requests");
                Query cu = re.orderByChild("uid").equalTo(uid);
                cu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Helpercust helper= new Helpercust(uid,mname,emails,mphone,cnic,passwords,mimage,rimg);
                        FirebaseDatabase.getInstance().getReference("user").child(uid).setValue(helper);
                        snapshot.child(uid).getRef().removeValue();
                        Toast.makeText(requestcustomerstatus.this,"registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(requestcustomerstatus.this,MainActivity3.class);
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