package com.vividdesigns.tutorialapp.commons;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vividdesigns.tutorialapp.fragments.LocalServiceFragment;
import com.vividdesigns.tutorialapp.fragments.RemoteServiceFragment;

/**
 * Viewpager class showing two fragments for the Local and Remote services
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private String[] fragmentTitles = {"LOCAL", "REMOTE"};


    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment selectedFragment;
        switch (position) {
            case 0:
                selectedFragment = new LocalServiceFragment();
                break;
            case 1:
                selectedFragment = new RemoteServiceFragment();
                break;
            default:
                selectedFragment = new LocalServiceFragment();
        }
        return selectedFragment;
    }

    @Override
    public int getCount() {
        return fragmentTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = fragmentTitles[position];
                break;
            case 1:
                title = fragmentTitles[position];
                break;
        }
        return title;
    }
}
