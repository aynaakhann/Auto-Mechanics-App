package com.example.rec;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class adaptertowdetailforcomplain extends FirebaseRecyclerAdapter<Helpertow_compmessage, adaptertowdetailforcomplain.myviewholder>
{

    private adaptertowdetailforcomplain.myviewholder holder;
    private int position;
    private Helpertow_compmessage modell;
    FirebaseAuth auth;

    Context context;

    public adaptertowdetailforcomplain(@NonNull FirebaseRecyclerOptions<Helpertow_compmessage> options, Context context) {
        super(options);this.context = context;
    }

    public adaptertowdetailforcomplain(@NonNull FirebaseRecyclerOptions<Helpertow_compmessage> options, adaptertowdetailforcomplain.myviewholder holder) {
        super(options);
        this.holder = holder;

    }


    public adaptertowdetailforcomplain(@NonNull FirebaseRecyclerOptions<Helpertow_compmessage> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull Helpertow_compmessage modell) {
        holder.name.setText(modell.getOwner_name());
        holder.sname.setText(modell.getShop_name());
        holder.phone.setText(modell.getOwner_phone());
        holder.email.setText(modell.getOwner_email());
        holder.sid.setText(modell.getShopid());
        holder.cnic.setText(modell.getOwner_cnic());
        holder.pass.setText(modell.getPassword());
        holder.sregno.setText(modell.getShop_regno());
        holder.rimgpath.setText(modell.getRpayment());
        holder.imgpath.setText(modell.getReg_img());
        holder.complain.setText(modell.getComplain());
        holder.custoid.setText(modell.getCid());
        //holder.charges.setText(modell.getCharges());
        Picasso.get().load(modell.getReg_img()).into(holder.img);

        String st= modell.cid;




        //Toast.makeText(context.getApplicationContext(), ""+modell.getShopid(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerrowtow,parent,false);
        return new adaptertowdetailforcomplain.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView sname,name,phone,email,sid,cnic,pass,sregno,rimgpath,imgpath,complain,custoid,charges;
        ImageView img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            sname=(TextView)itemView.findViewById(R.id.shopname);
            name=(TextView)itemView.findViewById(R.id.namet);
            phone=(TextView)itemView.findViewById(R.id.phonet);
            email=(TextView)itemView.findViewById(R.id.emailt);

            sid=(TextView)itemView.findViewById(R.id.shopid);
            cnic=(TextView)itemView.findViewById(R.id.cnict);
            pass=(TextView)itemView.findViewById(R.id.passt);
            sregno=(TextView)itemView.findViewById(R.id.shopregisno);
            complain=(TextView)itemView.findViewById(R.id.complain);
            custoid=(TextView) itemView.findViewById(R.id.customerid);
            imgpath=(TextView)itemView.findViewById(R.id.imagepath);
            rimgpath=(TextView)itemView.findViewById(R.id.rimgpath);
            //charges=(TextView)itemView.findViewById(R.id.charges);

            img=(ImageView)itemView.findViewById(R.id.pictureid);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String shopname,namee,phonee,emaill,shopidd,cnicc,passs,shopregn,rimg,img,cid,complainn;
                    shopname= sname.getText().toString();
                    namee= name.getText().toString();
                    phonee=phone.getText().toString();
                    emaill= email.getText().toString();
                    shopidd=sid.getText().toString();
                    //String chargesrs=charges.getText().toString();
                    cnicc=cnic.getText().toString();
                    passs= pass.getText().toString();
                    shopregn=sregno.getText().toString();
                    rimg=rimgpath.getText().toString();
                    img=imgpath.getText().toString();
                    cid=custoid.getText().toString();
                    complainn=complain.getText().toString();
                    Intent intent=new Intent(v.getContext(),towingdelete.class);
                    intent.putExtra("shopname",shopname);
                    intent.putExtra("name",namee);
                    intent.putExtra("phone",phonee);
                    intent.putExtra("shopid",shopidd);
                    intent.putExtra("email",emaill);
                    intent.putExtra("cnic",cnicc);
                    intent.putExtra("pass",passs);
                    intent.putExtra("shopreg",shopregn);
                    intent.putExtra("cid",cid);
                    intent.putExtra("img",img);
                    intent.putExtra("rimg",rimg);
                    //intent.putExtra("charges",chargesrs);
                    intent.putExtra("complain",complainn);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
