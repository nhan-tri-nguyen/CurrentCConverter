package com.example.android.currentcconverter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
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

    public BigDecimal getNumResult(String s) throws JSONException {
        final int mScale = 12;
        final int roundingStrategy = BigDecimal.ROUND_FLOOR;
        final String baseCurrency = "USD";
        // Find ratio of main and sub to USD
        String subCurrency = baseCurrency + mSubAbbr;
        String mainCurrency = baseCurrency + mMainAbbr;
        JSONObject jsonObject = new JSONObject(s);
        JSONObject currency = jsonObject.getJSONObject("quotes");

        // Calculate ratio sub to main and set to View
        if (mInput.trim().length() == 0) return new BigDecimal("0");
        BigDecimal subRatio = new BigDecimal(currency.getString(subCurrency)).setScale(mScale, roundingStrategy);
        BigDecimal mainRatio = new BigDecimal(currency.getString(mainCurrency)).setScale(mScale, roundingStrategy);
        BigDecimal res = subRatio.divide(mainRatio, mScale, roundingStrategy);
        BigDecimal inputAmount = new BigDecimal(mInput);
        res = res.multiply(inputAmount);
        res = new BigDecimal(new DecimalFormat("#.##").format(res));
        return res;
    }
}
