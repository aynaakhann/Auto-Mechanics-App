package com.example.map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
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

public class Towing_home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SwitchCompat switchCompat;
    FirebaseAuth auth;
    TextView tow_name,tow_ph;
    String cUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towing_home);
        switchCompat=findViewById(R.id.switchbtn);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.towNavigationView);
        toolbar=findViewById(R.id.toolbar);

        View headerr= navigationView.getHeaderView(0);
        //TextView text = (TextView) header.findViewById(R.id.textView);

        tow_name=headerr.findViewById(R.id.tow_profile_name);
        tow_ph=headerr.findViewById(R.id.tow_profile_phone);

        cUser=auth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Towing_Shop");
        Query checkUser = reference.orderByChild("shopid").equalTo(cUser);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(cUser).child("shop_name").getValue().toString();
                String phone = snapshot.child(cUser).child("owner_phone").getValue().toString();
              //  Toast.makeText(Towing_home.this, "name "+name, Toast.LENGTH_SHORT).show();
                //Toast.makeText(Towing_home.this, "phone "+phone, Toast.LENGTH_SHORT).show();
                tow_name.setText(name);
                tow_ph.setText(phone);
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Towing_home.this, drawerLayout,toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(new tmapFragment());

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
                    replaceFragment(new tmapFragment());
                } else if(id== R.id.servhistory){
                    replaceFragment(new towserviceshistory());
                }
                else if(id== R.id.Balance){
                    replaceFragment(new balance());
                }
                else if(id== R.id.aboutus){
                    replaceFragment(new aboutUs());
                }

                else if (id == R.id.btnSetting) {
                    replaceFragment(new tow_SettingFragment());
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
        fragmentTransaction.replace(R.id.containertow,fragment);
        fragmentTransaction.commit();
    }

}
