package com.example.rec;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class blockedmech extends Fragment {
    public static blockedmech newInstance() {
        return new blockedmech();
    }
    RecyclerView recview;
    adapterblockedmech adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mechanicdetails, container, false);
        ((MainActivity3) getActivity()).getSupportActionBar().setTitle("Blocked Mechanic Details");
        recview=view.findViewById(R.id.recviewformechanic);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<HelperMech> options = new FirebaseRecyclerOptions.Builder<HelperMech>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("/Admin/deleted_mechanic_accounts").orderByChild("key"), HelperMech.class).build();
        adapter=new adapterblockedmech(options);
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