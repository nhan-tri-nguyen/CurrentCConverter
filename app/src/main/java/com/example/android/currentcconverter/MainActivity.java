package com.example.android.currentcconverter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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

        //Initialize string currencies_array
        Resources res = getResources();
        String[] currencies = res.getStringArray(R.array.currencies_array);

        //Set up currenciesList
        currenciesList.add(new CurrentC(getName(currencies[0]), getAbbreviation(currencies[0]), R.drawable.afghanistan));
    }


}
