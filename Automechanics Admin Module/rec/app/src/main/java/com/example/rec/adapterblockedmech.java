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

public class adapterblockedmech extends FirebaseRecyclerAdapter<HelperMech, adapterblockedmech.myviewholder>
{
    private adapterblockedmech.myviewholder holder;
    private int position;
    private HelperMech modell;
    FirebaseAuth auth;
    Context context;
    adapterblockedmech adapterr;
    public adapterblockedmech(@NonNull FirebaseRecyclerOptions<HelperMech> options, Context context) {
        super(options);
        this.context = context;
    }
    public adapterblockedmech(@NonNull FirebaseRecyclerOptions<HelperMech> options, adapterblockedmech.myviewholder holder) {
        super(options);
        this.holder = holder;
    }

    public adapterblockedmech(@NonNull FirebaseRecyclerOptions<HelperMech> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperMech modell) {

        holder.name.setText(modell.getMname());
        holder.email.setText(modell.getMemail());
        holder.shid.setText(modell.getMid());
        holder.phone.setText(modell.getMphone());
        holder.cnic.setText(modell.getMcnic());
        holder.password.setText(modell.getMpass());
        holder.date.setText(modell.getRecimg());
        holder.charges.setText(modell.getCnicbackpic());
        holder.age.setText(modell.getMage());
    //    Picasso.get().load(modell.getMimage().toString()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerrowmech,parent,false);
        return new adapterblockedmech.myviewholder(view);
    }




    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView name,cnic,phone,shid,email,password,date,charges,age;
        ImageView img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.mname);
            cnic=(TextView)itemView.findViewById(R.id.mcnic);
            email=(TextView)itemView.findViewById(R.id.memail);
            phone=(TextView)itemView.findViewById(R.id.mphone);
            shid=(TextView)itemView.findViewById(R.id.mid);
            password=(TextView)itemView.findViewById(R.id.mpass);
            age=(TextView)itemView.findViewById(R.id.mage);
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
                    String datee=date.getText().toString();
                    String agee=age.getText().toString();
                    String chargess=charges.getText().toString();
                    Intent intent=new Intent(v.getContext(), block_mech_details.class);
                    intent.putExtra("mname",nameu);
                    intent.putExtra("mcnic",cnicu);
                    intent.putExtra("mphone",phoneu);
                    intent.putExtra("mid",idu);
                    intent.putExtra("memail",emailu);
                    intent.putExtra("mpasswd",passu);
                    intent.putExtra("mdate",datee);
                    intent.putExtra("mcharges",chargess);
                    intent.putExtra("mage",agee);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
