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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class adapterrequestmechactivation extends FirebaseRecyclerAdapter<HelperMechActivation, adapterrequestmechactivation.myviewholder>
{
    private adapterrequestmechactivation.myviewholder holder;
    private int position;
    private HelperMechActivation modell;
    FirebaseAuth auth;
    String imgg;

    Context context;
    adapterrequestmechactivation adapterr;
    public adapterrequestmechactivation(@NonNull FirebaseRecyclerOptions<HelperMechActivation> options, Context context) {
        super(options);
        this.context = context;
    }
    public adapterrequestmechactivation(@NonNull FirebaseRecyclerOptions<HelperMechActivation> options, adapterrequestmechactivation.myviewholder holder) {
        super(options);
        this.holder = holder;
    }

    public adapterrequestmechactivation(@NonNull FirebaseRecyclerOptions<HelperMechActivation> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperMechActivation modell) {

        holder.name.setText(modell.getMname());
        holder.cnic.setText(modell.getMcnic());
        holder.id.setText(modell.getMid());
        holder.categ.setText(modell.getMcnic());
        holder.chargess.setText(modell.getCharges());
        holder.age.setText(modell.getMage());
        holder.cnicbk.setText(modell.getCnicbackpic());
        holder.cnicft.setText(modell.getCnicfrontpic());
        holder.email.setText(modell.getMemail());
        holder.pass.setText(modell.getMpass());
        holder.phone.setText(modell.getMphone());
        holder.recimge.setText(modell.getRecimg());

        String idd=holder.id.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("mechanic");
        String mid = modell.getMid();
        reference.child(mid).child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageValue = dataSnapshot.getValue(String.class);
                holder.image.setText(imageValue);
                Picasso.get().load(imageValue).into(holder.img);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlermechrequest,parent,false);
        return new adapterrequestmechactivation.myviewholder(view);
    }




    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView name,cnic,id,categ,chargess,age,cnicbk,cnicft,email,pass,phone,recimge,image;
        ImageView img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.pictureid);
            name=(TextView)itemView.findViewById(R.id.mname);
            cnic=(TextView)itemView.findViewById(R.id.mcnic);
            email=(TextView)itemView.findViewById(R.id.memail);
            image=(TextView)itemView.findViewById(R.id.mimage);
            phone=(TextView)itemView.findViewById(R.id.mphone);
            pass=(TextView)itemView.findViewById(R.id.mpass);
            recimge=(TextView)itemView.findViewById(R.id.recimg);
            cnicbk=(TextView)itemView.findViewById(R.id.cnicb);
            cnicft=(TextView)itemView.findViewById(R.id.cnicf);
            age=(TextView)itemView.findViewById(R.id.mage);
            categ=(TextView)itemView.findViewById(R.id.category);
            chargess=(TextView)itemView.findViewById(R.id.charges);
            id=(TextView)itemView.findViewById(R.id.mid);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String namee=name.getText().toString();
                    String cnicc=cnic.getText().toString();
                    String phonee=phone.getText().toString();
                    String mid=id.getText().toString();
                    String emaill=email.getText().toString();
                    String passs=pass.getText().toString();
                    String imagepp=image.getText().toString();
                    String rimagepp=recimge.getText().toString();
                    String categgg=categ.getText().toString();
                    String ageee=age.getText().toString();
                    String cnicbb=cnicbk.getText().toString();
                    String cnicff=cnicft.getText().toString();
                    String chargss=chargess.getText().toString();
                    Intent intent=new Intent(v.getContext(), mechactivation.class);
                    intent.putExtra("name",namee);
                    intent.putExtra("cnic",cnicc);
                    intent.putExtra("phone",phonee);
                    intent.putExtra("mid",mid);
                    intent.putExtra("email",emaill);
                    intent.putExtra("pass",passs);
                    intent.putExtra("categ",categgg);
                    intent.putExtra("img",imagepp);
                    intent.putExtra("rimg",rimagepp);
                    intent.putExtra("age",ageee);
                    intent.putExtra("cnicback",cnicbb);
                    intent.putExtra("cnicfront",cnicff);
                    intent.putExtra("charges",chargss);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
