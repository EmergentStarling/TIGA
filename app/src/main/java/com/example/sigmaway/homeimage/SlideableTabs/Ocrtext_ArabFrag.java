package com.example.sigmaway.homeimage.SlideableTabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ocrtext_ArabFrag extends Fragment {
    String ImageName;

    TextView OcrText;
    Button TranslatedBtn;
    TextView ImageKey;
    String target="fa";
    List<String> value;
    Spinner LanguageSelector;
    File file;
    int pos=0;
    String[] LangList={"Farsi","English"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.wtf("ocr text fragment", "here");
        View v = inflater.inflate(R.layout.ocr_text_frag, container, false);
        final SharedPreferences sharedPref = getActivity().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        file = new File(String.valueOf(ImageUri));
        TranslatedBtn= (Button) v.findViewById(R.id.TranslateBtn);
        ImageName = file.getName().substring(0, file.getName().lastIndexOf('.'));
        ImageKey = (TextView) v.findViewById(R.id.Frag_ImgKey_TextView);
        OcrText = (TextView) v.findViewById(R.id.Frag_OcrText_TextView);
        LanguageSelector = (Spinner) v.findViewById(R.id.Translate_lang_selector);
        ArrayAdapter Spinner_Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,LangList );
        LanguageSelector.setAdapter(Spinner_Adapter);
        LanguageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              pos=position;
                if (position==0)
                target="fa";
                else if (position==1)
                    target="en";
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lang",target);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TranslatedBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                translate trans=new translate();
                trans.execute();
            }
        });
        Log.wtf("ocr text fragment", "here 1");
        /*parentfilepath = String.valueOf(file.getParentFile());
        File OCRFile = new File(parentfilepath + File.separator + ImageName + ".txt");
        File Key = new File(parentfilepath + File.separator + ImageName + ".key");*/
       /* String text = null;
        String key = null;*/
       /* try {
            text = read_file(getActivity(), OCRFile.getPath());
            key = read_file(getActivity(), Key.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        value = new ArrayList<String>();
        value= dataBaseAdapter.getdata(file.getName());
        ImageKey.setText("Key Words : " + value.get(1));
        OcrText.setMovementMethod(new ScrollingMovementMethod());
        ImageKey.setMovementMethod(new ScrollingMovementMethod());
        OcrText.setText(value.get(0));
        Log.wtf("key in ocr frag", String.valueOf(value.size()));
//        text = null;



        return v;
    }
    /*    public String read_file(Context context, String filename) {
            StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                } catch (IOException e) {
                    //You'll need to add proper error handling here
                }
            return String.valueOf(text);
        }*/
    public class translate extends AsyncTask<Void,Void,Integer>
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

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            Log.wtf("volley","1");
            String YOUR_API_KEY="AIzaSyCDO95TXRHa2RtHr1u_LBJaQSER9XFEKtU";
            String sourcetxt=value.get(0).replace(" ","%20");
            String sourcetext=sourcetxt.replace("\n","%0A");
           String url = "https://www.googleapis.com/language/translate/v2?key="+YOUR_API_KEY+"&q="+sourcetext+"&source=ar&target="+target;
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
                        if (pos==0)
                        {
                            long id=dataBaseAdapter.updatedata(file.getName(),"transtext",transtext);
                        }

                        else if (pos==1)
                        {
                            long id=dataBaseAdapter.updatedata(file.getName(),"engtransdata",transtext);
                        }
                            target="en";
                        long id=dataBaseAdapter.updatedata(file.getName(),"transtext",transtext);
                        MainPage fake= new MainPage();

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

                    NetworkResponse response = error.networkResponse;
                    String res = new String(response.data);
                    // Now you can use any deserializer to make sense of data
                    Log.wtf(" response", res);
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
            Integer i=1;
            return i;
        }

        @Override
        protected void onPostExecute(Integer s) {


            super.onPostExecute(s);

            Log.i("ocrtext translation", "onpostexecute");

        }
    }
}