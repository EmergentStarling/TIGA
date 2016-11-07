package com.example.sigmaway.homeimage.SlideableTabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.Communicator;
import com.example.sigmaway.homeimage.CustomClasses.Ocr;
import com.example.sigmaway.homeimage.MainActivities.DirectDocument;
import com.example.sigmaway.homeimage.MainActivities.HomeScreen;
import com.example.sigmaway.homeimage.MainActivities.NavigationBarActivity;
import com.example.sigmaway.homeimage.R;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends NavigationBarActivity implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener,Communicator {
    public ViewPager viewPager;
    TabHost tabHost;
    String TAG= "in main";

    FragmentManager obj;
    Intent getintent;
    String language;
    Ocr OcrObj;
    Myfragmentpageradapter myfragmentpageradapter;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);
        super.onCreateDrawer(savedInstanceState);
        /*if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Cannot connect to OpenCV Manager");
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View yourListView = findViewById(R.id.content_navigation_bar);
        ViewGroup parent = (ViewGroup) yourListView.getParent();
        parent.removeView(yourListView);
        View view=getLayoutInflater().inflate(R.layout.swipeable_fragment,parent,false);
        parent.addView(view);
        setTitle("");
        getintent=getIntent();
        language=getintent.getStringExtra("language");
        Log.i(TAG, "1");
        initViewPager();
        Log.i(TAG, "2");
        initTabHost();
    }
    public void analysedchanger(){
        FragmentManager manager=getSupportFragmentManager();
        AnalysedData analysis=(AnalysedData)manager.findFragmentByTag("Analysed Data");
        analysis.setwebview();
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
/*public void changepage()
{
   Log.w("changepage", ""+viewPager);
    viewPager.setCurrentItem(2);
}*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        Log.wtf("onPageScrolled","called");
    }

    @Override
    public void onPageSelected(int selecteditem) {
        Log.wtf("here","called");
        tabHost.setCurrentTab(selecteditem);
        if (tabHost.getCurrentTabTag().equals("Analysed Data"))
        { Log.w("main page analysed frag",obj.toString());
            Log.w("tag for refresh","android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
            AnalysedData analysis=(AnalysedData)obj.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
            Log.w("main page analysed frag",analysis.toString());
            analysis.setwebview();
        }
        if (tabHost.getCurrentTabTag().equals("Translated text"))
        { Log.w("main page analysed frag",obj.toString());
            Log.w("tag for refresh","android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
            TranslatedTextFrag translatedtext=(TranslatedTextFrag)obj.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
            Log.w("main page analysed frag",translatedtext.toString());
           translatedtext.settextview();
        }
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


        obj=getSupportFragmentManager();
        myfragmentpageradapter= new Myfragmentpageradapter(getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(myfragmentpageradapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void update() {
       // FragmentManager manager=getSupportFragmentManager();
        AnalysedData analysis=(AnalysedData)getSupportFragmentManager().findFragmentByTag("Analysed Data");
        analysis.setwebview();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.w("main page","on save instance");
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainPage.this,HomeScreen.class));
    }
}
