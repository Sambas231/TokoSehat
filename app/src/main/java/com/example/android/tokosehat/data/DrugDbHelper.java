package com.example.android.tokosehat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.tokosehat.data.DrugContract.DrugEntry;

public class DrugDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DrugDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "shop.db";

    private static final int DATABASE_VERSION = 1;

    public DrugDbHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + DrugEntry.TABLE_NAME + "("
                + DrugEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DrugEntry.COLUMN_DRUG_NAME + " TEXT NOT NULL, "
                + DrugEntry.COLUMN_DRUG_DISEASES + " TEXT NOT NULL, "
                + DrugEntry.COLUMN_DRUG_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + DrugEntry.COLUMN_DRUG_STATUS + " TEXT NOT NULL, "
                + DrugEntry.COLUMN_DRUG_TYPE + " TEXT NOT NULL, "
                + DrugEntry.COLUMN_DRUG_DOSAGE + " TEXT, "
                + DrugEntry.COLUMN_DRUG_SIDE_EFFECTS + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
