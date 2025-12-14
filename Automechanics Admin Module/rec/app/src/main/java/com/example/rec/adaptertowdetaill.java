package com.example.rec;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class adaptertowdetaill extends FirebaseRecyclerAdapter<HelperTow, adaptertowdetaill.myviewholder>
{
    private adaptertowdetaill.myviewholder holder;
    private int position;
    private HelperTow modell;
    FirebaseAuth auth;

    Context context;
    adaptertowdetaill adapterr;
    public adaptertowdetaill(@NonNull FirebaseRecyclerOptions<HelperTow> options, Context context) {
        super(options);
        this.context = context;
    }
    public adaptertowdetaill(@NonNull FirebaseRecyclerOptions<HelperTow> options, adaptertowdetaill.myviewholder holder) {
        super(options);
        this.holder = holder;
    }

    public adaptertowdetaill(@NonNull FirebaseRecyclerOptions<HelperTow> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperTow modell) {

        holder.name.setText(modell.getOwner_name());
        holder.email.setText(modell.getOwner_email());
        holder.shid.setText(modell.getShopid());
        holder.phone.setText(modell.getOwner_phone());
        holder.cnic.setText(modell.getOwner_cnic());
        holder.shopreg.setText(modell.getShop_regno());
        holder.password.setText(modell.getPassword());
        holder.imgpath.setText(modell.getReg_img());
        holder.rimgpath.setText(modell.getRpayment());
        holder.shopname.setText(modell.getShop_name());
        holder.date.setText(modell.getDate_of_the_month());
        holder.charges.setText(modell.getCharges());
        Picasso.get().load(modell.getReg_img()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singletowrequest,parent,false);
        return new adaptertowdetaill.myviewholder(view);
    }




    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView name,cnic,phone,shid,email,password,imgpath,rimgpath,shopname,shopreg,date,charges;
        ImageView img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.pictureid);
            name=(TextView)itemView.findViewById(R.id.namet);
            cnic=(TextView)itemView.findViewById(R.id.cnict);
            email=(TextView)itemView.findViewById(R.id.emailt);
            imgpath=(TextView)itemView.findViewById(R.id.imagepath);
            phone=(TextView)itemView.findViewById(R.id.phonet);
            shid=(TextView)itemView.findViewById(R.id.shopid);
            shopname=(TextView)itemView.findViewById(R.id.shopname);
            password=(TextView)itemView.findViewById(R.id.passt);
            rimgpath=(TextView)itemView.findViewById(R.id.rimgpath);
            shopreg=(TextView)itemView.findViewById(R.id.shopregisno);
            date=(TextView)itemView.findViewById(R.id.date);
            charges=(TextView)itemView.findViewById(R.id.charges);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameu=name.getText().toString();
                    String cnicu=cnic.getText().toString();
                    String phoneu=phone.getText().toString();
                    String idu=shid.getText().toString();
                    String emailu=email.getText().toString();
                    String passu=password.getText().toString();
                    String imagep=imgpath.getText().toString();
                    String rimagep=rimgpath.getText().toString();
                    String shopnamee=shopname.getText().toString();
                    String shopregg=shopreg.getText().toString();
                    String datee=date.getText().toString();
                    String chargess=charges.getText().toString();
                    Intent intent=new Intent(v.getContext(), towdetact.class);
                    intent.putExtra("tname",nameu);
                    intent.putExtra("tcnic",cnicu);
                    intent.putExtra("tphone",phoneu);
                    intent.putExtra("tid",idu);
                    intent.putExtra("tshopname",shopnamee);
                    intent.putExtra("temail",emailu);
                    intent.putExtra("tpasswd",passu);
                    intent.putExtra("timage",imagep);
                    intent.putExtra("receiptimage",rimagep);
                    intent.putExtra("tshopregg",shopregg);
                    intent.putExtra("tdate",datee);
                    intent.putExtra("tcharges",chargess);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
