package com.example.sigmaway.homeimage.volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Family on 12-10-2016.
 */

public class imgrequest {
    File file;

    public void imgrequest(Context c)
    {
        final SharedPreferences sharedPref = c.getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        file = new File(String.valueOf(ImageUri));
        ImageView mImageView;
        String url="http://gosigmaway.com:8085/RAWS/resources/home/admin/TIGA/Report/"+file.getName()+"/image1.png ";
        // mImageView = (ImageView) findViewById(R.id.myImage);
        // Retrieves an image specified by the URL, displays it in the UI.
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Log.w("imgrequest reponse",bitmap.toString());
                    saveToInternalStorage(bitmap);
                        //                mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        //              mImageView.setImageResource(R.drawable.image_load_error);
                    }
                })    {
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
        requestQueue.add(request);
    }
    private void saveToInternalStorage(Bitmap bitmapImage){
        File directory=new File(Environment.getExternalStorageDirectory()+"/Sigmaway/","Analysed/");

        if ( !directory.exists() ) {
            directory.mkdir();
        }
        File mypath=new File(directory.getPath(),file.getName());

        Log.w("imgrequest ","onsave called");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                Log.w("imgrequest ","fos closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
