package com.example.sigmaway.homeimage.MainActivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.GridViewAdapter;
import com.example.sigmaway.homeimage.CustomClasses.Ocr;
import com.example.sigmaway.homeimage.R;
import com.example.sigmaway.homeimage.SlideableTabs.MainPage;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DirectDocument extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Sigmaway/";
    List<String> Picture_Name;
    List<Uri> Picture_URI;
    Uri fileUri;
    Button click;
    GridView wall_gridView;
    boolean flag = true;
    double gridviewid;
    String TempUri;
    int SpinnerPosition = 0;
    Button ocrLanguageBtn;
    Context c;
    String lang;
    Spinner OcrLanguageSelector;
    EditText KeyWord;
    Button KeyWord_AddBtn;
    DataBaseAdapter dataBaseAdapter;
    Intent ImageDetails;
    String TAG = "Direct Documents";
    String[] lang_list={"Select Language","English","Arabic"};
    SharedPreferences sharedPref;

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

        /*if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Cannot connect to OpenCV Manager");
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }*/
        sharedPref = getApplication().getSharedPreferences("shrdpref", MODE_PRIVATE);

        Picture_URI = new ArrayList<Uri>();
        Picture_Name = new ArrayList<String>();
        gridviewid = 0.1;
        if (Build.VERSION.SDK_INT < 23)
        {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Sigmaway/" + "Documents");
            final File[] fList = mediaStorageDir.listFiles();
            if (!(fList == null)) {
                for (File file : fList) {
                    String fileURI = file.getPath();
                    if (fileURI.endsWith(".jpg")) {
                        String[] name = file.getName().split("_");
                        Picture_Name.add(name[name.length - 3]);
                        Picture_URI.add(Uri.parse(file.getAbsolutePath()));
                        wall_gridView.invalidateViews();
                    }
                }
            }
        }
        else{
            StoragePermissionGranted1();
        }

       //this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.directdocument);

      // this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        /*setTitle("TIGA");*/
      //  getActionBar().setIcon(R.mipmap.ic_tig_xxxhdpi640);
        Log.wtf("Oncreate"," Hi");
        OcrLanguageSelector= (Spinner) findViewById(R.id.ocr_language);
        ArrayAdapter Spinner_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,lang_list);
        OcrLanguageSelector.setAdapter(Spinner_Adapter);
        OcrLanguageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ocrLanguageBtn= (Button) findViewById(R.id.ocr_language_btn);
        ocrLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (gridviewid == 0.1) {
                        Log.wtf("gridview", String.valueOf(gridviewid));
                        Toast.makeText(getApplicationContext(), "select the image first", Toast.LENGTH_LONG).show();
                    } else {

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
                            ImageDetails = new Intent(DirectDocument.this, MainPage.class);

                            Log.i("Direct Document", "ocr lang btn");

                            TempUri = sharedPref.getString("ImgUri", null);
                            c = getApplicationContext();
                            Log.i("Document", "on long1");
                            AsyncTaskRunner runner = new AsyncTaskRunner();
                            runner.execute();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.wtf("Long Press Image", "could not launch");
                }
            }
        });


        //Click Button
        click = (Button) findViewById(R.id.click_btn);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        //Edit Text - key word
        KeyWord = (EditText) findViewById(R.id.KeyWord);
        //
        //Key Word btn
        KeyWord_AddBtn = (Button) findViewById(R.id.KeyWordAdd_btn);
        KeyWord_AddBtn.setOnClickListener(new View.OnClickListener() {
            boolean Click= false;
            @Override
            public void onClick(View v) {

                if (!Click) {
                    KeyWord_AddBtn.setText("Add");
                    KeyWord.setVisibility(View.VISIBLE);
                    click.setVisibility(View.GONE);
                    Click=true;
                }

                else {

                    String KeyWords = KeyWord.getText().toString();
                    //      JSONObject ImageName = new JSONObject();
                    //    JSONObject ImageData = new JSONObject();

                    try {
                        if (gridviewid == 0.1) {
                            Log.wtf("gridview", String.valueOf(gridviewid));
                            Toast.makeText(getApplicationContext(), "select the image first", Toast.LENGTH_LONG).show();
                        } else {
                            File imagename = new File(String.valueOf(Picture_URI.get((int) gridviewid)));
                            String justimagename = imagename.getName();
                            String justfinal = justimagename.replace("%20", " ");
                            String JustFinal = justfinal.substring(0, justfinal.lastIndexOf('.'));
                            Log.wtf("here", justfinal);
                            //    ImageData.put("keywords", KeyWords);
                            //  ImageName.put(justfinal, ImageData);
                            dataBaseAdapter =new DataBaseAdapter(DirectDocument.this);
                            String value =dataBaseAdapter.getvalue("key",justimagename);
                            if (value.equals("NULL"))
                            {
                                long id=dataBaseAdapter.updatedata(justimagename,"key",KeyWords);
                                Log.wtf("key if id", String.valueOf(id));
                            }
                            else
                            {
                                long id=dataBaseAdapter.updatedata(justimagename,"key",value+","+KeyWords);
                                Log.wtf("key else id ", String.valueOf(id));
                            }
                            File keywordfile = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME + "Documents" + File.separator + JustFinal + ".key");

                     /*       if (keywordfile.exists()) {
                                FileOutputStream fOut = new FileOutputStream(keywordfile, true);
                                OutputStreamWriter file = new OutputStreamWriter(fOut);
                                file.append("," + KeyWords);
                                //  file.flush();
                                file.close();
                                Toast.makeText(getApplicationContext(), "KEY STORED", Toast.LENGTH_SHORT).show();
                            } else {
                                FileWriter file = new FileWriter(keywordfile);
                                file.write(KeyWords);
                                file.flush();
                                file.close();
                                Toast.makeText(getApplicationContext(), "KEY STORED", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    KeyWord_AddBtn.setText("Add Key");
                    KeyWord.setVisibility(View.GONE);
                    click.setVisibility(View.VISIBLE);
                    Click=false;
                }
            }
        });
        //Grid View
        wall_gridView = (GridView) findViewById(R.id.wall_gridView);

       final GridViewAdapter wall_gridview_adapter = new GridViewAdapter(getApplicationContext(), Picture_URI, Picture_Name);
        wall_gridView.setAdapter(wall_gridview_adapter);
        wall_gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        wall_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (gridviewid == 0.1 || gridviewid == id) {
                    gridviewid = id;
                    Log.wtf("gridviewid in onitemclick", String.valueOf(gridviewid));
                    if (flag) {

                        flag = false;
                        Log.wtf("in if", "" + flag);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        String TempUri1 = String.valueOf(Picture_URI.get((int) id).getPath());
                        TempUri= TempUri1.replace("%20", " ");
                        editor.putString("ImgUri", TempUri);
                        editor.apply();
                        view.setActivated(true);
                    } else {
                        Log.wtf("in else if", "" + flag);

                       sharedPref.edit().remove("ImgUri");
                        view.setActivated(false);
                        flag = true;
                        gridviewid = 0.1;
                    }
                } else {

                    view.setActivated(true);
                    flag = false;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String TempUri1 = String.valueOf(Picture_URI.get((int) id).getPath());
                    TempUri= TempUri1.replace("%20", " ");
                    editor.putString("ImgUri", TempUri);
                    editor.apply();
                    gridviewid = id;
                }
            }
        });
/*        wall_gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ImageDetails = new Intent(DirectDocument.this, MainPage.class);
                    Log.i("Document", "on long1");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String TempUri1 = String.valueOf(Picture_URI.get((int) id).getPath());
                    TempUri= TempUri1.replace("%20", " ");
                    editor.putString("ImgUri", TempUri);
                    editor.apply();
                    c = getApplicationContext();
                    Log.i("Document", "on long1");
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.wtf("Long Press Image", "could not launch");
                }
                return false;
            }
        });*/

    }
    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);

        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.wtf("waiting for", "gridview invalidate");
               Picture_Name.add("Document");
                Picture_URI.add(fileUri);
                File file=new File(String.valueOf(fileUri));

                dataBaseAdapter =new DataBaseAdapter(DirectDocument.this);
                long id=dataBaseAdapter.insertdata(file.getName(), String.valueOf(fileUri),"NULL","NULL","NULL","NULL","YOU FORGOT TO SEND DATA FOR ANALYSIS or CLICK BUTTON BELOW");
                if (id==0)
                    Toast.makeText(getApplicationContext(),"data not inserted", Toast.LENGTH_SHORT).show();
                else if (id>=1)
                    Toast.makeText(getApplicationContext(),"database inserted", Toast.LENGTH_SHORT).show();
                captureImage();


                // successfully captured the image
                // display it in image view
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
           //     Toast.makeText(getApplicationContext(),"User cancelled image capture ", Toast.LENGTH_SHORT).show();
                wall_gridView.invalidateViews();
            }
            else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if (requestCode == 1)
            Log.w("addhome","onrequestpermission 1");
    }


    private void captureImage()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME + "Documents");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +  "Document_" + timeStamp + ".jpg");

        } else {
            return null;
        }
        return mediaFile;
    }
    public class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            Log.i("Document", "onpreexecute");
            progress = new ProgressDialog(DirectDocument.this);
            progress.setMessage("processing");
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File tempuifile = new File(TempUri);
            Ocr OcrObj = new Ocr();
            dataBaseAdapter =new DataBaseAdapter(DirectDocument.this);
            String value =dataBaseAdapter.getvalue("text",tempuifile.getName());
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

                if (SpinnerPosition==0)
                {

                }
                String OcrText = OcrObj.tesseract(c, bmp,lang);

                Log.i("OCr text from Document", OcrText );
                long id=dataBaseAdapter.updatedata(tempuifile.getName(),"text",OcrText);
                if (id==0)
                    Log.wtf("data not updated", String.valueOf(id));
                else if (id>=1)
                    Log.wtf("data updated", String.valueOf(id));
                Log.wtf("test if id", String.valueOf(id));
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
            TempUri=null;
            ImageDetails.putExtra("language",lang_list[SpinnerPosition]);
            startActivity(ImageDetails);
            progress.dismiss();
            super.onPostExecute(aVoid);
        }
    }
    public void StoragePermissionGranted1() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                Log.v(TAG, "Permission is granted");
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Sigmaway/" + "Documents");
                final File[] fList = mediaStorageDir.listFiles();
                if (!(fList == null)) {
                    for (File file : fList) {
                        String fileURI = file.getPath();
                        if (fileURI.endsWith(".jpg")) {
                            String[] name = file.getName().split("_");
                            Picture_Name.add(name[name.length - 3]);
                            Picture_URI.add(Uri.parse(file.getAbsolutePath()));
                            }
                    }
                }
                captureImage();
                //    return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_LOGS},1 );
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 3);
                //  return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            //return true;
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.w("addhome","onrequestpermission");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Log.w("addhome",permissions[0].toString()+"   "+ grantResults[0]);
            Log.w("addhome",permissions[1].toString()+"   "+ grantResults[1]);
            Log.w("addhome",permissions[2].toString()+"   "+ grantResults[2]);
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.w("permission"," external");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    // Create the storage directory if it does not exist
                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Sigmaway/" + "Documents");
                    final File[] fList = mediaStorageDir.listFiles();
                    if (!(fList == null)) {
                        for (File file : fList) {
                            String fileURI = file.getPath();
                            if (fileURI.endsWith(".jpg")) {
                                String[] name = file.getName().split("_");
                                Picture_Name.add(name[name.length - 3]);
                                Picture_URI.add(Uri.parse(file.getAbsolutePath()));
                                wall_gridView.invalidateViews();
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "cannot create and access directory", Toast.LENGTH_SHORT).show();
                }
            }
            if (permissions[2].equals(Manifest.permission.CAMERA))
            {

                Log.wtf("camera","permisson");
                if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {


                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    captureImage();
                    Toast.makeText(getApplicationContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Camera Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
