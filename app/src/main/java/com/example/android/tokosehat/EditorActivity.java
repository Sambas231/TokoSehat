package com.example.android.tokosehat;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.android.tokosehat.data.DrugContract.DrugEntry;

import com.example.android.tokosehat.data.DrugContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private boolean mPetHasChanged = false;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };

    private EditText mName;
    private EditText mDiseases;
    private EditText mPrice;
    private Spinner mStatusSpinner;

    private String mStatus = DrugEntry.STATUS_OUT_OF_STOCK;

    private static final int PET_LOADER = 0;
    private Uri mCurrentUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mCurrentUri = getIntent().getData();

        mName = (EditText) findViewById(R.id.editor_edit_name);
        mDiseases = (EditText) findViewById(R.id.editor_edit_diseases);
        mPrice = (EditText) findViewById(R.id.editor_price_edit_text);
        mStatusSpinner = (Spinner) findViewById(R.id.editor_spinner);

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
