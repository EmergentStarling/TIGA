package com.example.sigmaway.homeimage.MainActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.PopWindow.Keyword_Popup;
import com.example.sigmaway.homeimage.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class New_Preview extends AppCompatActivity {
    ImageView BackgroundImage;
    Uri FileURI;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_preview);
       BackgroundImage= (ImageView) findViewById(R.id.New_Preview_BAckgroung_Image);
        sharedPref = getApplication().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        FileURI = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        Log.wtf("new preview",FileURI.toString());

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        String path= FileURI.getPath();
        Log.wtf("new preview onpost", path);
        Picasso.with(New_Preview.this.getApplicationContext())
                .load(new File(path))
                .resize(1500,1500)
                .into(BackgroundImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
            if (!(FileURI.equals(null)))
            {
               DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(this);
                String value =dataBaseAdapter.getvalue("key",(new File(FileURI.toString())).getName());
                if (value.equals("NULL"))
                {
                    Intent Keywordpopup=new Intent(New_Preview.this,Keyword_Popup.class);
                    startActivityForResult(Keywordpopup,1);

                }

            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Enter Keyword First",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            Log.wtf("preview","pop closed");
        }
    }
}

