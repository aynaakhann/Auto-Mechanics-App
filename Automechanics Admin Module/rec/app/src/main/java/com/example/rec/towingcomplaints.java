package com.example.rec;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class towingcomplaints extends Fragment {
    public static towingcomplaints newInstance() {
        return new towingcomplaints();
    }
    RecyclerView recview;
    adaptertowdetailforcomplain adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mechanicdetails, container, false);
        ((MainActivity3) getActivity()).getSupportActionBar().setTitle("Towing Complains");
        recview=view.findViewById(R.id.recviewformechanic);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Helpertow_compmessage> options =
                new FirebaseRecyclerOptions.Builder<Helpertow_compmessage>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("/Admin/complains_towing").orderByChild("key"),Helpertow_compmessage.class)
                        .build();
        adapter=new adaptertowdetailforcomplain(options);
        if (recview != null) {
            recview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }



        /*   //Query query=FirebaseDatabase.getInstance().getReference("Admin").child("complains_towing").child("custid");
        FirebaseRecyclerOptions<Helpertow_compmessage> options =
                new FirebaseRecyclerOptions.Builder<Helpertow_compmessage>()
                        .setQuery(query, snapshot -> {
                            Helpertow_compmessage user = snapshot.getValue(Helpertow_compmessage.class);
                            user.setKey(snapshot.getKey());
                            return user;
                        })
                        .build();*/
/*
       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String st=snapshot.getKey();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
*/
    /*    SnapshotParser<Helpertow_compmessage> itemParser = new SnapshotParser<Helpertow_compmessage>() {
            @NonNull
            @Override
            public Helpertow_compmessage parseSnapshot(@NonNull DataSnapshot snapshot) {
                Helpertow_compmessage item = snapshot.getValue(Helpertow_compmessage.class);
                item.setKey(snapshot.getKey());
                return item;
            }
        };
       */
       /* FirebaseRecyclerOptions<Helpertow_compmessage> options =
                new FirebaseRecyclerOptions.Builder<Helpertow_compmessage>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Admin").child("complains_towing").getRef().orderByKey(),Helpertow_compmessage.class)
                        .build();*/

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