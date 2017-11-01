package com.example.android.currentcconverter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class FavoritesFragment extends Fragment implements OnClickListener {

    ImageView favCurrencyImageView;
    TextView countryNameTextView;
    TextView abbrCurrencyTextView;
    FloatingActionButton addFab;

    private void setView() {
        //Render after choosing a currency
        int myPosition = MainActivity.positionArr[1];
        if (myPosition != -1) {
            CurrentC myCurrency = MainActivity.currenciesList.get(myPosition);
            favCurrencyImageView.setImageResource(myCurrency.getFlagResourcesId());
            countryNameTextView.setText(myCurrency.getCurrentCName());
            abbrCurrencyTextView.setText(myCurrency.getCurrentCAbbreviations());
        }
    }

    private void findView(View view) {
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
        findView(view);
        setViewOnClickListener();
        setView();
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CountrySelection.class);
        intent.putExtra("fragment", 1);
        startActivity(intent);
    }
}
