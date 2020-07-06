package com.example.tollpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddTollRates extends AppCompatActivity {

    String vid[],vname[];
    ArrayAdapter<String> utadapter;
    Spinner type;

    EditText single,returning;
    Button submit;
    String tollid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toll_rates);

        SharedPreferences sh=getSharedPreferences("toll",MODE_PRIVATE);
          tollid=sh.getString("tollid","");

        type=findViewById(R.id.sptype);

        single=findViewById(R.id.ettollratesingle);
        returning=findViewById(R.id.ettollratereturn);
        submit=findViewById(R.id.btnsubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Vehtypeid=vid[type.getSelectedItemPosition()];
                String Singlerate=single.getText().toString().trim();
                String Returnrate=returning.getText().toString().trim();

                InsertTollrate ins=new InsertTollrate();
                ins.execute(Vehtypeid,Singlerate,Returnrate,tollid);


            }
        });
       GetType gt=new GetType();
        gt.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.home: {

                Intent i=new Intent(AddTollRates.this,TollHome.class);
                startActivity(i);
               break;
            }
            case R.id.logout: {
                Intent i=new Intent(AddTollRates.this,Login.class);
                startActivity(i);
                break;

            }
        }
        return super.onOptionsItemSelected(item);
    }

    public  class InsertTollrate extends  AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("InsertTollrate");
            wb.addProperty("Vehtypeid",strings[0]);
            wb.addProperty("Singlerate",strings[1]);
            wb.addProperty("Returnrate",strings[2]);
            wb.addProperty("tollid",strings[3]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(AddTollRates.this, ""+s, Toast.LENGTH_SHORT).show();

            single.setText("");
            returning.setText("");
        }
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
            utadapter=new ArrayAdapter<String>(AddTollRates.this,android.R.layout.simple_spinner_item,vname);
            utadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            type.setAdapter(utadapter);

        }
    }
}
