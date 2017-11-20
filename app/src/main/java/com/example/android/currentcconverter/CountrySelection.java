package com.example.android.currentcconverter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;


public class CountrySelection extends AppCompatActivity implements CurrentCAdapter.ListItemClickListener{

   private RecyclerView selectionRecyclerView;
   private CurrentCAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);

        selectionRecyclerView = (RecyclerView) findViewById(R.id.selectionRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        selectionRecyclerView.setLayoutManager(layoutManager);

        // Increase performance if child layout has fixed size
        selectionRecyclerView.setHasFixedSize(true);

        // mAdapter is responsible for displaying item
        mAdapter = new CurrentCAdapter(MainActivity.currenciesList,this);
        selectionRecyclerView.setAdapter(mAdapter);

        onNewIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate searchView
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();

        //Get support for searchView for searchable configuration
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        intent.putExtra("fragment", getIntent().getIntExtra("fragment",-1));
        setIntent(intent);
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        mAdapter.getFilter().filter(query);
        //setSelectionView();
    }

    @Override
    public void onListItemClick(int position) {
        // Returning result to the previous fragment;
        Intent intent = getIntent();
        int fromFragment = intent.getIntExtra("fragment", -1);
        if (fromFragment == -1) return;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("from", fromFragment);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
