package com.example.sigmaway.homeimage.SlideableTabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.ImageInfo;
import com.example.sigmaway.homeimage.R;
import com.example.sigmaway.homeimage.volley.VolleySendData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TranslatedTextFrag extends Fragment   {
    TextView Translatedtext;
    Button Analysis;
    File file;
    List<String> value;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.translated_text_frag, container, false);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        file = new File(String.valueOf(ImageUri));
        Log.wtf("translate frag","on create");
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        value = new ArrayList<String>();
        value= dataBaseAdapter.getdata(file.getName());

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Analysis= (Button)getActivity().findViewById(R.id.sendforanalysis);

        Analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageInfo info=new ImageInfo();
                info.ImageName=file.getName();
                info.EngText=value.get(0);
                info.Keyword=value.get(1);
                info.UserID="8800304343";
                VolleySendData obj=new VolleySendData();
                String result= obj.VolleySend(getActivity(),info);
                Toast.makeText(getActivity().getApplicationContext(),result,Toast.LENGTH_LONG);

            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        Translatedtext= (TextView) getActivity().findViewById(R.id.translated_text);
       Translatedtext.setMovementMethod(new ScrollingMovementMethod());
        Log.wtf("translate frag","on resume");
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        final File file = new File(String.valueOf(ImageUri));
        String lang=sharedPref.getString("lang","fa");
        Log.wtf("translated text",lang);
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        String value= dataBaseAdapter.GetTranslatedText(file.getName(),lang);
        Translatedtext.setText(value);
    }

}
