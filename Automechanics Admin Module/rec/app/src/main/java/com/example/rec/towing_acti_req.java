package com.example.rec;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class towing_acti_req extends Fragment {
    public static towing_acti_req newInstance() {
        return new towing_acti_req();
    }
    RecyclerView recview;
    adapterrequesttowactivation adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mechanicdetails, container, false);
        ((MainActivity3) getActivity()).getSupportActionBar().setTitle("Towing Activation Requests");
        recview=view.findViewById(R.id.recviewformechanic);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<HelperTow> options = new FirebaseRecyclerOptions.Builder<HelperTow>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("/Admin/towing_activation_requests"), HelperTow.class).build();
        if(adapter==null){
            adapter=new adapterrequesttowactivation(options);
        }
        adapter=new adapterrequesttowactivation(options);
        recview.setAdapter(adapter);

        return view;
    }

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