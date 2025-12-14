package com.example.map;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Calendar;

public class adaptercusttowactivities extends FirebaseRecyclerAdapter<HelperTow_complains, adaptercusttowactivities.myviewholder>
{
    private myviewholder holder;
    private int position;
    private HelperTow_complains modell;
    FirebaseAuth auth;

    Context context;

    public adaptercusttowactivities(@NonNull FirebaseRecyclerOptions<HelperTow_complains> options, Context context) {
        super(options);
        this.context = context;
    }

    public adaptercusttowactivities(@NonNull FirebaseRecyclerOptions<HelperTow_complains> options, myviewholder holder) {
        super(options);
        this.holder = holder;
    }
    public adaptercusttowactivities(@NonNull FirebaseRecyclerOptions<HelperTow_complains> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperTow_complains modell) {
        holder.sname.setText(modell.getShop_name());
        holder.oname.setText(modell.getOwner_name());
        holder.address.setText(modell.getShop_name());
        holder.sphone.setText(modell.getOwner_phone());
        holder.time.setText(modell.getTime());
        String st= modell.getStatuscomplain();
        holder.status.setText(st);
        if(modell.getStatuscomplain().equals("sent")){
            holder.complainbtn.setVisibility(View.GONE);
        }

        holder.complainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText comp=new EditText(v.getContext());
                AlertDialog.Builder complainDialog= new AlertDialog.Builder(v.getContext());
                complainDialog.setTitle("Complain");
                complainDialog.setMessage("Enter your Complain");
                complainDialog.setView(comp);
                complainDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        // shopid,cid,complain,statuscomplain, owner_name,owner_cnic,shop_name,shop_regno,owner_phone,owner_email,password,reg_img,rpayment;

                        String tcomplain = comp.getText().toString();
                        String shopid = modell.getShopid();
                        String cid = modell.getcId();
                        String complainstatus = modell.getStatuscomplain();
                        String oname = modell.getOwner_name();
                        String ocnic = modell.getOwner_cnic();
                        String sname = modell.getShop_name();
                        String sregno = modell.getShop_regno();
                        String ophone = modell.getOwner_phone();
                        String oemail = modell.getOwner_email();
                        String password = modell.getPassword();
                        String rimg = modell.getReg_img();
                        String rpay = modell.getRpayment();
                        Calendar calendar = Calendar.getInstance();
                        LocalDate date = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            date = LocalDate.now();
                        }
                        String key =  FirebaseDatabase.getInstance().getReference("Admin").child("complains_towing").push().getKey();
                        Helpertow_compmessage helper = new Helpertow_compmessage(shopid,cid,tcomplain,"",oname,ocnic,sname,sregno,ophone,oemail,password,rimg,rpay,date.toString());
                        FirebaseDatabase.getInstance().getReference("Admin").child("complains_towing").child(key).setValue(helper);
                        DatabaseReference reff=FirebaseDatabase.getInstance().getReference("History_Tow");


                        Query q=reff.orderByChild("cId").equalTo(modell.getcId());
                        q.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot childs : snapshot.getChildren()){
                                    if(childs.child("cId").getValue().toString().equals(modell.getcId())){
                                        childs.child("statuscomplain").getRef().setValue("sent");
                                        modell.setStatuscomplain("sent");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                complainDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                complainDialog.create().show();
            }
        });
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_activities_layout,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView sname,oname,address,sphone,status,time;
        TextView uid,email,password,imgpath,rimgpath;
        ImageView img;
        Button complainbtn;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
          //  img=(ImageView)itemView.findViewById(R.id.pictureid);
            sname=(TextView)itemView.findViewById(R.id.tshop_name);
            oname=(TextView)itemView.findViewById(R.id.ownname);
            time=(TextView)itemView.findViewById(R.id.timee);
            address=(TextView)itemView.findViewById(R.id.saddress);
            sphone=(TextView)itemView.findViewById(R.id.sphone);
            status=(TextView)itemView.findViewById(R.id.btnstatus);
            complainbtn=(Button) itemView.findViewById(R.id.compalain_btn);
            //blockbtn=(Button)itemView.findViewById(R.id.bmenu);
            String shopnamee=sname.getText().toString();
            String ownernamee=oname.getText().toString();
            String saddress=address.getText().toString();
            String ophone=sphone.getText().toString();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String shopnamee=sname.getText().toString();
                    String ownernamee=oname.getText().toString();
                    String saddress=address.getText().toString();
                    String ophone=sphone.getText().toString();

                    //Toast.makeText(v.getContext(), ""+shopnamee+ownernamee, Toast.LENGTH_SHORT).show();

                    /*Intent intent=new Intent(v.getContext(), customerdelete.class);
                    intent.putExtra("username",nameu);
                    intent.putExtra("usercnic",cnicu);
                    intent.putExtra("userphone",phoneu);
                    intent.putExtra("userid",idu);
                    intent.putExtra("useremail",emailu);
                    intent.putExtra("userpass",passu);
                    intent.putExtra("userimage",imagep);
                    intent.putExtra("userreceiptimage",rimagep);
                    Toast.makeText(v.getContext(), ""+imagep, Toast.LENGTH_SHORT).show();
                    v.getContext().startActivity(intent);*/
                }
            });
        }
    }
}








