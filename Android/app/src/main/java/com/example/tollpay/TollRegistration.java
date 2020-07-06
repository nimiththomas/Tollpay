package com.example.tollpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class TollRegistration extends AppCompatActivity {

    TextInputLayout boothno,place,highwayname,gatecount;
    MaterialButton next;
    String BoothNo,Pwd,Place,HighwayName,GateCount,Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_registration);

        getSupportActionBar().hide();


        boothno=findViewById(R.id.ettollno);

        place=findViewById(R.id.etplace);
        highwayname=findViewById(R.id.ethighway);
        gatecount=findViewById(R.id.ettollgates);
        next=findViewById(R.id.btnnext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoothNo=boothno.getEditText().getText().toString();

                Place=place.getEditText().getText().toString();
                HighwayName=highwayname.getEditText().getText().toString();

                GateCount=gatecount.getEditText().getText().toString();

                Intent i=new Intent(TollRegistration.this,TollRegistration2.class);
                i.putExtra("BoothNo",BoothNo);
                i.putExtra("Place",Place);
                i.putExtra("HighwayName",HighwayName);
                i.putExtra("GateCount",GateCount);
                startActivity(i);


            }
        });


    }


}
