package com.example.android.currentcconverter.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by ngtrnhan1205 on 12/3/17.
 */

public class CurrentCProvider extends ContentProvider {

    private static final int FAVCURRENTC = 100;
    private static final int FAVCURRENTC_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // Match for the whole favCurrentC table
        sUriMatcher.addURI(FavCurrentCContract.CONTENT_AUTHORITY,
                FavCurrentCContract.PATH_FAVCURRENTC, FAVCURRENTC);
        // Match for a row of favCurrentC table
        sUriMatcher.addURI(FavCurrentCContract.CONTENT_AUTHORITY,
                FavCurrentCContract.PATH_FAVCURRENTC + "/#", FAVCURRENTC_ID);
    }

    // Database helper object
    private FavCurrentCDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new FavCurrentCDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVCURRENTC:
                cursor = database.query(FavCurrentCContract.FavCurrentCEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case FAVCURRENTC_ID:
                selection = FavCurrentCContract.FavCurrentCEntry._ID;
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(FavCurrentCContract.FavCurrentCEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private Uri insertFavCurrentC(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(FavCurrentCContract.FavCurrentCEntry.TABLE_NAME,
                null, values);
        if (id == -1) {
            Toast.makeText(getContext(),
                    "Failed to add your new favorite currency!", Toast.LENGTH_LONG);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVCURRENTC:
                return insertFavCurrentC(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private int updateFavCurrentC(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(FavCurrentCContract.FavCurrentCEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVCURRENTC:
                return updateFavCurrentC(uri, values, selection, selectionArgs);
            case FAVCURRENTC_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = FavCurrentCContract.FavCurrentCEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateFavCurrentC(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
}
