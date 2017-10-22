package com.example.android.currentcconverter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class BasicFragmentPagerAdapter extends FragmentPagerAdapter {

    public BasicFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position== 0) {
            return new FavoritesFragment();
        }
        else {
            return new ConversionFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
