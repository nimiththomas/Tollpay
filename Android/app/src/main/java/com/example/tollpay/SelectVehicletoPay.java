package com.example.tollpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectVehicletoPay extends AppCompatActivity {

    RecyclerView rcv_uvehlist;
    String tollid,userid;
    List<SelectVehiclePayBean>selectVehiclePayBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicleto_pay);


        userid=getIntent().getStringExtra("userid");
         tollid=getIntent().getStringExtra("tollid");

        Toast.makeText(this, "Tollid="+tollid, Toast.LENGTH_SHORT).show();

        GetUserVehicle gu=new GetUserVehicle();
        gu.execute(userid);

        rcv_uvehlist=findViewById(R.id.rcv_uvehlist);
        selectVehiclePayBeanList=new ArrayList<>();
    }

    public class GetUserVehicle extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("GetUserVehicle");
            wb.addProperty("userid",strings[0]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("zz",s);
SelectVehiclePayBean selectVehiclePayBean;
            try {
                JSONArray j=new JSONArray(s);

                for (int i=0;i<j.length();i++)
                {
                    JSONObject obj=j.getJSONObject(i);
                    selectVehiclePayBean=new SelectVehiclePayBean();

                    selectVehiclePayBean.setName(obj.getString("uv_name"));
                    selectVehiclePayBean.setReg(obj.getString("uv_regno"));
                    selectVehiclePayBean.setType(obj.getString("vehicletype_name"));
                    selectVehiclePayBean.setTypeid(obj.getString("vehicletype_id"));

                    selectVehiclePayBeanList.add(selectVehiclePayBean);


                }
SelectVehiclePayAdapter selectVehiclePayAdapter=new SelectVehiclePayAdapter(selectVehiclePayBeanList,getApplicationContext(),tollid);
rcv_uvehlist.setLayoutManager(new LinearLayoutManager(SelectVehicletoPay.this));
rcv_uvehlist.setAdapter(selectVehiclePayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
