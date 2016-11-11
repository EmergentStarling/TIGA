package com.example.sigmaway.homeimage.MainActivities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
/*

        // Add a marker in Sydney and move the camera
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getApplicationContext());
        List<String> Coordinates=dataBaseAdapter.GetCoordinates();
        for (String temp:Coordinates)
        {
            Log.wtf("map activity",temp);
            String[] LatLog=null;
            if (!(temp ==null)) {
                LatLog = temp.split(",");
                LatLng random = new LatLng(Double.parseDouble(LatLog[0]), Double.parseDouble(LatLog[1]));
                mMap.addMarker(new MarkerOptions().position(random));
            }
        }
*/

       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
