package com.example.map;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class language_user extends AppCompatActivity {
     int d; //get radiobutton id
     String e; //get radiobtn text
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_language_user);

        Button chng_language=findViewById(R.id.change_language);
        Button next=findViewById(R.id.next_tologin);
        RadioGroup roles=findViewById(R.id.roles);
        RadioButton Mechanic=findViewById(R.id.mechanic);
        RadioButton customer=findViewById(R.id.customer);
        RadioButton towing=findViewById(R.id.towing);

        chng_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage();
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d= roles.getCheckedRadioButtonId();
                RadioButton radioButton= findViewById(d);
                e=radioButton.getText().toString();
                if(e.equals("Customer") || e.equals("صارف")){
                    Intent intent=new Intent(language_user.this, login_customer.class);
                    startActivity(intent);
                }
                else if(e.equals("Mechanic") || e.equals("مکینک")){
                    Intent intent=new Intent(language_user.this,login_mechanic.class);
                    startActivity(intent);
                    //finish();
                }
                else if(e.equals("Towing Workshop") || e.equals("ٹاونگ ورکشاپ")){
                    Intent intent=new Intent(language_user.this,login_tow.class);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }
    private void changeLanguage() {
        String[] languages={"English","اردو"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(languages,-1  ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0)
                {
                    setLocale("");
                    recreate(); //activity refresh
                }
                else if (which==1)
                {
                    setLocale("ur");
                    recreate();
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


        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        //stays the same after restarting the app so we're passing language to object
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("app_lang",language);
        editor.apply();
    }

    //for loading same language everytime we start app
    private void loadLocale(){
        //receiving the object that has the user selected language and loading it while starting the app
        SharedPreferences preferences=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=preferences.getString("app_lang","");
        setLocale(language);
    }
}