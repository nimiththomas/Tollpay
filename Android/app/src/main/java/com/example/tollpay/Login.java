package com.example.tollpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    TextInputLayout usnm,upas;
    TextView newuser;

    Button submit;
    String email,pwd;
    String dname,aid,areg,type,userid,username,ureg,tid,tno;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        usnm=findViewById(R.id.etusername);
        upas=findViewById(R.id.etpassword);
        submit=findViewById(R.id.btnlogin);

        submit.setBackgroundColor(getResources().getColor(R.color.blue));


        newuser=findViewById(R.id.newuser);

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,FirstPage.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=usnm.getEditText().getText().toString();
                pwd=upas.getEditText().getText().toString();

                Toast.makeText(Login.this, email+pwd, Toast.LENGTH_SHORT).show();

                getLogin gt=new getLogin();
                gt.execute(email,pwd);


            }
        });


    }

    protected class getLogin extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("getLogin");
            wb.addProperty("email", strings[0]);
            wb.addProperty("password", strings[1]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);




            Log.d("zz", s);

            if (s.equals("[]")) {
                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Login.this, " Login Successfull", Toast.LENGTH_SHORT).show();

                try {
                    JSONArray j = new JSONArray(s);

                    JSONObject obj = j.getJSONObject(0);
                    type = obj.getString("type");


                    if (type.equals("user")) {
                        username = obj.getString("name");
                        userid = obj.getString("id");
                        ureg=obj.getString("reg");

                        SharedPreferences.Editor sh = getSharedPreferences("user", MODE_PRIVATE).edit();
                        sh.putString("username", username);
                        sh.putString("userid", userid);
                        sh.putString("userreg", ureg);
                        sh.apply();

                        Intent i = new Intent(Login.this, UserHome.class);
                        startActivity(i);

                    } else if (type.equals("amb")) {
                        dname = obj.getString("name");
                        aid = obj.getString("id");
                        areg = obj.getString("reg");


                        SharedPreferences.Editor sh = getSharedPreferences("ambulance", MODE_PRIVATE).edit();
                        sh.putString("drivername", dname);
                        sh.putString("ambid", aid);
                        sh.putString("ambreg", areg);
                        sh.apply();

                        Intent i = new Intent(Login.this, AmbulanceHome.class);
                        startActivity(i);
                    }
                    else if (type.equals("toll")) {
                        Log.d("zz",obj.getString("name"));

                        tno = obj.getString("name");
                        tid = obj.getString("id");
                       // areg = obj.getString("reg");


                        SharedPreferences.Editor sh = getSharedPreferences("toll", MODE_PRIVATE).edit();
                        sh.putString("tollno", tno);
                        sh.putString("tollid", tid);
                      //  sh.putString("ambreg", areg);
                        sh.apply();

                        Intent i = new Intent(Login.this, TollHome.class);
                        startActivity(i);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }
}
