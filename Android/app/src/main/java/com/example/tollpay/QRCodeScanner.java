package com.example.tollpay;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QRCodeScanner extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    String id;
    Dialog popdialog;
    ImageView closebutton;
    Button pay;

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_q_r_code_scanner);

        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_LONG).show();

        id = result.getContents().trim();


        checkscanid ch = new checkscanid();
        ch.execute(id);


    }

    public class updatestatus extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("updatestatus");
            wb.addProperty("paidid", strings[0]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(QRCodeScanner.this, "" + s, Toast.LENGTH_SHORT).show();
        }
    }

    public class checkscanid extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("checkscanid");
            wb.addProperty("paidid", strings[0]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(QRCodeScanner.this, "" + s, Toast.LENGTH_SHORT).show();
            if (s.equals("Allow Entry")) {
                popdialog = new Dialog(QRCodeScanner.this);
                amountpopup();
                updatestatus up = new updatestatus();
                up.execute(id);
            } else if (s.equals("Expired")) {
                popdialog = new Dialog(QRCodeScanner.this);
                rejectpopup();
            } else if (s.equals("Not In the List")) {
                popdialog = new Dialog(QRCodeScanner.this);
                rejectpopup();
            }
        }
    }

    public void amountpopup() {
        popdialog.setContentView(R.layout.allowentry);

        closebutton = popdialog.findViewById(R.id.clearbutton);
        pay = popdialog.findViewById(R.id.btnpay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popdialog.dismiss();
            }
        });

        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popdialog.dismiss();
            }
        });

        popdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popdialog.show();

    }

    public void rejectpopup() {
        popdialog.setContentView(R.layout.expired);

        closebutton = popdialog.findViewById(R.id.clearbutton);
        pay = popdialog.findViewById(R.id.btnpay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popdialog.dismiss();
            }
        });

        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popdialog.dismiss();
            }
        });

        popdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popdialog.show();

    }


}
