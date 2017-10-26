package com.example.android.currentcconverter;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ngtrnhan1205 on 10/21/17.
 */

public class ConversionFragment extends Fragment {

    TextView inputTextView;
    TextView outputTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion, container, false);
        inputTextView = (TextView) view.findViewById(R.id.inputTextView);
        outputTextView = (TextView) view.findViewById(R.id.outputTextView);
        //Log.i("another things", String.valueOf(inputTextView));
        outputTextView.setText(inputTextView.getText().toString());
        new ConversionGetUrl().execute("http://www.apilayer.net/api/live?access_key=24775a3235b8dde4dddf11ade489004a&format=1");
        return view;
    }

    private class ConversionGetUrl extends GetUrl{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            outputTextView.setText(s);
        }
    }
}
