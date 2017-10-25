package com.example.android.currentcconverter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

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
    }

    class GetURL extends AsyncTask<String, Void, String> {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        @Override
        protected String doInBackground(String... urls) {
            Request request = new Request.Builder().url(urls[0]).build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == "") {
                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }
}
