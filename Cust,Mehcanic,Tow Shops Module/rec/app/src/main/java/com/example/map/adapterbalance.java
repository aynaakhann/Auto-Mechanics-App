package com.example.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class adapterbalance extends FirebaseRecyclerAdapter<HelpertowBalance, adapterbalance.myviewholder>
{
    private myviewholder holder;
    private int position;
    private HelpertowBalance modell;
    FirebaseAuth auth;
    String cname;
    Context context;
    

    public adapterbalance(@NonNull FirebaseRecyclerOptions<HelpertowBalance> options, Context context) {
        super(options);
        this.context = context;
    }

    public adapterbalance(@NonNull FirebaseRecyclerOptions<HelpertowBalance> options, myviewholder holder) {
        super(options);
        this.holder = holder;
    }
    public adapterbalance(@NonNull FirebaseRecyclerOptions<HelpertowBalance> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelpertowBalance modell) {
        String cid= modell.getCid();
        DatabaseReference re= FirebaseDatabase.getInstance().getReference("user");
        Query cu = re.orderByChild("uid").equalTo(cid);
        cu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cname=snapshot.child(cid).child("username").getValue().toString();
                holder.custname.setText(cname);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.date.setText(modell.getKey());
        holder.comp.setText(modell.getComplain());
        holder.deductcharg.setText(modell.getStatuscomplain());

    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.towing_balance_layout,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView comp,custname,deductcharg,date;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            custname=(TextView)itemView.findViewById(R.id.cname);
            date=(TextView)itemView.findViewById(R.id.date);
            comp=(TextView)itemView.findViewById(R.id.complaint);
            deductcharg=(TextView)itemView.findViewById(R.id.deductcharges);

            String cname=custname.getText().toString();
            String cdate=date.getText().toString();
            String dcharge=deductcharg.getText().toString();
            String complain=comp.getText().toString();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String cname=custname.getText().toString();
                    String cdate=date.getText().toString();
                    String dcharge=deductcharg.getText().toString();
                    String complain=comp.getText().toString();
                }
            });
        }
    }
}








