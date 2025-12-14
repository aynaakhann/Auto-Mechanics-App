package com.example.map;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.FillResponse;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.Reference;
import java.util.Locale;


public class settingsFragment extends Fragment {

    LinearLayout editProfile,resetpassword,changeLanguage;
    public settingsFragment() {
        // Required empty public constructor
    }

    ImageView resetpass;
    FirebaseAuth auth;
    String uid,email,maill;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        loadLocale();
        editProfile = view.findViewById(R.id.editProfileforCust);
        changeLanguage=view.findViewById(R.id.changeLanguagefromSettingsforCust);
//        resetpassword=view.findViewById(R.id.resetPasswordCust);

//        resetpass= view.findViewById(R.id.resetpass);
        auth=FirebaseAuth.getInstance();
        uid= auth.getCurrentUser().getUid();
        maill=auth.getCurrentUser().getEmail();

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditProfileCustomer();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


//        resetpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(v.getContext());
//                passwordResetDialog.setTitle("Reset Password?");
//                passwordResetDialog.setMessage("Email ID: "+maill+"\nAre you sure to reset password?");
//
//                passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //extract the email and send reset link
//
//
//                        auth.sendPasswordResetEmail(maill).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(getContext(),"Reset Link sent to your email address.", Toast.LENGTH_LONG);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getContext(),"Error! Reset Link is not Sent."+e.getMessage(),Toast.LENGTH_LONG);
//                            }
//                        });
//                    }
//                });
//                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                });
//                passwordResetDialog.create().show();
//
//            }
//        });
        return view;
    }
    private void changeLanguage() {
        String[] languages={"English","اردو"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(languages,-1  ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0)
                {
                    setLocale("");
//                    recreate(); //activity refresh
                    getFragmentManager().beginTransaction().detach(settingsFragment.this).commit();
                    getFragmentManager().beginTransaction().attach(settingsFragment.this).commit();

                }
                else if (which==1)
                {
                    setLocale("ur");
                    getFragmentManager().beginTransaction().detach(settingsFragment.this).commit();
                    getFragmentManager().beginTransaction().attach(settingsFragment.this).commit();
                }
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();


    }

    //for controlling language
    private void setLocale(String language) {
        Locale locale=new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration=new Configuration();
        configuration.locale=locale;


        getActivity().getBaseContext().getResources().updateConfiguration(configuration,getActivity().getBaseContext().getResources().getDisplayMetrics());
        //stays the same after restarting the app so we're passing language to object
        SharedPreferences.Editor editor=getActivity().getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("app_lang",language);
        editor.apply();
    }

    //for loading same language everytime we start app
    private void loadLocale(){
        //receiving the object that has the user selected language and loading it while starting the app
        SharedPreferences preferences=getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        String language=preferences.getString("app_lang","");
        setLocale(language);
    }
}