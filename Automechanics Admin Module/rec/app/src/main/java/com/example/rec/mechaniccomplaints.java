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

public class mechaniccomplaints extends Fragment {
    public static mechaniccomplaints newInstance() {
        return new mechaniccomplaints();
    }
    RecyclerView recview;
    adaptermechanicdetailforcomplain adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mechanicdetails, container, false);
        ((MainActivity3) getActivity()).getSupportActionBar().setTitle("Mechanic Complains");
        recview=view.findViewById(R.id.recviewformechanic);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<HelperMech_complains> options =
                new FirebaseRecyclerOptions.Builder<HelperMech_complains>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("/Admin/complains_mechanic").orderByChild("key"),HelperMech_complains.class)
                        .build();
        adapter=new adaptermechanicdetailforcomplain(options);
        if (recview != null) {
            recview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


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
    public void onDestroy() {
        super.onDestroy();
        // Release resources and references
        recview.setAdapter(null);
        recview = null;
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