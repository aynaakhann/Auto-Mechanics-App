package com.example.rec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class customerdetact extends AppCompatActivity {
    TextView email,id,img,name,cnic,phone,namme;
    ImageView imgv,imgr;
    String uname,ucnic,uemail,upass,uphone,uimage,uid,rimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custdetail);
        Intent intent= getIntent();
         uname = intent.getStringExtra("username");
         ucnic = intent.getStringExtra("usercnic");
         upass = intent.getStringExtra("userpass");
         uemail = intent.getStringExtra("useremail");
         uphone = intent.getStringExtra("userphone");
         uid = intent.getStringExtra("userid");
         uimage = intent.getStringExtra("userimage");
        rimg = intent.getStringExtra("userreceiptimage");
        imgv=findViewById(R.id.picture);
        imgr=findViewById(R.id.receipt);
        email=findViewById(R.id.mechemail);
        id=findViewById(R.id.mechid);
        img=findViewById(R.id.imageid);
        name=findViewById(R.id.namem);
        namme=findViewById(R.id.mechname);
        cnic=findViewById(R.id.mechcnic);
        phone=findViewById(R.id.mechphone);
        //Toast.makeText(this, uimage+rimg, Toast.LENGTH_SHORT).show();
        Picasso.get().load(uimage).into(imgv);
        /*
        Picasso.get().load(rimg).into(imgr);*/

        email.setText(uemail);
        id.setText(uid);
        name.setText(uname);
        namme.setText(uname);
        cnic.setText(ucnic);
        phone.setText(uphone);
        TextView rating=findViewById(R.id.rating);
        Query q=FirebaseDatabase.getInstance().getReference("Ratings");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("final_customer_rating").child(uid).exists()) {
                    String ratings=snapshot.child("final_customer_rating").child(uid).child("rating").getValue().toString();
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