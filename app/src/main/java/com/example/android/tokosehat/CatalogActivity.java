package com.example.android.tokosehat;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.tokosehat.data.DrugContract;
import com.example.android.tokosehat.data.DrugContract.DrugEntry;

import com.example.android.tokosehat.data.DrugDbHelper;

import java.util.ArrayList;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER = 0;
    private Uri uri;
    DrugCursorAdapter drugCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ListView listView = (ListView) findViewById(R.id.view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);

                uri = ContentUris.withAppendedId(DrugEntry.CONTENT_URI, id);
                intent.setData(uri);

                startActivity(intent);
            }
        });

        drugCursorAdapter = new DrugCursorAdapter(this, null);
        listView.setAdapter(drugCursorAdapter);

        getLoaderManager().initLoader(PET_LOADER, null, this);
    }


    private boolean isDbEmpty() {
        String[] projection = new String[] {DrugEntry._ID,
                DrugEntry.COLUMN_DRUG_NAME,
                DrugEntry.COLUMN_DRUG_DISEASES,
                DrugEntry.COLUMN_DRUG_PRICE,
                DrugEntry.COLUMN_DRUG_STATUS,
                DrugEntry.COLUMN_DRUG_TYPE,
                DrugEntry.COLUMN_DRUG_DOSAGE,
                DrugEntry.COLUMN_DRUG_SIDE_EFFECT};
        Cursor cursor = getContentResolver().query(DrugEntry.CONTENT_URI, projection, null, null, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;

    }

    private void defaultInsert() {
        ArrayList<Drug> drugArrayList = new ArrayList<>();
        drugArrayList.add(new Drug("asdf", "kalsdjflasd", 5666, "dklasjf", "alskdjfl", "askdjfkl", "ldsj"));

        int size = drugArrayList.size();

        for (int i = 0; i < size; i++) {
            ContentValues values = new ContentValues();
            values.put(DrugEntry.COLUMN_DRUG_NAME, drugArrayList.get(i).getName());
            values.put(DrugEntry.COLUMN_DRUG_DISEASES, drugArrayList.get(i).getDiseases());
            values.put(DrugEntry.COLUMN_DRUG_PRICE, drugArrayList.get(i).getPrice());
            values.put(DrugEntry.COLUMN_DRUG_STATUS, drugArrayList.get(i).getStatus());
            values.put(DrugEntry.COLUMN_DRUG_TYPE, drugArrayList.get(i).getType());
            values.put(DrugEntry.COLUMN_DRUG_DOSAGE, drugArrayList.get(i).getDosages());
            values.put(DrugEntry.COLUMN_DRUG_SIDE_EFFECT, drugArrayList.get(i).getSideEffects());

            getContentResolver().insert(DrugEntry.CONTENT_URI, values);
        }
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[] {DrugEntry._ID, DrugEntry.COLUMN_DRUG_NAME, DrugEntry.COLUMN_DRUG_DISEASES, DrugEntry.COLUMN_DRUG_PRICE, DrugEntry.COLUMN_DRUG_STATUS};
        return new CursorLoader(this, DrugEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        drugCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        drugCursorAdapter.swapCursor(null);
    }


}
