package com.example.android.currentcconverter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ngtrnhan1205 on 11/20/17.
 */

public class FavCurrentCDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favcurrentc.db";
    // Increment version if change database schema
    public static final int DATABASE_VERSION = 1;

    public FavCurrentCDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAV_CURRENCY_TABLE = "CREATE TABLE " +
                FavCurrentCContract.FavCurrentCEntry.TABLE_NAME + " (" +
                FavCurrentCContract.FavCurrentCEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavCurrentCContract.FavCurrentCEntry.COLUMN_ABBR + " TEXT NOT NULL, " +
                FavCurrentCContract.FavCurrentCEntry.COLUMN_IMG_RES_ID + " INTEGER NOT NULL, " +
                FavCurrentCContract.FavCurrentCEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL" + ");";
        db.execSQL(SQL_CREATE_FAV_CURRENCY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavCurrentCContract.FavCurrentCEntry.TABLE_NAME);
        onCreate(db);
    }
}
