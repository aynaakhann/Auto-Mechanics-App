package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class profileFragment extends Fragment {
    FirebaseAuth auth;
    TextView signout;//logout
    FirebaseDatabase database;
    TextView username, number, cnic,userid;
    String userEmail;
    String uem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        TextView pname= view.findViewById(R.id.profileusername);
        TextView pnameh= view.findViewById(R.id.profilename);
        TextView pnumber= view.findViewById(R.id.profilenumber);
        TextView pcnic= view.findViewById(R.id.profilecnic);
        TextView pemail= view.findViewById(R.id.profileemail);
        TextView userid= view.findViewById(R.id.userid);
        ImageView pimage=view.findViewById(R.id.pictureid);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uiid = auth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUser = reference.orderByChild("uid").equalTo(auth.getInstance().getCurrentUser().getUid());
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("username").getValue().toString();
                String phone = snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("phone").getValue().toString();
                String email = snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("email").getValue().toString();
                String cnic = snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("cnic").getValue().toString();
                String image=  snapshot.child(auth.getInstance().getCurrentUser().getUid()).child("image").getValue(String.class);
                Picasso.get().load(image).into(pimage);
                pnameh.setText(name);
                pname.setText(name);
                pnumber.setText(phone);
                pemail.setText(email);
                pcnic.setText(cnic);
                userid.setText(uiid);
                Picasso.get().load(image).into(pimage); //to retireve image
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            pname.setText(name);
            pemail.setText(email);
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            //boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
*/

        TextView signout_txt= (TextView) view.findViewById(R.id.signout);

        username= (TextView) view.findViewById(R.id.profileusername);
        number= (TextView) view.findViewById(R.id.profilenumber);
        cnic= (TextView) view.findViewById(R.id.profilecnic);


        signout_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getInstance().signOut();
                Intent intent=new Intent(getActivity(),language_user.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}