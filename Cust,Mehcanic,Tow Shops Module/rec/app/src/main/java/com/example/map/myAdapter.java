package com.example.map;

import static android.widget.GridLayout.HORIZONTAL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myViewHolder> {

    Context context;
    ArrayList<Model> reqList;

    public myAdapter(ArrayList<Model> reqList, Context context)
    {
        this.reqList = reqList;
        this.context=context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.request_layout,parent,false);
        return new myViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.reqid.setText(String.valueOf(reqList.get(position).getId()));
        holder.cust_name.setText(reqList.get(position).getCust_name());
//        holder.ratings.setText(reqList.get(position).getRatings());
        holder.distance.setText(reqList.get(position).getDistance());
//        holder.time_travel.setText(req.get(position).getTime_travel());
        holder.viewCustProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                String id=holder.reqid.getText().toString();
                String name=holder.cust_name.getText().toString();
                String dist=holder.distance.getText().toString();
//                String ratings=holder.ratings.getText().toString();
                bundle.putString("custId",id);
                bundle.putString("custName",name);
                bundle.putString("custDist",dist);
//                bundle.putString("custRatings",ratings);

                AppCompatActivity activity=(AppCompatActivity) v.getContext();

                custProfileforTowingFragment towing=new custProfileforTowingFragment();
                towing.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containertow,towing).addToBackStack(null).commit();
            }
        });

//        holder.towRejectCustReq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Declined Request", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    @Override
    public int getItemViewType(int position) {
        return HORIZONTAL;
    }

    @Override
    public int getItemCount() {
        return reqList.size();
    }


}
