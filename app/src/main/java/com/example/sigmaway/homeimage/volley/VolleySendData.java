package com.example.sigmaway.homeimage.volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.sigmaway.homeimage.CustomClasses.ImageInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
       Thread p= new Thread() {
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
                                if ((Result.equals("Success")) || (Result.equals("Successfully updated")))
                                {
                                    ServerCall obj=new ServerCall();
                                    obj.Servercall(c,progress);

                                }
                                else if (Result.equals("Error updating data"))
                                    progress.dismiss();

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
                            param1.put("image_keywords", info.Keyword);
                            param1.put("ocr_text", info.EngText);
                            param1.put("user_id", info.UserID);
                            return param1;
                        }
                    };
                    requestQueue.add(customRequest);
                    return Result;
                }
            }
