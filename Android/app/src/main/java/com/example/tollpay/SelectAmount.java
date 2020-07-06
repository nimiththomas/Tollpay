package com.example.tollpay;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectAmount extends AppCompatActivity {

    TextView singleamt, returnamt;
    CardView cardsingle, cardreturn;
    String sin, ret, vname, vreg;
    Dialog popdialog;
    ImageView closebutton, imageView;
    Button yes, no;
    String Amount, userid, tollid, typeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_amount);
        imageView = findViewById(R.id.imageview);
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);
        userid = sh.getString("userid", "");

        singleamt = findViewById(R.id.tvsingle);
        returnamt = findViewById(R.id.tvreturn);
        cardsingle = findViewById(R.id.cardsingle);
        cardreturn = findViewById(R.id.cardreturn);

        tollid = getIntent().getStringExtra("tollid");
        typeid = getIntent().getStringExtra("typeid");
        vname = getIntent().getStringExtra("vname");
        vreg = getIntent().getStringExtra("vreg");
        Toast.makeText(this, "" + tollid, Toast.LENGTH_SHORT).show();

        GetTollAmount gt = new GetTollAmount();
        gt.execute(tollid, typeid);

        cardreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount = ret;
                popdialog = new Dialog(v.getContext());
                confirmpopup();

            }
        });

        cardsingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount = sin;
                popdialog = new Dialog(v.getContext());
                confirmpopup();
            }
        });

    }

    private void confirmpopup() {

        popdialog.setContentView(R.layout.confirmtollpay);

      //  closebutton = popdialog.findViewById(R.id.clearbutton);
        yes = popdialog.findViewById(R.id.btnyes);
        no = popdialog.findViewById(R.id.btnno);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PayAmount p = new PayAmount();
                p.execute(tollid, typeid, userid, vreg, Amount);


            }
        });

       /* closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popdialog.dismiss();
            }
        });*/

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popdialog.dismiss();
            }
        });

        popdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popdialog.show();
    }

    public class PayAmount extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("PayAmount");
            wb.addProperty("tollid", strings[0]);
            wb.addProperty("typeid", strings[1]);
            wb.addProperty("userid", strings[2]);
            wb.addProperty("vreg", strings[3]);
            wb.addProperty("amt", strings[4]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("zz", s);

            Toast.makeText(SelectAmount.this, s, Toast.LENGTH_SHORT).show();

            try {
                JSONArray j = new JSONArray(s);


                JSONObject obj = j.getJSONObject(0);
                String result = obj.getString("result");

                if (result.equals("Success")) {

                    String paidid = obj.getString("paidid");
                    //  String text = vreg; // Whatever you need to encode in the QR code
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    // bitmap = null;
                    OutputStream outputStream = null;
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(paidid, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        storeImage(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

                Intent i = new Intent(SelectAmount.this, UserHome.class);
                startActivity(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
// To be safe, you should check that the SDCard is mounted
// using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new
                File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

// This location works best if you want the created images to be shared
// between applications and persist after your app has been uninstalled.

// Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
// Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "QR_" + timeStamp + ".jpg";
        SharedPreferences.Editor sh = getSharedPreferences("user", MODE_PRIVATE).edit();
        sh.putString("qrcode", mImageName);
        sh.apply();
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public class GetTollAmount extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("GetTollAmount");
            wb.addProperty("tollid", strings[0]);
            wb.addProperty("typeid", strings[1]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("zz", s);

            try {
                JSONArray j = new JSONArray(s);
                JSONObject obj = j.getJSONObject(0);

                sin = obj.getString("single");
                ret = obj.getString("return");

                Log.d("zz", sin);
                Log.d("zz", ret);


                singleamt.setText("₹  " + sin);
                returnamt.setText("₹  " + ret);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
