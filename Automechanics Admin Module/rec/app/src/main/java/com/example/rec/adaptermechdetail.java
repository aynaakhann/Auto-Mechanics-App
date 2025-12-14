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

public class adaptermechdetail extends FirebaseRecyclerAdapter<HelperMech, adaptermechdetail.myviewholder>
{
    private adaptercustdetail.myviewholder holder;
    private int position;
    private HelperMech modell;
    FirebaseAuth auth;

    Context context;

    public adaptermechdetail(@NonNull FirebaseRecyclerOptions<HelperMech> options, Context context) {
        super(options);
        this.context = context;
    }
    public adaptermechdetail(@NonNull FirebaseRecyclerOptions<HelperMech> options, adaptercustdetail.myviewholder holder) {
        super(options);
        this.holder = holder;
    }

    public adaptermechdetail(@NonNull FirebaseRecyclerOptions<HelperMech> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperMech modell) {
        holder.name.setText(modell.getMname());
        holder.email.setText(modell.getMemail());
        holder.uid.setText(modell.getMid());
        holder.phone.setText(modell.getMphone());
        holder.cnic.setText(modell.getMcnic());
        holder.agee.setText(modell.getMage());
        holder.password.setText(modell.getMpass());
        holder.imgpath.setText(modell.getMimage());
        holder.rimgpath.setText(modell.getRecimg());
        holder.cate.setText(modell.getCategory());
        holder.cnicfr.setText(modell.getCnicfrontpic());
        holder.cnicbc.setText(modell.getCnicbackpic());
        Picasso.get().load(modell.getMimage().toString()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new adaptermechdetail.myviewholder(view);
    }




    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView name,cnic,phone,uid,email,agee,password,imgpath,rimgpath,cate,cnicfr,cnicbc;
        ImageView img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.pictureid);
            name=(TextView)itemView.findViewById(R.id.nametext);
            cnic=(TextView)itemView.findViewById(R.id.cnictext);
            email=(TextView)itemView.findViewById(R.id.emailtext);
            imgpath=(TextView)itemView.findViewById(R.id.imagepath);
            phone=(TextView)itemView.findViewById(R.id.phonetext);
            uid=(TextView)itemView.findViewById(R.id.idtext);
            agee=(TextView)itemView.findViewById(R.id.ageetext);
            password=(TextView)itemView.findViewById(R.id.passtext);
            rimgpath=(TextView)itemView.findViewById(R.id.rimgpath);
            cate=(TextView)itemView.findViewById(R.id.categories);
            cnicfr=(TextView)itemView.findViewById(R.id.cnicft);
            cnicbc=(TextView)itemView.findViewById(R.id.cnicbt);
            //blockbtn=(Button)itemView.findViewById(R.id.bmenu);
            String nameu=name.getText().toString();
            String cnicu=cnic.getText().toString();
            String phoneu=phone.getText().toString();
            String idu=uid.getText().toString();
            String emailu=email.getText().toString();
            String ageu=agee.getText().toString();
            String passu=password.getText().toString();
            String imagep=imgpath.getText().toString();
            String rimagep=rimgpath.getText().toString();
            String categ=cate.getText().toString();
            String fcnic=cnicfr.getText().toString();
            String bcnic=cnicbc.getText().toString();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameu=name.getText().toString();
                    String cnicu=cnic.getText().toString();
                    String phoneu=phone.getText().toString();
                    String idu=uid.getText().toString();
                    String emailu=email.getText().toString();

                    String ageu=agee.getText().toString();
                    String passu=password.getText().toString();
                    String imagep=imgpath.getText().toString();
                    String rimagep=rimgpath.getText().toString();
                    String categ=cate.getText().toString();
                    String fcnic=cnicfr.getText().toString();
                    String bcnic=cnicbc.getText().toString();
                    Intent intent=new Intent(v.getContext(), mechanicdetact.class);
                    intent.putExtra("mname",nameu);
                    intent.putExtra("mcnic",cnicu);
                    intent.putExtra("mphone",phoneu);
                    intent.putExtra("mid",idu);
                    intent.putExtra("mage",ageu);
                    intent.putExtra("memail",emailu);
                    intent.putExtra("mpasswd",passu);
                    intent.putExtra("mimage",imagep);
                    intent.putExtra("receiptimage",rimagep);
                    intent.putExtra("mcatgr",categ);
                    intent.putExtra("cnicfrontpic",fcnic);
                    intent.putExtra("cnicbackpic",bcnic);


                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
