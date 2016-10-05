package com.example.sigmaway.homeimage.volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sigmaway.homeimage.CustomClasses.Communicator;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.R;
import com.example.sigmaway.homeimage.SlideableTabs.AnalysedData;
import com.example.sigmaway.homeimage.SlideableTabs.MainPage;
import com.example.sigmaway.homeimage.SlideableTabs.Myfragmentpageradapter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Family on 22-09-2016.
 */

public class ServerCall {
    File file;
    long id;
    Communicator comm;
    public void Servercall(final Context c, final ProgressDialog progressDialog)
    {    final SharedPreferences sharedPref = c.getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
       file = new File(String.valueOf(ImageUri));

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        Log.wtf("volley","1");
        String url= "http://gosigmaway.com:8085/RAWS/process/armyService?imageName="+file.getName();
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("response array", response);
                DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(c);
                Log.d("in sever call" , String.valueOf(c));
                   id=dataBaseAdapter.updatedata(file.getName(),"AnalysedData",response);
                if (id==0)
                {
                    Log.wtf("Analysed data not updated", String.valueOf(id));
                    /*MainPage obj=new MainPage();
                    obj.changepage();*/
                    progressDialog.dismiss();
                }

                else if (id>=1) {
                    Log.wtf("Analysed data updated", String.valueOf(id));
                 /*   MainPage obj=new MainPage();
                    obj.changepage();*/
               /*     MainPage obj=new MainPage();
                    obj.analysedchanger();*/
                    progressDialog.dismiss();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("result", " hi fail listning" + error);

              /*  NetworkResponse response = error.networkResponse;
                String res = new String(response.data);
                // Now you can use any deserializer to make sense of data
                Log.wtf(" response", res);*/
                progressDialog.dismiss();
            }
        })
                //header for rapidminer
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.wtf("volley ", "header");
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s","amrita","7aarora7");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
