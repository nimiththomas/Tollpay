package com.example.tollpay;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences mySharedPref;

    public SharedPref(Context context)
    {
        mySharedPref=context.getSharedPreferences("user",Context.MODE_PRIVATE);
    }

    public void directQRModeState(Boolean state)
    {
        SharedPreferences.Editor editor=mySharedPref.edit();
        editor.putBoolean("QRMode",state);
        editor.commit();

    }

    public Boolean loadQrState()
    {
        Boolean state=mySharedPref.getBoolean("QRMode",false);
        return state;
    }

    public void SessionState(Boolean mode)
    {
        SharedPreferences.Editor editor=mySharedPref.edit();
        editor.putBoolean("Session",mode);
        editor.commit();
    }

    public Boolean loadSessionState()
    {
        Boolean mode=mySharedPref.getBoolean("Session",false);
        return mode;
    }



}
