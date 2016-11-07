package com.example.sigmaway.homeimage.CustomClasses;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.sigmaway.homeimage.MainActivities.DirectDocument;

import java.io.File;
import java.util.List;

/**
 * Created by Family on 24-10-2016.
 */

public class LocationGetter {

    Context c;
    SharedPreferences sharedPreferences;

    public LocationGetter(Context context) {
        this.c = context;
        Log.wtf("location getter", "constructor called");
    }

    public void Getter(Context c) {

        Log.wtf("location getter", "getter called 0");
        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);

        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.NO_REQUIREMENT);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(false);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
        locationCritera.setSpeedRequired(false);
        List<String> providerName = locationManager.getProviders(locationCritera, true);
        for (String loc:providerName)
        {
            Log.wtf("location getter", "getter called: " + loc);
            if (loc.equals("gps"))
            {
                Log.wtf("location getter",loc+" here");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this.locationListener);
            }
            if (loc.equals("network"))
            {
                Log.wtf("location getter",loc+" here");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.locationListener);
            }
        }




      //locationManager.removeUpdates(this.locationListener);
      //locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    }
    public final LocationListener locationListener = new LocationListener()
    {

        @Override
        public void onLocationChanged(Location location)
        {
            Log.wtf("loacation getter",location.toString());
            sharedPreferences = c.getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
            Uri FileURI = Uri.parse(sharedPreferences.getString("ImgUri","null"));
            Log.w("getter", String.valueOf(FileURI));
            if (!(FileURI.toString().equals("null")))
           {

                LocationAddress loc=new LocationAddress();
                List<String> Address= null;
                try {
                    Address = loc.getAddressFromLocation(location.getLatitude(),location.getLongitude(),c);
                    File file=new File(String.valueOf(FileURI));
                    String imgname= file.getName();
                   DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(c);
                    File f2=new File(file.getParent() +File.separator+ Address.get(1) + "_" + imgname);
                    boolean bool= file.renameTo(f2);
                    Log.wtf("getter new file",  f2.getAbsolutePath()+ bool);
                   int value =dataBaseAdapter.Locationupdatedata(imgname,f2.getName(),Address.get(0),location.getLatitude()+","+location.getLongitude(), String.valueOf(f2.toURI()));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ImgUri", f2.getPath());
                    editor.apply();
                Log.wtf("getter location update", String.valueOf(value));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(c, "longitude:"+location.getLongitude()+"  latitude:"+location.getLatitude()+"\n"+Address,Toast.LENGTH_LONG).show();
                LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
                locationManager.removeUpdates(locationListener);
            }
            else {
                return;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        //    Toast.makeText(c, "on provider disabled",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
           // Toast.makeText(c, "on provider enabled",Toast.LENGTH_LONG).show();
            LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
          //  Toast.makeText(c, "on status change",Toast.LENGTH_LONG).show();
        }
    };
    public int LocationServiceChecker()
    {LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);

        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.NO_REQUIREMENT);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(false);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String providerName = locationManager.getBestProvider(locationCritera,false);
        Log.wtf("location getter","getter called: "+providerName);
        if (providerName.equals("gps") && locationManager.isProviderEnabled(providerName)) {
            // Provider is enabled
            if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                // return;
                Log.wtf("location getter","in checkself");
            }


          return 1;
        }
        else
        {
            Log.wtf("location getter","in else");
            return 0;
            // Provider not enabled, prompt user to enable it
            //Toast.makeText([OUTERCLASS].this, R.string.please_turn_on_gps, Toast.LENGTH_LONG).show();
        }
    }
    public void LocationAlert(final Activity act)
    {
        new AlertDialog.Builder(act)
                .setTitle("Location Service Disabled")
                .setMessage("Please enable location service")
                .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.wtf("location getter","enable clicked");
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        act.startActivityForResult(myIntent,299);


                    }
                })
                .show();
    }
}
