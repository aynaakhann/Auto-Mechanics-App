package com.example.map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class adapterRecyclerOption extends FirebaseRecyclerAdapter<Model,adapterRecyclerOption.myviewholder>
{
    private myviewholder holder;
    private int position;
    private Model modell;
    FirebaseAuth auth;

    Context context;

    public adapterRecyclerOption(@NonNull FirebaseRecyclerOptions<Model> options, Context context) {
        super(options);
        this.context = context;
    }

    public adapterRecyclerOption(@NonNull FirebaseRecyclerOptions<Model> options, myviewholder holder) {
        super(options);
        this.holder = holder;
    }

    public adapterRecyclerOption(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull Model modell) {
        holder.name.setText(modell.getCust_name());

    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView name;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.custname);
        }
    }
}

