package com.example.tollpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class UserHome extends AppCompatActivity {

    String name,reg,userid;

    TextView aname,areg;

    CardView maps,wallet,viewqr,profilecard,feedback;

    ImageView myprofile;
    private static final String TAG ="NewToken" ;
    String token="";
    SharedPref sharedPref;
    Button add;
    Dialog popdialog;
    ImageView closebutton;
    String Bamount;
    Intent notifyintent;
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        sharedPref=new SharedPref(this);



        maps=findViewById(R.id.cardusermaps);
        profilecard=findViewById(R.id.profilecard);
        myprofile=findViewById(R.id.ic_myprofile);
        wallet=findViewById(R.id.cardwallet);
        viewqr=findViewById(R.id.cardviewqr);
        feedback=findViewById(R.id.cardfeedback);

        SharedPreferences sh=getSharedPreferences("user",MODE_PRIVATE);
        userid=sh.getString("userid","");
        name=sh.getString("username","");
        reg=sh.getString("userreg","");

        GetWallet gw=new GetWallet();
        gw.execute(userid);

        getSupportActionBar().setTitle("Hi "+name+"!");




        String storedtoken=sh.getString("token","");

        if(!storedtoken.equals(""))
        {
            Log.d(TAG,storedtoken);

        }
        else
        {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                    new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.e(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            token = task.getResult().getToken();

                            Log.d(TAG, token);
                            Toast.makeText(UserHome.this, token, Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor sh1 = getSharedPreferences("user", MODE_PRIVATE).edit();
                            sh1.putString("token", token);
                            sh1.apply();

                            UpdateUserToken up=new UpdateUserToken();
                            up.execute(token,userid);
                        }
                    });
        }


        aname=findViewById(R.id.profilename);
        areg=findViewById(R.id.profileregno);


        profilecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(UserHome.this,UserProfile.class);
                startActivity(i);

            }
        });

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(UserHome.this,UserProfile.class);
                startActivity(i);

            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(UserHome.this,NewMapsActivity.class);
                startActivity(i);

            }
        });



        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sharedPref.loadQrState()==true)
                {
                    Intent i =new Intent(UserHome.this,AddtoWallet.class);
                    startActivity(i);
                }

                Intent i =new Intent(UserHome.this,AddtoWallet.class);
                startActivity(i);

            }
        });
        viewqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(UserHome.this,ViewQRCode.class);
                startActivity(i);

            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(UserHome.this,Feedback.class);
                i.putExtra("id",userid);
                startActivity(i);

            }
        });

        aname.setText(name);
        areg.setText(reg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.reset)
        {

            AlertDialog.Builder builder=new AlertDialog.Builder(UserHome.this);
            builder.setTitle("Log out");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(UserHome.this,Login.class);
                    finishAffinity();
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();



//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(getIntent());
//            overridePendingTransition(0, 0);
        }

        return super.onOptionsItemSelected(item);
    }


    public class UpdateUserToken extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("UpdateUserToken");
            wb.addProperty("token", strings[0]);
            wb.addProperty("userid", strings[1]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("zz",s);
        }
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
            Toast.makeText(UserHome.this, "" + s, Toast.LENGTH_SHORT).show();
            Log.d("utp", s);
            try {
                JSONArray uobj = new JSONArray(s);
                JSONObject juobj = uobj.getJSONObject(0);

                Bamount = juobj.getString("walletamount");
                String status = juobj.getString("walletstatus");

               // balamount.setText("₹  " + juobj.getString("walletamount"));


                if(status.equals("1"))
                {
                    if(Integer.parseInt(Bamount)<100)
                    {
                        sendNotification();
                        popdialog = new Dialog(UserHome.this);
                        rechargepopup();

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification() {
        String NOTIFICATION_CHANNEL_ID="user_notif";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

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




        notifyintent = new Intent(UserHome.this, UserHome.class);
        notifyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);



        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, notifyintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle("Wallet Balance Running Low")
                .setSmallIcon(R.mipmap.ic_launcher_new)
                .setColor(Color.RED)
                .setContentText("Keep the amount above ₹ 100 for proper functioning of wallet");



        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());


    }

    public void rechargepopup() {
        popdialog.setContentView(R.layout.amountlow);

        closebutton = popdialog.findViewById(R.id.clearbutton);
        pay = popdialog.findViewById(R.id.btnpay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(UserHome.this,AddtoWallet.class);
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




}
