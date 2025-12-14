package com.example.rec.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.rec.HelperMech;
import com.example.rec.MainActivity3;
import com.example.rec.R;

import com.example.rec.adaptermechdetail;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class mechanicdetails extends Fragment {
    public static mechanicdetails newInstance() {
        return new mechanicdetails();
    }
    RecyclerView recview;
    adaptermechdetail adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mechanicdetails, container, false);
        ((MainActivity3) getActivity()).getSupportActionBar().setTitle("Search here(with username)..");
        recview=view.findViewById(R.id.recviewformechanic);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<HelperMech> options = new FirebaseRecyclerOptions.Builder<HelperMech>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("mechanic").orderByKey(), HelperMech.class).build();
        adapter=new adaptermechdetail(options);
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
        FirebaseRecyclerOptions<HelperMech> options = new FirebaseRecyclerOptions.Builder<HelperMech>().setQuery(FirebaseDatabase.getInstance().getReference().child("mechanic").orderByChild("mname").startAt(s).endAt(s+"\uf8ff"), HelperMech.class).build();
        adapter=new adaptermechdetail(options);
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