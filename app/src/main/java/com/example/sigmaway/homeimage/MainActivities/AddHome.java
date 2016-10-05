package com.example.sigmaway.homeimage.MainActivities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sigmaway.homeimage.R;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddHome extends AppCompatActivity {

    List<String> HomeList;
    ArrayAdapter<String> List_View_Adapter;
    ListView HomeListview;
    EditText HomeName;
    Button HomeAddButton;
    String TAG = "taking permission";
    String ROOT_DIRECTORY_NAME;
    File mediaStorageDir;
    Button DirectDocumentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_home);
        this.setTitle("Home");
        Log.wtf("home","called");
        HomeList = new ArrayList<String>();
        ROOT_DIRECTORY_NAME = "Sigmaway";
        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), ROOT_DIRECTORY_NAME);
        DirectDocumentBtn = (Button) findViewById(R.id.DirectDocumentBtn);
        DirectDocumentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doc=new Intent(AddHome.this,DirectDocument.class);
                startActivity(doc);
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(ROOT_DIRECTORY_NAME, "Oops! Failed create " + ROOT_DIRECTORY_NAME + " directory");
                }
            }

            File[] fList = mediaStorageDir.listFiles();
            if (!(fList == null)) {
                for (File file : fList) {
                    if (file.isDirectory()) {
                        if (file.getName().equals("tessdata") || file.getName().equals("Documents") )
                            Log.wtf("add home", "tess data  or Document file found");
                        else
                            HomeList.add(file.getName());
                    }

                }
            }
        } else {
            StoragePermissionGranted();
        }
        //Listview
        HomeListview = (ListView) findViewById(R.id.HomeList);
        List_View_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, HomeList);
        HomeListview.setAdapter(List_View_Adapter);
        HomeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent Room_List = new Intent(AddHome.this, AddRoom.class);
                SharedPreferences sharedPref = getApplication().getSharedPreferences("shrdpref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Homename", HomeList.get(position));
                editor.apply();
                startActivity(Room_List);
            }
        });
        //editview
        HomeName = (EditText) findViewById(R.id.AddHomeEdit);
        //button
        HomeAddButton = (Button) findViewById(R.id.AddHome_btn);
        HomeAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeList.add(HomeName.getText().toString());
                HomeListview.invalidateViews();
            }
        });
    }

    public void StoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                Log.v(TAG, "Permission is granted");

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
            if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    // Create the storage directory if it does not exist
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs())
                        {
                            Log.d(ROOT_DIRECTORY_NAME, "Oops! Failed create " + ROOT_DIRECTORY_NAME + " directory");
                        }
                        File[] fList = mediaStorageDir.listFiles();
                        if (!(fList == null)) {
                            for (File file : fList) {
                                if (file.isDirectory())
                                    HomeList.add(file.getName());
                                HomeListview.invalidateViews();
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "cannot create and access directory", Toast.LENGTH_SHORT).show();
                }
            }
           else  if (permissions[1]==Manifest.permission.CAMERA)
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    Toast.makeText(getApplicationContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Camera Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
            }

        }






    }
}