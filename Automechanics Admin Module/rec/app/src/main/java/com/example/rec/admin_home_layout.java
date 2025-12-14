package com.example.rec;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.perf.FirebasePerformance;

import java.time.LocalDate;


public class admin_home_layout extends Fragment {
    FirebaseAnalytics mFirebaseAnalytics;
    WebView chartWebView;
    public long cust, mech, tow;
    TextView pending_mech_requests, pending_tow_requests, d1,d2,d3,d4,d5,d6, mechanic_complain,tow_complain,towing_activation,mechanic_activation;
    public admin_home_layout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity3) getActivity()).getSupportActionBar().setTitle("Admin Home");
        View view = inflater.inflate(R.layout.fragment_admin_home_layout, container, false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);

        chartWebView = view.findViewById(R.id.chartWebView);
        pending_mech_requests=view.findViewById(R.id.pending_mech_req);
        pending_tow_requests=view.findViewById(R.id.pending_tow_req);
        towing_activation=view.findViewById(R.id.towing_acti);
        mechanic_activation=view.findViewById(R.id.mech_acti);
        WebSettings webSettings = chartWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //date textview
        d1=view.findViewById(R.id.date);
        d2=view.findViewById(R.id.dateee);
        d3=view.findViewById(R.id.datee);
        d4=view.findViewById(R.id.datte);
        d5=view.findViewById(R.id.dattte);
        d6=view.findViewById(R.id.daatte);

        mechanic_complain=view.findViewById(R.id.mechanic_complains);
        tow_complain=view.findViewById(R.id.towing_complains);
        //current date
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        d1.setText(currentDate.toString());
        d2.setText(currentDate.toString());
        d3.setText(currentDate.toString());
        d4.setText(currentDate.toString());
        d5.setText(currentDate.toString());
        d6.setText(currentDate.toString());
        FirebaseDatabase.getInstance().getReference("user")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Generate the HTML content dynamically based on the data
                        cust = dataSnapshot.getChildrenCount(); // Get the count of child nodes
                        FirebaseDatabase.getInstance().getReference("mechanic")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // Generate the HTML content dynamically based on the data
                                        mech = dataSnapshot.getChildrenCount(); // Get the count of child nodes
                                        FirebaseDatabase.getInstance().getReference("Towing_Shop")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        // Generate the HTML content dynamically based on the data
                                                        tow = dataSnapshot.getChildrenCount(); // Get the count of child nodes
                                                        String htmlContent = generateChartHtml(cust, mech,tow);
                                                        chartWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        // Handle any errors that occur
                                                    }
                                                });
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle any errors that occur
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occur
                    }
                });

        FirebaseDatabase.getInstance().getReference("/Admin/mechanic_requests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshott) {
                        long mech_req = dataSnapshott.getChildrenCount();
                        pending_mech_requests.setText(String.valueOf(mech_req));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occur
                    }
                });
        FirebaseDatabase.getInstance().getReference("/Admin/towing_requests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       long tow_req = dataSnapshot.getChildrenCount();
                        pending_tow_requests.setText(String.valueOf(tow_req));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occur
                    }
                });
        FirebaseDatabase.getInstance().getReference("/Admin/complains_towing")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long comptow = dataSnapshot.getChildrenCount();
                        tow_complain.setText(String.valueOf(comptow));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occur
                    }
                });
        FirebaseDatabase.getInstance().getReference("/Admin/complains_mechanic")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long compmech = dataSnapshot.getChildrenCount();
                        mechanic_complain.setText(String.valueOf(compmech));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        FirebaseDatabase.getInstance().getReference("/Admin/towing_activation_requests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long acttow = dataSnapshot.getChildrenCount();
                        towing_activation.setText(String.valueOf(acttow));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occur
                    }
                });
        FirebaseDatabase.getInstance().getReference("/Admin/mechanic_activation_requests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long actmec = dataSnapshot.getChildrenCount();
                        mechanic_activation.setText(String.valueOf(actmec));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occur
                    }
                });

        chartWebView.setWebViewClient(new WebViewClient());

        return view;
    }
    private String generateChartHtml(long table1Count, long table2Count, long table3Count) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>");
        htmlBuilder.append("<script type=\"text/javascript\">");
        htmlBuilder.append("google.charts.load('current', {packages: ['corechart']});");
        htmlBuilder.append("google.charts.setOnLoadCallback(drawChart);");
        htmlBuilder.append("function drawChart() {");
        htmlBuilder.append("var data = google.visualization.arrayToDataTable([");
        htmlBuilder.append("['Table', 'Count'],");
        htmlBuilder.append("['Customers', " + table1Count + "],");
        htmlBuilder.append("['Mechanics', " + table2Count + "],");
        htmlBuilder.append("['Towing Shops', " + table3Count + "]");
        htmlBuilder.append("]);");
        htmlBuilder.append("var options = {");
        htmlBuilder.append("title: 'Auto-Mechanics Users Ratio: ',");
        htmlBuilder.append("pieHole: 0.4");
        htmlBuilder.append("};");
        htmlBuilder.append("var chart = new google.visualization.PieChart(document.getElementById('chartContainer'));");
        htmlBuilder.append("chart.draw(data, options);");
        htmlBuilder.append("}");
        htmlBuilder.append("</script>");
        htmlBuilder.append("</head>");
        htmlBuilder.append("<body>");
        htmlBuilder.append("<div id=\"chartContainer\" style=\"width: 400px; height: 300px;\"></div>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }

}