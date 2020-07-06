package com.example.tollpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile extends AppCompatActivity {
    String userid, name, email, contact, address, reg;
    ImageView img, editprofile, changepassword;
    Switch qrstate;

    SharedPref sharedPref;


    TextView name1, email1, contact1, address1, tvregno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        sharedPref=new SharedPref(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        name1 = findViewById(R.id.tvname);
        email1 = findViewById(R.id.tvemail);
        contact1 = findViewById(R.id.tvcontact);
        address1 = findViewById(R.id.tvaddress);
        tvregno = findViewById(R.id.tvregno);


        qrstate = findViewById(R.id.switchstate);

        editprofile = findViewById(R.id.editprofile);
        changepassword = findViewById(R.id.changepassword);

        img = findViewById(R.id.imageView);


        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");


        if(sharedPref.loadQrState()==true)
        {
            qrstate.setChecked(true);
        }

        myprofile my = new myprofile();
        my.execute(userid);

        qrstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    sharedPref.directQRModeState(true);
                    Toast.makeText(UserProfile.this, "ON", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sharedPref.directQRModeState(false);
                    Toast.makeText(UserProfile.this, "OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, EditUserProfile.class);
                i.putExtra("name", name);
                i.putExtra("email", email);
                i.putExtra("contact", contact);
               // i.putExtra("address", address);
                startActivity(i);
            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(UserProfile.this,UserChangePassword.class);
                startActivity(i);
            }
        });

        // img.setBackgroundResource(R.drawable.ic_account_circle_blue_24dp);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id=item.getItemId();plug
//        if(id==R.id.home)
//        {
//            Intent i=new Intent(MyProfile.this,UserHome.class);
//            startActivity(i);
//        }
//
//        if(id==R.id.signout)
//        {
//            Intent i=new Intent(MyProfile.this,NewLogin.class);
//            startActivity(i);
//        }
//        return super.onOptionsItemSelected(item);
//    }


    public class myprofile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("userprofile");
            wb.addProperty("userid", strings[0]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("zz", s);

            try {
                JSONArray j = new JSONArray(s);
                JSONObject jo = j.getJSONObject(0);
                name = jo.getString("name");
                email = jo.getString("email");
                contact = jo.getString("contact");
               // address = jo.getString("address");
                //reg = jo.getString("regno");

                name1.setText(name);
                email1.setText(email);
                contact1.setText(contact);
//                address1.setText(address);
//                tvregno.setText(reg);

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Intent i = new Intent(UserProfile.this, UserProfile.class);
//            startActivity(i);

        }
    }


}
