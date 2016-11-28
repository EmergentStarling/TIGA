package com.example.sigmaway.homeimage.SlideableTabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.ImageInfo;
import com.example.sigmaway.homeimage.R;
import com.example.sigmaway.homeimage.volley.VolleySendData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


                translate trans=new translate();
                trans.execute();
         //       Toast.makeText(getActivity().getApplicationContext(),result,Toast.LENGTH_LONG);

            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        Translatedtext= (TextView) getActivity().findViewById(R.id.translated_text);
       Translatedtext.setMovementMethod(new ScrollingMovementMethod());
        Log.wtf("translate frag","on resume");

    }

    public void settextview(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        final File file = new File(String.valueOf(ImageUri));
        String lang=sharedPref.getString("lang","en");
        Log.wtf("translated text",lang);
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        String value= dataBaseAdapter.GetTranslatedText(file.getName(),lang);
        Translatedtext.setText(value);
    }
   private class translate extends AsyncTask<Void,Void,Integer>
    {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            Log.i("ocrtext translation", "onpreexecute");
            progress = new ProgressDialog(getActivity());
            progress.setMessage("processing");
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            super.onPreExecute();

        }
        @Override
        protected Integer doInBackground(Void... params) {
            DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
            String engtext= dataBaseAdapter.GetTranslatedText(file.getName(),"en");
            Log.wtf("translated text frag",engtext.toString());
            if (engtext.equals("NULL"))
            {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                Log.wtf("volley","1");
                String YOUR_API_KEY="AIzaSyCDO95TXRHa2RtHr1u_LBJaQSER9XFEKtU";
                String sourcetxt=value.get(0).replace(" ","%20");
                String sourcetext=sourcetxt.replace("\n","%0A");
                String url = "https://www.googleapis.com/language/translate/v2?key="+YOUR_API_KEY+"&q="+sourcetext+"&source=ar&target=en";
                // String url=" https://www.googleapis.com/language/translate/v2?key="+YOUR_API_KEY+"&source=en&target=de&callback=translateText&q="+source;
                //String url= "http://gosigmaway.com:8085/RAWS/process/armyService";

                StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("response array", response);
                        try {
                            JSONObject responseobj=new JSONObject(response);
                            //   JSONObject trasnlatetext= responseobj.getJSONObject("translateText");
                            JSONObject dataobj=responseobj.getJSONObject("data");
                            JSONArray translationsobj= dataobj.getJSONArray("translations");
                            JSONObject translatedText=translationsobj.getJSONObject(0);
                            String transtext= translatedText.getString("translatedText");
                            DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());

                            long id=dataBaseAdapter.updatedata(file.getName(),"engtransdata",transtext);

                            Log.wtf("key if id", String.valueOf(id));
                            progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("result", " hi fail listning" + error);

                /*    NetworkResponse response = error.networkResponse;
                    String res = new String(response.data);
                    // Now you can use any deserializer to make sense of data
                    Log.wtf(" response", res);*/
                        progress.dismiss();

                    }
                })
                        //header for rapidminer
       /* {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.wtf("volley ", "header");
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s","gouravchawla","30march93");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }}*/
                        ;
                requestQueue.add(stringRequest);
            }

            Integer i=1;
            return i;
        }

        @Override
        protected void onPostExecute(Integer s) {
            ImageInfo info=new ImageInfo();
            DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
            String engtext= dataBaseAdapter.GetTranslatedText(file.getName(),"en");
            info.ImageName=file.getName();
            info.EngText=engtext;
            info.Keyword=value.get(1);
            info.UserID="8800304343";
            VolleySendData obj=new VolleySendData();
            progress.dismiss();
            String result= obj.VolleySend(getActivity(),info);
            Log.wtf("translated text",result);

            super.onPostExecute(s);

            Log.i("ocrtext translation", "onpostexecute");

        }
    }
}
