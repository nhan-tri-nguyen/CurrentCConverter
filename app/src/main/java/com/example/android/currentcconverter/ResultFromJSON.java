package com.example.android.currentcconverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by ngtrnhan1205 on 11/20/17.
 */

public class ResultFromJSON {

    private String mMainAbbr;
    private String mSubAbbr;
    private String mInput;

    public ResultFromJSON(String mainAbbr, String subAbbr, String input) {
        mMainAbbr = mainAbbr;
        mSubAbbr = subAbbr;
        mInput = input;
    }

    public double getNumResult(String s) throws JSONException {
        // Find ratio of main and sub to USD
        String subCurrency = "USD" + mSubAbbr;
        String mainCurrency = "USD" + mMainAbbr;
        JSONObject jsonObject = new JSONObject(s);
        JSONObject currency = jsonObject.getJSONObject("quotes");
        // Calculate ratio sub to main and set to View
        if (mInput.trim().length() == 0) return 0;
        double res = currency.getDouble(subCurrency) / currency.getDouble(mainCurrency);
        res *= Double.parseDouble(mInput);
        return res;

    }

    public String getStrResult(String s) throws JSONException {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        String res = decimalFormat.format(getNumResult(s));
        return res;
    }
}
