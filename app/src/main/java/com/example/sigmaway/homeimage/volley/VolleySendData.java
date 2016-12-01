package com.example.sigmaway.homeimage.volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.ImageInfo;
import com.example.sigmaway.homeimage.R;
import com.example.sigmaway.homeimage.SlideableTabs.MainPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Family on 14-09-2016.
 */
public class VolleySendData {
    String Result = null;
    ProgressDialog progress;

    public String VolleySend(final Activity c, final ImageInfo info) {


        final RequestQueue requestQueue = Volley.newRequestQueue(c);
        Log.wtf("volley", "1");
        final String url = "http://gosigmaway.com/api/tiga/pushData.php ";

        progress = new ProgressDialog(c);
        progress.setMessage("processing");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        Thread p = new Thread() {
            public void run() {
                ((Activity) c).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("thread from volleu send", "Hurray..!!");
                        progress.show();
                    }

                });

            }
        };
        p.start();
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response array", response.toString());
                try {
                    Result = response.getString("result");

                    Log.wtf(" volley", Result);

                    if ((Result.equals("Success")) || (Result.equals("Successfully updated"))) {
                        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(c);
                        SharedPreferences sharedPref = c.getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
                        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
                        File file = new File(String.valueOf(ImageUri));
                        List<String> fullanalyseddata = dataBaseAdapter.GetAnalysedText(file.getName());
                        String GoogleAnalysisData = fullanalyseddata.get(1);

                        ServerCall obj = new ServerCall();
                        obj.Servercall(c, progress);

                        if (GoogleAnalysisData==null)
                        {
                            GoogleAnalyticsApi obj1 = new GoogleAnalyticsApi();
                            obj1.GoogleAnalyticsApi(c, progress, info);
                        }
                        else
                        {

                            GoogleAnalysisData = GoogleAnalysisData.trim();
                            Log.wtf("volley", " before googleanalysisdata if " + GoogleAnalysisData + GoogleAnalysisData.equals("Error inserting Google API response."));
                            if ((GoogleAnalysisData.equals("Error inserting Google API response."))
                                    || (GoogleAnalysisData.equals("Unexpected error occured in database: "))
                                    || (GoogleAnalysisData.equals("Curl error")) || (GoogleAnalysisData.equals("Parameters were not set."))
                                    || (GoogleAnalysisData.equals("<h4 style=\"text-align:center;\">Access Forbidden</h4>")))
                            {
                                Log.wtf("volley", " in googleanalysisdata if");
                                GoogleAnalyticsApi obj1 = new GoogleAnalyticsApi();
                                obj1.GoogleAnalyticsApi(c, progress, info);
                            }

                        }
                    } else if (Result.equals("Error updating data")) {
                        progress.dismiss();

                    }

                                /*MainPage obj=new MainPage();
                                obj.changepage();*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("result", " hi fail listning" + error);
                progress.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param1 = new HashMap<String, String>();
                param1.put("image_name", info.ImageName);
                param1.put("image_keywords", c.getString(R.string.Key_Word) + info.Keyword);
                param1.put("ocr_text", info.EngText);
                param1.put("user_id", info.UserID);
                return param1;
            }
        };
        requestQueue.add(customRequest);
        return Result;
    }
}
