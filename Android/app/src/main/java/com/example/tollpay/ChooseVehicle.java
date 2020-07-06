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
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChooseVehicle extends AppCompatActivity {

    TextInputLayout regno,vehname;
    MaterialButton register;
    Spinner type;

    String vid[],vname[];


    String Vehtypeid,RegNo,Vehname;

    Dialog popdialog;

    Button login;

    ArrayAdapter<String> utadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vehicle);

        getSupportActionBar().hide();

        regno=findViewById(R.id.etregno);
        vehname=findViewById(R.id.etvehname);
        type=findViewById(R.id.sptype);
        register=findViewById(R.id.btnregister);

        GetType gt=new GetType();
        gt.execute();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vehtypeid=vid[type.getSelectedItemPosition()];
                RegNo=regno.getEditText().getText().toString();
                Vehname=vehname.getEditText().getText().toString();

                InsertVehicle iv=new InsertVehicle();
                iv.execute(Vehname,RegNo,Vehtypeid);

            }
        });



    }



    public class InsertVehicle extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("InsertVehicle");
            wb.addProperty("name",strings[0]);
            wb.addProperty("regno",strings[1]);
            wb.addProperty("typeid",strings[2]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("zz",s);

            Toast.makeText(ChooseVehicle.this, s, Toast.LENGTH_SHORT).show();

            if(s.equals("Succesfully Inserted")) {

                popdialog = new Dialog(ChooseVehicle.this);

                Shownopopup();
            }
            else {

                Toast.makeText(ChooseVehicle.this, "Insertion Failed.... Try Again", Toast.LENGTH_SHORT).show();

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


                Intent i=new Intent(ChooseVehicle.this,Login.class);
                startActivity(i);

            }
        });

        popdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popdialog.show();
    }


    public class GetType extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("GetType");
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("utp",s);
            try {
                JSONArray uobj= new JSONArray(s);
                vid=new String[uobj.length()+1];
                vname=new String[uobj.length()+1];
                vname[0]="--Select Vehicle Type--";
                vid[0]="";
                for(int i=0;i<uobj.length();i++)
                {
                    JSONObject juobj=uobj.getJSONObject(i);
                    vid[i+1]=juobj.getString("vid");
                    vname[i+1]=juobj.getString("vname");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
          utadapter=new ArrayAdapter<String>(ChooseVehicle.this,android.R.layout.simple_spinner_item,vname);
            utadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            type.setAdapter(utadapter);

        }
    }
}
