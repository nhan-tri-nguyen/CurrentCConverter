package com.promise.android.currentcconverter.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ngtrnhan1205 on 11/20/17.
 */

public class FavCurrentCContract {

    public static final String CONTENT_AUTHORITY = "com.promise.android.currentcconverter";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVCURRENTC = "favCurrentC";

    public static final class FavCurrentCEntry implements BaseColumns {

        public static final String TABLE_NAME = "favCurrentC";
        public static final String COLUMN_IMG_RES_ID = "imgId";
        public static final String COLUMN_ABBR = "abbr";
        public static final String COLUMN_AMOUNT = "amount";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVCURRENTC);
    }
}
