package com.example.rec;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;

public class towingdelete extends AppCompatActivity {
    FirebaseAuth auth;
    LocalDate date = null;
    FloatingActionButton call;
    int d; //get radiobutton id
    String e,cleft; //get radiobtn text
    TextView shname,oname,ophone,oshopid,oemail,ocnic,opass,otregno,customerid,oshimg,shrimg,oshcomplain,tcharges;
    TextView custname,custcnic,custphone,custid,custemail;
    Button proceedbtn,backbutton;
    String currentcharges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towingdelete);
        Intent intent= getIntent();
        auth=FirebaseAuth.getInstance();
        String shopname = intent.getStringExtra("shopname");
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String shopid = intent.getStringExtra("shopid");
        String temail = intent.getStringExtra("email");
        String tcnic = intent.getStringExtra("cnic");
        String tpass = intent.getStringExtra("pass");
        String tsregistration = intent.getStringExtra("shopreg");
        String cid = intent.getStringExtra("cid");
        String timg = intent.getStringExtra("img");
        String trimg = intent.getStringExtra("rimg");
        String tcomplain = intent.getStringExtra("complain");
        call=findViewById(R.id.calltowingshop);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });
        custname=findViewById(R.id.cname);
        custcnic=findViewById(R.id.ccnic);
        custphone=findViewById(R.id.cphone);
        custid=findViewById(R.id.cid);
        custemail=findViewById(R.id.cemail);
        shname=findViewById(R.id.sname);
        oname=findViewById(R.id.oname);
        ophone=findViewById(R.id.ophone);
        oshopid=findViewById(R.id.tid);
        oemail=findViewById(R.id.oemail);
        tcharges=findViewById(R.id.scharges);
        ocnic=findViewById(R.id.ocnic);
        otregno=findViewById(R.id.sregnumber);
        customerid=findViewById(R.id.cid);
        oshcomplain=findViewById(R.id.complain);
        shname.setText(shopname);
        oname.setText(name);
        ophone.setText(phone);
        oshopid.setText(shopid);
        oemail.setText(temail);
        ocnic.setText(tcnic);
        otregno.setText(tsregistration);
        customerid.setText(cid);
        oshcomplain.setText(tcomplain);

        proceedbtn=findViewById(R.id.proceed);

        RadioGroup actions=findViewById(R.id.action);
        RadioButton deletetowaccount=findViewById(R.id.deleteaccount);
        RadioButton deduct_charges=findViewById(R.id.deductcharges);
        RadioButton ignore_complain=findViewById(R.id.ignorecomplain);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now();
        }
        //backbutton=findViewById(R.id.backbtn);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Towing_Shop");
                    Query checkUser = reference.orderByChild("shopid").equalTo(shopid);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            currentcharges=snapshot.child(shopid).child("charges").getValue().toString();
                            tcharges.setText(currentcharges);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        DatabaseReference re=FirebaseDatabase.getInstance().getReference("user");
        Query cu = re.orderByChild("uid");
        cu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childs : snapshot.getChildren()){
                    if(childs.child("uid").getValue().toString().equals(cid)){
                        String cname=childs.child("username").getValue().toString();
                        String ccnic=childs.child("cnic").getValue().toString();
                        String cphone=childs.child("phone").getValue().toString();
                        String cemail=childs.child("email").getValue().toString();
                        custname.setText(cname);
                        custcnic.setText(ccnic);
                        custphone.setText(cphone);
                        custid.setText(cid);
                        custemail.setText(cemail);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        auth= FirebaseAuth.getInstance();
        proceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d= actions.getCheckedRadioButtonId();
                RadioButton radioButton= findViewById(d);
                e=radioButton.getText().toString();

                // checkedId is the RadioButton selected
                AlertDialog.Builder deductdialog= new AlertDialog.Builder(towingdelete.this);
                if(e.equals("Delete towing account permanently")){
    deductdialog.setTitle("Confirm Delete");
    deductdialog.setMessage("Are you sure you want to delete & Block this towing Account?\nShop Name: "+shopname+"\n"+"Owner Name: "+name);
    deductdialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {


            auth.signInWithEmailAndPassword(temail,tpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

  /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Towing_Shop");
                        Query checkUser = reference.orderByChild("shopid").equalTo(shopid);
                        checkUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               *//* String cnic=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("owner_cnic").getValue().toString();
                                String email=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("owner_email").getValue().toString();
                                String password=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("password").getValue().toString();
                                String shopname=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("shop_name").getValue().toString();
                                String username=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("owner_name").getValue().toString();
                                String phone=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("owner_phone").getValue().toString();
                                String shopiid=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("shopid").getValue().toString();
                                String shopreg=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("shop_regno").getValue().toString();
                               // String rpayment=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("rpayment").getValue().toString();
                                String rimg=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("reg_img").getValue().toString();*//*
                                cleft=snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("charges").getValue().toString();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    date = LocalDate.now();
                                }

                                snapshot.child(shopid).getRef().removeValue();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });*/
                        auth.getCurrentUser().delete();
                        Intent intent=new Intent(towingdelete.this,MainActivity3.class);
                        startActivity(intent);
                        //Toast.makeText(towingdelete.this,"Blocked successfully",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(towingdelete.this,"deleted already",Toast.LENGTH_LONG).show();
                    }
                }
            });
            //remove from complains_towing                                  done
            Query ct = FirebaseDatabase.getInstance().getReference("/Admin/complains_towing").orderByChild("key");
            ct.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshott) {
                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                        if(childs.child("shopid").getValue().toString().equals(shopid)){
                            dataSnapshott.child(shopid).getRef().removeValue();

                        }
                    }                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            //remove from towing locations                                  done
            Query tl = FirebaseDatabase.getInstance().getReference("towing_locations").orderByChild("shopid").equalTo(shopid);

            tl.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshott) {
                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                        if(childs.child("shopid").getValue().toString().equals(shopid)){
                            dataSnapshott.getRef().removeValue();
                        }
                    }                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Query tll = FirebaseDatabase.getInstance().getReference("Towing_Shop").orderByChild("shopid");
            tll.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshott) {
                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                        if(childs.child("shopid").getValue().toString().equals(shopid)){
                            String chrgss=childs.child("charges").getValue().toString();
                            HelperTow towhelper= new HelperTow(shopid,name,tcnic,shopname,tsregistration,phone,temail,tpass,trimg,cid,date.toString(),"",chrgss);
                            FirebaseDatabase.getInstance().getReference("Admin").child("deleted_towing_accounts").child(shopid).setValue(towhelper);
                            dataSnapshott.child(shopid).getRef().removeValue();
                        }
                    }                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //remove from activation requests done
            Query qqq = FirebaseDatabase.getInstance().getReference("/Admin/towing_activation_requests").orderByChild("key");
            qqq.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshott) {
                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                        if(childs.child("shopid").getValue().toString().equals(shopid)){
                            dataSnapshott.getRef().removeValue();
                        }
                    }                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }
    });
    deductdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        }
    });
    deductdialog.create().show();
 }

