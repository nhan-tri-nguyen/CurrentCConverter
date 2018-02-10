package com.example.android.currentcconverter;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.currentcconverter.data.FavCurrentCContract;
import com.example.android.currentcconverter.data.FavCurrentCDbHelper;
import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class FavoritesFragment extends Fragment implements FavCurrentCAdapter.ListenerInterface {

    private ImageView favCurrencyImageView;
    private TextView countryNameTextView;
    private TextView abbrCurrencyTextView;
    private FloatingActionButton addFab;
    private EditText amountEditText;
    private FavCurrentCAdapter mAdapter;

    private void Rendering() {
        // Render previous amount
        amountEditText.setText(MainActivity.sharedPreferences.getString("favoriteAmount",""));
        // Render for FavCurrency
        int myPosition = MainActivity.positionArr[1];
        if (myPosition != -1) {
            CurrentC myCurrency = MainActivity.currenciesList.get(myPosition);
            favCurrencyImageView.setImageResource(myCurrency.getFlagResourcesId());
            countryNameTextView.setText(myCurrency.getCurrentCName());
            abbrCurrencyTextView.setText(myCurrency.getCurrentCAbbreviations());
            updateInputChange();
        }
        favListRender();
    }

    private void favListRender() {
        // Render for FavList
        String jsonString = MainActivity.sharedPreferences.getString("json","");
        int myPos = MainActivity.positionArr[3];
        if (myPos != -1 && !jsonString.equals("")) {
            CurrentC currentC = MainActivity.currenciesList.get(myPos);
            // Get result from JSON
            String subAbbr = currentC.getCurrentCAbbreviations();
            ResultFromJSON res = new ResultFromJSON(abbrCurrencyTextView.getText().toString(),
                    subAbbr, amountEditText.getText().toString());
            BigDecimal amount = new BigDecimal("0");
            try {
                amount = res.getNumResult(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Pass result into database
            addFavCurrentC(subAbbr, currentC.getFlagResourcesId(), amount);
            mAdapter.swapCursor(getFavCurrentC());
        } else if (jsonString.equals("")) {
            Toast.makeText(getActivity(), MainActivity.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }

    private void updateInputChange() {
        String jsonString = MainActivity.sharedPreferences.getString("json","");
        if (jsonString.equals("")) return;
        ResultFromJSON res;
        Cursor cursor = getFavCurrentC();
        // Iterate through the database
        while (cursor.moveToNext()) {
            String subAbbr = cursor.getString(cursor.
                    getColumnIndex(FavCurrentCContract.FavCurrentCEntry.COLUMN_ABBR));
            long id = cursor.getLong(cursor.
                    getColumnIndex(FavCurrentCContract.FavCurrentCEntry._ID));
            res = new ResultFromJSON(abbrCurrencyTextView.getText().toString(),
                    subAbbr, amountEditText.getText().toString());
            BigDecimal amount = new BigDecimal("0");
            try {
                amount = res.getNumResult(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!updateFavCurrentC(amount, id)) {
                Toast.makeText(getActivity(), MainActivity.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
            }
        }
        mAdapter.swapCursor(getFavCurrentC());
    }

    private boolean updateFavCurrentC(BigDecimal amount, long id) {
        // Update the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavCurrentCContract.FavCurrentCEntry.COLUMN_AMOUNT, amount.toString());
        //noinspection ConstantConditions
        return getActivity().getContentResolver()
                .update(FavCurrentCContract.FavCurrentCEntry.CONTENT_URI,
                contentValues, FavCurrentCContract.FavCurrentCEntry._ID + "=" + id,
                null) > 0;
    }

    private void setAllViews(View view) {
        RecyclerView favRecyclerView;
        // Set view for Recycler View
        favRecyclerView = view.findViewById(R.id.favRecyclerView);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Connect to the database
        FavCurrentCDbHelper dbHelper = new FavCurrentCDbHelper(getActivity());
        Cursor cursor = getFavCurrentC();
        mAdapter = new FavCurrentCAdapter(getActivity(), cursor, this);
        favRecyclerView.setAdapter(mAdapter);

        // Set view for other views
        favCurrencyImageView = view.findViewById(R.id.favCurrencyImageView);
        countryNameTextView = view.findViewById(R.id.countryNameTextView);
        abbrCurrencyTextView = view.findViewById(R.id.abbrCurrencyTextView);
        amountEditText = view.findViewById(R.id.amountEditText);
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
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.sharedPreferences.edit().putString("favoriteAmount", amountEditText.getText().toString()).apply();
                updateInputChange();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        setAllViews(view);
        setViewOnClickListener();
        // Render after adding a country
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
        String[] projection = {
                FavCurrentCContract.FavCurrentCEntry._ID,
                FavCurrentCContract.FavCurrentCEntry.COLUMN_AMOUNT,
                FavCurrentCContract.FavCurrentCEntry.COLUMN_IMG_RES_ID,
                FavCurrentCContract.FavCurrentCEntry.COLUMN_ABBR
        };
        //noinspection ConstantConditions
        return getActivity().getContentResolver().query(FavCurrentCContract.FavCurrentCEntry.CONTENT_URI,
                projection, null, null, null);
    }

    private void addFavCurrentC(String abbr, int imgId, BigDecimal amount) {

        // Add info to content values and insert into database
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavCurrentCContract.FavCurrentCEntry.COLUMN_ABBR, abbr);
        contentValues.put(FavCurrentCContract.FavCurrentCEntry.COLUMN_IMG_RES_ID, imgId);
        contentValues.put(FavCurrentCContract.FavCurrentCEntry.COLUMN_AMOUNT, amount.toString());

        //noinspection ConstantConditions
        getActivity().getContentResolver()
                .insert(FavCurrentCContract.FavCurrentCEntry.CONTENT_URI,contentValues);
    }

    private boolean removeFavCurrentC(long id) {
        //noinspection ConstantConditions
        return getActivity().getContentResolver()
                .delete(FavCurrentCContract.FavCurrentCEntry.CONTENT_URI,
                FavCurrentCContract.FavCurrentCEntry._ID + "=" + id,
                null) > 0;
    }

    @Override
    public void onListItemLongClick(final View view) {
        //noinspection ConstantConditions
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("You are going to delete this item")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (removeFavCurrentC((long) view.getTag())) {
                            mAdapter.swapCursor(getFavCurrentC());
                        } else {
                            Toast.makeText(getActivity(), "Error! Item can not be deleted!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}