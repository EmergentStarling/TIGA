package com.example.sigmaway.homeimage.MainActivities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.CaptureImage;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.R;

import java.io.File;

public class New_Camera extends AppCompatActivity {
    DataBaseAdapter dataBaseAdapter;
    public static final int MEDIA_TYPE_IMAGE = 1;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    Uri fileUri;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_camera);
        sharedPref = getApplication().getSharedPreferences("shrdpref", MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    )
                captureImage();
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_LOGS},1 );
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.wtf("waiting for", "gridview invalidate");
                File file=new File(String.valueOf(fileUri));

                dataBaseAdapter =new DataBaseAdapter(New_Camera.this);
                long id=dataBaseAdapter.insertdata(file.getName(), String.valueOf(fileUri),"NULL","NULL","NULL","NULL","YOU FORGOT TO SEND DATA FOR ANALYSIS or CLICK BUTTON BELOW");
                if (id==0)
                {
                    Toast.makeText(getApplicationContext(),"Click Again", Toast.LENGTH_SHORT).show();
                    captureImage();
                }

                else if (id>=1)
                {
                    Toast.makeText(getApplicationContext(),"Image Captured", Toast.LENGTH_SHORT).show();
                    Intent NewPreview=new Intent(New_Camera.this,New_Preview.class);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("ImgUri", fileUri.getPath());
                    editor.apply();
//                    NewPreview.putExtra("fileuri",fileUri.toString());
                    Log.wtf("putting extra in new preview", String.valueOf(new File(fileUri.getPath())));
                    startActivity(NewPreview);
                }
                // successfully captured the image
                // display it in image view
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                     Toast.makeText(getApplicationContext(),"User cancelled image capture ", Toast.LENGTH_SHORT).show();
                Intent Previous=new Intent(New_Camera.this,DirectDocument.class);
                startActivity(Previous);
            }
            else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1)
            Log.w("addhome","onrequestpermission 1");
    }
    private void captureImage()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = new CaptureImage().getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED)
            captureImage();
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_LOGS},1 );

        }

    }
}
