package com.example.tollpay;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ViewQRCode extends AppCompatActivity {
    ImageView qrcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_q_r_code);

        qrcode = findViewById(R.id.imgqr);

        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);
        String qr = sh.getString("qrcode", "");

        if(qr.equals(""))
        {
            Toast.makeText(this, "No Qr available", Toast.LENGTH_SHORT).show();
        }


        File imgFile = new File("/" + Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName() + "/" +
                "Files/" + qr);


        Log.d("zz", String.valueOf(imgFile));
        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


            qrcode.setImageBitmap(myBitmap);

        }
        ;


    }
}
