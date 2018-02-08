package com.example.android.currentcconverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

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
        // Find ratio of main and sub to USD
        String subCurrency = "USD" + mSubAbbr;
        String mainCurrency = "USD" + mMainAbbr;
        JSONObject jsonObject = new JSONObject(s);
        JSONObject currency = jsonObject.getJSONObject("quotes");

        // Calculate ratio sub to main and set to View
        if (mInput.trim().length() == 0) return new BigDecimal("0");
        BigDecimal subRatio = new BigDecimal(currency.getString(subCurrency));
        BigDecimal mainRatio = new BigDecimal(currency.getString(mainCurrency));
        BigDecimal res = subRatio.divide(mainRatio, 2, BigDecimal.ROUND_FLOOR);
        BigDecimal inputAmount = new BigDecimal(mInput);
        res = res.multiply(inputAmount);
        return res;
    }
}
