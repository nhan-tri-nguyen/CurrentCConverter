package com.example.android.currentcconverter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

        //Send info back to the previous fragment when list item is pressed
        selectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = getIntent();
                int fromFragment = intent.getIntExtra("fragment", -1);
                Log.i("anything", String.valueOf(fromFragment));
                if (fromFragment == -1) return;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("from", fromFragment);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}
