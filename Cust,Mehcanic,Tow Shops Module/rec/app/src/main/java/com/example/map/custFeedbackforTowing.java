package com.example.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class custFeedbackforTowing extends AppCompatActivity {
    TextView textView;
    Button submit;
    RatingBar rb;
    EditText problem;
    float rating;
    EditText feedback;
    String feedbackst;
    FirebaseAuth auth;
    String shopID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_feedbackfor_towing);
        textView =findViewById(R.id.textViewCT);
        submit=findViewById(R.id.submitfeedbackCT);
        rb=findViewById(R.id.ratingBarCT);
        feedback=findViewById(R.id.feedback);
        feedbackst=feedback.getText().toString();
        Intent intent=getIntent();
        shopID =intent.getStringExtra("sId");
        auth=FirebaseAuth.getInstance();
        String userid=auth.getCurrentUser().getUid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(custFeedbackforTowing.this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
                /*FirebaseDatabase database = FirebaseDatabase.getInstance();
                String key=database.getReference("Cust_Ratings").push().getKey();
                FirebaseDatabase.getInstance().getReference("Towing Ratings").child(shopID).child(key).setValue(rating);*/

                Query q=FirebaseDatabase.getInstance().getReference("Ratings").child("tow_ratings").child(shopID);
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int totalValue=0;
                        String countchild;
                        int countc=1;
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            countchild=String.valueOf(snapshot.getChildrenCount());
                            countc=Integer.parseInt(countchild);
                            int nval=Integer.parseInt(childSnapshot.getValue().toString());
                            totalValue += nval;
                        }
                        countc=countc*5;
                        int tval=(totalValue*5)/countc;
                        FirebaseDatabase.getInstance().getReference("Ratings").child("final_tow_rating").child(shopID).child("rating").setValue(String.valueOf(tval));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String key=database.getReference("tow_ratings").push().getKey();
                feedbackst=feedback.getText().toString();
                FirebaseDatabase.getInstance().getReference("Ratings").child("towing_feedback").child(shopID).child(key).setValue(feedbackst);
                FirebaseDatabase.getInstance().getReference("Ratings").child("tow_ratings").child(shopID).child(key).setValue(rating);
                Intent intent=new Intent(custFeedbackforTowing.this,cust_mainHome.class);
                startActivity(intent);
                finish();
            }
        });

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(v==0)
                {
                    textView.setText("Very Dissatisfied");
                    rating=v;
                }
                else if(v==1)
                {
                    rating=v;
                    textView.setText("Dissatisfied");
                }
                else if(v==2||v==3)
                {
                    rating=v;
                    textView.setText("OK");
                }
                else if(v==4)
                {
                    rating=v;
                    textView.setText("satisfied");
                }
                else if(v==5)
                {
                    rating=v;
                    textView.setText("very satisfied");
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Alert")
                .setIcon(R.drawable.ic_alert_error_msg)
                .setMessage("You cannot exit!")  ;

        builder.setNegativeButton("ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }}