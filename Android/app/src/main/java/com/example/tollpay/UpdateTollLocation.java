package com.example.tollpay;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateTollLocation extends AppCompatActivity implements View.OnClickListener {

    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;

    private Button btnGetLocation = null;
    private Button btnUpLocation = null;
    private EditText editLocation = null;
    private ProgressBar pb = null;



    TollLocationBean tollLocationBean;

    private static final String TAG = "Debug";
    private Boolean flag = false;

    DatabaseReference reff;
    String longitude,latitude,tollid;
    SharedPreferences sh;
    String cityName=null;

    Double lat,lon;
   private  List<LatLng>TollLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_toll_location);

        TollLocation=new ArrayList<>();

         sh=getSharedPreferences("toll",MODE_PRIVATE);
        tollid=sh.getString("tollid","");

        Toast.makeText(this, tollid, Toast.LENGTH_SHORT).show();


        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        editLocation = (EditText) findViewById(R.id.editTextLocation);

        btnGetLocation = (Button) findViewById(R.id.btnLocation);

        btnUpLocation = (Button) findViewById(R.id.btnUpLocation);

        tollLocationBean=new TollLocationBean();
        reff= FirebaseDatabase.getInstance().getReference("TollLocation");


        btnUpLocation.setVisibility(View.INVISIBLE);

        btnUpLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                tollLocationBean.setLatitude(String.valueOf(lat));
//                tollLocationBean.setLongitude(String.valueOf(lon));
                TollLocation.add(new LatLng(lat,lon));
                reff.child(tollid).setValue(TollLocation);

                UpdateLocation up=new UpdateLocation();
                up.execute(tollid,String.valueOf(lat),String.valueOf(lon),cityName);


            }
        });

        btnGetLocation.setOnClickListener(this);

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
    }


    public class UpdateLocation extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("UpdateLocation");
            wb.addProperty("tollid",strings[0]);
            wb.addProperty("latitude",strings[1]);
            wb.addProperty("longitude",strings[2]);
            wb.addProperty("place",strings[3]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(UpdateTollLocation.this, s, Toast.LENGTH_SHORT).show();

            if(s.equals("Succesfully Updated"))
            {
              //  sh.edit().putBoolean("first_time",false).commit();

                Intent i=new Intent(UpdateTollLocation.this,TollHome.class);
                startActivity(i);


            }
            else
            {
                Toast.makeText(UpdateTollLocation.this, "Updation Failed...Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
//        flag = displayGpsStatus();
//        if (flag) {

            Log.v(TAG, "onClick");

            editLocation.setText("Please!! move your device to" +
                    " see the changes in coordinates." + "\nWait..");

            pb.setVisibility(View.VISIBLE);
            btnUpLocation.setVisibility(View.VISIBLE);
            locationListener = new MyLocationListener();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationMangaer.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10, locationListener);

//        } else {
//            //alertbox("Gps Status!!", "Your GPS is: OFF");
//        }
    }

    /*----Method to Check GPS is enable or disable ----- */
   /* private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    *//*----------Method to create an AlertBox ------------- *//*
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }*/

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {


        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onLocationChanged(Location loc) {
            editLocation.setText("");
            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(getBaseContext(),"Location changed : Lat: " +
                            loc.getLatitude()+ " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            lat=loc.getLatitude();
            lon=loc.getLongitude();
             longitude = "Longitude: " +loc.getLongitude();
            Log.v(TAG, longitude);
             latitude = "Latitude: " +loc.getLatitude();
            Log.v(TAG, latitude);

            /*----------to get City-Name from coordinates ------------- */

            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName=addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = longitude+"\n"+latitude +
                    "\n\nMy Currrent Place is: "+cityName;
            editLocation.setText(s);
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }


}