if(e.equals("Deduct charges")){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Towing_Shop");
                            Query checkUser = reference.orderByChild("shopid").equalTo(shopid);
                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String chargesleft=snapshot.child(shopid).child("charges").getValue().toString();
                                    final EditText dedet=new EditText(towingdelete.this);
                                    dedet.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    AlertDialog.Builder deductdialog= new AlertDialog.Builder(towingdelete.this);
                                    deductdialog.setTitle("Deduct Charges");
                                    String ch=tcharges.getText().toString();
                                    deductdialog.setMessage("Shop Name: "+shopname+"\n"+"Owner Name: "+name+"\nCurrent Charges: "+ch);
                                    deductdialog.setView(dedet);

                                    deductdialog.setPositiveButton("Deduct", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            String deductedcharges = dedet.getText().toString();
                                            int currcharges = Integer.parseInt(chargesleft);
                                            int dedcharges = Integer.parseInt(deductedcharges);
                                            if(dedcharges<currcharges){
                                                int remcharges = currcharges - dedcharges;
                                                Toast.makeText(towingdelete.this, "Charges " + remcharges + " Deducted Successfully!", Toast.LENGTH_SHORT).show();
                                                String setcharges=snapshot.child(shopid).child("charges").getValue().toString();
                                                FirebaseDatabase.getInstance().getReference("Towing_Shop").child(shopid).child("charges").setValue(String.valueOf(remcharges));
                                                tcharges.setText(String.valueOf(remcharges));
                                                currcharges=remcharges;

                                                String  dc=String.valueOf(dedcharges);
                                                String  rc=String.valueOf(remcharges);
                                                String key =  FirebaseDatabase.getInstance().getReference("Admin").child("towing_charges_deduction").push().getKey();
                                                Helpertow_compmessage helper = new Helpertow_compmessage(shopid,cid,tcomplain,dc,name,tcnic,shopname,tsregistration,phone,temail,tpass,trimg,"",date.toString(),rc);
                                                FirebaseDatabase.getInstance().getReference("Admin").child("towing_charges_deduction").child(key).setValue(helper);

                                                //remove from complains_towing                                  done
                                                Query ct = FirebaseDatabase.getInstance().getReference("/Admin/complains_towing").orderByChild("key");
                                                ct.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshott) {
                                                        for (DataSnapshot childs : dataSnapshott.getChildren()){
                                                            if(childs.child("shopid").getValue().toString().equals(shopid)){
                                                                dataSnapshott.getRef().removeValue();
                                                            }
                                                        }                }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                auth.signInWithEmailAndPassword(temail,tpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()){
                                                            auth.sendPasswordResetEmail(temail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });
                                                        }
                                                        else {
                                                        }
                                                    }
                                                });


                                            }
                                            else{
                                                Toast.makeText(towingdelete.this, "Deducted charges exceeds actual amount!", Toast.LENGTH_SHORT).show();
                                                dedet.setText("");
                                                Query qqq = FirebaseDatabase.getInstance().getReference("/Admin/towing_activation_requests").orderByChild("key");
                                                qqq.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshott) {
                                                        for (DataSnapshot childs : dataSnapshott.getChildren()){
                                                            if(childs.child("shopid").getValue().toString().equals(shopid)){
                                                                dataSnapshott.getRef().removeValue();
                                                            }
                                                        }                }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                        }
                                    });
                                    deductdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    deductdialog.create().show();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                        }
if(e.equals("Ignore Complain")){
    final EditText dedet=new EditText(towingdelete.this);
    AlertDialog.Builder ignoredialog= new AlertDialog.Builder(towingdelete.this);
    ignoredialog.setTitle("Ignore Complain");
    //String ch=tcharges.getText().toString();

    ignoredialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            //remove from complains_towing
            Query ct = FirebaseDatabase.getInstance().getReference("/Admin/complains_towing").orderByChild("key");
            ct.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshott) {
                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                        if(childs.child("shopid").getValue().toString().equals(shopid)){
                            dataSnapshott.getRef().removeValue();
                        }
                    }                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    });
    ignoredialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        }
    });
    ignoredialog.create().show();
                        }

            }
        });
    /*    backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent=new Intent(towingdelete.this,MainActivity3.class);
                startActivity(backintent);
                finish();
            }
        });*/
    }
}