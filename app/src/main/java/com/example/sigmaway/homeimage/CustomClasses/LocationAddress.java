package com.example.sigmaway.homeimage.CustomClasses;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationAddress {
    private static final String TAG = "LocationAddress";
    List<String> result ;
    public List<String> getAddressFromLocation(final double latitude, final double longitude,final Context context) throws InterruptedException {
        result =null;
        result=new ArrayList<String>();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sb1 = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                       sb1.append(address.getLocality()).append(",");
                        sb1.append(address.getCountryName());
                        sb.append(address.getCountryName());
                        result.add(0,sb.toString());
                        result.add(1,sb1.toString());
                        Log.e(TAG, "result: "+result);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                }
            }
        };
        thread.start();
        thread.join();
        Log.e(TAG, "result after: "+result);
        return result;
    }

}
