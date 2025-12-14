package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if (user != null) {
                   DatabaseReference re= FirebaseDatabase.getInstance().getReference("user");
                   Query cu = re.orderByChild("uid");
                   cu.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           for (DataSnapshot childs : dataSnapshot.getChildren()){
                               String un=childs.child("username").getValue().toString();
                               if(childs.child("uid").getValue().equals(user.getUid().toString())){
                                   startActivity(new Intent(getApplicationContext(),cust_mainHome.class));
                                   finish();
                                   Toast.makeText(splashScreen.this,"Welcome Back "+un+"!",Toast.LENGTH_LONG).show();
                               }
                           }
                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });


                   DatabaseReference reffd= FirebaseDatabase.getInstance().getReference("Towing_Shop");
                   Query cudu = reffd.orderByChild("shopid");
                   cudu.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           for (DataSnapshot childs : dataSnapshot.getChildren()){
                               if(childs.child("shopid").getValue().equals(user.getUid())){
                                   Integer charges=Integer.parseInt(childs.child("charges").getValue().toString());
                                   if(charges<=1){
                                       String oid=childs.child("shopid").getValue().toString();
                                       String sn=childs.child("shop_name").getValue().toString();
                                       String on=childs.child("owner_name").getValue().toString();
                                       String oc=childs.child("owner_cnic").getValue().toString();
                                       String op=childs.child("owner_phone").getValue().toString();
                                       String oe=childs.child("owner_email").getValue().toString();
                                       String ops=childs.child("password").getValue().toString();
                                       String orn=childs.child("shop_regno").getValue().toString();
                                       String oi=childs.child("reg_img").getValue().toString();
                                       Intent intent=new Intent(splashScreen.this,account_deactivate_towing.class);
                                       intent.putExtra("oname",on);
                                       intent.putExtra("ouserid",oid);
                                       intent.putExtra("ocnic",oc);
                                       intent.putExtra("ophone",op);
                                       intent.putExtra("oemail",oe);
                                       intent.putExtra("opassword",ops);
                                       intent.putExtra("oregno",orn);
                                       intent.putExtra("oimage",oi);
                                       intent.putExtra("oshop",sn);
                                       startActivity(intent);
                                       finish();
                                       Toast.makeText(splashScreen.this,sn+" Your Account is deactivated!",Toast.LENGTH_LONG).show();
                                   }
                                   else{
                                       DatabaseReference reff= FirebaseDatabase.getInstance().getReference("Towing_Shop");
                                       Query cuuu = reff.orderByChild("shopid");
                                       cuuu.addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               for (DataSnapshot childs : dataSnapshot.getChildren()){
                                                   if(childs.child("shopid").getValue().equals(user.getUid())){
                                                       String sn=childs.child("shop_name").getValue().toString();
                                                       startActivity(new Intent(getApplicationContext(),Towing_home.class));
                                                       finish();
                                                       Toast.makeText(splashScreen.this,"Welcome Back "+sn+"!",Toast.LENGTH_LONG).show();
                                                   }
                                               }
                                           }
                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {
                                           }
                                       });

                                   }
                               }
                           }
                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                       }
                   });

                   DatabaseReference m= FirebaseDatabase.getInstance().getReference("mechanic");
                   Query qm = m.orderByChild("mid");
                   qm.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           for (DataSnapshot childs : dataSnapshot.getChildren()){
                               if(childs.child("mid").getValue().equals(user.getUid())){
                                   Integer charges=Integer.parseInt(childs.child("charges").getValue().toString());
                                   if(charges<=1){
                                       String mid=childs.child("mid").getValue().toString();
                                       String mname=childs.child("mname").getValue().toString();
                                       String categ=childs.child("category").getValue().toString();
                                       String mage=childs.child("mage").getValue().toString();
                                       String mcnic=childs.child("mcnic").getValue().toString();
                                       String memail=childs.child("memail").getValue().toString();
                                       String cnicback=childs.child("cnicbackpic").getValue().toString();
                                       String cnicfront=childs.child("cnicfrontpic").getValue().toString();
                                       String mpass=childs.child("mpass").getValue().toString();
                                       String mphone=childs.child("mphone").getValue().toString();
                                       String recimg=childs.child("recimg").getValue().toString();
                                       String chargess=childs.child("category").getValue().toString();
                                       String mimage=childs.child("mimage").getValue().toString();

                                       Intent intent=new Intent(splashScreen.this,account_deactivation_mechanic.class);
                                       intent.putExtra("id",mid);
                                       intent.putExtra("name",mname);
                                       intent.putExtra("categ",categ);
                                       intent.putExtra("age",mage);
                                       intent.putExtra("cnic",mcnic);
                                       intent.putExtra("email",memail);
                                       intent.putExtra("cnicb",cnicback);
                                       intent.putExtra("cnicf",cnicfront);
                                       intent.putExtra("pass",mpass);
                                       intent.putExtra("phone",mphone);
                                       intent.putExtra("rimg",recimg);
                                       intent.putExtra("charges",chargess);
                                       intent.putExtra("mimg",mimage);
                                       startActivity(intent);
                                       finish();
                                       Toast.makeText(splashScreen.this,mname+" Your Account is deactivated!",Toast.LENGTH_LONG).show();
                                   }
                                   else{
                                       DatabaseReference ref= FirebaseDatabase.getInstance().getReference("mechanic");
                                       Query cuu = ref.orderByChild("mid");
                                       cuu.addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               for (DataSnapshot childs : dataSnapshot.getChildren()){
                                                   if(childs.child("mid").getValue().equals(user.getUid())){
                                                       String mn=childs.child("mname").getValue().toString();
                                                       startActivity(new Intent(getApplicationContext(),Home.class));
                                                       finish();
                                                       Toast.makeText(splashScreen.this,"Welcome Back "+mn+"!",Toast.LENGTH_LONG).show();
                                                   }
                                               }
                                           }
                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {
                                           }
                                       });
                                   }
                               }
                           }
                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                       }
                   });
                }else{
                    startActivity(new Intent(getApplicationContext(),language_user.class));
                    finish();
                }
            }
        },900);
    }
}