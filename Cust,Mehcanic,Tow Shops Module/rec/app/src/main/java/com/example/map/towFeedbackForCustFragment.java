package com.example.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class towFeedbackForCustFragment extends Fragment {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    float rating;
    TextView textView;
    Button submit;
    RatingBar rb;
    String userid;
    FirebaseAuth auth;
    String cID;
    EditText feedback;
    String feedbackst;
    public towFeedbackForCustFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tow_feedback_for_cust, container, false);
        textView =view.findViewById(R.id.textView);
        submit=view.findViewById(R.id.towsubmitfeedback);
        feedback=view.findViewById(R.id.feedback);
        feedbackst=feedback.getText().toString();
        FirebaseApp.initializeApp(getContext());
        Bundle bundle=this.getArguments();
        cID =bundle.getString("cID");
        auth=FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        myRef.child("History").child(cID).child(userid).child("request_status").removeValue();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(requireContext(), "Feedback Submitted", Toast.LENGTH_SHORT).show();

            //    FirebaseDatabase.getInstance().getReference("Towing Ratings").child(cID).child(key).setValue( problem.getText().toString());
                myRef.child("requests").child(userid).child(cID).removeValue();

                Query q=FirebaseDatabase.getInstance().getReference("Ratings").child("cust_ratings").child(cID);
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
                          //  Toast.makeText(getContext(), ""+childSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                        }
                        countc=countc*5;
                        int tval=(totalValue*5)/countc;

                        FirebaseDatabase.getInstance().getReference("Ratings").child("final_customer_rating").child(cID).child("rating").setValue(String.valueOf(tval));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String key=database.getReference("cust_ratings").push().getKey();
                feedbackst=feedback.getText().toString();
                FirebaseDatabase.getInstance().getReference("Ratings").child("customer_feedback").child(cID).child(key).setValue(feedbackst);
                FirebaseDatabase.getInstance().getReference("Ratings").child("cust_ratings").child(cID).child(key).setValue(rating);
                replaceFragment(new tmapFragment());
            }
        });

        rb=view.findViewById(R.id.towratingBarforCust);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(v==0)
                {
                    textView.setText("Very Dissatisfied");
                    rating=v;
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    String key=database.getReference("Customer_Ratings").push().getKey();
//                    FirebaseDatabase.getInstance().getReference("Towing Ratings").child(cID).child(key).setValue(v);
                }
                else if(v==1)
                {
                    rating=v;
                    textView.setText("Dissatisfied");
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    String key=database.getReference("Customer_Ratings").push().getKey();
//                    FirebaseDatabase.getInstance().getReference("Towing Ratings").child(cID).child(key).setValue(v);
                }
                else if(v==2||v==3)
                {
                    rating=v;
                    textView.setText("OK");
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    String key=database.getReference("Customer_Ratings").push().getKey();
//                    FirebaseDatabase.getInstance().getReference("Towing Ratings").child(cID).child(key).setValue(v);
                }
                else if(v==4)
                {
                    rating=v;
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    String key=database.getReference("Customer_Ratings").push().getKey();
//                    FirebaseDatabase.getInstance().getReference("Towing Ratings").child(cID).child(key).setValue(v);
                    textView.setText("satisfied");
                }
                else if(v==5)
                {
                    rating=v;
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    String key=database.getReference("Customer_Ratings").push().getKey();
//                    FirebaseDatabase.getInstance().getReference("Towing Ratings").child(cID).child(key).setValue(v);
                    textView.setText("very satisfied");
                }

            }
        });

        return view;
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentmanager =getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentmanager.beginTransaction();
        fragmentTransaction.replace(R.id.containertow,fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
                builder.setTitle("Exit Alert")
                        .setIcon(R.drawable.ic_alert_error_msg)
                        .setMessage("You cannot exit!")  ;
                builder.setNegativeButton("OK", null);
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}