package com.example.rec;

import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

public class mechanicdelete extends AppCompatActivity {
    FirebaseAuth auth;
    LocalDate date = null;
    int d; //get radiobutton id
    String e; //get radiobtn text
    String mmidd;
    TextView shname,oname,ophone,oshopid,oemail,ocnic,opass,otregno,customerid,oshimg,shrimg,oshcomplain,tcharges;
    TextView custname,custcnic,custphone,custid,custemail;
    Button proceedbtn;
    String currentcharges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanicdelete);
        Intent intent= getIntent();
        auth=FirebaseAuth.getInstance();
        String name = intent.getStringExtra("mname");
        String phone = intent.getStringExtra("phone");
        String mid = intent.getStringExtra("mid");
        String email = intent.getStringExtra("email");
        String cnic = intent.getStringExtra("cnic");
        String pass = intent.getStringExtra("pass");
        String cid = intent.getStringExtra("cid");
        String mimg = intent.getStringExtra("img");
        String trimg = intent.getStringExtra("rimg");
        String tcomplain = intent.getStringExtra("complain");
        custname=findViewById(R.id.cname);
        custcnic=findViewById(R.id.ccnic);
        custphone=findViewById(R.id.cphone);
        custid=findViewById(R.id.cid);
        custemail=findViewById(R.id.cemail);
        oname=findViewById(R.id.oname);
        ophone=findViewById(R.id.ophone);
        oshopid=findViewById(R.id.tid);
        oemail=findViewById(R.id.oemail);
        tcharges=findViewById(R.id.scharges);
        ocnic=findViewById(R.id.ocnic);
        customerid=findViewById(R.id.cid);
        oshcomplain=findViewById(R.id.complain);
        oname.setText(name);
        ophone.setText(phone);
        oshopid.setText(mid);
        oemail.setText(email);
        ocnic.setText(cnic);
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

        DatabaseReference referenc = FirebaseDatabase.getInstance().getReference("mechanic");
        Query checkUser = referenc.orderByChild("mid").equalTo(mid);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentcharges=snapshot.child(mid).child("charges").getValue().toString();
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
                if(e.equals("Delete mechanic account permanently")){
                    AlertDialog.Builder deductdialog= new AlertDialog.Builder(mechanicdelete.this);
                    deductdialog.setTitle("Confirm Delete");
                    deductdialog.setMessage("Are you sure you want to delete & Block this Mechanic Account?\nMechanic Name: "+name);
                    deductdialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                     mmidd=auth.getCurrentUser().getUid().toString();
                                    if(task.isSuccessful()){
                                        auth.getCurrentUser().delete();
                                        Intent intent=new Intent(mechanicdelete.this,MainActivity3.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(mechanicdelete.this,"deleted already",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            //remove from complains_towing                                  done
                            Query ct = FirebaseDatabase.getInstance().getReference("/Admin/complains_mechanic").orderByChild("key");
                            ct.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshott) {
                                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                                        if(childs.child("mid").getValue().toString().equals(mid)){
                                            dataSnapshott.child(mid).getRef().removeValue();
                                        }
                                    }                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //remove from towing locations                                  done
                            Query tll = FirebaseDatabase.getInstance().getReference("mechanic").orderByChild("mid");
                            tll.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshott) {
                                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                                        if(childs.child("mid").getValue().toString().equals(mid)){
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                date = LocalDate.now();
                                            }
                                            String agee=childs.child("mage").getValue().toString();
                                            String chr=childs.child("charges").getValue().toString();
                                            HelperMech helper= new HelperMech(mid,name,cnic,agee,phone,email,pass,mimg,cid,date.toString(),"",chr);
                                            FirebaseDatabase.getInstance().getReference("Admin").child("deleted_mechanic_accounts").child(mid).setValue(helper);
                                            dataSnapshott.child(mid).getRef().removeValue();
                                        }
                                    }                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                            Query tl = FirebaseDatabase.getInstance().getReference("/mech_locations").orderByChild("mech_id").equalTo(mid);
                            tl.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshott) {
                                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                                        if(childs.child("mech_id").getValue().toString().equals(mid)){
                                            dataSnapshott.getRef().removeValue();
                                            //Toast.makeText(mechanicdelete.this, ""+dataSnapshott.getValue().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //remove from activation requests done
                            Query qqq = FirebaseDatabase.getInstance().getReference("/Admin/mechanic_activation_requests").orderByChild("key");
                            qqq.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshott) {
                                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                                        if(childs.child("mid").getValue().toString().equals(mid)){
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
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("mechanic");
                    Query checkUser = reference.orderByChild("mid").equalTo(mid);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String chargesleft=snapshot.child(mid).child("charges").getValue().toString();
                            final EditText dedet=new EditText(mechanicdelete.this);
                            dedet.setInputType(InputType.TYPE_CLASS_NUMBER);
                            AlertDialog.Builder deductdialog= new AlertDialog.Builder(mechanicdelete.this);
                            deductdialog.setTitle("Deduct Charges");
                            String ch=tcharges.getText().toString();
                            deductdialog.setMessage("Mechanic Name: "+name+"\n"+"\nCurrent Charges: "+ch);
                            deductdialog.setView(dedet);

                            deductdialog.setPositiveButton("Deduct", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    String deductedcharges = dedet.getText().toString();
                                    int currcharges = Integer.parseInt(chargesleft);
                                    int dedcharges = Integer.parseInt(deductedcharges);
                                    if(dedcharges<currcharges){
                                        int remcharges = currcharges - dedcharges;
                                        Toast.makeText(mechanicdelete.this, "Charges " + remcharges + " Deducted Successfully!", Toast.LENGTH_SHORT).show();
                                        String setcharges=snapshot.child(mid).child("charges").getValue().toString();
                                        FirebaseDatabase.getInstance().getReference("mechanic").child(mid).child("charges").setValue(String.valueOf(remcharges));
                                        tcharges.setText(String.valueOf(remcharges));
                                        currcharges=remcharges;

                                        String  dc=String.valueOf(dedcharges);
                                        String  rc=String.valueOf(remcharges);
                                        String key =  FirebaseDatabase.getInstance().getReference("Admin").child("mechanic_charges_deduction").push().getKey();
                                        //    String mid,mname,mcnic,mage,mphone,memail,mpass,mimage,category,date,time,deducted_charges,current_charges;
                                        HelperMech_compmessage helper = new HelperMech_compmessage(mid,name,cnic,cid,phone,email,pass,mimg,"",date.toString(),tcomplain,dc,rc);//time me complain pass     mage me cid
                                        FirebaseDatabase.getInstance().getReference("Admin").child("mechanic_charges_deduction").child(key).setValue(helper);

                                        //remove from complains_towing                                  done
                                        Query ct = FirebaseDatabase.getInstance().getReference("/Admin/complains_mechanic").orderByChild("key");
                                        ct.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshott) {
                                                for (DataSnapshot childs : dataSnapshott.getChildren()){
                                                    if(childs.child("mid").getValue().toString().equals(mid)){
                                                        dataSnapshott.getRef().removeValue();
                                                    }
                                                }                }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                }
                                            }
                                        });

                                    }
                                    else{
                                        Toast.makeText(mechanicdelete.this, "Deducted charges exceeds actual amount!", Toast.LENGTH_SHORT).show();
                                        dedet.setText("");
                                        Query qqq = FirebaseDatabase.getInstance().getReference("/Admin/mechanic_activation_requests").orderByChild("key");
                                        qqq.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshott) {
                                                for (DataSnapshot childs : dataSnapshott.getChildren()){
                                                    if(childs.child("mid").getValue().toString().equals(mid)){

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
                  //  final EditText dedet=new EditText(mechanicdelete.this);
                    AlertDialog.Builder ignoredialog= new AlertDialog.Builder(mechanicdelete.this);
                    ignoredialog.setTitle("Ignore Complain?");
                    String ch=tcharges.getText().toString();

                    ignoredialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //remove from complains_towing                                  done
                            Query ct = FirebaseDatabase.getInstance().getReference("/Admin/complains_mechanic").orderByChild("key");
                            ct.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshott) {
                                    for (DataSnapshot childs : dataSnapshott.getChildren()){
                                        if(childs.child("mid").getValue().toString().equals(mid)){
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