package com.example.tollpay;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class UserRegistration1 extends AppCompatActivity {

    TextInputLayout name, phone, email;
    TextInputLayout pwd, confirm;
    MaterialButton next;
    String Name, Pwd, Phone, Email, Confirm;

    Dialog popdialog;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration1);

        getSupportActionBar().hide();

        name = findViewById(R.id.etname);
        pwd = findViewById(R.id.etnewpassword);
        phone = findViewById(R.id.etphone);
        email = findViewById(R.id.etmail);
        next = findViewById(R.id.btnnext);
        confirm = findViewById(R.id.etcomfirmpassword);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = name.getEditText().getText().toString();
                Pwd = pwd.getEditText().getText().toString();
                Phone = phone.getEditText().getText().toString();
                Email = email.getEditText().getText().toString();
                Confirm = confirm.getEditText().getText().toString();

                UserReg us = new UserReg();
                us.execute(Name, Email, Phone, Pwd);
            }
        });
    }

    public class UserReg extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("UserReg");
            wb.addProperty("name", strings[0]);
            wb.addProperty("email", strings[1]);
            wb.addProperty("phone", strings[2]);
            wb.addProperty("pwd", strings[3]);
            wb.callWebService();

            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(UserRegistration1.this, s, Toast.LENGTH_SHORT).show();

            Intent i = new Intent(UserRegistration1.this, ChooseVehicle.class);
            startActivity(i);


        }
    }


}
