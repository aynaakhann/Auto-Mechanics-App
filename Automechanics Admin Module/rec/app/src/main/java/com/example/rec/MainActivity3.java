package com.example.rec;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.rec.databinding.ActivityMain3Binding;
import com.example.rec.ui.home.customerdetails;
import com.example.rec.ui.home.mechanicdetails;
import com.example.rec.ui.home.towingdetails;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.perf.v1.TraceMetric;

public class MainActivity3 extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain3Binding binding;
    FirebaseAnalytics mFirebaseAnalytics;
     WebView chartWebView;
    public long cust, mech, tow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setSupportActionBar(binding.appBarMain.toolbar);
        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);

        replaceFragment(new admin_home_layout());

// Start a trace for a specific operation
        Trace trace = FirebasePerformance.getInstance().newTrace("my_operation");
        trace.start();

// Perform the operation you want to track
// ...

// Stop the trace when the operation is complete
        trace.stop();

// Retrieve performance data for the trace
        long metric = trace.getLongMetric(TraceMetric.newBuilder().getName());
//                getMetric(TraceMetric.TRACE_DURATION);

// Display the performance data in your app
        /*TextView textView = findViewById(R.id.my_text_view);
        textView.setText("Trace duration: " + metric.getValue() + " ms");*/
        /*Bundle bundle = new Bundle();
        String id="";
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        String name="jaweria";
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_customerdetails, R.id.nav_mechanicdetails, R.id.nav_towingdetails)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_customerdetails) {
                    replaceFragment(new customerdetails());
                } else if (id == R.id.nav_mechanicdetails) {
                    replaceFragment(new mechanicdetails());
                } else if(id==R.id.nav_towingdetails){
                    replaceFragment(new towingdetails());
                }
                else if (id == R.id.nav_requestmechanic) {
                    replaceFragment(new requestmechanic());
                }else if (id == R.id.nav_requesttowing) {
                    replaceFragment(new requesttowing());
                }else if (id == R.id.nav_mechaniccomplaints) {
                    replaceFragment(new mechaniccomplaints());
                }else if (id == R.id.nav_towingcomplaints) {
                    replaceFragment(new towingcomplaints());
                }else if(id==R.id.nav_home){
                    replaceFragment(new admin_home_layout());
                }
                else if(id==R.id.nav_blocked_accounts_towing){
                    replaceFragment(new blockedtowshops());
                }
                else if(id==R.id.nav_blocked_accounts_mechanic){
                    replaceFragment(new blockedmech());
                }
                else if(id==R.id.nav_requesttowingactivation){
                    replaceFragment(new towing_acti_req());
                }
                else if(id==R.id.nav_requestmechanicactivation){
                    replaceFragment(new mech_acti_req());
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentmanager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentmanager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main,fragment);
        fragmentTransaction.commit();
    }
}