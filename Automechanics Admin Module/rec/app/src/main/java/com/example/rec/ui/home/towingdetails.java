package com.example.rec.ui.home;

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

import com.example.rec.HelperTow;
import com.example.rec.MainActivity3;
import com.example.rec.R;
import com.example.rec.adaptertowdetaill;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class towingdetails extends Fragment {
    public static towingdetails newInstance() {
        return new towingdetails();
    }
    RecyclerView recview;
    adaptertowdetaill adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mechanicdetails, container, false);
        ((MainActivity3) getActivity()).getSupportActionBar().setTitle("Search here(with username)..");
        recview=view.findViewById(R.id.recviewformechanic);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<HelperTow> options = new FirebaseRecyclerOptions.Builder<HelperTow>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Towing_Shop"), HelperTow.class).build();
        adapter=new adaptertowdetaill(options);
        recview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){

        inflater.inflate(R.menu.search_menu,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s) {

                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<HelperTow> options = new FirebaseRecyclerOptions.Builder<HelperTow>().setQuery(FirebaseDatabase.getInstance().getReference().child("Towing_Shop").orderByChild("owner_name").startAt(s).endAt(s+"\uf8ff"), HelperTow.class).build();
        adapter=new adaptertowdetaill(options);
        adapter.startListening();
        recview.setAdapter(adapter);

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