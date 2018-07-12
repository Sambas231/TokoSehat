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

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER = 0;

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

                Uri uri = ContentUris.withAppendedId(DrugEntry.CONTENT_URI, id);
                intent.setData(uri);

                startActivity(intent);
            }
        });

        drugCursorAdapter = new DrugCursorAdapter(this, null);
        listView.setAdapter(drugCursorAdapter);

        getLoaderManager().initLoader(PET_LOADER, null, this);
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
