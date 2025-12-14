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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class mechactivation extends AppCompatActivity {
    TextView username,mechname,pphone,mcnic,aage,id,eemail,categories;
    ImageView img,recimg,imcnicf,imcnicb;
    Button acceptbtn;
    String name,cnic,phone,mid,email,pass,categ,imgg,rimg,age,cnicb,cnicf,charges,im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requsetmechanicstatus);
        Intent intent= getIntent();
        name = intent.getStringExtra("name");
        cnic = intent.getStringExtra("cnic");
        phone = intent.getStringExtra("phone");
        mid = intent.getStringExtra("mid");
        email = intent.getStringExtra("email");
        pass = intent.getStringExtra("pass");
        categ = intent.getStringExtra("categ");
        imgg = intent.getStringExtra("img");
        rimg = intent.getStringExtra("rimg");
        age = intent.getStringExtra("age");
        cnicb = intent.getStringExtra("cnicback");
        cnicf = intent.getStringExtra("cnicfront");
        charges = intent.getStringExtra("charges");

       acceptbtn=findViewById(R.id.acceptrequest);
        RadioGroup mechstatus=findViewById(R.id.status);
        RadioButton unpaid=findViewById(R.id.unpaid);
        RadioButton paid=findViewById(R.id.paid);
       /* DatabaseReference re=FirebaseDatabase.getInstance().getReference("mechanic");
        Query cu = re.orderByChild("mid");
        cu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childs : snapshot.getChildren()){
                    if(childs.child("mid").getValue().toString().equals(mid)){
                        im=childs.child("mimage").getValue().toString();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        imcnicf=findViewById(R.id.cnicfront);
        imcnicb=findViewById(R.id.cnicback);

        username=findViewById(R.id.namem);
        mechname=findViewById(R.id.mechname);
        img=findViewById(R.id.pictureid);
        pphone=findViewById(R.id.mechphone);
        mcnic=findViewById(R.id.mechcnic);
        aage=findViewById(R.id.mechAge);
        id=findViewById(R.id.mechid);
        eemail=findViewById(R.id.mechemail);
        recimg=findViewById(R.id.receipt);
        categories=findViewById(R.id.mcateg);
        username.setText(name);
        mechname.setText(name);
        pphone.setText(phone);
        mcnic.setText(cnic);
        aage.setText(age);
        id.setText(mid);
        categories.setText(categ);
        eemail.setText(email);
        //Picasso.get().load(im).into(img);
        Picasso.get().load(rimg).into(recimg);
        Picasso.get().load(cnicf).into(imcnicf);
        Picasso.get().load(cnicb).into(imcnicb);

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
                DatabaseReference re= FirebaseDatabase.getInstance().getReference("/Admin/mechanic_activation_requests");
                Query cu = re.orderByChild("mid").equalTo(mid);
                cu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

//                        String mid,mname,mcnic,mage,mphone,memail,mpass,mimage,category,recimg,cnicfrontpic,cnicbackpic,charges;

                   //     HelperMechActivation helper= new HelperMechActivation(mid,name,cnic,age,phone,email,pass,im,categ,rimg,cnicf,cnicb,charges);
                     //   FirebaseDatabase.getInstance().getReference("mechanic").child(mid).setValue(helper);
                        FirebaseDatabase.getInstance().getReference("mechanic").child(mid).child("charges").setValue("3000");


                        snapshot.child(mid).getRef().removeValue();
                        Toast.makeText(mechactivation.this,"Account Activated!", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(mechactivation.this,MainActivity3.class);
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