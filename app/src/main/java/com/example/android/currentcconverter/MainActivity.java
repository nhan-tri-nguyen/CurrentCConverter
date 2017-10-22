package com.example.android.currentcconverter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set uup viewpager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        BasicFragmentPagerAdapter myAdapter = new BasicFragmentPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(myAdapter);
    }
}
