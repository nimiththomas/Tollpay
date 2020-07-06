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

public class TollRegistration2 extends AppCompatActivity {

    MaterialButton register;
    String BoothNo,Pwd,Place,HighwayName,GateCount,Confirm,Tollusername;
    TextInputLayout pwd,confirm,tollusername;
    Dialog popdialog;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_registration2);

        getSupportActionBar().hide();

        confirm=findViewById(R.id.etcomfirmpassword);
        register=findViewById(R.id.btnregister);
        pwd=findViewById(R.id.etnewpassword);
        tollusername=findViewById(R.id.ettollusername);


        BoothNo=getIntent().getStringExtra("BoothNo");
        Place=getIntent().getStringExtra("Place");
        HighwayName=getIntent().getStringExtra("HighwayName");
        GateCount=getIntent().getStringExtra("GateCount");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pwd=pwd.getEditText().getText().toString();
                Confirm=confirm.getEditText().getText().toString();
                Tollusername=tollusername.getEditText().getText().toString();


                TollReg tr=new TollReg();
                tr.execute(BoothNo,Place,HighwayName,Pwd,GateCount,Tollusername);
            }
        });

    }

    public class TollReg extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("TollReg");
            wb.addProperty("BoothNo",strings[0]);
            wb.addProperty("Place",strings[1]);
            wb.addProperty("HighwayName",strings[2]);
            wb.addProperty("Pwd",strings[3]);
            wb.addProperty("GateCount",strings[4]);
            wb.addProperty("Tollusername",strings[5]);
            wb.callWebService();

            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(TollRegistration2.this, s, Toast.LENGTH_SHORT).show();

            if(s.equals("Succesfully Inserted")) {

                popdialog = new Dialog(TollRegistration2.this);

                Shownopopup();
            }
            else {

                Toast.makeText(TollRegistration2.this, "Insertion Failed.... Try Again", Toast.LENGTH_SHORT).show();

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


                Intent i=new Intent(TollRegistration2.this,Login.class);
                startActivity(i);

            }
        });

        popdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popdialog.show();
    }

}
