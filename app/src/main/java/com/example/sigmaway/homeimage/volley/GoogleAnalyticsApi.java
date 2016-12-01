package com.example.sigmaway.homeimage.volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sigmaway.homeimage.CustomClasses.Communicator;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.ImageInfo;
import com.example.sigmaway.homeimage.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Family on 22-09-2016.
 */

public class GoogleAnalyticsApi {
    File file;
    long id;
    Communicator comm;
    public void GoogleAnalyticsApi(final Context c, final ProgressDialog progressDialog, final ImageInfo info)
    {    final SharedPreferences sharedPref = c.getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
       file = new File(String.valueOf(ImageUri));

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        Log.wtf("volley","1");
        String url="http://gosigmaway.com/api/tiga/analyzeText.php";
        //String url= "http://gosigmaway.com:8085/RAWS/process/tiga?imageName="+file.getName().replace(" ","%20");
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("google response array", response);
                DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(c);
                Log.d("in sever call" , String.valueOf(c));
                   id=dataBaseAdapter.updatedata(file.getName(),"GoogleAnalyticsApiResponse",response);
              /*  if (id==0)
                {
                    Log.wtf("Analysed data not updated", String.valueOf(id));
                    *//*MainPage obj=new MainPage();
                    obj.changepage();*//*
                   imgrequest img= new imgrequest();
                    img.imgrequest(c);
                    progressDialog.dismiss();

                }

                else if (id>=1) {
                    Log.wtf("Analysed data updated", String.valueOf(id));
                 *//*   MainPage obj=new MainPage();
                    obj.changepage();*//*
               *//*     MainPage obj=new MainPage();
                    obj.analysedchanger();*//*
                    imgrequest img= new imgrequest();
                    img.imgrequest(c);
                    progressDialog.dismiss();


                }*/
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param1 = new HashMap<String, String>();
                param1.put("image_name", file.getName());
                param1.put("translated_text", info.EngText);
                return param1;
            }
        };
        requestQueue.add(stringRequest);
    }
}
