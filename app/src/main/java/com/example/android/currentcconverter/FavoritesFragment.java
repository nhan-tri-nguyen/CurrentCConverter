package com.example.android.currentcconverter;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.currentcconverter.data.FavCurrentCContract;
import com.example.android.currentcconverter.data.FavCurrentCDbHelper;

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class FavoritesFragment extends Fragment implements FavCurrentCAdapter.ListenerInterface {

    private ImageView favCurrencyImageView;
    private TextView countryNameTextView;
    private TextView abbrCurrencyTextView;
    private FloatingActionButton addFab;
    private SQLiteDatabase mDb;
    private FavCurrentCAdapter mAdapter;
    private RecyclerView favRecyclerView;

    private void Rendering() {
        //Render for FavCur
        int myPosition = MainActivity.positionArr[1];
        if (myPosition != -1) {
            CurrentC myCurrency = MainActivity.currenciesList.get(myPosition);
            favCurrencyImageView.setImageResource(myCurrency.getFlagResourcesId());
            countryNameTextView.setText(myCurrency.getCurrentCName());
            abbrCurrencyTextView.setText(myCurrency.getCurrentCAbbreviations());
        }
        // Render for FavList
        myPosition = MainActivity.positionArr[3];
        if (myPosition != -1) {
            CurrentC currentC = MainActivity.currenciesList.get(myPosition);
            addFavCurrentC(currentC.getCurrentCAbbreviations(), currentC.getFlagResourcesId(),100);
            mAdapter.swapCursor(getFavCurrentC());
        }
    }

    private void setAllViews(View view) {
        // Set view for Recycler View
        favRecyclerView = view.findViewById(R.id.favRecyclerView);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Connect to the database
        FavCurrentCDbHelper dbHelper = new FavCurrentCDbHelper(getActivity());
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getFavCurrentC();
        mAdapter = new FavCurrentCAdapter(getActivity(), cursor, this);
        favRecyclerView.setAdapter(mAdapter);

        // Set view for other views
        favCurrencyImageView = view.findViewById(R.id.favCurrencyImageView);
        countryNameTextView = view.findViewById(R.id.countryNameTextView);
        abbrCurrencyTextView = view.findViewById(R.id.abbrCurrencyTextView);
        addFab = view.findViewById(R.id.addFab);
    }

    private void setViewOnClickListener() {
        favCurrencyImageView.setOnClickListener(this);
        addFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CountrySelection.class);
                intent.putExtra("fragment", 3);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        setAllViews(view);
        setViewOnClickListener();
        // Render after choosing a country
        Rendering();
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CountrySelection.class);
        intent.putExtra("fragment", 1);
        startActivity(intent);
    }

    private Cursor getFavCurrentC() {
        return mDb.query(
                FavCurrentCContract.FavCurrentCEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /*
    public void setSwipe() {
        // Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long id = (long) viewHolder.itemView.getTag();
                removeFavCurrentC(id);
                mAdapter.swapCursor(getFavCurrentC());
            }
        }).attachToRecyclerView(favRecyclerView);
    }
    */


    private long addFavCurrentC(String abbr, int imgId, int amount) {

        // Add info to content values and insert into database
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavCurrentCContract.FavCurrentCEntry.COLUMN_ABBR, abbr);
        contentValues.put(FavCurrentCContract.FavCurrentCEntry.COLUMN_IMG_RES_ID, imgId);
        contentValues.put(FavCurrentCContract.FavCurrentCEntry.COLUMN_AMOUNT, amount);

        return mDb.insert(FavCurrentCContract.FavCurrentCEntry.TABLE_NAME,
                null, contentValues);
    }

    private boolean removeFavCurrentC(long id) {
        return mDb.delete(FavCurrentCContract.FavCurrentCEntry.TABLE_NAME,
                FavCurrentCContract.FavCurrentCEntry._ID + "=" + id,
                null) > 0;
    }

    @Override
    public void onListItemLongClick(final View view) {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("You are going to delete this item")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeFavCurrentC((long) view.getTag());
                        mAdapter.swapCursor(getFavCurrentC());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
