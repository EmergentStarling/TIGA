package com.example.sigmaway.homeimage.MainActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Documents extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int MEDIA_TYPE_IMAGE = 1;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Sigmaway";
    String Room_Selected;
    Spinner wall_selector;
    Button wall_click;
    GridView wall_gridView;
    boolean flag = true;
    int SpinnerPosition = 0;
    double gridviewid;
    List<Uri> Picture_URI;
    String TempUri;
    List<String> Picture_Name;
    Button KeyWord_AddBtn;
    EditText KeyWord;
    String HomeName;
    Context c;

    String[] wall_list = {"Left Wall Doc", "Right Wall Doc", "Front Wall Doc", "Back Wall Doc", "Floor Wall Doc", "Ceiling Wall Doc"};
    SharedPreferences sharedPref;
    String TAG = "documents";
    Intent ImageDetails;
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
    private Uri fileUri; // file uri to store image/video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Cannot connect to OpenCV Manager");
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        gridviewid = 0.1;
        Picture_URI = new ArrayList<Uri>();
        Picture_Name = new ArrayList<String>();
        sharedPref = getApplication().getSharedPreferences("shrdpref", MODE_PRIVATE);
        HomeName = sharedPref.getString("Homename", "no name");
        Intent roominventory_receiving_intent = getIntent();
        Room_Selected = roominventory_receiving_intent.getStringExtra("room_Selected");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Sigmaway/" + HomeName + File.separator + Room_Selected + File.separator + "Documents");
        File[] fList = mediaStorageDir.listFiles();
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
        //spinner
        wall_selector = (Spinner) findViewById(R.id.Wall_selector);
        ArrayAdapter Spinner_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, wall_list);
        wall_selector.setAdapter(Spinner_Adapter);
        wall_selector.setOnItemSelectedListener(this);
        //
        //Spinner button
        wall_click = (Button) findViewById(R.id.click_wall_btn);
        wall_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        //
        //Edit Text - key word
        KeyWord = (EditText) findViewById(R.id.KeyWord);
        //
        //Key Word btn
        KeyWord_AddBtn = (Button) findViewById(R.id.KeyWordAdd_btn);
        KeyWord_AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        String JustFinal =  justfinal.substring(0,justfinal.lastIndexOf('.'));
                        Log.wtf("here", justfinal);
                        //    ImageData.put("keywords", KeyWords);
                        //  ImageName.put(justfinal, ImageData);

                        File keywordfile = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME + File.separator + HomeName + File.separator + Room_Selected + File.separator + "Documents" + File.separator + JustFinal + ".key");

                        if (keywordfile.exists()) {
                            FileOutputStream fOut = new FileOutputStream(keywordfile, true);
                            OutputStreamWriter file = new OutputStreamWriter(fOut);
                            file.append(","+KeyWords);
                            //  file.flush();
                            file.close();
                            Toast.makeText(getApplicationContext(), "KEY STORED", Toast.LENGTH_SHORT).show();
                        } else {
                            FileWriter file = new FileWriter(keywordfile);
                            file.write(KeyWords);
                            file.flush();
                            file.close();
                            Toast.makeText(getApplicationContext(), "KEY STORED", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
        //Grid View
        wall_gridView = (GridView) findViewById(R.id.wall_gridView);

        GridViewAdapter wall_gridview_adapter = new GridViewAdapter(getApplicationContext(), Picture_URI, Picture_Name);
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
                        view.setActivated(true);
                    } else {
                        Log.wtf("in else if", "" + flag);
                        view.setActivated(false);
                        flag = true;
                        gridviewid = 0.1;
                    }
                } else {
                    view.setActivated(true);
                    flag = false;
                    gridviewid = id;
                }
            }
        });
        wall_gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ImageDetails = new Intent(Documents.this, MainPage.class);
                    Log.i("Document", "on long1");
                    SharedPreferences.Editor editor = sharedPref.edit();
                   String TempUri1 = String.valueOf(Picture_URI.get((int) id).getPath());
                    TempUri= TempUri1.replace("%20", " ");
                    editor.putString("ImgUri", TempUri);
                    editor.apply();
                    c = getApplicationContext();
                    Log.i("Document", "on long1");
                    AsyncTaskRunner1 runner = new AsyncTaskRunner1();
                    runner.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.wtf("Long Press Image", "could not launch");
                }
                return false;
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //if condition is to rectify which parent view is pressed
        if (parent == wall_selector) {
            Log.i("spinner ", String.valueOf(position));
            // to identify the wall user selected to click
            SpinnerPosition = position;
            // Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.wtf("waiting for", "gridview invalidate");
                Picture_Name.add(wall_list[SpinnerPosition]);
                Picture_URI.add(fileUri);
                wall_gridView.invalidateViews();


                // successfully captured the image
                // display it in image view
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME + File.separator + HomeName + File.separator + Room_Selected + File.separator + "Documents");
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
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + HomeName + "_" + Room_Selected + "_" + wall_list[SpinnerPosition] + "_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);

        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

   public class AsyncTaskRunner1 extends AsyncTask<Void, Void, Void> {
        Ocr OcrObj = new Ocr();
       ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            Log.i("Document", "onpreexecute");
            progress = new ProgressDialog(Documents.this);
            progress.setMessage("processing");
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File tempuifile = new File(TempUri);
            String JustFinal =  tempuifile.getName().substring(0,tempuifile.getName().lastIndexOf('.'));
           // String JustFinal =  tempuifile.getName().substring(tempuifile.getName().lastIndexOf('.')-1);
            Log.wtf("do in background", TempUri);
            File OCRFile = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME + File.separator + HomeName + File.separator + Room_Selected + File.separator + "Documents" + File.separator + JustFinal + ".txt");
      //      Looper.prepare();

            if (OCRFile.exists()) {
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
                bmp = Bitmap.createBitmap(GrayImg.cols(), GrayImg.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(GrayImg, bmp);
                //Mat mRgba= new Mat(bmp.getHeight(),bmp.getWidth(), CvType.CV_32SC1);
                String OcrText = OcrObj.tesseract(c, bmp,"eng");


                Log.i("OCr text from Document", OcrText );
         /*       if (OcrText==null)
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
                }*/

                try {
                    FileWriter file = new FileWriter(OCRFile);
                    file.write(OcrText);
                    file.flush();
                    file.close();
                   // Toast.makeText(getApplicationContext(), "Text Saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }


        }

        @Override
        protected void onPostExecute(Void aVoid) {




            startActivity(ImageDetails);
            progress.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}
