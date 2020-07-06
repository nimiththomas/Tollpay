package com.example.tollpay;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface OnLoadLocationListener {

    void onLoadLocationSuccess(List<LatLng> latLangs);

    void onLoadLocationFailed(String msg);
}
