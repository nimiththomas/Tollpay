package com.example.tollpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.craftman.cardform.CardForm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CardPayment extends AppCompatActivity  implements View.OnClickListener
{
    String userid,amount,Bamount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);

        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sh=getSharedPreferences("user",MODE_PRIVATE);
        userid=sh.getString("userid","");

        Intent i=getIntent();
        amount=i.getStringExtra("amount");
        Bamount=i.getStringExtra("Bamount");
       // id=i.getStringExtra("id");



        TextView prevname = findViewById(R.id.card_preview_name);
        prevname.setHint("Name on Card");
        TextView name=findViewById(R.id.card_name);
        name.setHint("Name on Card");

        TextView amt=findViewById(R.id.payment_amount);
        amt.setText("₹ "+amount);

        Button btnpay=findViewById(R.id.btn_pay);
        btnpay.setText("Pay ₹"+"  " +amount);

        btnpay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int AddedAmount=Integer.parseInt(amount)+Integer.parseInt(Bamount);

        AddWallet gb= new AddWallet();
        gb.execute(userid,String.valueOf(AddedAmount));
    }

    protected class AddWallet extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb=new WebServiceCaller();

            wb.setSoapObject("AddWallet");
            wb.addProperty("userid",strings[0]);
            wb.addProperty("amount",strings[1]);
            wb.callWebService();
            return wb.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(CardPayment.this, ""+s, Toast.LENGTH_SHORT).show();

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            // bitmap = null;
            OutputStream outputStream = null;
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(userid, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                storeImage(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }

            Intent i=new Intent(CardPayment.this,AddtoWallet.class);
            startActivity(i);
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

}
