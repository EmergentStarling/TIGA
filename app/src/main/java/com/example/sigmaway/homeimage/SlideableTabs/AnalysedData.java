package com.example.sigmaway.homeimage.SlideableTabs;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.R;

import java.io.File;

/**
 * Created by Family on 21-09-2016.
 */

public class AnalysedData extends android.support.v4.app.Fragment {
    WebView AnalysedData;
    File file;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.analysed_data, container, false);
        AnalysedData= (WebView) v.findViewById(R.id.analysed_text);
        AnalysedData.getSettings().setBuiltInZoomControls(true);
        AnalysedData.getSettings().setDisplayZoomControls(true);
       // AnalysedData.setMovementMethod(new ScrollingMovementMethod());
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        file = new File(String.valueOf(ImageUri));
        Log.wtf("analyse frag","on create");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setwebview();
    }

    public void setwebview(){
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        String analyseddata=dataBaseAdapter.GetAnalysedText(file.getName());

        if (/*analyseddata.equals("NULL")||*/ (analyseddata==null))
            //      AnalysedData.setText("YOU FORGOT TO SEND DATA FOR ANALYSIS :p");
            AnalysedData.loadData("YOU FORGOT TO SEND DATA FOR ANALYSIS :p", "text", "utf-8");
        else
            //    AnalysedData.setText(Html.fromHtml(analyseddata));
            AnalysedData.loadData(analyseddata, "text/html" , "utf-8");
    }
}
