package com.example.android.tokosehat;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.tokosehat.data.DrugContract.DrugEntry;

import com.example.android.tokosehat.data.DrugContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private boolean mDrugHasChanged = false;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDrugHasChanged = true;
            return false;
        }
    };

    private EditText mName;
    private EditText mDiseases;
    private EditText mPrice;
    private Spinner mStatusSpinner;

    private String mStatus = DrugEntry.STATUS_OUT_OF_STOCK;

    private static final int DRUG_LOADER = 0;
    private Uri mCurrentUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mCurrentUri = getIntent().getData();

        getLoaderManager().initLoader(DRUG_LOADER, null, this);

        mName = (EditText) findViewById(R.id.editor_edit_name);
        mDiseases = (EditText) findViewById(R.id.editor_edit_diseases);
        mPrice = (EditText) findViewById(R.id.editor_price_edit_text);
        mStatusSpinner = (Spinner) findViewById(R.id.editor_spinner);

        mName.setOnTouchListener(onTouchListener);
        mDiseases.setOnTouchListener(onTouchListener);
        mPrice.setOnTouchListener(onTouchListener);
        mStatusSpinner.setOnTouchListener(onTouchListener);


        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter statusSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_status_options, android.R.layout.simple_spinner_item);

        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mStatusSpinner.setAdapter(statusSpinnerAdapter);

        mStatusSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selection = (String) adapterView.getItemAtPosition(position);
                if (!selection.isEmpty()) {
                    if (selection.equals(getString(R.string.status_available))) {
                        mStatus = DrugEntry.STATUS_AVAILABLE;
                    }
                    else {
                        mStatus = DrugEntry.STATUS_OUT_OF_STOCK;
                    }
                }
            }

        });
    }

    private void savePet() {
        String nameString = mName.getText().toString().trim();
        String diseasesString = mDiseases.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();

        if (mCurrentUri == null && nameString.isEmpty() && diseasesString.isEmpty() &&
                priceString.isEmpty() && mStatus == DrugEntry.STATUS_OUT_OF_STOCK) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DrugEntry.COLUMN_DRUG_NAME, nameString);
        values.put(DrugEntry.COLUMN_DRUG_DISEASES, diseasesString);
        values.put(DrugEntry.COLUMN_DRUG_STATUS, mStatus);

        int price = 0;
        if (!priceString.isEmpty()) {
            price = Integer.parseInt(priceString);
        }
        values.put(DrugEntry.COLUMN_DRUG_PRICE, price);

        if (mCurrentUri == null) {
            Uri newUri = getContentResolver().insert(DrugEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(EditorActivity.this, "Insert drug failed", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(EditorActivity.this, "Insert drug success", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(EditorActivity.this, "Update drug failed", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(EditorActivity.this, "Update drug success", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
