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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private TextView mPriceItem;
    private TextView mPriceDozen;
    private TextView mPriceBox;
    private TextView mStatus;
    private TextView mType;
    private TextView mDosage;
    private TextView mSideEffect;
    private RelativeLayout item;
    private RelativeLayout dozen;
    private RelativeLayout box;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mName = (TextView) findViewById(R.id.detail_name);
        mDiseases = (TextView) findViewById(R.id.detail_diseases);
        mPriceItem = (TextView) findViewById(R.id.detail_price_text_item);
        mPriceDozen = (TextView) findViewById(R.id.detail_price_text_dozen);
        mPriceBox = (TextView) findViewById(R.id.detail_price_text_box);
        mStatus = (TextView) findViewById(R.id.detail_status);
        mType = (TextView) findViewById(R.id.detail_type);
        mDosage = (TextView) findViewById(R.id.detail_dosage);
        mSideEffect = (TextView) findViewById(R.id.detail_side_effect);
        item = (RelativeLayout) findViewById(R.id.layout_item);
        dozen = (RelativeLayout) findViewById(R.id.layout_dozen);
        box = (RelativeLayout) findViewById(R.id.layout_box);

        uri = getIntent().getData();

        getLoaderManager().initLoader(PET_LOADER, null,  this);
        setTitle("Details");
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
        return new CursorLoader(this, uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {

            int nameIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_NAME);
            int benefitsIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_BENEFITS);
            int itemPriceIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_PRICE_ITEM);
            int dozenPriceIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_PRICE_DOZEN);
            int boxPriceIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_PRICE_BOX);
            int statusIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_STATUS);
            int typeIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_TYPE);
            int dosageIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_DOSAGE);
            int sideEffectIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_SIDE_EFFECTS);

            String currName = cursor.getString(nameIndex);
            String currDiseases = cursor.getString(benefitsIndex);
            int currItemPrice = cursor.getInt(itemPriceIndex);
            int currDozenPrice = cursor.getInt(dozenPriceIndex);
            int currBoxPrice = cursor.getInt(boxPriceIndex);
            String currStatus = cursor.getString(statusIndex);
            String currType = cursor.getString(typeIndex);
            String currDosage = cursor.getString(dosageIndex);
            String currSideEffect = cursor.getString(sideEffectIndex);

            if (currItemPrice <= 0) {
                item.setVisibility(View.GONE);
            }
            else {
                item.setVisibility(View.VISIBLE);
                mPriceItem.setText(String.valueOf(currItemPrice));
            }

            if (currDozenPrice <= 0) {
                dozen.setVisibility(View.GONE);
            }
            else {
                dozen.setVisibility(View.VISIBLE);
                mPriceDozen.setText(String.valueOf(currDozenPrice));
            }

            if (currBoxPrice <= 0) {
                box.setVisibility(View.GONE);
            }
            else {
                box.setVisibility(View.VISIBLE);
                mPriceBox.setText(String.valueOf(currBoxPrice));
            }

            mName.setText(currName);
            mDiseases.setText(currDiseases);
            mStatus.setText(currStatus);
            mType.setText(currType);
            mDosage.setText(currDosage);
            mSideEffect.setText(currSideEffect);

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader.reset();
    }
}
