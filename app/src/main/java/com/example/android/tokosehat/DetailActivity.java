package com.example.android.tokosehat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.tokosehat.data.DrugContract.DrugEntry;

import com.example.android.tokosehat.data.DrugContract;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri uri;
    private static final int PET_LOADER = 0;

    private String mQuantity;

    private TextView mName;
    private TextView mDiseases;
    private TextView mPrice;
    private TextView mStatus;
    private Spinner mSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mName = (TextView) findViewById(R.id.detail_name);
        mDiseases = (TextView) findViewById(R.id.detail_diseases);
        mPrice = (TextView) findViewById(R.id.detail_price_text);
        mStatus = (TextView) findViewById(R.id.detail_status);
        mSpinner = (Spinner) findViewById(R.id.spinner_quantity);

        uri = getIntent().getData();

        getLoaderManager().initLoader(PET_LOADER, null,  this);
        setTitle("Details");

        ArrayAdapter quantitySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_quantity_options, android.R.layout.simple_spinner_item);
        quantitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner.setAdapter(quantitySpinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) adapterView.getItemAtPosition(i);

                if (!selection.isEmpty()) {
                    if (selection.equals(getString(R.string.quantity_item))) {
                        mQuantity = DrugEntry.QUANTITY_ITEM;
                    }
                    else if (selection.equals(getString(R.string.quantity_dozen))) {
                        mQuantity = DrugEntry.QUANTITY_DOZEN;
                    }
                    else {
                        mQuantity = DrugEntry.QUANTITY_BOX;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mQuantity = DrugEntry.QUANTITY_ITEM;
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_item:
                Intent intent = new Intent(DetailActivity.this, EditorActivity.class);
                intent.setData(uri);
                startActivity(intent);
                return true;

            case R.id.action_delete_item:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteDrug() {
        int rowsDeleted = getContentResolver().delete(uri, null, null);

        if (rowsDeleted == 0) {
            Toast.makeText(DetailActivity.this, "Delete drug failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(DetailActivity.this, "Delete drug success", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this drug?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteDrug();
            }
        });
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = new String[] {DrugEntry._ID, DrugEntry.COLUMN_DRUG_NAME, DrugEntry.COLUMN_DRUG_BENEFITS, DrugEntry.COLUMN_DRUG_PRICE, DrugEntry.COLUMN_DRUG_STATUS};
        return new CursorLoader(this, uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {

            int nameIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_NAME);
            int diseasesIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_BENEFITS);
            int priceIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_PRICE);
            int statusIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_STATUS);
            int quantityIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_QUANTITY);

            String currName = cursor.getString(nameIndex);
            String currDiseases = cursor.getString(diseasesIndex);
            String currPrice = String.valueOf(cursor.getInt(priceIndex));
            String currStatus = cursor.getString(statusIndex);
            String currQuantity = cursor.getString(quantityIndex);

            switch (currQuantity) {
                case DrugEntry.QUANTITY_ITEM:
                    mSpinner.setSelection(0);
                    break;

                case DrugEntry.QUANTITY_DOZEN:
                    mSpinner.setSelection(1);
                    break;

                default:
                    mSpinner.setSelection(2);
                    break;
            }

            mName.setText(currName);
            mDiseases.setText(currDiseases);
            mPrice.setText(currPrice);
            mStatus.setText(currStatus);

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader.reset();
    }
}
