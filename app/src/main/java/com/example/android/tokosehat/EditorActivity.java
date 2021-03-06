package com.example.android.tokosehat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.tokosehat.data.DrugContract.DrugEntry;


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
    private EditText mItemPrice;
    private EditText mDozenPrice;
    private EditText mBoxPrice;
    private Spinner mStatusSpinner;

    private String mStatus = DrugEntry.STATUS_OUT_OF_STOCK;

    private static final int DRUG_LOADER = 0;
    private Uri mCurrentUri;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mCurrentUri = getIntent().getData();

        if (mCurrentUri != null) {
            getLoaderManager().initLoader(DRUG_LOADER, null, this);
        }
        mName = (EditText) findViewById(R.id.editor_edit_name);
        mDiseases = (EditText) findViewById(R.id.editor_edit_diseases);
        mItemPrice = (EditText) findViewById(R.id.editor_price_edit_item);
        mDozenPrice = (EditText) findViewById(R.id.editor_price_edit_dozen);
        mBoxPrice = (EditText) findViewById(R.id.editor_price_edit_box);
        mStatusSpinner = (Spinner) findViewById(R.id.editor_spinner);

        mName.setOnTouchListener(onTouchListener);
        mDiseases.setOnTouchListener(onTouchListener);
        mItemPrice.setOnTouchListener(onTouchListener);
        mBoxPrice.setOnTouchListener(onTouchListener);
        mDozenPrice.setOnTouchListener(onTouchListener);
        mStatusSpinner.setOnTouchListener(onTouchListener);

        ArrayAdapter statusSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_status_options, android.R.layout.simple_spinner_item);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mStatusSpinner.setAdapter(statusSpinnerAdapter);

        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) adapterView.getItemAtPosition(i);

                if (!selection.isEmpty()) {
                    if (selection.equals(getString(R.string.status_available))) {
                        mStatus = DrugEntry.STATUS_AVAILABLE;
                    }
                    else {
                        mStatus = DrugEntry.STATUS_OUT_OF_STOCK;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mStatus = DrugEntry.STATUS_OUT_OF_STOCK;
            }
        });
    }



    private void saveDrug() {
        String nameString = mName.getText().toString().trim();
        String diseasesString = mDiseases.getText().toString().trim();
        String itemPriceString = mItemPrice.getText().toString().trim();
        String dozenPriceString = mDozenPrice.getText().toString().trim();
        String boxPriceString = mBoxPrice.getText().toString().trim();

        if (mCurrentUri == null && nameString.isEmpty() && diseasesString.isEmpty() &&
                itemPriceString.isEmpty() && mStatus.equals(DrugEntry.STATUS_OUT_OF_STOCK)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DrugEntry.COLUMN_DRUG_NAME, nameString);
        values.put(DrugEntry.COLUMN_DRUG_BENEFITS, diseasesString);
        values.put(DrugEntry.COLUMN_DRUG_STATUS, mStatus);

        int itemPrice = 0;
        int dozenPrice = 0;
        int boxPrice = 0;
        if (!itemPriceString.isEmpty()) {
            itemPrice = Integer.parseInt(itemPriceString);
        }
        if (!dozenPriceString.isEmpty()) {
            dozenPrice = Integer.parseInt(dozenPriceString);
        }
        if (!boxPriceString.isEmpty()) {
            boxPrice = Integer.parseInt(boxPriceString);
        }

        values.put(DrugEntry.COLUMN_DRUG_PRICE_ITEM, itemPrice);
        values.put(DrugEntry.COLUMN_DRUG_PRICE_DOZEN, dozenPrice);
        values.put(DrugEntry.COLUMN_DRUG_PRICE_BOX, boxPrice);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_item:
                saveDrug();
                finish();
                return true;

            case android.R.id.home:
                if (!mDrugHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard Changes?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mDrugHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardClickButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardClickButtonListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {DrugEntry._ID, DrugEntry.COLUMN_DRUG_NAME, DrugEntry.COLUMN_DRUG_BENEFITS, DrugEntry.COLUMN_DRUG_PRICE_ITEM, DrugEntry.COLUMN_DRUG_PRICE_DOZEN, DrugEntry.COLUMN_DRUG_PRICE_BOX, DrugEntry.COLUMN_DRUG_STATUS};

        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_NAME);
            int diseasesIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_BENEFITS);
            int itemPriceIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_PRICE_ITEM);
            int dozenPriceIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_PRICE_DOZEN);
            int boxPriceIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_PRICE_BOX);
            int statusIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_STATUS);


            String name = cursor.getString(nameIndex);
            String diseases = cursor.getString(diseasesIndex);
            String status = cursor.getString(statusIndex);
            int itemPrice = cursor.getInt(itemPriceIndex);
            int dozenPrice = cursor.getInt(dozenPriceIndex);
            int boxPrice = cursor.getInt(boxPriceIndex);

            mName.setText(name);
            mDiseases.setText(diseases);
            mItemPrice.setText(String.valueOf(itemPrice));
            mDozenPrice.setText(String.valueOf(dozenPrice));
            mBoxPrice.setText(String.valueOf(boxPrice));

            switch (status) {
                case DrugEntry.STATUS_AVAILABLE:
                    mStatusSpinner.setSelection(0);
                    break;

                case DrugEntry.STATUS_OUT_OF_STOCK:
                    mStatusSpinner.setSelection(1);
                    break;

            }

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mName.setText("");
        mDiseases.setText("");
        mItemPrice.setText("");
        mDozenPrice.setText("");
        mBoxPrice.setText("");
        mStatusSpinner.setSelection(0);
    }
}
