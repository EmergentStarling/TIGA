package com.example.sigmaway.homeimage.PopWindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.MainActivities.New_Preview;
import com.example.sigmaway.homeimage.R;

import java.io.File;

public class Keyword_Popup extends AppCompatActivity {
SharedPreferences sharedPref;
    String FileURI;
    Button addkeyword;
    EditText keywords;
    DataBaseAdapter dataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword_popup);
        sharedPref = getApplication().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        FileURI = sharedPref.getString("ImgUri", "no name");
        addkeyword= (Button) findViewById(R.id.Popup_KeyWordAdd_btn);
        keywords= (EditText) findViewById(R.id.Popup_KeyWord);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addkeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Enter Keyword First",Toast.LENGTH_LONG).show();
    }
}