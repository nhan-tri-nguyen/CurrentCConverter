package com.promise.android.currentcconverter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences sharedPreferences;
    static ArrayList<CurrentC> currenciesList = new ArrayList<>();
    // Array of the positions of all currencies
    static int[] positionArr = new int[4];
    // Keep track of the pos of currencies
    // when filtering currencies in CountrySelection
    static int[] filteredPosArr = new int[200];
    public static final String ACCESS_KEY = "060cdd5f28bcadbeea155864b0bb2501";
    public static final String BASE_URL = "http://apilayer.net/api/";
    public static final String ENDPOINT = "live";
    public static final String ERROR_MESSAGE = "Please check your internet connection!";

    public String getAbbreviation(String s) {
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < 3; i++) {
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    public String getName(String s) {
        StringBuilder result = new StringBuilder("");
        for (int i = 4; i < s.length() - 4; ++i) {
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    private void initializeDatabase() {
        positionArr[0] = sharedPreferences.getInt("main",16);
        positionArr[2] = sharedPreferences.getInt("sub",16);
        positionArr[1] = sharedPreferences.getInt("favorites",16);
        positionArr[3] = -1;
    }

    private void setInfo(ViewPager viewPager) {
        //Get back response from CountrySelection after choosing a country
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        /*
        fromFragment == 0: mainCountry in conversion
                     == 1: favCountry in favorites
                     == 2: subCountry in conversion
                     == 3: addFab in favorites
        */
        int fromFragment = intent.getIntExtra("from", -1);

        if (position != -1 && fromFragment != -1)  {
            //Returning to the previous tab
            if (fromFragment == 3){
                viewPager.setCurrentItem(1);
                positionArr[fromFragment] = filteredPosArr[position];
            } else if (fromFragment == 2) {
                viewPager.setCurrentItem(0);
                positionArr[fromFragment] = filteredPosArr[position];
                sharedPreferences.edit().putInt("sub",positionArr[fromFragment]).apply();
            } else {
                viewPager.setCurrentItem(fromFragment);
                positionArr[fromFragment] = filteredPosArr[position];
                if (fromFragment == 0) {
                    sharedPreferences.edit().putInt("main",positionArr[fromFragment]).apply();
                } else {
                    sharedPreferences.edit().putInt("favorites",positionArr[fromFragment]).apply();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        sharedPreferences = this.getSharedPreferences("com.example.android.currentcconverter", Context.MODE_PRIVATE);
        //Set up viewpager
        ViewPager viewPager = findViewById(R.id.viewpager);
        BasicFragmentPagerAdapter myAdapter = new BasicFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.tabbar);
        tabLayout.setupWithViewPager(viewPager);

        //Initialize String[] currencies_array
        Resources res = getResources();
        String[] currencies = res.getStringArray(R.array.currencies_array);

        //Initialize TypedArray for flags images
        TypedArray flags = res.obtainTypedArray(R.array.flags);

        //Set up currenciesList for the selectionCountry
        if (currenciesList.size() == 0) {
            for (int i = 0; i < currencies.length; ++i) {
                currenciesList.
                        add(new CurrentC(getName(currencies[i]),
                                getAbbreviation(currencies[i]),
                                flags.getResourceId(i, -1)));
            }
        }
        flags.recycle();
        initializeDatabase();
        setInfo(viewPager);
        updateJSON();
    }

    private void updateJSON() {
        new GetUrl().execute(BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


}
