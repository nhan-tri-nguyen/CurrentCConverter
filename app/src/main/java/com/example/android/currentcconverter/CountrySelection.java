package com.example.android.currentcconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class CountrySelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);

        //Set up selectionListView
        ListView selectionListView = (ListView) findViewById(R.id.selectionListView);
        MainActivity.currentCAdapter = new CurrentCAdapter(this, MainActivity.currenciesList);
        selectionListView.setAdapter(MainActivity.currentCAdapter);
    }
}
