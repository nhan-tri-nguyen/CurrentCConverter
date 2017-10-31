package com.example.android.currentcconverter;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import org.w3c.dom.Text;

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class ConversionFragment extends Fragment implements OnClickListener{

    TextView inputTextView;
    TextView outputTextView;
    ImageView mainCountryImageView;
    TextView abbrMainTextView;
    ImageView subCountryImageView;
    TextView abbrSubTextView;
    FloatingActionButton switchFab;

    private void setInfo() {
        //Render after choosing a currency
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion, container, false);
        inputTextView = view.findViewById(R.id.inputTextView);
        outputTextView = view.findViewById(R.id.outputTextView);
        mainCountryImageView = view.findViewById(R.id.mainCoutnryImageView);
        abbrMainTextView =view.findViewById(R.id.abbrMainTextView);
        subCountryImageView = view.findViewById(R.id.subCountryImageView);
        abbrSubTextView = view.findViewById(R.id.abbrSubTextView);
        switchFab = view.findViewById(R.id.switchFab);

        //Set view to OnClickListener
        mainCountryImageView.setOnClickListener(this);
        subCountryImageView.setOnClickListener(this);
        switchFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = MainActivity.positionArr[0];
                MainActivity.positionArr[0] = MainActivity.positionArr[2];
                MainActivity.positionArr[2] = tmp;
                setInfo();
            }
        });

        outputTextView.setText(inputTextView.getText().toString());
        new ConversionGetUrl().execute("http://www.apilayer.net/api/live?access_key=24775a3235b8dde4dddf11ade489004a&format=1");

        setInfo();
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CountrySelection.class);
        if (subCountryImageView == v){
            intent.putExtra("fragment", 2);
        }
        else intent.putExtra("fragment", 0);
        startActivity(intent);
    }

    private class ConversionGetUrl extends GetUrl{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            outputTextView.setText(s);
        }
    }
}
