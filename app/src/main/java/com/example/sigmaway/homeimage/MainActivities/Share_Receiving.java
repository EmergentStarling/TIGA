package com.example.sigmaway.homeimage.MainActivities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.CaptureImage;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class Share_Receiving extends AppCompatActivity {

    String picturePath;
    String Destination;
    DataBaseAdapter dataBaseAdapter;
    SharedPreferences sharedPref;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
       /*String data=intent.getParcelableExtra(Intent.EXTRA_STREAM);
        Log.wtf("share","action "+action+ " type "+ type+ " data "+data);*/

        if (Intent.ACTION_SEND.equals(action) && type != null) {

           if (type.startsWith("image/")) {
               dataBaseAdapter=new DataBaseAdapter(this);
               sharedPref = getApplication().getSharedPreferences("shrdpref", MODE_PRIVATE);
                handleSendImage(intent); // Handle single image being sent
            }
        }
        else
            Toast.makeText(this,"Ooopppss!!! Select the image again",Toast.LENGTH_LONG).show();
    }



    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (imageUri != null) {

            if(imageUri.getScheme().equals("content"))
            {
                Log.wtf("receiving intent",imageUri.toString()+ "  type  " +imageUri.getScheme());
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(imageUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                // String picturePath contains the path of selected Image
                Log.wtf(" import content image",picturePath);
            }
            else if (imageUri.getScheme().equals("file"))
            {

              picturePath=imageUri.getPath();
                Log.wtf(" import file image",picturePath);

            }
            try {
                File sd = Environment.getExternalStorageDirectory();
                if (sd.canWrite()) {
                    String sourceImagePath = picturePath;
                    File source = new File(sourceImagePath);
                    Log.wtf("import image", "source" + source.getPath());
                    if (source.exists()) {
                        Log.wtf("import image", "src exist");
                        FileChannel src = new FileInputStream(source).getChannel();
                        Destination = String.valueOf(new CaptureImage().getOutputMediaFilepath());
                        FileChannel dst = new FileOutputStream(Destination).getChannel();
                        long bits = dst.transferFrom(src, 0, src.size());
                        Log.wtf("import image", "bytes transfered" + bits);
                        src.close();
                        dst.close();
                    }
                    File file = new File(Destination);
                    long id = dataBaseAdapter.insertdata(file.getName(), String.valueOf(Uri.fromFile(file)), "NULL", "NULL", "NULL", "NULL", "YOU FORGOT TO SEND DATA FOR ANALYSIS or CLICK BUTTON BELOW");
                    if (id == 0) {
                        Toast.makeText(getApplicationContext(), "Import Again", Toast.LENGTH_SHORT).show();

                    } else if (id >= 1) {
                        Toast.makeText(getApplicationContext(), "Import Successfull", Toast.LENGTH_SHORT).show();
                        Intent NewPreview = new Intent(Share_Receiving.this, New_Preview.class);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ImgUri", Destination);
                        editor.apply();
//                    NewPreview.putExtra("fileuri",fileUri.toString());
                        startActivity(NewPreview);
                    }
                } else {
                    Log.wtf("import image", "cannot write");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
                }
            }catch (Exception e) {
                e.printStackTrace();
            }




        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                )
        {
            ;
            Log.wtf(" homescreen","all Permission granted");
            handleSendImage(intent);
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
        }

    }
}
