package com.example.android.currentcconverter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    static ArrayList<CurrentC> currenciesList = new ArrayList<>();
    static CurrentCAdapter currentCAdapter;
    static int[] positionArr = {-1, -1, -1};
    static int fromFragment;

    public String getAbbreviation(String s) {
        String result = "";
        for (int i = 0; i < 3; i++) result += s.charAt(i);
        return  result;
    }

    public String getName(String s) {
        String result = "";
        for (int i = 4; i < s.length() - 4; ++i)
            result += s.charAt(i);
        return  result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up viewpager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        BasicFragmentPagerAdapter myAdapter = new BasicFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabbar);
        tabLayout.setupWithViewPager(viewPager);

        //Initialize String[] currencies_array
        Resources res = getResources();
        String[] currencies = res.getStringArray(R.array.currencies_array);

        //Initialize TypedArray for flags images
        TypedArray flags = res.obtainTypedArray(R.array.flags);

        //Set up currenciesList for the selectionCountry
        for (int i = 0; i < currencies.length; ++i) {
            currenciesList.
                    add(new CurrentC(getName(currencies[i]),
                            getAbbreviation(currencies[i]),
                            flags.getResourceId(i, -1)));
        }

        //Get back response from CountrySelection after choosing a country
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        /*
        fromFragment == 0: mainCountry in conversion
                     == 1: favCountry in favorites
                     == 2: subCountry in conversion
        */
        fromFragment = intent.getIntExtra("from", -1);

        if (position == -1 || fromFragment == -1) {
            //Show error if it occurred
            Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG);
        }
        else {

            //Returning to the previous tab
            if (fromFragment == 2) {
                viewPager.setCurrentItem(0);
                positionArr[fromFragment] = position;
            }
            else {
                viewPager.setCurrentItem(fromFragment);
                positionArr[fromFragment] = position;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
