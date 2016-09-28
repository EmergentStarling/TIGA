package com.example.sigmaway.homeimage.SlideableTabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Family on 08-02-2016.
 */
public class Myfragmentpageradapter extends FragmentPagerAdapter {
    List<Fragment> listFragments;
    public Myfragmentpageradapter(FragmentManager fm, List<Fragment> listFragments) {
        super(fm);
        this.listFragments =listFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position) ;
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }
}
