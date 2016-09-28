package com.example.sigmaway.homeimage.SlideableTabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.example.sigmaway.homeimage.CustomClasses.Communicator;
import com.example.sigmaway.homeimage.CustomClasses.Ocr;
import com.example.sigmaway.homeimage.R;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener,Communicator {
    public ViewPager viewPager;
    TabHost tabHost;
    String TAG= "in main";


    Intent getintent;
    String language;
    Ocr OcrObj;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipeable_fragment);
        getintent=getIntent();
        language=getintent.getStringExtra("language");
        Log.i(TAG, "1");
        initViewPager();
        Log.i(TAG, "2");
        initTabHost();



    }

    private void initTabHost() {
        Log.i(TAG,"3");
        tabHost=(TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        Log.i(TAG, "4");
        String[] tabname=null;
        if (language.equals("English"))
        {
           tabname= new String[]{"Image", "Ocr Text & Key","Analysed Data"};
        }
        else if (language.equals("Arabic"))
        {
           tabname= new String[]{"Image", "Ocr Text & Key", "Translated text","Analysed Data"};
        }

        Log.i(TAG,"language"+language);
        for (int i=0;i<tabname.length;i++)
        {
            Log.i(TAG,"6");
            TabHost.TabSpec tabSpec;
            tabSpec=tabHost.newTabSpec(tabname[i]);
            tabSpec.setIndicator(tabname[i]);
            Log.i(TAG, "6 1");
            tabSpec.setContent(new FakeContent(getApplicationContext()));
            Log.i(TAG, "6 1.2");
            tabHost.addTab(tabSpec);
            Log.i(TAG, "6 2");
        }
        tabHost.setOnTabChangedListener(this);
        Log.i(TAG, "7");
    }

    @Override
    public void onTabChanged(String tabId) {

        int selecteditem= tabHost.getCurrentTab();
        viewPager.setCurrentItem(selecteditem);

        HorizontalScrollView horizontalScrollView= (HorizontalScrollView) findViewById(R.id.horizonatal_scrool);
        View tabview = tabHost.getCurrentTabView();
        int scrollpos= tabview.getLeft()-(horizontalScrollView.getWidth()-tabview.getWidth())/2;
        horizontalScrollView.smoothScrollTo(scrollpos,0);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        Log.wtf("onPageScrolled","called");

        int selecteditem= tabHost.getCurrentTab();

    }

    @Override
    public void onPageSelected(int selecteditem) {
        Log.wtf("here","called");
        tabHost.setCurrentTab(selecteditem);
     /*   if (tabHost.getCurrentTabTag().equals("Analysed Data"))
        {
            TabHost.TabSpec tabSpec=tabHost.getAccessibilityClassName();
            v.invalidate();
        }*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    Log.wtf("onPageScrollStateChanged","called");
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> listFragments =new ArrayList<Fragment>();

        if (language.equals("English"))

        {
            listFragments.add(new ImageDisplayFragment());
            listFragments.add(new OcrTextFragment());
            listFragments.add(new AnalysedData());
        }
        if (language.equals("Arabic"))

        {
            listFragments.add(new ImageDisplayFragment());
            listFragments.add(new Ocrtext_ArabFrag());
            listFragments.add(new TranslatedTextFrag());
           listFragments.add(new AnalysedData());
        }



        Myfragmentpageradapter myfragmentpageradapter= new Myfragmentpageradapter(getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(myfragmentpageradapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void update() {
        FragmentManager manager=getSupportFragmentManager();
        AnalysedData analysis=(AnalysedData)manager.findFragmentByTag("Analysed Data");
        analysis.setwebview();
    }

    public  class FakeContent implements TabHost.TabContentFactory
    {   Context context;
        public FakeContent(Context mcontext){
            context=mcontext;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeView =new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }

}
