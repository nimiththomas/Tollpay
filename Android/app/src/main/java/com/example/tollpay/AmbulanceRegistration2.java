package com.example.tollpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AmbulanceRegistration2 extends AppCompatActivity {

    TextInputLayout regno,pwd,confirm;
    MaterialButton register;
    String Name,Address,Phone,Email,Regno,Pwd,Confirm;

    Dialog popdialog;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_registration2);

        getSupportActionBar().hide();

        regno=findViewById(R.id.etregno);
        pwd=findViewById(R.id.etnewpassword);
        confirm=findViewById(R.id.etcomfirmpassword);
        register=findViewById(R.id.btnregister);

        Name=getIntent().getStringExtra("name");
        Address=getIntent().getStringExtra("address");
        Phone=getIntent().getStringExtra("phone");
        Email=getIntent().getStringExtra("mail");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regno=regno.getEditText().getText().toString();
                Pwd=pwd.getEditText().getText().toString();
                Confirm=confirm.getEditText().getText().toString();


                AmbReg ar=new AmbReg();
                ar.execute(Name,Address,Email,Phone,Regno,Pwd);
            }
        });

    }

    public class AmbReg extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("AmbReg");
            wb.addProperty("name",strings[0]);
            wb.addProperty("address",strings[1]);
            wb.addProperty("email",strings[2]);
            wb.addProperty("phone",strings[3]);
            wb.addProperty("regno",strings[4]);
            wb.addProperty("pwd",strings[5]);
            wb.callWebService();

            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("zz",s);

            Toast.makeText(AmbulanceRegistration2.this, s, Toast.LENGTH_SHORT).show();

            if(s.equals("Succesfully Inserted")) {

                popdialog = new Dialog(AmbulanceRegistration2.this);

                Shownopopup();
            }
            else {

                Toast.makeText(AmbulanceRegistration2.this, "Insertion Failed.... Try Again", Toast.LENGTH_SHORT).show();

            }



        }
    }


    public void Shownopopup() {
        popdialog.setContentView(R.layout.regcompleted);

        login = popdialog.findViewById(R.id.btnlogin);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popdialog.dismiss();


                Intent i=new Intent(AmbulanceRegistration2.this,Login.class);
                startActivity(i);

            }
        });

        popdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popdialog.show();
    }

}
