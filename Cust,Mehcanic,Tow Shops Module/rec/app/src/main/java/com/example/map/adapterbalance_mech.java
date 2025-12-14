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

public class adapterbalance_mech extends FirebaseRecyclerAdapter<HelperMech_compmessage, adapterbalance_mech.myviewholder>
{
    private myviewholder holder;
    private int position;
    private HelperMech_compmessage modell;
    FirebaseAuth auth;
    String cname;
    Context context;
    String namee;

    public adapterbalance_mech(@NonNull FirebaseRecyclerOptions<HelperMech_compmessage> options, Context context) {
        super(options);
        this.context = context;
    }

    public adapterbalance_mech(@NonNull FirebaseRecyclerOptions<HelperMech_compmessage> options, myviewholder holder) {
        super(options);
        this.holder = holder;
    }
    public adapterbalance_mech(@NonNull FirebaseRecyclerOptions<HelperMech_compmessage> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperMech_compmessage modell) {

        String cid= modell.getMage();

        DatabaseReference re=FirebaseDatabase.getInstance().getReference("user");
        Query cu = re.orderByChild("uid");
        cu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childs : snapshot.getChildren()){
                    if(childs.child("uid").getValue().toString().equals(cid)){
                        String cname=childs.child("username").getValue().toString();
                        holder.custname.setText(cname)                                                                               ;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.date.setText(modell.getDate());
        holder.comp.setText(modell.getTime());
        holder.deductcharg.setText(modell.getDeducted_charges());

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








