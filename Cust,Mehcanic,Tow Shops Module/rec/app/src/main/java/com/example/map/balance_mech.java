package com.example.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class balance_mech extends Fragment {
    public static balance_mech newInstance() {
        return new balance_mech();
    }
    RecyclerView recview;
    String st;
    adapterbalance_mech adapter;
    TextView rbalance;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
   // DatabaseReference myRef=database.getReference("/History/cId");
    FirebaseAuth auth;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        recview=view.findViewById(R.id.recforbalance);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        auth= FirebaseAuth.getInstance();
        rbalance=view.findViewById(R.id.rembalance);
        DatabaseReference re=FirebaseDatabase.getInstance().getReference("mechanic");
        Query cu = re.orderByChild("mid");
        cu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childs : snapshot.getChildren()){
                    if(childs.child("mid").getValue().toString().equals(auth.getCurrentUser().getUid())){
                        String chargs=childs.child("charges").getValue().toString();
                        rbalance.setText(chargs);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String userid=auth.getCurrentUser().getUid();
        FirebaseRecyclerOptions<HelperMech_compmessage> options = new FirebaseRecyclerOptions.Builder<HelperMech_compmessage>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("/Admin/mechanic_charges_deduction").orderByChild("mid").equalTo(userid), HelperMech_compmessage.class).build();
        adapter=new adapterbalance_mech(options);
        recview.setAdapter(adapter);
        return view;
    }
    //.child("cId").orderByChild("currentLoginUser")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (recview != null && recview.getLayoutManager() == null) {
            recview.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        adapter.notifyDataSetChanged();

    }


}