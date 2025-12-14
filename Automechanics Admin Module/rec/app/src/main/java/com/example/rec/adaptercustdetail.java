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
import com.squareup.picasso.Picasso;

public class adaptercustdetail extends FirebaseRecyclerAdapter<Helpercust, adaptercustdetail.myviewholder>
{
    private myviewholder holder;
    private int position;
    private Helpercust modell;
    FirebaseAuth auth;

    Context context;

    public adaptercustdetail(@NonNull FirebaseRecyclerOptions<Helpercust> options, Context context) {
        super(options);
        this.context = context;
    }

    public adaptercustdetail(@NonNull FirebaseRecyclerOptions<Helpercust> options, myviewholder holder) {
        super(options);
        this.holder = holder;
    }

    public adaptercustdetail(@NonNull FirebaseRecyclerOptions<Helpercust> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull Helpercust modell) {
        holder.name.setText(modell.getUsername());
        holder.email.setText(modell.getEmail());
        holder.uid.setText(modell.getUid());
        holder.phone.setText(modell.getPhone());
        holder.cnic.setText(modell.getCnic());
        holder.password.setText(modell.getPassword());
        holder.imgpath.setText(modell.getImage());
        Picasso.get().load(modell.getImage()).into(holder.img);
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new adaptercustdetail.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView name,cnic,phone,uid,email,password,imgpath,rimgpath;
        ImageView img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.pictureid);
            name=(TextView)itemView.findViewById(R.id.nametext);
            imgpath=(TextView)itemView.findViewById(R.id.imagepath);
            cnic=(TextView)itemView.findViewById(R.id.cnictext);
            email=(TextView)itemView.findViewById(R.id.emailtext);
            phone=(TextView)itemView.findViewById(R.id.phonetext);
            uid=(TextView)itemView.findViewById(R.id.idtext);
            password=(TextView)itemView.findViewById(R.id.passtext);
            rimgpath=(TextView)itemView.findViewById(R.id.rimgpath);
            //blockbtn=(Button)itemView.findViewById(R.id.bmenu);
            String nameu=name.getText().toString();
            String cnicu=cnic.getText().toString();
            String phoneu=phone.getText().toString();
            String idu=uid.getText().toString();
            String passu=password.getText().toString();
            String emailu=email.getText().toString();
            String imagep=imgpath.getText().toString();
            String rimagep=rimgpath.getText().toString();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameu=name.getText().toString();
                    String cnicu=cnic.getText().toString();
                    String phoneu=phone.getText().toString();
                    String idu=uid.getText().toString();
                    String passu=password.getText().toString();
                    String imagep=imgpath.getText().toString();
                    String emailu=email.getText().toString();
                    String rimagep=rimgpath.getText().toString();
                    Intent intent=new Intent(v.getContext(), customerdetact.class);
                    intent.putExtra("username",nameu);
                    intent.putExtra("usercnic",cnicu);
                    intent.putExtra("userphone",phoneu);
                    intent.putExtra("userid",idu);
                    intent.putExtra("useremail",emailu);
                    intent.putExtra("userpass",passu);
                    intent.putExtra("userimage",imagep);
                    intent.putExtra("userreceiptimage",rimagep);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}








