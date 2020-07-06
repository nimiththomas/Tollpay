package com.example.tollpay;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class AddtoWallet extends AppCompatActivity {
    TextView balamount;
    Button add;
    Dialog popdialog;
    ImageView closebutton;
    String Bamount;
    Intent notifyintent;
    Button pay;
    EditText payamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_wallet);

        getSupportActionBar().setTitle("My Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);
        String userid = sh.getString("userid", "");

        balamount = findViewById(R.id.balamount);
        add = findViewById(R.id.btnaddmoney);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popdialog = new Dialog(v.getContext());

                amountpopup();
            }
        });
        GetWallet g = new GetWallet();
        g.execute(userid);
    }


    public void amountpopup() {
        popdialog.setContentView(R.layout.payamount);

        closebutton = popdialog.findViewById(R.id.clearbutton);
        pay = popdialog.findViewById(R.id.btnpay);
        payamount = popdialog.findViewById(R.id.etamount);
        payamount.setHint("₹  0");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Amount = payamount.getText().toString().trim();

                Intent i = new Intent(AddtoWallet.this, CardPayment.class);
                i.putExtra("Bamount", Bamount);
                i.putExtra("amount", Amount);
                startActivity(i);


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
        popdialog.setContentView(R.layout.walletlocked);

        closebutton = popdialog.findViewById(R.id.clearbutton);
        pay = popdialog.findViewById(R.id.btnpay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountpopup();
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

    public void rechargepopup() {
        popdialog.setContentView(R.layout.amountlow);

        closebutton = popdialog.findViewById(R.id.clearbutton);
        pay = popdialog.findViewById(R.id.btnpay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountpopup();
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


    protected class GetWallet extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();

            wb.setSoapObject("GetWallet");
            wb.addProperty("userid", strings[0]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(AddtoWallet.this, "" + s, Toast.LENGTH_SHORT).show();
            Log.d("utp", s);
            try {
                JSONArray uobj = new JSONArray(s);
                JSONObject juobj = uobj.getJSONObject(0);

                Bamount = juobj.getString("walletamount");
                String status = juobj.getString("walletstatus");

                balamount.setText("₹  " + juobj.getString("walletamount"));

                if (status.equals("0")) {
                    popdialog = new Dialog(AddtoWallet.this);

                    rejectpopup();
                    // Toast.makeText(AddtoWallet.this, "Wallet is locked", Toast.LENGTH_SHORT).show();
                }
                if (status.equals("1")) {
                    if (Integer.parseInt(Bamount) < 100) {

                        popdialog = new Dialog(AddtoWallet.this);

                        rechargepopup();

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification() {
        String NOTIFICATION_CHANNEL_ID = "user_notif";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.

            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notifyintent = new Intent(AddtoWallet.this, AddtoWallet.class);
        notifyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle("Wallet Balance Running Low")
                .setSmallIcon(R.mipmap.ic_launcher_new)
                .setColor(Color.RED)
                .setContentText("Keep the amount above ₹ 100 for proper functioning of wallet");


        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());


    }


}
