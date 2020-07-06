package com.example.tollpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TollChangePassword extends AppCompatActivity implements View.OnClickListener {
    EditText currentpassword,newpassword,retypenewpassword;
    String cp,np,rnp,userid;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tollchange_password);
        getSupportActionBar().setTitle("Change Password");

        currentpassword = findViewById(R.id.etcurrentpassword);
        newpassword = findViewById(R.id.etnewpassword);
        retypenewpassword = findViewById(R.id.etretypenewpassword);
        button = findViewById(R.id.btnsave);
        button.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("toll", MODE_PRIVATE);
        userid = sharedPreferences.getString("tollid", "");

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
//        int id=item.getItemId();
//        if(id==R.id.home)
//        {
//            Intent i=new Intent(AmbulanceChangePassword.this,UserHome.class);
//            startActivity(i);
//        }
//
//        if(id==R.id.signout)
//        {
//            Intent i=new Intent(AmbulanceChangePassword.this,NewLogin.class);
//            startActivity(i);
//        }
//        return super.onOptionsItemSelected(item);
//    }


    public class changepassword extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("tollchangepassword");
            wb.addProperty("userid",strings[0]);
            wb.addProperty("currentpassword",strings[1]);
            wb.addProperty("newpassword",strings[2]);
            wb.addProperty("retypenewpassword",strings[3]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(TollChangePassword.this, s, Toast.LENGTH_SHORT).show();

            Intent i=new Intent(TollChangePassword.this,TollProfile.class);
            startActivity(i);
        }
    }


    @Override
    public void onClick(View v) {
        cp=currentpassword.getText().toString();
        np=newpassword.getText().toString();
        rnp=retypenewpassword.getText().toString();

        changepassword update =new changepassword();
        update.execute(userid,cp,np,rnp);

    }


}
