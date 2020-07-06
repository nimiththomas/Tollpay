package com.example.tollpay;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;

import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class NewMapsActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryEventListener {

    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentUser;
    private DatabaseReference myLocationRef;
    private GeoFire geoFire;
    private List<LatLng>tollArea;
    String vid[],vname[];
    LatLng latLng;
    private List<TollLocationBean>tollLocationBeanList;
    TollLocationBean tollLocationBean;
    int i,count=0,k;
    String   place;
    GeoQuery geoQuery;
    String latcenter[],loncenter[],userid;

    Intent notifyintent;

    SharedPref sharedPref;

    private String TollNo,Place,Key,Tollid,reg;

    private OnLoadLocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_maps);
        sharedPref=new SharedPref(this);

        SharedPreferences sh=getSharedPreferences("user",MODE_PRIVATE);
        userid=sh.getString("userid","");
        reg=sh.getString("userreg","");

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

                        buildLocationRequest();
                        buildLocationCallback();

                        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(NewMapsActivity.this);



                        GetAllTollCoordinates gt=new GetAllTollCoordinates();
                        gt.execute();
                        settingGeoFire();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(NewMapsActivity.this, "You must enable Permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


    }



    public class GetAllTollCoordinates extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("GetAllTollCoordinates");
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Log.d("utp",s);
            try {
                JSONArray uobj= new JSONArray(s);
                vid=new String[uobj.length()];

                tollArea=new ArrayList<>();
                tollLocationBeanList=new ArrayList<>();
                for(int i=0;i<uobj.length();i++)
                {
                    JSONObject juobj=uobj.getJSONObject(i);

                    vid[i]=juobj.getString("tollid");

                //     tollArea=new ArrayList<>();
                    tollArea.add(new LatLng(Double.valueOf(juobj.getString("latitude")),Double.valueOf(juobj.getString("longitude"))));
                //    Log.d("zz",String.valueOf(tollArea));

                 tollLocationBean=new TollLocationBean();
                tollLocationBean.setId(juobj.getString("tollid"));
                tollLocationBean.setLatitude(juobj.getString("latitude"));
                    tollLocationBean.setLongitude(juobj.getString("longitude"));
                    tollLocationBeanList.add(tollLocationBean);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(NewMapsActivity.this);

            latcenter = new String[tollArea.size()];
            loncenter = new String[tollArea.size()];

        }
    }


    private void settingGeoFire() {
       myLocationRef= FirebaseDatabase.getInstance().getReference("MyLocation");
       geoFire=new GeoFire(myLocationRef);
    }


    private void buildLocationCallback() {
        locationCallback=new LocationCallback()
        {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
              if(mMap!=null)
              {


                  geoFire.setLocation(reg, new GeoLocation(locationResult.getLastLocation().getLatitude(),
                          locationResult.getLastLocation().getLongitude()), new GeoFire.CompletionListener() {
                      @Override
                      public void onComplete(String key, DatabaseError error) {
                          if(currentUser!=null)currentUser.remove();
                          currentUser=mMap.addMarker(new MarkerOptions()
                                  .position(new LatLng(locationResult.getLastLocation().getLatitude(),
                                          locationResult.getLastLocation().getLongitude()))
                                  .title("You"));
                          //After Add Marker Move Camera

                          mMap.animateCamera(CameraUpdateFactory
                                  .newLatLngZoom(currentUser.getPosition(),12.0f));

                      }
                  });

              }
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       mMap.getUiSettings().setZoomControlsEnabled(true);


       if(fusedLocationProviderClient !=null)

           if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
           {
               if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
                   return;;
           }

          fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());




           for(LatLng latLng : tollArea)
           {
                    mMap.addCircle(new CircleOptions().center(latLng)
                       .radius(500)
                       .strokeColor(Color.BLUE)
                       .fillColor(Color.TRANSPARENT)
                       .strokeWidth(5.0f));

               geoQuery=geoFire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),0.5f );
               geoQuery.addGeoQueryEventListener(NewMapsActivity.this);

           }
    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        count=0;
        super.onStop();
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        Key=key;
        Log.d("zz",key);


     //   getTolldetails();


        Geocoder gcd = new Geocoder(getBaseContext(),
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.latitude, location
                    .longitude, 1);
            if (addresses.size() > 0)
               // System.out.println(addresses.get(0).getLocality());
          place=addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }


        UserNotification gt=new UserNotification();
        gt.execute(place,Key);

    }



    @Override
    public void onKeyExited(String key) {
       // sendNotification("TollPay",String.format("left the toll area",key));
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
//        sendNotification("TollPay",String.format("moved in  the dangerous area",key));
    }



    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String tollPay, String format) {
        String NOTIFICATION_CHANNEL_ID="toll_multiple";
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
        Context context;

        if(sharedPref.loadQrState()==true)
        {
             notifyintent=new Intent(NewMapsActivity.this,ViewQRCode.class);
           /* notifyintent.putExtra("tollid",Tollid);
            notifyintent.putExtra("userid",userid);*/
            notifyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }else {

             notifyintent = new Intent(NewMapsActivity.this, SelectVehicletoPay.class);
            notifyintent.putExtra("tollid", Tollid);
            notifyintent.putExtra("userid", userid);
            notifyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }


        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, notifyintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(tollPay)
                .setSmallIcon(R.drawable.ic_action_location)
                .setColor(Color.RED)
                .setContentText(format);



        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());


    }



    public class UserNotification extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();
            wb.setSoapObject("UserNotification");
            wb.addProperty("place",strings[0]);
            wb.addProperty("regno",strings[1]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("zz",s);
            try {
                JSONArray uobj= new JSONArray(s);

                    JSONObject juobj=uobj.getJSONObject(0);
                    TollNo=juobj.getString("boothno");
                    Place=juobj.getString("place");
                    Tollid=juobj.getString("tollid");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendNotification("TollPay",String.format("entered the toll area of "+TollNo+","+Place,Key));
        }
    }
}
