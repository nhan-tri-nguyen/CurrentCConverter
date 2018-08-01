package com.promise.android.currentcconverter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import static com.promise.android.currentcconverter.Constants.SENTINEL;
import static com.promise.android.currentcconverter.MainActivity.currenciesList;


public class CountrySelection extends AppCompatActivity implements CurrentCAdapter.ListItemClickListener {

    private CurrentCAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView selectionRecyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);

        selectionRecyclerView = findViewById(R.id.selectionRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        selectionRecyclerView.setLayoutManager(layoutManager);

        // Increase performance if child layout has fixed size
        selectionRecyclerView.setHasFixedSize(true);

        // mAdapter is responsible for displaying item
        mAdapter = new CurrentCAdapter(currenciesList, this, getApplicationContext());
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
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        intent.putExtra("fragment", getIntent().getIntExtra("fragment", SENTINEL));
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
        int fromFragment = intent.getIntExtra("fragment", SENTINEL);
        if (fromFragment == SENTINEL) return;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("from", fromFragment);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
