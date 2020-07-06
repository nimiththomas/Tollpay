package com.example.tollpay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class OrderPlaced extends AppCompatActivity {

    private  static  int splash_time=4000;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);


        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(OrderPlaced.this,AddtoWallet.class);
                startActivity(i);
                finish();
            }
        },splash_time);
    }
}
