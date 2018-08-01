package com.promise.android.currentcconverter;

/**
 * Created by ngtrnhan1205 on 10/25/17.
 */

public class CurrentC {

    //Represent the name of the currency such as Canadian Dollar
    private String currentCName;
    //Represent the currency abbreviation such as CAD
    private String currentCAbbreviations;
    //Contain the resource id of the image
    private int flagResourcesId;

    //Set up the CurrentC object
    public CurrentC(String mCurrentCName, String mCurrentCAbbreviations, int mFlagResourcesId) {

        currentCName = mCurrentCName;

        currentCAbbreviations = mCurrentCAbbreviations;

        flagResourcesId = mFlagResourcesId;

    }

    //Get currency's name
    public String getCurrentCName() {
        return currentCName;
    }

    //Get currency's abbreviation
    public String getCurrentCAbbreviations() {
        return currentCAbbreviations;
    }

    //Get flag's image resources id
    public int getFlagResourcesId() {
        return flagResourcesId;
    }
}
