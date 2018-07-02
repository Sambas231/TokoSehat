package com.example.android.tokosehat.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.android.tokosehat.data.DrugContract.DrugEntry;

public class DrugProvider extends ContentProvider {

    public static final String LOG_TAG = DrugProvider.class.getSimpleName();

    private static final int DRUGS = 100;
    private static final int DRUG_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DrugContract.CONTENT_AUTHORITY, DrugContract.PATH_DRUG, DRUGS);
        sUriMatcher.addURI(DrugContract.CONTENT_AUTHORITY, DrugContract.PATH_DRUG + "/#", DRUG_ID);
    }

    private DrugDbHelper drugDbHelper;




    @Override
    public boolean onCreate() {
        drugDbHelper = new DrugDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = drugDbHelper.getReadableDatabase();
        Cursor cursor;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DRUGS:
                cursor = db.query(DrugEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case DRUG_ID:
                selection = DrugEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(DrugEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DRUGS:
                return DrugEntry.CONTENT_LIST_TYPE;
            case DRUG_ID:
                return DrugEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DRUGS:

        }
    }

    private Uri insertPet (Uri uri, ContentValues values) {
        String name = values.getAsString(DrugEntry.COLUMN_DRUG_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Please insert valid name");
        }

        String diseases = values.getAsString(DrugEntry.COLUMN_DRUG_DISEASES);
        if (diseases == null) {
            throw new IllegalArgumentException("Please insert valid diseases");
        }

        Integer price = values.getAsInteger(DrugEntry.COLUMN_DRUG_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Please insert valid price");
        }

        Integer status = values.getAsInteger(DrugEntry.COLUMN_DRUG_STATUS);
        if (status == null || !DrugEntry.isValidStatus(status)) {
            throw new IllegalArgumentException("Please choose the status");
        }

        SQLiteDatabase db = drugDbHelper.getWritableDatabase();


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
