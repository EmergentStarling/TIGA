package com.example.sigmaway.homeimage.MainActivities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.CaptureImage;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.LocationGetter;
import com.example.sigmaway.homeimage.R;

import java.io.File;

public class New_Camera extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    Uri fileUri;
    LocationGetter obj;
    DataBaseAdapter dataBaseAdapter;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_camera);
        sharedPref = getApplication().getSharedPreferences("shrdpref", MODE_PRIVATE);
        obj= new LocationGetter(getApplicationContext());
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            & checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    )
            {

                Activity act=New_Camera.this;
                int temp= obj.LocationServiceChecker();
                Log.w("temp from location", String.valueOf(temp));
                if (temp==1)
                    captureImage();
                else if(temp==0)
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Location Service Disabled")
                            .setMessage("Please enable location service")
                            .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.wtf("location getter","enable clicked");
                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    // myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivityForResult(myIntent,299);
                                }
                            })
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                                        location();
                                        dialog.dismiss();
                                    }
                                    return false;
                                }
                            })

                            .show();
                }
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_LOGS,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1 );
            }
        }
        else if (Build.VERSION.SDK_INT < 23)
        {

            Activity act=New_Camera.this;
            int temp= obj.LocationServiceChecker();
            Log.w("temp from location", String.valueOf(temp));
            if (temp==1)
                captureImage();
            else if(temp==0)
            {
                new AlertDialog.Builder(this)
                        .setTitle("Location Service Disabled")
                        .setMessage("Please enable location service")
                        .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.wtf("location getter","enable clicked");
                                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                // myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(myIntent,299);
                            }
                        })
                        .setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    location();
                                    dialog.dismiss();
                                }
                                return false;
                            }
                        })
                        .show();
            }
        }
        //  captureImage();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        Log.wtf("New camera", String.valueOf(requestCode));
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.wtf("waiting for", "gridview invalidate");
                File file = new File(String.valueOf(fileUri));
                dataBaseAdapter = new DataBaseAdapter(New_Camera.this);
                long id = dataBaseAdapter.insertdata(file.getName(), String.valueOf(fileUri), "NULL", "NULL", "NULL", "NULL", "YOU FORGOT TO SEND DATA FOR ANALYSIS or CLICK BUTTON BELOW");
                if (id == 0) {
                    Toast.makeText(getApplicationContext(), "Click Again", Toast.LENGTH_SHORT).show();
                    captureImage();
                } else if (id >= 1) {
                    Toast.makeText(getApplicationContext(), "Image Captured", Toast.LENGTH_SHORT).show();

                    Intent NewPreview = new Intent(New_Camera.this, New_Preview.class);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("ImgUri", fileUri.getPath());
                    editor.apply();
//                    NewPreview.putExtra("fileuri",fileUri.toString());
                    Log.wtf("putting extra in new preview", fileUri.getPath());
                    startActivity(NewPreview);
                }
                // successfully captured the image
                // display it in image view
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), "User cancelled image capture ", Toast.LENGTH_SHORT).show();
                Intent Previous = new Intent(New_Camera.this, HomeScreen.class);
                startActivity(Previous);
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 299) {
            location();
        }
    }
    private void captureImage()
    {   SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("ImgUri");
        editor.apply();
        obj.Getter(getApplicationContext());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = new CaptureImage().getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED)
        {

            Activity act=New_Camera.this;
            int temp= obj.LocationServiceChecker();
            Log.w("temp from location", String.valueOf(temp));
            if (temp==1)
                captureImage();
            else if(temp==0)
            {
                new AlertDialog.Builder(this)
                        .setTitle("Location Service Disabled")
                        .setMessage("Please enable location service")
                        .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.wtf("location getter","enable clicked");
                                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                // myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(myIntent,299);
                            }
                        })
                        .setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    location();
                                    dialog.dismiss();
                                }
                                return false;
                            }
                        })
                        .show();
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_LOGS},1 );
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(obj.locationListener);
    }
    public void location()
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(false);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String providerName = locationManager.getBestProvider(locationCritera, false);
        Log.wtf("new camera",providerName);
        if (providerName.equals("gps") && locationManager.isProviderEnabled(providerName)) {
            captureImage();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Turn On Location For Better Analysis",Toast.LENGTH_LONG).show();
            captureImage();
        }
    }
}
