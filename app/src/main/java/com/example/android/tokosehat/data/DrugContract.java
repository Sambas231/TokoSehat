package com.example.android.tokosehat.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import android.telecom.PhoneAccountHandle;

import com.example.android.tokosehat.R;

/**
 * Api Contract for TokoSehat app.
 */

public class DrugContract {

    // To prevent someone from instantiating this class
    // Empty Constructor
    private DrugContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.tokosehat";
    public static final Uri BASE_CONTENT_URI =  Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DRUG = "drugs";

    /**
     * Inner class that defines constant values for the drugs database table
     */
    public static final class DrugEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DRUG);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DRUG;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DRUG;

        public static final String TABLE_NAME = "drugs";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DRUG_NAME = "name";
        public static final String COLUMN_DRUG_DISEASESAS = "diseases";
        public static final String COLUMN_DRUG_PRICE = "price";
        public static final String COLUMN_DRUG_STATUS = "status";

        public static final String STATUS_AVAILABLE = "Available";
        public static final String STATUS_OUT_OF_STOCK = "Out of stock";

        //TODO: make validation method for status

    }


}
