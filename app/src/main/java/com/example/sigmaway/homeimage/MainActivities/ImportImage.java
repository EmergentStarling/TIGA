package com.example.sigmaway.homeimage.MainActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.CaptureImage;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImportImage extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    String picturePath;
    String Destination;
    DataBaseAdapter dataBaseAdapter;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_image);
       importimage();
        sharedPref = getApplication().getSharedPreferences("shrdpref", MODE_PRIVATE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Log.wtf(" import image", String.valueOf(selectedImage));
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            // String picturePath contains the path of selected Image
            Log.wtf(" import image",picturePath);
            try {
                File sd = Environment.getExternalStorageDirectory();
                if (sd.canWrite()) {
                    String sourceImagePath= picturePath;
                    File source= new File(sourceImagePath);
                    Log.wtf("import image","source"+source.getPath());
                    if (source.exists()) {
                        Log.wtf("import image","src exist");
                        FileChannel src = new FileInputStream(source).getChannel();
                        Destination=String.valueOf(new CaptureImage().getOutputMediaFilepath());
                        FileChannel dst = new FileOutputStream(Destination).getChannel();
                        long bits= dst.transferFrom(src, 0, src.size());
                        Log.wtf("import image","bytes transfered"+bits);
                        src.close();
                        dst.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            File file=new File(Destination);
            long id = dataBaseAdapter.insertdata(file.getName(), String.valueOf(Uri.fromFile(file)), "NULL", "NULL", "NULL", "NULL", "YOU FORGOT TO SEND DATA FOR ANALYSIS or CLICK BUTTON BELOW");
            if (id == 0) {
                Toast.makeText(getApplicationContext(), "Import Again", Toast.LENGTH_SHORT).show();
                importimage();
            } else if (id >= 1) {
                Toast.makeText(getApplicationContext(), "Import Successfull", Toast.LENGTH_SHORT).show();
                Intent NewPreview = new Intent(ImportImage.this, New_Preview.class);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ImgUri", Destination);
                editor.apply();
//                    NewPreview.putExtra("fileuri",fileUri.toString());
                startActivity(NewPreview);
            }
        }
      else  if (requestCode == RESULT_LOAD_IMAGE && resultCode ==RESULT_CANCELED)
        {
            Toast.makeText(getApplicationContext(), "User Cancelled Image Import ", Toast.LENGTH_SHORT).show();
            Intent Previous = new Intent(ImportImage.this, HomeScreen.class);
            startActivity(Previous);
        }

    }
    private void importimage(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
}