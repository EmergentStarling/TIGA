package com.example.sigmaway.homeimage.SlideableTabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by Family on 21-09-2016.
 */

public class AnalysedData extends android.support.v4.app.Fragment {
    WebView AnalysedData;
    File file;
    Button DetailedResult;
    TableLayout AnalysedDataTable;
    NestedScrollView scrollView;
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

        AnalysedDataTable = (TableLayout) v.findViewById(R.id.analysed_table);
        scrollView= (NestedScrollView) v.findViewById(R.id.analysed_data_scrollview);
        //   WebSettings webSettings = AnalysedData.getSettings();
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
        AnalysedData.getSettings().setUseWideViewPort(false);
        AnalysedData.getSettings().setLoadWithOverviewMode(false);
        scrollView.setVisibility(View.GONE);
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        List<String> fullanalyseddata=dataBaseAdapter.GetAnalysedText(file.getName());
        String analyseddata=fullanalyseddata.get(0);
        Log.wtf("setwebview","called");
        String directory= "file://"+Environment.getExternalStorageDirectory().getAbsolutePath()+"/Sigmaway/Analysed/"+file.getName();

        String img="<html>\n" +
                "<body>\n" +
                "<img src=\""+directory+"\" alt=\"Analysis Image\" style=\"width:350px;height:350px;\">\n" +
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
        List<String> fullanalyseddata=dataBaseAdapter.GetAnalysedText(file.getName());
        String analyseddata=fullanalyseddata.get(0);
        String GoogleAnalysisData=fullanalyseddata.get(1);
        Log.w("from analysed data ",GoogleAnalysisData);
        if (GoogleAnalysisData!=null)
        {
            try {
                scrollView.setVisibility(View.VISIBLE);
                Context c =getActivity().getApplicationContext();
                AnalysedDataTable.removeAllViews();
                LayoutInflater inflater =getActivity().getLayoutInflater();
                LinearLayout row = (LinearLayout) inflater.inflate(R.layout.analysed_data_table_row, AnalysedDataTable, false);
                TextView name_tv = (TextView) row.findViewById(R.id.name);
                TextView type_tv= (TextView) row.findViewById(R.id.type);
                TextView wiki_url_tv= (TextView) row.findViewById(R.id.wiki_url);
                name_tv.setText("Name");
                name_tv.setTextSize(20);
                type_tv.setText("Category");
                type_tv.setTextSize(20);
                wiki_url_tv.setText("Wiki URL");
                wiki_url_tv.setTextSize(20);
                AnalysedDataTable.addView(row,0);

                JSONObject responseobj = new JSONObject(GoogleAnalysisData);
                JSONArray entitiesobj= responseobj.getJSONArray("entities");
                Log.w("from analysed data ","google analysed data array size"+entitiesobj.length());
                for (int i=0; i< entitiesobj.length();i++)
                {
                    JSONObject temp=entitiesobj.getJSONObject(i);
                    String name=temp.getString("name");
                    String type=temp.getString("type");
                    String wikipedia_url ="";
                    if (temp.getJSONObject("metadata").has("wikipedia_url"))
                    {
                        wikipedia_url=temp.getJSONObject("metadata").getString("wikipedia_url");
                    }
                    row = (LinearLayout) inflater.inflate(R.layout.analysed_data_table_row, AnalysedDataTable, false);
                    name_tv = (TextView) row.findViewById(R.id.name);
                    type_tv= (TextView) row.findViewById(R.id.type);
                    wiki_url_tv= (TextView) row.findViewById(R.id.wiki_url);
                    name_tv.setText(name);
                    type_tv.setText(type);
                    wiki_url_tv.setText(wikipedia_url);
                    AnalysedDataTable.addView(row,i+1);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //   JSONObject trasnlatetext= responseobj.getJSONObject("translateText");

       // String GoogleAnalysedApiData= dataBaseAdapter.get
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
