package com.example.sigmaway.homeimage.CustomClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sigmaway.homeimage.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Family on 09-11-2016.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myviewholder> {
    List<AnalysedViewTabInfo> TabInfo=new ArrayList<AnalysedViewTabInfo>() ;
    String TAG="recyclerviewadapter";
    Context context;
    LinearLayout selectedholder;
    GoogleMap mMap;

    public RecycleViewAdapter(List<AnalysedViewTabInfo> info, Context c, GoogleMap map)
    {
        Log.wtf(TAG,"constructor called");
        this.TabInfo=  info;
        this.context=c;
        this.mMap=map;
    }
    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Log.wtf(TAG,"oncreateviewholder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysed_view_singleitem,parent,false);
        myviewholder holder=new myviewholder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(myviewholder holder, int position) {
        Log.wtf(TAG,"onbindviewholder called");
        AnalysedViewTabInfo analysedViewTabInfo= TabInfo.get(position);
        holder.PlaceName.setText(analysedViewTabInfo.PlaceName);
        holder.PlaceImageCount.setText(String.valueOf(analysedViewTabInfo.ImgCount));
        if (position==0)
        {
            holder.linearLayout.setSelected(true);
            selectedholder=holder.linearLayout;
        }
    }


    @Override
    public int getItemCount() {
        return TabInfo.size();
    }

    class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView PlaceName,PlaceImageCount;
        LinearLayout linearLayout;
        public myviewholder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.AnalysedRecyclerViewLinearLayout);
            PlaceName= (TextView) itemView.findViewById(R.id.PlaceName);
            PlaceImageCount= (TextView) itemView.findViewById(R.id.PlaceImageCount);
        }

        @Override
        public void onClick(View v)
        {
            View view=v;
            int position=getAdapterPosition();
            Log.wtf("from analysed tab","position: "+position);
            if(position>=0)
            {
               /* Log.wtf("from analysed tab","view tag "+PlaceName.getTag()+" id "+PlaceName.getId());
                Log.wtf("from analysed tab","view tag "+linearLayout.getTag()+" id "+linearLayout.getId());*/
               // myviewholder temp=new myviewholder(view);
                if (linearLayout.isSelected())
                {

                }
                else
                {

                    selectedholder.setSelected(false);
                  linearLayout.setSelected(true);
                    selectedholder= (LinearLayout) v.findViewById(R.id.AnalysedRecyclerViewLinearLayout);

                        if (TabInfo.size()!=0)
                            mapcall(TabInfo.get(getAdapterPosition()));

                  }
                    /* v.setActivated(true);
            myviewholder holder=new myviewholder(v);
            holder.linearLayout.setActivated(true);*/
            /*PlaceName.setBackgroundColor(Color.BLUE);
            PlaceImageCount.setBackgroundColor(Color.BLUE);*/
                // linearLayout.setBackgroundColor(Color.BLUE);
            }

        }
    }
    public void mapcall(AnalysedViewTabInfo info) {
        if (mMap == null) {
            Log.wtf("map activity", "null");
        }
        else {
            mMap.clear();
            LatLng random=null;
            for (String temp : info.ImgCoordinates) {
                Log.wtf("map activity", temp);
                String[] LatLog = null;
                if (!(temp == null)) {
                    LatLog = temp.split(",");
                    random = new LatLng(Double.parseDouble(LatLog[0]), Double.parseDouble(LatLog[1]));
                    mMap.addMarker(new MarkerOptions().position(random));
                }
            }
            if (!(info.PlaceName.equals("Total")||info.PlaceName.equals("")))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(random));

        }
    }
}
