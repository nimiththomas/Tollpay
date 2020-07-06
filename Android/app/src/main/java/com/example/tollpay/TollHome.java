package com.example.tollpay;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class TollHome extends AppCompatActivity  {

    private static final String TAG ="NewToken" ;
    CardView cardmaps,cardupdate,cardrate,cardqrscanner,cardprofile,feedback,test;
    String token="",tollid;
    private GeofencingClient geofencingClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_home);

        getSupportActionBar().setTitle("Home");






        SharedPreferences sh=getSharedPreferences("toll",MODE_PRIVATE);
      String  tollnumber=sh.getString("tollno","");
          tollid=sh.getString("tollid","");
//
//          SendNotification snd=new SendNotification();
//          snd.execute(tollid);

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
                          Toast.makeText(TollHome.this, token, Toast.LENGTH_SHORT).show();

                          SharedPreferences.Editor sh1 = getSharedPreferences("toll", MODE_PRIVATE).edit();
                          sh1.putString("token", token);
                          sh1.apply();

                          UpdateToken up=new UpdateToken();
                          up.execute(token,tollid);
                      }
                  });
      }

        cardmaps=findViewById(R.id.cardmaps);
        cardupdate=findViewById(R.id.cardupdate);
        cardrate=findViewById(R.id.cardaddrate);
        cardqrscanner=findViewById(R.id.cardqrscanner);
        cardprofile=findViewById(R.id.cardprofile);
        feedback=findViewById(R.id.cardfeedback);



        cardmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(TollHome.this,TollViewMap.class);
                startActivity(i);
            }
        });

        cardupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(TollHome.this,NewUpdateTollLocation.class);
                startActivity(i);
            }
        });

        cardrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(TollHome.this,AddTollRates.class);
                startActivity(i);
            }
        });
        cardqrscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(TollHome.this,QRCodeScannerModified.class);
                startActivity(i);
            }
        });

        cardprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(TollHome.this,TollProfile.class);
                startActivity(i);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(TollHome.this,Feedback.class);
                i.putExtra("id",tollid);
                startActivity(i);
            }
        });

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

            AlertDialog.Builder builder=new AlertDialog.Builder(TollHome.this);
            builder.setTitle("Log out");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(TollHome.this,Login.class);
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


    public class UpdateToken extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("UpdateToken");
            wb.addProperty("token", strings[0]);
            wb.addProperty("tollid", strings[1]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("zz",s);
        }
    }

    public class SendNotification extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("SendNotification");
            wb.addProperty("tollid", strings[0]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("zz",s);
        }
    }

   }