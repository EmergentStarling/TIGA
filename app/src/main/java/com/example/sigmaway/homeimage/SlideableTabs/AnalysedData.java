package com.example.sigmaway.homeimage.SlideableTabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.R;

import java.io.File;

/**
 * Created by Family on 21-09-2016.
 */

public class AnalysedData extends android.support.v4.app.Fragment {
    WebView AnalysedData;
    File file;
    Button DetailedResult;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.analysed_data, container, false);
        AnalysedData= (WebView) v.findViewById(R.id.analysed_text);
        DetailedResult= (Button) v.findViewById(R.id.analysed_Details);
        DetailedResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDetailedResult();

            }
        });
   //     WebSettings webSettings = AnalysedData.getSettings();
        AnalysedData.getSettings().setJavaScriptEnabled(true);
        AnalysedData.getSettings().setDomStorageEnabled(true);
        AnalysedData.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        // Force links and redirects to open in the WebView instead of in a browser
     //   AnalysedData.setWebViewClient(new WebViewClient());
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
        Log.wtf("analysed data","called");
        setwebview();
    }

    public void setwebview()
    {
       /* AnalysedData.getSettings().setJavaScriptEnabled(true);
        AnalysedData.getSettings().setDomStorageEnabled(true);
        AnalysedData.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);*/
        AnalysedData.clearCache(true);
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        String analyseddata=dataBaseAdapter.GetAnalysedText(file.getName());
        Log.wtf("setwebview","called");
        String directory= "file://"+Environment.getExternalStorageDirectory().getAbsolutePath()+"/Sigmaway/Analysed/"+file.getName();

        String img="<html>\n" +
                "<body>\n" +
                "<img src=\""+directory+"\" alt=\"Mountain View\" style=\"width:350px;height:350px;\">\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        Log.w("directory from webview",new File(directory).getPath());
        if (/*analyseddata.equals("NULL")||*/ (analyseddata==null))
            //      AnalysedData.setText("YOU FORGOT TO SEND DATA FOR ANALYSIS :p");
            AnalysedData.loadData("<HTML><body><P>YOU FORGOT TO SEND DATA FOR ANALYSIS or CLICK BUTTON BELOW </P></BODY></hTML>", "text", null);
        else
            //    AnalysedData.setText(Html.fromHtml(analyseddata));

            //AnalysedData.loadData(img, "text/html" , "utf-8");
        AnalysedData.loadDataWithBaseURL("", img, "text/html","utf-8", "");
        AnalysedData.invalidate();
    }
    public void setDetailedResult()
    {
        AnalysedData.clearCache(true);

        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        String analyseddata=dataBaseAdapter.GetAnalysedText(file.getName());
        Log.wtf("setwebview","called");
        String directory= "file://"+Environment.getExternalStorageDirectory().getAbsolutePath()+"/Sigmaway/Analysed/"+file.getName();

        String img="<html>\n" +
                "<body>\n" +
                "<img src=\""+directory+"\" alt=\"Analysis Image\" style=\"width:304px;height:228px;\">\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        Log.w("directory from webview",new File(directory).getPath());
        if (/*analyseddata.equals("NULL")||*/ (analyseddata==null))
            //      AnalysedData.setText("YOU FORGOT TO SEND DATA FOR ANALYSIS :p");
            AnalysedData.loadData("<HTML><body><P>YOU FORGOT TO SEND DATA FOR ANALYSIS or CLICK BUTTON BELOW </P></BODY></hTML>", "text", null);
        else
            //    AnalysedData.setText(Html.fromHtml(analyseddata));

            //AnalysedData.loadData(img, "text/html" , "utf-8");
            AnalysedData.loadDataWithBaseURL("", img+"\n"+analyseddata, "text/html","utf-8", "");
        AnalysedData.invalidate();
    }
}
