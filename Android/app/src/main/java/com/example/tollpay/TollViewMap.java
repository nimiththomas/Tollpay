package com.example.tollpay;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TollViewMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String tollid, lat, lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_view_map);
        SharedPreferences sh = getSharedPreferences("toll", MODE_PRIVATE);
        tollid = sh.getString("tollid", "");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        GetTollCoordinates gt = new GetTollCoordinates();
        gt.execute(tollid);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public class GetTollCoordinates extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("GetTollCoordinates");
            wb.addProperty("tollid", strings[0]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("utp", s);
            try {
                JSONArray uobj = new JSONArray(s);

                JSONObject juobj = uobj.getJSONObject(0);
                lat = juobj.getString("latitude");
                lon = juobj.getString("longitude");

                onchangedLocation();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void onchangedLocation() {

        Double l1 = Double.parseDouble(lat);
        Double l2 = Double.parseDouble(lon);
        LatLng sydney = new LatLng(l1, l2);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Toll Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//Log.d("hi",latitude);
    }
}
