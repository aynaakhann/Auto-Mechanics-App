package com.example.map;

import static android.widget.GridLayout.HORIZONTAL;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myMechAdpter extends RecyclerView.Adapter<myMechViewHolder>{

    Context context;
    ArrayList<MechModel> reqListforMech;

    public myMechAdpter(ArrayList<MechModel> reqListforMech, Context context) {
        this.context = context;
        this.reqListforMech = reqListforMech;
    }

    @NonNull
    @Override
    public myMechViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.mech_single_req_layout,parent,false);
        return new myMechViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myMechViewHolder holder, int position) {

        holder.reqid.setText(String.valueOf(reqListforMech.get(position).getCustidforMech()));
        holder.cust_name.setText(reqListforMech.get(position).getCust_nameforMech());
//        holder.ratings.setText(reqListforMech.get(position).getCustRatings());
        holder.distance.setText(reqListforMech.get(position).getCustdistanceforMech());
//        holder.time_travel.setText(req.get(position).getTime_travel());
        holder.viewCustProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                String id=holder.reqid.getText().toString();
                String name=holder.cust_name.getText().toString();
                String dist=holder.distance.getText().toString();
//                String ratings=holder.ratings.getText().toString();
                bundle.putString("custIdforMech",id);
                bundle.putString("custNameforMech",name);
                bundle.putString("custDistforMech",dist);
//                bundle.putString("custRatingsforMech",ratings);

                AppCompatActivity activity=(AppCompatActivity) v.getContext();

                customerProfileforReqFragment mech=new customerProfileforReqFragment();
                mech.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,mech).addToBackStack(null).commit();
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
    public int getItemCount() {
        return reqListforMech.size();
    }

    @Override
    public int getItemViewType(int position) {
        return HORIZONTAL;
    }
}
