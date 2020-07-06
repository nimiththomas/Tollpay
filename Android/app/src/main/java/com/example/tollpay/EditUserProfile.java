package com.example.tollpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditUserProfile extends AppCompatActivity implements View.OnClickListener {

    String name,address,phone,email,userid;
    EditText Name,Address,Phone,Email;
    ImageView img;

    String name1,address1,phone1,email1;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userprofile);
        getSupportActionBar().setTitle("Edit Profile");
        Intent i=getIntent();
        name=i.getStringExtra("name");
        email=i.getStringExtra("email");
        phone=i.getStringExtra("contact");
       // address=i.getStringExtra("address");
        Name = findViewById(R.id.etname);
        Email = findViewById(R.id.etemail);
        Phone = findViewById(R.id.etcontact);
        //Address = findViewById(R.id.etaddress);
        button = findViewById(R.id.btnsave);

        //img=findViewById(R.id.imageView);
       // img.setBackgroundResource(R.drawable.ic_account_circle_blue_24dp);

        button.setOnClickListener(this);

        Name.setText(name);
        Email.setText(email);
        Phone.setText(phone);
       // Address.setText(address);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id=item.getItemId();
//        if(id==R.id.home)
//        {
//            Intent i=new Intent(EditAmbulanceProfile.this,UserHome.class);
//            startActivity(i);
//        }
//
//        if(id==R.id.signout)
//        {
//            Intent i=new Intent(EditAmbulanceProfile.this,NewLogin.class);
//            startActivity(i);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public  class updateprofile extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wb = new WebServiceCaller();
            wb.setSoapObject("updateuserprofile");
            wb.addProperty("userid",strings[0]);
            wb.addProperty("name",strings[1]);
            wb.addProperty("email",strings[2]);
            wb.addProperty("phone",strings[3]);
           // wb.addProperty("address",strings[4]);
            wb.callWebService();
            return wb.getResponse();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(EditUserProfile.this, s, Toast.LENGTH_SHORT).show();

            Intent i=new Intent(EditUserProfile.this,UserProfile.class);
            startActivity(i);

        }
    }

    @Override
    public void onClick(View v) {

        name1=Name.getText().toString();
        email1=Email.getText().toString();
        phone1=Phone.getText().toString();
        //address1=Address.getText().toString();

        updateprofile update =new updateprofile();
        update.execute(userid,name1,email1,phone1);

    }
}
