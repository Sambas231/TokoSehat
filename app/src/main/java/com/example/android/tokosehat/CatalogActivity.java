package com.example.android.tokosehat;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.android.tokosehat.data.DrugContract;
import com.example.android.tokosehat.data.DrugContract.DrugEntry;

import com.example.android.tokosehat.data.DrugDbHelper;

import java.util.ArrayList;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private DrugDbHelper dbHelper;
    private static final int DRUG_LOADER = 0;
    private SearchView search;

    DrugCursorAdapter drugCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ListView listView = (ListView) findViewById(R.id.view);
        dbHelper = new DrugDbHelper(this);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);

                Uri uri = ContentUris.withAppendedId(DrugEntry.CONTENT_URI, id);
                intent.setData(uri);

                startActivity(intent);
            }
        });

        drugCursorAdapter = new DrugCursorAdapter(CatalogActivity.this, null);
        listView.setAdapter(drugCursorAdapter);

        getLoaderManager().initLoader(DRUG_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (android.support.v7.widget.SearchView) menu.findItem(R.id.search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setQueryHint("Search Drug");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Cursor cursor = getDrugListByKeywords(s);
                drugCursorAdapter.swapCursor(cursor);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cursor = getDrugListByKeywords(s);
                drugCursorAdapter.swapCursor(cursor);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!isDbEmpty()) {
                    insertDrug();
                }
                else {
                    defaultInsert();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isDbEmpty() {
        String[] projection = new String[] {DrugEntry._ID,
                DrugEntry.COLUMN_DRUG_NAME,
                DrugEntry.COLUMN_DRUG_BENEFITS,
                DrugEntry.COLUMN_DRUG_PRICE_ITEM,
                DrugEntry.COLUMN_DRUG_PRICE_DOZEN,
                DrugEntry.COLUMN_DRUG_PRICE_BOX,
                DrugEntry.COLUMN_DRUG_STATUS,
                DrugEntry.COLUMN_DRUG_TYPE,
                DrugEntry.COLUMN_DRUG_DOSAGE,
                DrugEntry.COLUMN_DRUG_SIDE_EFFECTS};
        Cursor cursor = getContentResolver().query(DrugEntry.CONTENT_URI, projection, null, null, null);

        if (cursor.moveToFirst()) {
            return false;
        }
        return true;
    }

    private void insertDrug() {
        ContentValues values = new ContentValues();
        values.put(DrugEntry.COLUMN_DRUG_NAME, "Paracetamol");
        values.put(DrugEntry.COLUMN_DRUG_BENEFITS, "Demam, Nyeri");
        values.put(DrugEntry.COLUMN_DRUG_PRICE_ITEM, 7000);
        values.put(DrugEntry.COLUMN_DRUG_STATUS, DrugEntry.STATUS_AVAILABLE);
        values.put(DrugEntry.COLUMN_DRUG_TYPE, DrugEntry.TYPE_TABLET);
        values.put(DrugEntry.COLUMN_DRUG_DOSAGE, "3 * 1");
        values.put(DrugEntry.COLUMN_DRUG_SIDE_EFFECTS, "Ngantuk");

        Uri temp = getContentResolver().insert(DrugEntry.CONTENT_URI, values);

        if (temp == null) {
            Toast.makeText(CatalogActivity.this, "Insert a drug failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(CatalogActivity.this, "Insert a drug success", Toast.LENGTH_SHORT).show();
        }
    }

    private void defaultInsert() {
        ArrayList<Drug> drugArrayList = new ArrayList<>();
        drugArrayList.add(new Drug(
                "asdf",
                "salkdjfskdf",
                12000,
                134234,
                14234,
                DrugEntry.STATUS_AVAILABLE,
                DrugEntry.TYPE_TABLET,
                "asdfsdf",
                "askjdfklsajd"
        ));


        int size = drugArrayList.size();
        for (int i = 0; i < size; i++) {
            ContentValues values = new ContentValues();
            values.put(DrugEntry.COLUMN_DRUG_NAME, drugArrayList.get(i).getName());
            values.put(DrugEntry.COLUMN_DRUG_BENEFITS, drugArrayList.get(i).getBenefits());
            values.put(DrugEntry.COLUMN_DRUG_PRICE_ITEM, drugArrayList.get(i).getItemPrice());
            values.put(DrugEntry.COLUMN_DRUG_PRICE_DOZEN, drugArrayList.get(i).getDozenPrice());
            values.put(DrugEntry.COLUMN_DRUG_PRICE_BOX, drugArrayList.get(i).getBoxPrice());
            values.put(DrugEntry.COLUMN_DRUG_STATUS, drugArrayList.get(i).getStatus());
            values.put(DrugEntry.COLUMN_DRUG_TYPE, drugArrayList.get(i).getType());
            values.put(DrugEntry.COLUMN_DRUG_DOSAGE, drugArrayList.get(i).getDosage());
            values.put(DrugEntry.COLUMN_DRUG_SIDE_EFFECTS, drugArrayList.get(i).getSideEffect());

            getContentResolver().insert(DrugEntry.CONTENT_URI, values);
        }

    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[] {DrugEntry._ID, DrugEntry.COLUMN_DRUG_NAME, DrugEntry.COLUMN_DRUG_BENEFITS, DrugEntry.COLUMN_DRUG_PRICE_ITEM, DrugEntry.COLUMN_DRUG_STATUS};
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

    public Cursor getDrugListByKeywords(String search) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT " + DrugEntry._ID + ", "
                + DrugEntry.COLUMN_DRUG_NAME + ", "
                + DrugEntry.COLUMN_DRUG_BENEFITS  + ", "
                + DrugEntry.COLUMN_DRUG_PRICE_ITEM + ", "
                + DrugEntry.COLUMN_DRUG_STATUS + ", "
                + DrugEntry.COLUMN_DRUG_TYPE + ", "
                + DrugEntry.COLUMN_DRUG_DOSAGE + ", "
                + DrugEntry.COLUMN_DRUG_SIDE_EFFECTS + " FROM "
                + DrugEntry.TABLE_NAME + " WHERE "
                + DrugEntry.COLUMN_DRUG_NAME + " LIKE '%" +search + "%';";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        }
        else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

}
