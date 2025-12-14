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

public class adaptermechanicdetailforcomplain extends FirebaseRecyclerAdapter<HelperMech_complains, adaptermechanicdetailforcomplain.myviewholder>
{

    private adaptermechanicdetailforcomplain.myviewholder holder;
    private int position;
    private HelperMech_complains modell;
    FirebaseAuth auth;

    Context context;

    public adaptermechanicdetailforcomplain(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options, Context context) {
        super(options);this.context = context;
    }

    public adaptermechanicdetailforcomplain(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options, adaptermechanicdetailforcomplain.myviewholder holder) {
        super(options);
        this.holder = holder;

    }


    public adaptermechanicdetailforcomplain(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperMech_complains modell) {
        holder.name.setText(modell.getMname());
        holder.phone.setText(modell.getMphone());
        holder.email.setText(modell.getMemail());
        holder.mid.setText(modell.getMid());//mid
        holder.cnic.setText(modell.getMcnic());
        holder.pass.setText(modell.getMpass());
        holder.imgpath.setText(modell.getMimage());
        holder.complain.setText(modell.getStatuscomplain());
        holder.cid.setText(modell.getCid());
        //holder.charges.setText(modell.getCharges());
        //Picasso.get().load(modell.getMimage()).into(holder.pic);

        String st= modell.cid;




        //Toast.makeText(context.getApplicationContext(), ""+modell.getShopid(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerrowmech,parent,false);
        return new adaptermechanicdetailforcomplain.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView name,phone,email,mid,cnic,pass,age,imgpath,rimgpath,complain,cid;
        ImageView pic;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.mname);
            phone=(TextView)itemView.findViewById(R.id.mphone);
            email=(TextView)itemView.findViewById(R.id.memail);

            mid=(TextView)itemView.findViewById(R.id.mid);
            cnic=(TextView)itemView.findViewById(R.id.mcnic);
            pass=(TextView)itemView.findViewById(R.id.mpass);
            complain=(TextView)itemView.findViewById(R.id.complain);
            cid=(TextView) itemView.findViewById(R.id.cid);
            imgpath=(TextView)itemView.findViewById(R.id.imagepath);
            rimgpath=(TextView)itemView.findViewById(R.id.rimgpath);
            age=(TextView)itemView.findViewById(R.id.mage);


            pic=(ImageView)itemView.findViewById(R.id.pictureid);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String namee= name.getText().toString();
                    String phonee=phone.getText().toString();
                    String emaill= email.getText().toString();
                    String midd=mid.getText().toString();
                    String cnicc=cnic.getText().toString();
                    String passs= pass.getText().toString();
              //      String rimg=rimgpath.getText().toString();
                    String img=imgpath.getText().toString();
                    String cidd=cid.getText().toString();
                    String agee=age.getText().toString();
                    String complainn=complain.getText().toString();
                    Intent intent=new Intent(v.getContext(),mechanicdelete.class);
                    intent.putExtra("mname",namee);
                    intent.putExtra("phone",phonee);
                    intent.putExtra("mid",midd);
                    intent.putExtra("email",emaill);
                    intent.putExtra("cnic",cnicc);
                    intent.putExtra("pass",passs);
                    intent.putExtra("cid",cidd);
                    intent.putExtra("img",img);
                    intent.putExtra("rimg","rimg");
                    intent.putExtra("complain",complainn);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
