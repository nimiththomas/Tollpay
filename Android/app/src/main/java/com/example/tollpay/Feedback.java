package com.example.tollpay;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Feedback extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {
    EditText fed;
    Button submit;
    String Fed,usid,s;
    RatingBar ratingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent i=getIntent();
       usid=i.getStringExtra("id");


        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        ratingbar.setOnRatingBarChangeListener(this);
//        SharedPreferences sh=getSharedPreferences("user",MODE_PRIVATE);
//        usid=sh.getString("userid","");

        fed=findViewById(R.id.et_feedback);
        submit=findViewById(R.id.btn_submit);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");
        submit.setOnClickListener(this);

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        s = String.valueOf(rating);

    }

    protected class feedback extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb= new WebServiceCaller();
            wb.setSoapObject("feedback");
            wb.addProperty("userid",strings[0]);
            wb.addProperty("ufeedback",strings[1]);
            wb.addProperty("rating",strings[2]);
            wb.callWebService();

            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("fk",s);
            Toast.makeText(Feedback.this, s, Toast.LENGTH_SHORT).show();
            Intent i= new Intent(Feedback.this,Feedback.class);
            startActivity(i);
        }
    }
    @Override
    public void onClick(View v) {

        Fed=fed.getText().toString();

        feedback fk=new feedback();
        fk.execute(usid,Fed,s);

    }
}
