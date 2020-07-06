package com.example.tollpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AmbulanceHome extends AppCompatActivity {

    String name,reg;

    TextView aname,areg;

    CardView maps,profilecard,viewqr;

    ImageView myprofile,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_home);
        getSupportActionBar().hide();

        maps=findViewById(R.id.cardusermaps);
        profilecard=findViewById(R.id.profilecard);
        myprofile=findViewById(R.id.ic_myprofile);
        logout=findViewById(R.id.ic_logout);

        SharedPreferences sh=getSharedPreferences("ambulance",MODE_PRIVATE);
        name=sh.getString("drivername","");
        reg=sh.getString("ambreg","");

        aname=findViewById(R.id.profilename);
        areg=findViewById(R.id.profileregno);


        aname.setText(name);
        areg.setText(reg);


        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AmbulanceHome.this,AmbulanceMaps.class);
                startActivity(i);

            }
        });

        profilecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AmbulanceHome.this,AmbulanceProfile.class);
                startActivity(i);

            }
        });

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AmbulanceHome.this,AmbulanceProfile.class);
                startActivity(i);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AmbulanceHome.this);
                builder.setTitle("Log out");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(AmbulanceHome.this,Login.class);
                        finishAffinity();
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();


            }
        });
    }
}
