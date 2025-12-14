package com.example.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class currentmechanicactivities extends Fragment {
    public static currentmechanicactivities newInstance() {
        return new currentmechanicactivities();
    }
    RecyclerView recview;
    String st;
    adaptercustmechactivities adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currentmechservices, container, false);
        recview=view.findViewById(R.id.recviewformechservices);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        auth= FirebaseAuth.getInstance();
        String cuserid=auth.getCurrentUser().getUid();
       // Toast.makeText(getContext(), ""+cuserid, Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions<HelperMech_complains> options = new FirebaseRecyclerOptions.Builder<HelperMech_complains>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("History_Mech").orderByChild("cid").equalTo(cuserid), HelperMech_complains.class).build();
        adapter=new adaptercustmechactivities(options);
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