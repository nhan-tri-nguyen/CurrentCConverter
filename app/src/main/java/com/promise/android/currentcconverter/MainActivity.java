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
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;


import java.util.ArrayList;

import static com.promise.android.currentcconverter.Constants.ACCESS_KEY;
import static com.promise.android.currentcconverter.Constants.ADD_FAVORITES;
import static com.promise.android.currentcconverter.Constants.BASE_URL;
import static com.promise.android.currentcconverter.Constants.CONVERSION_FRAGMENT;
import static com.promise.android.currentcconverter.Constants.ENDPOINT;
import static com.promise.android.currentcconverter.Constants.FAVORITES_FRAGMENT;
import static com.promise.android.currentcconverter.Constants.MAIN_CURRENCY_CONVERSION;
import static com.promise.android.currentcconverter.Constants.MAIN_CURRENCY_FAVORITES;
import static com.promise.android.currentcconverter.Constants.SENTINEL;
import static com.promise.android.currentcconverter.Constants.SUB_CURRENCY_CONVERSION;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences sharedPreferences;
    static ArrayList<CurrentC> currenciesList = new ArrayList<>();
    // Array of the positions of all currencies
    static int[] positionArr = new int[4];
    // Keep track of the pos of currencies
    // when filtering currencies in CountrySelection
    static int[] filteredPosArr = new int[200];
    private final int NUMBER_CHARACTERS_OF_ABBR = 3;
    //  Default currency is CAD
    private final int DEFAULT_CURRENCY = 16;

    //  Get the first 3 characters
    public String getAbbreviation(String s) {
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < NUMBER_CHARACTERS_OF_ABBR; i++) {
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    //  Get the rest of the string
    public String getName(String s) {
        StringBuilder result = new StringBuilder("");
        for (int i = NUMBER_CHARACTERS_OF_ABBR + 1; i < s.length() - NUMBER_CHARACTERS_OF_ABBR - 1; ++i) {
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    private void initializeDatabase() {
        positionArr[MAIN_CURRENCY_CONVERSION] = sharedPreferences.getInt("main",DEFAULT_CURRENCY);
        positionArr[SUB_CURRENCY_CONVERSION] = sharedPreferences.getInt("sub",DEFAULT_CURRENCY);
        positionArr[MAIN_CURRENCY_FAVORITES] = sharedPreferences.getInt("favorites",DEFAULT_CURRENCY);
        positionArr[ADD_FAVORITES] = SENTINEL;
    }

    private void setInfo(ViewPager viewPager) {
        //  Get back response from CountrySelection after choosing a country
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", SENTINEL);
        /*
        fromFragment == 0: mainCountry in conversion
                     == 1: favCountry in favorites
                     == 2: subCountry in conversion
                     == 3: addFab in favorites
        */
        int fromFragment = intent.getIntExtra("from", SENTINEL);

        if (position != SENTINEL && fromFragment != SENTINEL)  {
            //Returning to the previous tab
            switch (fromFragment) {
                case ADD_FAVORITES: {
                    viewPager.setCurrentItem(FAVORITES_FRAGMENT);
                    positionArr[fromFragment] = filteredPosArr[position];
                    break;
                }

                case SUB_CURRENCY_CONVERSION: {
                    viewPager.setCurrentItem(CONVERSION_FRAGMENT);
                    positionArr[fromFragment] = filteredPosArr[position];
                    sharedPreferences.edit().putInt("sub",positionArr[fromFragment]).apply();
                    break;
                }

                case MAIN_CURRENCY_CONVERSION: {
                    viewPager.setCurrentItem(CONVERSION_FRAGMENT);
                    positionArr[fromFragment] = filteredPosArr[position];
                    sharedPreferences.edit().putInt("main",positionArr[fromFragment]).apply();
                    break;
                }

                case MAIN_CURRENCY_FAVORITES: {
                    viewPager.setCurrentItem(FAVORITES_FRAGMENT);
                    positionArr[fromFragment] = filteredPosArr[position];
                    sharedPreferences.edit().putInt("favorites",positionArr[fromFragment]).apply();
                    break;
                }

                default: {
                    viewPager.setCurrentItem(CONVERSION_FRAGMENT);
                    Log.e("fromFragment not found:", String.valueOf(fromFragment));
                    break;
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
                                flags.getResourceId(i, SENTINEL)));
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
