package com.promise.android.currentcconverter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class BasicFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public BasicFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.CONVERSION_FRAGMENT:
                return new ConversionFragment();
            case Constants.FAVORITES_FRAGMENT:
                return new FavoritesFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        //Generate title
        switch (position) {
            case Constants.CONVERSION_FRAGMENT:
                return mContext.getString(R.string.fragment_conversion).toUpperCase(Locale.getDefault());
            case Constants.FAVORITES_FRAGMENT:
                return mContext.getString(R.string.fragment_favorite).toUpperCase(Locale.getDefault());
        }
        return null;
    }
}
