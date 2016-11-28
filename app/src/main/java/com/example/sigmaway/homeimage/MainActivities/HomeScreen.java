package com.example.sigmaway.homeimage.MainActivities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.LocationGetter;
import com.example.sigmaway.homeimage.R;

public class HomeScreen extends AppCompatActivity implements OnClickListener {
    Button HomeNew;
    Button HomePrevious;
    Button HomeImport;
    Button HomeMapView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        setTitle("");
        toolbar= (Toolbar) findViewById(R.id.Home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_tig_mdpi160_40x40);

     /*   toolbar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("home","on clicked "+v.getId()==);
            }
        });*/
    /*    getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        HomeImport= (Button) findViewById(R.id.Home_Import);
        HomeMapView= (Button) findViewById(R.id.home_Map);
        HomeNew= (Button) findViewById(R.id.Home_New);
        HomePrevious= (Button) findViewById(R.id.Home_Previous);
        HomePrevious.setOnClickListener(this);
        HomeNew.setOnClickListener(this);
        HomeImport.setOnClickListener(this);
        HomeMapView.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                    )
            {
                Log.wtf(" homescreen","all Permission granted marshmellow");
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_LOGS,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1 );
            }
        }
        else if (Build.VERSION.SDK_INT < 23)
        {

            if (
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            &&ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    )
            {
                Log.wtf(" homescreen","all Permission granted lollipop");
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_LOGS,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1 );
            }
            }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
      switch (v.getId()){
          case R.id.Home_Import:
              intent=new Intent(HomeScreen.this,ImportImage.class);
                startActivity(intent);
              break;
          case R.id.Home_New:
              intent=new Intent(HomeScreen.this,New_Camera.class);
              startActivity(intent);
              break;
          case R.id.Home_Previous:
              intent=new Intent(HomeScreen.this,DirectDocument.class);
              startActivity(intent);
              break;
          case R.id.home_Map:
              intent=new Intent(HomeScreen.this,AnalysedView.class);
              startActivity(intent);
              break;
      }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==android.R.id.home)
        {
            Log.w("home","opOptionItemclicked");
            // home button from toolbar clicked
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                )
        {
            Toast.makeText(HomeScreen.this,"All Permission Granted", Toast.LENGTH_SHORT).show();
            Log.wtf(" homescreen","all Permission granted");
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_LOGS,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1 );
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
