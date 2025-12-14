package com.example.map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView profile_img;
    TextView mech_name,mech_ph;
    SwitchCompat switchCompat;
    FirebaseAuth auth;
    String currentUser;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUser=auth.getInstance().getCurrentUser().getUid();

        switchCompat=findViewById(R.id.switchbtn);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar=findViewById(R.id.toolbar);

        View header = navigationView.getHeaderView(0);
//        TextView text = (TextView) header.findViewById(R.id.textView);

        profile_img=header.findViewById(R.id.mech_profile_img);
        mech_name=header.findViewById(R.id.mech_profile_name);
        mech_ph=header.findViewById(R.id.mech_profile_phone);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("mechanic");
        Query checkUser = reference.orderByChild("mid").equalTo(currentUser);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(currentUser).child("mname").getValue().toString();
                String phone = snapshot.child(currentUser).child("mphone").getValue().toString();
                String image=  snapshot.child(currentUser).child("mimage").getValue(String.class);

              //  Toast.makeText(Home.this, "name "+name, Toast.LENGTH_SHORT).show();
//                Toast.makeText(Home.this, "phone "+phone, Toast.LENGTH_SHORT).show();

                mech_name.setText(name);
                mech_ph.setText(phone);
                Picasso.get().load(image).into(profile_img); //to retireve image
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //for switch compat (online , offline state)
        SharedPreferences preferences=getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        switchCompat.setChecked(preferences.getBoolean("switchState",true));

        //for setting custom toolbar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Home.this, drawerLayout,toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(new MapFragment());

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor=getSharedPreferences("myPrefs",MODE_PRIVATE).edit();
                if(isChecked){
                    editor.putBoolean("switchState", true);
                    editor.apply();
                    switchCompat.setChecked(true);

                }
                else{
                    editor.putBoolean("switchState", false);
                    editor.apply();
                    switchCompat.setChecked(false);
                }

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.btnHome) {
                    replaceFragment(new MapFragment());
                }
             else if(id== R.id.servhistory){
                replaceFragment(new mechservicehistory());
            }
                else if(id== R.id.Balance){
                replaceFragment(new balance_mech());
            }
                else if(id== R.id.aboutus){
                  replaceFragment(new aboutUs());
            }else if (id == R.id.btnSetting) {
                    replaceFragment(new mech_SettingFragment());
                } else if (id == R.id.btnSignout) {
                    auth.getInstance().signOut();
                    Intent intent=new Intent(getApplicationContext(),language_user.class);
                    startActivity(intent);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentmanager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentmanager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }
}