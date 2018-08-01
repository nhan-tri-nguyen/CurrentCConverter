package com.promise.android.currentcconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.math.BigDecimal;

import static com.promise.android.currentcconverter.Constants.ERROR_MESSAGE;
import static com.promise.android.currentcconverter.Constants.MAIN_CURRENCY_CONVERSION;
import static com.promise.android.currentcconverter.Constants.SENTINEL;
import static com.promise.android.currentcconverter.Constants.SUB_CURRENCY_CONVERSION;


/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class ConversionFragment extends Fragment implements OnClickListener {

    private EditText inputEditText;
    private TextView outputTextView;
    private ImageView mainCountryImageView;
    private TextView abbrMainTextView;
    private ImageView subCountryImageView;
    private TextView abbrSubTextView;
    private GridLayout keyboard;
    private FloatingActionButton switchFab;

    private void updateCurrencyChange() {
        // Render after choosing a currency or swap
        int mainCurrencyPos = MainActivity.positionArr[MAIN_CURRENCY_CONVERSION];

        if (mainCurrencyPos != SENTINEL) {

            CurrentC mainCurrency = MainActivity.currenciesList.get(mainCurrencyPos);

            mainCountryImageView.setImageResource(mainCurrency.getFlagResourcesId());

            abbrMainTextView.setText(mainCurrency.getCurrentCAbbreviations());
        }

        int subCurrencyPos = MainActivity.positionArr[SUB_CURRENCY_CONVERSION];

        if (subCurrencyPos != SENTINEL) {

            CurrentC subCurrency = MainActivity.currenciesList.get(subCurrencyPos);

            subCountryImageView.setImageResource(subCurrency.getFlagResourcesId());

            abbrSubTextView.setText(subCurrency.getCurrentCAbbreviations());
        }

        inputEditText.setText(MainActivity.sharedPreferences.getString("conversionAmount", "0"));
    }

    private void updateInputChange() {

        // Render for new amount
        String jsonString = MainActivity.sharedPreferences.getString("json", "");

        if (!jsonString.equals("")) {

            ResultFromJSON res = new ResultFromJSON(
                    abbrMainTextView.getText().toString(),
                    abbrSubTextView.getText().toString(),
                    inputEditText.getText().toString()
            );

            BigDecimal finalAnswer = new BigDecimal("0");

            try {
                finalAnswer = res.getNumResult(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            outputTextView.setText(finalAnswer.toString());
        } else {

            Toast.makeText(getContext(), ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }

    private void findView(View view) {

        inputEditText = view.findViewById(R.id.inputEditText);

        outputTextView = view.findViewById(R.id.outputTextView);

        mainCountryImageView = view.findViewById(R.id.mainCoutnryImageView);

        abbrMainTextView = view.findViewById(R.id.abbrMainTextView);

        subCountryImageView = view.findViewById(R.id.subCountryImageView);

        abbrSubTextView = view.findViewById(R.id.abbrSubTextView);

        switchFab = view.findViewById(R.id.switchFab);

        keyboard = view.findViewById(R.id.keyboard);
    }

    private void swap(int first, int second) {

        int tmp = first;

        first = second;

        second = tmp;
    }

    private void setViewOnClickListener() {
        // Set view to OnClickListener
        mainCountryImageView.setOnClickListener(this);
        subCountryImageView.setOnClickListener(this);

        switchFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                swap(MainActivity.positionArr[MAIN_CURRENCY_CONVERSION], MainActivity.positionArr[SUB_CURRENCY_CONVERSION]);

                MainActivity.sharedPreferences.edit().putInt(
                        "main",
                        MainActivity.positionArr[MAIN_CURRENCY_CONVERSION]
                ).apply();

                MainActivity.sharedPreferences.edit().putInt(
                        "sub",
                        MainActivity.positionArr[SUB_CURRENCY_CONVERSION]
                ).apply();

                updateCurrencyChange();

                updateInputChange();
            }
        });

        // Set clickListener to keyboard
        int childCount = keyboard.getChildCount();

        for (int i = 0; i < childCount; ++i) {

            TextView button = (TextView) keyboard.getChildAt(i);

            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tmp = view.getTag().toString();

                    String current = inputEditText.getText().toString();

                    if (tmp.equals("del") || current.length() == 0) {

                        if (current.length() == 1) {
                            inputEditText.setText("0");
                        } else {
                            inputEditText.setText(current.substring(0, current.length() - 1));
                        }

                    } else if (!current.contains(".") || !tmp.equals(".")) {

                        if (current.equals("0") && !tmp.equals(".")) {
                            inputEditText.setText(tmp);
                        } else {
                            inputEditText.append(tmp);
                        }

                    } else {

                        Toast.makeText(getContext(), "A number can not contain two decimal separators", Toast.LENGTH_LONG).show();
                        return;
                    }

                    MainActivity.sharedPreferences.edit().putString(
                            "conversionAmount", inputEditText.getText().toString()
                    ).apply();

                    updateInputChange();
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion, container, false);

        findView(view);

        setViewOnClickListener();

        updateCurrencyChange();

        updateInputChange();

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CountrySelection.class);

        if (subCountryImageView == v) {
            intent.putExtra("fragment", SUB_CURRENCY_CONVERSION);
        } else {
            intent.putExtra("fragment", MAIN_CURRENCY_CONVERSION);
        }

        startActivity(intent);
    }
}
