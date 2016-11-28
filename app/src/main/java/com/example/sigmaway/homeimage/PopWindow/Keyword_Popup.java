package com.example.sigmaway.homeimage.PopWindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.LocationGetter;
import com.example.sigmaway.homeimage.MainActivities.New_Preview;
import com.example.sigmaway.homeimage.R;

import java.io.File;

public class Keyword_Popup extends AppCompatActivity {
SharedPreferences sharedPref;
    String FileURI;
    Button addkeyword;
    EditText keywords;
    DataBaseAdapter dataBaseAdapter;
    TextInputLayout inputLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword_popup);
        sharedPref = getApplication().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        FileURI = sharedPref.getString("ImgUri", "no name");
        inputLayout= (TextInputLayout) findViewById(R.id.KeyWord_popup_inputlayout);
        addkeyword= (Button) findViewById(R.id.Popup_KeyWordAdd_btn);
        keywords= (EditText) findViewById(R.id.Popup_KeyWord);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addkeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileURI = sharedPref.getString("ImgUri", "no name");
                dataBaseAdapter =new DataBaseAdapter(Keyword_Popup.this);
                long id=dataBaseAdapter.updatedata(new File(FileURI).getName(),"key",keywords.getText().toString());
                Log.wtf("key if id", String.valueOf(id));
                setResult(RESULT_OK);
                startActivity(new Intent(Keyword_Popup.this,Language_Popup.class));
                Log.w("keyword popup","before finish");
                finish();
                Log.w("keyword popup","after finish");


            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Keyword_Popup.this,Language_Popup.class));
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationGetter obj=new LocationGetter(getApplicationContext());
        locationManager.removeUpdates(obj.locationListener);
    }
}
