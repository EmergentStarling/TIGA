package com.example.sigmaway.homeimage.MainActivities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.sigmaway.homeimage.CustomClasses.AnalysedViewTabInfo;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.RecycleViewAdapter;
import com.example.sigmaway.homeimage.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class AnalysedView extends NavigationBarActivity implements OnMapReadyCallback {
    RecyclerView recyclerView;
    RecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String[] PlaceName;
    int[] PlaceImageCount;
    private GoogleMap mMap;
    List<AnalysedViewTabInfo> DistinctLoc;
    SupportMapFragment mapFragment;
    ArrayList<AnalysedViewTabInfo> analysedViewTabInfo;
    List<AnalysedViewTabInfo> Final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);
        super.onCreateDrawer(savedInstanceState);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View yourListView = findViewById(R.id.content_navigation_bar);
        ViewGroup parent = (ViewGroup) yourListView.getParent();
        parent.removeView(yourListView);
        View view = getLayoutInflater().inflate(R.layout.analysed_view, parent, false);
        parent.addView(view);
        setTitle("");
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(getApplicationContext());
        DistinctLoc = new ArrayList<AnalysedViewTabInfo>();
        DistinctLoc = dataBaseAdapter.GetDistinctLoc();
        Final=new ArrayList<>();
        Log.w("analysedview ", "Distinctloc upper" + DistinctLoc.size());
        List<AnalysedViewTabInfo> Coordinates = new ArrayList<AnalysedViewTabInfo>();
        Coordinates = dataBaseAdapter.GetCoordinates();
        Log.w("analysedview primaryloop", "in else");
        AnalysedViewTabInfo temp1 = new AnalysedViewTabInfo();
        // temp1.ImgCoordinates=new ArrayList<>();
        try {
            for (AnalysedViewTabInfo temp : Coordinates) {
                temp1.ImgCoordinates.add(temp.imgCoordinates);
                temp1.ImgCount++;
            }
            temp1.PlaceName = "Total";
            Final.add(temp1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < DistinctLoc.size(); i++) {
            AnalysedViewTabInfo info = DistinctLoc.get(i);
            Log.w("analysedview mainloop out", String.valueOf(i));
            if (info.PlaceName!=null) {
                for (int j = 0; j < Coordinates.size(); j++) {
                    Log.w("analysedview mainloop", String.valueOf(j));
                    try {
                        if (!(Coordinates.get(j).PlaceName == null)) {
                            if (Coordinates.get(j).PlaceName.equals(info.PlaceName)) {
                                info.ImgCount++;

                                info.ImgCoordinates.add(Coordinates.get(j).imgCoordinates);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Log.w("analysedview mainloop out add", String.valueOf(i));
           /* if (info.PlaceName.equals(""))
                info.PlaceName="No Name";*/
                Final.add(info);
                Log.w("analysedview ", "Distinctloc " + Final.size());
            }
        }
        analysedViewTabInfo = new ArrayList<AnalysedViewTabInfo>();
        PlaceName = new String[]{"all", "delhi", "chandigarh"};
        PlaceImageCount = new int[]{5, 2, 3};
        for (int i = 0; i < PlaceName.length; i++) {
            String name = PlaceName[i];
            int count = PlaceImageCount[i];
            AnalysedViewTabInfo info = new AnalysedViewTabInfo();
            info.PlaceName = name;
            info.ImgCount = count;
            analysedViewTabInfo.add(info);
        }
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.analysedviewframe);
        recyclerView = (RecyclerView) findViewById(R.id.AnalysedRecyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.AnalysedViewMap);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap == null) {
            Log.wtf("map activity", "null");
        } else if (Final.size()>0){
            mMap.clear();
            AnalysedViewTabInfo info=Final.get(0);
            LatLng random = null;
            for (String temp : info.ImgCoordinates) {
                Log.wtf("map activity", temp);
                String[] LatLog = null;
                if (!(temp == null)) {
                    LatLog = temp.split(",");
                    random = new LatLng(Double.parseDouble(LatLog[0]), Double.parseDouble(LatLog[1]));
                    mMap.addMarker(new MarkerOptions().position(random));
                }

            }
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(random));
            adapter = new RecycleViewAdapter(Final, this,mMap);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }

/*        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getApplicationContext());
        List<AnalysedViewTabInfo> Coordinates=dataBaseAdapter.GetCoordinates();

        for (String temp:Coordinates)
        {
            Log.wtf("map activity",temp);
            String[] LatLog=null;
            if (!(temp ==null)) {
                LatLog = temp.split(",");
                LatLng random = new LatLng(Double.parseDouble(LatLog[0]), Double.parseDouble(LatLog[1]));
                mMap.addMarker(new MarkerOptions().position(random));
            }
        }*/
    }


}