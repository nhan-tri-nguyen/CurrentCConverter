package com.example.android.currentcconverter;

import android.content.Intent;
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

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class ConversionFragment extends Fragment implements OnClickListener{

    TextView inputTextView;
    TextView outputTextView;
    ImageView mainCountryImageView;
    ImageView subCountryImageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion, container, false);
        inputTextView = view.findViewById(R.id.inputTextView);
        outputTextView = view.findViewById(R.id.outputTextView);
        mainCountryImageView = view.findViewById(R.id.mainCoutnryImageView);
        subCountryImageView = view.findViewById(R.id.subCountryImageView);
        mainCountryImageView.setOnClickListener(this);
        subCountryImageView.setOnClickListener(this);
        outputTextView.setText(inputTextView.getText().toString());
        new ConversionGetUrl().execute("http://www.apilayer.net/api/live?access_key=24775a3235b8dde4dddf11ade489004a&format=1");
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
