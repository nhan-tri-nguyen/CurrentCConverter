package com.example.android.currentcconverter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class BasicFragmentPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public BasicFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ConversionFragment();
            case 1:
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
        switch (position){
            case 0:
                return mContext.getString(R.string.fragment_conversion).toUpperCase(Locale.getDefault());
            case 1:
                return mContext.getString(R.string.fragment_favorite).toUpperCase(Locale.getDefault());
        }
        return null;
    }
}
