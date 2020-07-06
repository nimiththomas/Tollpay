package com.example.tollpay;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QRCodeScannerModified extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    String id, userid;
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

        SharedPreferences sharedPreferences = getSharedPreferences("toll", MODE_PRIVATE);
        userid = sharedPreferences.getString("tollid", "");
        Log.d("zz","tollid:"+userid);

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
        Log.d("zz","userid:"+id);


        modifiedscan ch = new modifiedscan();
        ch.execute(id, userid);


    }


    public class modifiedscan extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("modifiedscan");
            wb.addProperty("userid", strings[0]);
            wb.addProperty("tollid", strings[1]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(QRCodeScannerModified.this, "" + s, Toast.LENGTH_SHORT).show();
            Log.d("zz",s);

            if(s.equals("Success")){
                popdialog = new Dialog(QRCodeScannerModified.this);
                amountpopup();
            }
            else if(s.equals("Failed"))
            {
                popdialog = new Dialog(QRCodeScannerModified.this);
                rejectpopup();
            }




          /*  if (s.equals("Allow Entry")) {
                popdialog = new Dialog(QRCodeScannerModified.this);
                amountpopup();

            } else if (s.equals("Expired")) {
                popdialog = new Dialog(QRCodeScannerModified.this);
                rejectpopup();
            } else if (s.equals("Not In the List")) {
                popdialog = new Dialog(QRCodeScannerModified.this);
                rejectpopup();
            }*/
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

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
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
        popdialog.setContentView(R.layout.invalidqr);

        closebutton = popdialog.findViewById(R.id.clearbutton);
        pay = popdialog.findViewById(R.id.btnpay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popdialog.dismiss();

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
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
