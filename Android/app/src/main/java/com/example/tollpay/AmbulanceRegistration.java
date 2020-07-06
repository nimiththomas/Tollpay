package com.example.tollpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AmbulanceRegistration extends AppCompatActivity {

    TextInputLayout name,address,phone,email;
    MaterialButton next;
    String Name,Address,Phone,Email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_registration);

        getSupportActionBar().hide();

        name=findViewById(R.id.etname);
        address=findViewById(R.id.etaddress);
        phone=findViewById(R.id.etphone);
        email=findViewById(R.id.etmail);
        next=findViewById(R.id.btnnext);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name=name.getEditText().getText().toString();
                Address=address.getEditText().getText().toString();
                Phone=phone.getEditText().getText().toString();
                Email=email.getEditText().getText().toString();

                Intent i=new Intent(AmbulanceRegistration.this,AmbulanceRegistration2.class);
                i.putExtra("name",Name);
                i.putExtra("address",Address);
                i.putExtra("phone",Phone);
                i.putExtra("mail",Email);
                startActivity(i);

            }
        });

    }
}
