package com.example.sigmaway.homeimage.CustomClasses;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.googlecode.tesseract.android.TessBaseAPI.OEM_CUBE_ONLY;
import static com.googlecode.tesseract.android.TessBaseAPI.OEM_DEFAULT;

/**
 * Created by Family on 07-09-2016.
 */
public class Ocr {
    String TAG= "OCR";
    String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Sigmaway/";
    String[] language={"eng.traineddata","ara.cube.bigrams","ara.cube.fold","ara.cube.lm","ara.cube.nn","ara.cube.params","ara.cube.size","ara.cube.word-freq","ara.traineddata"};
    Context c;
    ArrayList<Rect> Pics=new ArrayList<Rect>();
    public void Ocr(Context context){

        this.c=context;
        String[] paths = new String[]
                { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }

        }
        for (String lang:language)
        {   Log.v(TAG, "hey c");

            if (!(new File(DATA_PATH + "tessdata/" + lang )).exists()) {
                try {

                    AssetManager assetManager = c.getAssets();
                    InputStream in = assetManager.open("tessdata/" + lang );
                    //GZIPInputStream gin = new GZIPInputStream(in);
                    OutputStream out = new FileOutputStream(DATA_PATH
                            + "tessdata/" + lang);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    //while ((lenf = gin.read(buff)) > 0) {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    //gin.close();
                    out.close();

                    Log.v(TAG, "Copied " + lang + " traineddata");
                } catch (IOException e) {
                    Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
                }
            }

        }

    }
 public String tesseract(Context context,Bitmap bmpImg, String lang){
      this.c=context;

     Log.v(TAG, "Ctesseract 1" );
       TessBaseAPI baseApi = new TessBaseAPI();
     Log.v(TAG, "Ctesseract 2" );
       baseApi.setDebug(true);
     Log.v(TAG, "Ctesseract 3" );
     if (lang.equals("eng"))
    baseApi.init(DATA_PATH,lang,OEM_DEFAULT);
     if (lang.equals("ara"))
      baseApi.init(DATA_PATH,lang,OEM_CUBE_ONLY);
     Log.v(TAG, "Ctesseract 4" );
       baseApi.setImage(bmpImg);
     Log.v(TAG, "Ctesseract 5  "  );
       String recognizedText = baseApi.getUTF8Text();
     Log.v(TAG, "Ctesseract 6" );
       baseApi.end();
       if ( lang.equalsIgnoreCase("eng") )
     {
         recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9\\p{Punct}\\p{Space}]+", " ");
     }
   /*  if ( lang.equalsIgnoreCase("ara") )
     {
         recognizedText = recognizedText.replaceAll("[0600-06FF]", " ");
     }*/
       //recognizedText = recognizedText.trim();
     return recognizedText;

    }
}
