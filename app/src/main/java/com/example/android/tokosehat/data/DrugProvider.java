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
import android.util.Log;

import com.example.android.tokosehat.data.DrugContract.DrugEntry;
import com.example.android.tokosehat.data.DrugContract;

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
                return insertDrug(uri, contentValues);
            default :
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }
    }

    private Uri insertDrug (Uri uri, ContentValues values) {
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

        String status = values.getAsString(DrugEntry.COLUMN_DRUG_STATUS);
        if (status == null || !DrugEntry.isValidStatus(status)) {
            throw new IllegalArgumentException("Please choose the status");
        }

        SQLiteDatabase db = drugDbHelper.getWritableDatabase();
        
        long id = db.insert(DrugEntry.TABLE_NAME, null, values);
        
        if (id == -1) {
          Log.e(LOG_TAG, "Failed to insert row for " + uri);
          return null;
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = drugDbHelper.getWritableDatabase();
        
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        
        switch (match) {
            case DRUGS:
                rowsDeleted = db.delete(DrugEntry.TABLE_NAME, selection, selectionArgs);
                break;
                
            case DRUG_ID:
                selection = DrugEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(DrugEntry.TABLE_NAME, selection, selectionArgs);
                break;
                
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        
        return rowsDeleted;
        
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        
        switch (match) {
            case DRUGS:
                return updatePet(uri, contentValues, selection, selectionArgs);
                
            case DRUG_ID:
                selection = DrugEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
                
            default:
                throw new IllegalArgumentException("Updating is not supported for " + uri);
                  
        }
    }
    
    private int updatePet (Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(DrugEntry.COLUMN_DRUG_NAME)) {
            String name = values.getAsString(DrugEntry.COLUMN_DRUG_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Drug need valid name");
            }
        }
        
        if (values.containsKey(DrugEntry.COLUMN_DRUG_DISEASES)) {
            String diseases = values.getAsString(DrugEntry.COLUMN_DRUG_DISEASES);
            
            if (diseases == null) {
                throw new IllegalArgumentException("Drug need valid diseases");
            }
        }
        
        if (values.containsKey(DrugEntry.COLUMN_DRUG_PRICE)) {
            Integer price = values.getAsInteger(DrugEntry.COLUMN_DRUG_PRICE);
            
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Drug need valid price");
            }
        }
        
        if (values.containsKey(DrugEntry.COLUMN_DRUG_STATUS)) {
            String status = values.getAsString(DrugEntry.COLUMN_DRUG_STATUS);
            
            if (status == null || !DrugEntry.isValidStatus(status)) {
                throw new IllegalArgumentException("Please choose the right option");
            }
        }
        
        if (values.size() == 0) {
            return 0;
        }
        
        SQLiteDatabase db = drugDbHelper.getWritableDatabase();
        
        int rowsUpdated = db.update(DrugEntry.TABLE_NAME, values, selection,selectionArgs);
        
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        
        return rowsUpdated;
        
    }
}
