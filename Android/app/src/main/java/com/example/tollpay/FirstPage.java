package com.example.tollpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstPage extends AppCompatActivity {

    Button amb,toll,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        getSupportActionBar().hide();

        amb=findViewById(R.id.tvamb);
        toll=findViewById(R.id.tvtoll);
        user=findViewById(R.id.btnuser);


        amb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FirstPage.this,AmbulanceRegistration.class);
                startActivity(i);


            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(FirstPage.this,UserRegistration1.class);
                startActivity(i);
            }
        });

        toll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FirstPage.this,TollRegistration.class);
                startActivity(i);
            }
        });
    }
}
