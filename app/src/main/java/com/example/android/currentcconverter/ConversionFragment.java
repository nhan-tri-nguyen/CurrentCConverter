package com.example.android.currentcconverter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.json.JSONException;

import java.math.BigDecimal;


/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class ConversionFragment extends Fragment implements OnClickListener{

    private TextView inputTextView;
    private TextView outputTextView;
    private ImageView mainCountryImageView;
    private TextView abbrMainTextView;
    private ImageView subCountryImageView;
    private TextView abbrSubTextView;
    private FloatingActionButton switchFab;

    private void setInfo() {
        // Render after choosing a currency or swap
        if (MainActivity.positionArr[0] != -1) {
            CurrentC myCurrency1 = MainActivity.currenciesList.get(MainActivity.positionArr[0]);
            mainCountryImageView.setImageResource(myCurrency1.getFlagResourcesId());
            abbrMainTextView.setText(myCurrency1.getCurrentCAbbreviations());
        }
        if (MainActivity.positionArr[2] != -1) {
            CurrentC myCurrency2 = MainActivity.currenciesList.get(MainActivity.positionArr[2]);
            subCountryImageView.setImageResource(myCurrency2.getFlagResourcesId());
            abbrSubTextView.setText(myCurrency2.getCurrentCAbbreviations());
        }

        // Render for new amount
        String jsonString = MainActivity.sharedPreferences.getString("json", "");
        if (!jsonString.equals("")) {
            ResultFromJSON res = new ResultFromJSON(abbrMainTextView.getText().toString(),
                    abbrSubTextView.getText().toString(),
                    inputTextView.getText().toString());
            BigDecimal finalAnswer = new BigDecimal("0");
            Log.i("number", finalAnswer.toString());
            try {
                finalAnswer = res.getNumResult(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("number", finalAnswer.toString());
            outputTextView.setText(finalAnswer.toString());
        } else {
            Toast.makeText(getContext(), MainActivity.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }

    private void findView (View view) {
        inputTextView = view.findViewById(R.id.inputTextView);
        outputTextView = view.findViewById(R.id.outputTextView);
        mainCountryImageView = view.findViewById(R.id.mainCoutnryImageView);
        abbrMainTextView =view.findViewById(R.id.abbrMainTextView);
        subCountryImageView = view.findViewById(R.id.subCountryImageView);
        abbrSubTextView = view.findViewById(R.id.abbrSubTextView);
        switchFab = view.findViewById(R.id.switchFab);
    }

    private void setViewOnClickListener() {
        // Set view to OnClickListener
        mainCountryImageView.setOnClickListener(this);
        subCountryImageView.setOnClickListener(this);
        switchFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = MainActivity.positionArr[0];
                MainActivity.positionArr[0] = MainActivity.positionArr[2];
                MainActivity.positionArr[2] = tmp;
                MainActivity.sharedPreferences.edit().putInt("main",MainActivity.positionArr[0]).apply();
                MainActivity.sharedPreferences.edit().putInt("sub",MainActivity.positionArr[2]).apply();
                setInfo();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion, container, false);
        findView(view);
        setViewOnClickListener();
        setInfo();
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CountrySelection.class);
        if (subCountryImageView == v){
            intent.putExtra("fragment", 2);
        } else intent.putExtra("fragment", 0);
        startActivity(intent);
    }
}
