package com.example.sigmaway.homeimage.PopWindow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.ImageInfo;
import com.example.sigmaway.homeimage.CustomClasses.Ocr;
import com.example.sigmaway.homeimage.R;
import com.example.sigmaway.homeimage.SlideableTabs.MainPage;
import com.example.sigmaway.homeimage.SlideableTabs.Ocrtext_ArabFrag;
import com.example.sigmaway.homeimage.volley.VolleySendData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Language_Popup extends AppCompatActivity {
    SharedPreferences sharedPref;

    Button LangBtn;
    Intent ImageDetails;
    String TempUri;
    Context c;
    DataBaseAdapter dataBaseAdapter;
    private static final String IMAGE_DIRECTORY_NAME = "Sigmaway/";
    Uri FileURI;
    String lang;
    File file;
    List<String> value1;
    Activity activity;
    Spinner spinner;
    int SpinnerPosition = 0;
    String[] lang_list={"Select Language","English","Arabic"};
    String TAG="Language_POPup";
    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            Log.i(TAG, "OpenCV loaded successfully 1");
            //  super.onManagerConnected(status);
            switch (status) {

                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    Mat GrayImg = new Mat();
                }
                break;
                default: {
                    Log.i(TAG, "OpenCV loaded successfully 1");
                    super.onManagerConnected(status);
                    Log.i(TAG, "OpenCV loaded successfully 1");
                }
                break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_popup);
        sharedPref = getApplication().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        FileURI = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        LangBtn= (Button) findViewById(R.id.popup_ocr_language_btn);
        spinner= (Spinner) findViewById(R.id.popup_ocr_language);
        ArrayAdapter Spinner_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,lang_list);
        spinner.setAdapter(Spinner_Adapter);
        activity=this;
        file=new File(FileURI.toString());
        dataBaseAdapter =new DataBaseAdapter(getApplicationContext());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i=getIntent();
                i.putExtra("spinnerposition",SpinnerPosition);
                setResult(RESULT_OK,i);*/
                if (SpinnerPosition==0)
                {
                    Toast.makeText(getApplicationContext(), "select the language first", Toast.LENGTH_LONG).show();

                }
                else
                {
                    if (SpinnerPosition==1)
                        lang="eng";
                    else if (SpinnerPosition==2)
                        lang="ara";
                    ImageDetails = new Intent(Language_Popup.this, MainPage.class);

                    Log.i("Direct Document", "ocr lang btn");

                    TempUri= FileURI.getPath();
                    c = getApplicationContext();
                    Log.i("Document", "on long1");
                    AsyncTaskRunner2 runner = new AsyncTaskRunner2();
                    runner.execute();
                }

            }
        });

    }
    private class AsyncTaskRunner2 extends AsyncTask<Void, Void, Void> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            Log.i("Document", "onpreexecute");
            progress = new ProgressDialog(Language_Popup.this);
            progress.setMessage("processing");
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            if (!OpenCVLoader.initDebug()) {
                Log.e(TAG, "Cannot connect to OpenCV Manager");
            } else {
                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File tempuifile = new File(TempUri);
            Ocr OcrObj = new Ocr();
            dataBaseAdapter =new DataBaseAdapter(Language_Popup.this);
            String value =dataBaseAdapter.getvalue("text",tempuifile.getName());
            Log.wtf("direct doc text check",value.toString());
            if (value.equals("NULL"))
            {
                String JustFinal =  tempuifile.getName().substring(0,tempuifile.getName().lastIndexOf('.'));
                // String JustFinal =  tempuifile.getName().substring(tempuifile.getName().lastIndexOf('.')-1);
                Log.wtf("do in background", TempUri);
                File OCRFile = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME + "Documents" + File.separator + JustFinal + ".txt");
                //      Looper.prepare();
                Log.i("aysnc", "ocr obj");
                OcrObj.Ocr(c);
                Log.i("aysnc", "ocr obj called");
                Bitmap bmp = BitmapFactory.decodeFile(TempUri);

                Mat GrayImg = new Mat();
                Mat Img = new Mat(bmp.getHeight(), bmp.getWidth(), CvType.CV_32SC1);
                Utils.bitmapToMat(bmp, Img);
                Imgproc.cvtColor(Img, GrayImg, Imgproc.COLOR_RGBA2GRAY);
                Imgproc.adaptiveThreshold(GrayImg, GrayImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 39, 15);
                bmp.recycle();
                bmp = Bitmap.createBitmap(GrayImg.cols(), GrayImg.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(GrayImg, bmp);
                //Mat mRgba= new Mat(bmp.getHeight(),bmp.getWidth(), CvType.CV_32SC1);


                    String OcrText = OcrObj.tesseract(c, bmp,lang);

                    Log.i("OCr text from Document", OcrText );
                    long id=dataBaseAdapter.updatedata(tempuifile.getName(),"text",OcrText);
                    if (id==0)
                        Log.wtf("data not updated", String.valueOf(id));
                    else if (id>=1)
                        Log.wtf("data updated", String.valueOf(id));
                    Log.wtf("test if id", String.valueOf(id));
                value1 = new ArrayList<String>();
                value1= dataBaseAdapter.getdata(file.getName());
            }
            else
            {
                Log.wtf("text already found ",value );
            }

          /*  if (OCRFile.exists()) {   dataBaseAdapter =new DataBaseAdapter(DirectDocument.this);
               *//* long id=dataBaseAdapter.updatedata(tempuifile.getName(), "text","here i am bro");
                if (id<0)
                    // Toast.makeText(getApplicationContext(),"data not updated", Toast.LENGTH_SHORT).show();
                    Log.i("update", "not updated");
                else
                    Log.i("update", "updated");*//*
                return null;
            } else {
                OcrObj.Ocr(c);
                Bitmap bmp = BitmapFactory.decodeFile(TempUri);
                Mat GrayImg = new Mat();
                Mat Img = new Mat(bmp.getHeight(), bmp.getWidth(), CvType.CV_32SC1);
                Utils.bitmapToMat(bmp, Img);
                Imgproc.cvtColor(Img, GrayImg, Imgproc.COLOR_RGBA2GRAY);
                Imgproc.adaptiveThreshold(GrayImg, GrayImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 39, 15);
                bmp.recycle();
                ;
                bmp = Bitmap.createBitmap(GrayImg.cols(), GrayImg.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(GrayImg, bmp);
                //Mat mRgba= new Mat(bmp.getHeight(),bmp.getWidth(), CvType.CV_32SC1);
                String OcrText = OcrObj.tesseract(c, bmp);
                Log.i("OCr text from Document", OcrText );
                dataBaseAdapter =new DataBaseAdapter(DirectDocument.this);
                String value =dataBaseAdapter.getvalue("text",tempuifile.getName());
                if (value.equals("NULL"))
                {
                    long id=dataBaseAdapter.updatedata(tempuifile.getName(),"text",OcrText);

                    Log.wtf("test if id", String.valueOf(id));
                }
                else
                {
                    Log.wtf("text already found ",value );
                }
                    //Toast.makeText(getApplicationContext(),"database updated", Toast.LENGTH_SHORT).show();


         *//*       if (OcrText==null)
                {
                    Toast.makeText(c, "no text Found", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("OcrText", OcrText);
                    Log.i("OCr text from Document", OcrText);
                    editor.apply();
                }

                else
                {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("OcrText", OcrText);
                    Log.i("OCr text from Document", OcrText);
                    editor.apply();
                }*//*

                try {
                    FileWriter file = new FileWriter(OCRFile);
                    file.write(OcrText);
                    file.flush();
                    file.close();
                    // Toast.makeText(getApplicationContext(), "Text Saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TempUri=null;

            progress.dismiss();
            if (SpinnerPosition==1)
            {
                ImageInfo info=new ImageInfo();
                info.ImageName=file.getName();
                info.EngText=value1.get(0);
                info.Keyword=value1.get(1);
                info.UserID="8800304343";
                VolleySendData obj=new VolleySendData();
                String result= obj.VolleySend(activity,info);
                ImageDetails.putExtra("language",lang_list[SpinnerPosition]);
                startActivity(ImageDetails);
                finish();
            }
            if (SpinnerPosition==2)
            {
                translate trans=new translate();
                trans.execute();
            }




        }
    }



    private class translate extends AsyncTask<Void,Void,Integer>
    {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            Log.i("ocrtext translation", "onpreexecute");
            progress = new ProgressDialog(activity);
            progress.setMessage("processing");
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            super.onPreExecute();

        }
        @Override
        protected Integer doInBackground(Void... params) {
            String engtext= dataBaseAdapter.GetTranslatedText(file.getName(),"en");
            Log.wtf("translated text frag",engtext.toString());
            if (engtext.equals("NULL"))
            {
                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                Log.wtf("volley","1");
                String YOUR_API_KEY="AIzaSyCDO95TXRHa2RtHr1u_LBJaQSER9XFEKtU";
                String sourcetxt=value1.get(0).replace(" ","%20");
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
                            long id=dataBaseAdapter.updatedata(file.getName(),"engtransdata",transtext);
                            ImageInfo info=new ImageInfo();
                            String engtext= dataBaseAdapter.GetTranslatedText(file.getName(),"en");
                            Log.wtf("arbic translated text",engtext);
                            info.ImageName=file.getName();
                            info.EngText=transtext;
                            info.Keyword=value1.get(1);
                            info.UserID="8800304343";
                            VolleySendData obj=new VolleySendData();

                            String result= obj.VolleySend(activity,info);
                            Log.wtf("key if id", String.valueOf(id));

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

          //  Log.wtf("translated text",result);
            super.onPostExecute(s);
            Log.i("ocrtext translation", "onpostexecute");
            ImageDetails.putExtra("language",lang_list[SpinnerPosition]);
            startActivity(ImageDetails);
            progress.dismiss();
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Select Language First",Toast.LENGTH_LONG).show();
    }
}
