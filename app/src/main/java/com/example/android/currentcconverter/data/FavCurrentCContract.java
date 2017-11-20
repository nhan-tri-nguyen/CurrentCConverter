package com.example.android.currentcconverter.data;

import android.provider.BaseColumns;

/**
 * Created by ngtrnhan1205 on 11/20/17.
 */

public class FavCurrentCContract {

    public static final class FavCurrentCEntry implements BaseColumns {

        public static final String TABLE_NAME = "favcurrentc";
        public static final String COLUMN_IMG_RES_ID = "imgid";
        public static final String COLUMN_ABBR = "abbr";
        public static final String COLUMN_AMOUNT = "amount";
    }
}
