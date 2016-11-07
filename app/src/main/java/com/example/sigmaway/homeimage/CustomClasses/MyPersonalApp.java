package com.example.sigmaway.homeimage.CustomClasses;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.example.sigmaway.homeimage.MainActivities.DirectDocument;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Family on 29-09-2016.
 */

    public class MyPersonalApp extends MultiDexApplication{

        /**
         * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
         */
        File logFile;
        public void onCreate() {
            super.onCreate();

            Log.wtf("Mypersonalapp","here");
            if ( isExternalStorageWritable() ) {
                Log.wtf("Mypersonalapp","writeable");
                File appDirectory = new File( Environment.getExternalStorageDirectory() + "/MyPersonalAppFolder" );
                File logDirectory = new File( appDirectory + "/log" );
                logFile = new File( logDirectory, "logcat" + System.currentTimeMillis() + ".txt" );

                // create app folder
                if ( !appDirectory.exists() ) {
                    appDirectory.mkdir();
                }

                // create log folder
                if ( !logDirectory.exists() ) {
                    logDirectory.mkdir();
                }
                logging runner=new logging();
                runner.execute();

            } else if ( isExternalStorageReadable() ) {
                // only readable
            } else {
                // not accessible
            }
        }

        /* Checks if external storage is available for read and write */
        public boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
                return true;
            }
            return false;
        }

        /* Checks if external storage is available to at least read */
        public boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
                return true;
            }
            return false;
        }
    public class logging extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            Log.v("Mypersonal","in thread");
            // clear the previous logcat and then write the new one to the file
            try {
              //  Runtime.getRuntime().exec("logcat -c");
                Process process = Runtime.getRuntime().exec("logcat -f "+ logFile + " -v time -d *:V");
             /*   BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                // Log.v("Mypersonal",process.toString());
                StringBuilder log=new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                 //   Log.v("Mypersonal",line);
                    log.append(line);
                }*/
            }
            catch (IOException e) {
                //handle exception
            }
            return null;
        }
    }
}

