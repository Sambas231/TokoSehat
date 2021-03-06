package com.example.android.tokosehat.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import android.telecom.PhoneAccountHandle;

import com.example.android.tokosehat.R;

/** Api Contract for TokoSehat app. */
public class DrugContract {

  // To prevent someone from instantiating this class
  // Empty Constructor
  private DrugContract() {}

  public static final String CONTENT_AUTHORITY = "com.example.android.tokosehat";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
  public static final String PATH_DRUG = "tokosehat";

  /** Inner class that defines constant values for the drugs database table */
  public static final class DrugEntry implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DRUG);

    public static final String CONTENT_LIST_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DRUG;

    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DRUG;

    public static final String TABLE_NAME = "drugs";

    public static final String _ID = BaseColumns._ID;
    public static final String COLUMN_DRUG_NAME = "name";
    public static final String COLUMN_DRUG_BENEFITS = "benefits";
    public static final String COLUMN_DRUG_PRICE_ITEM = "pricePerItem";
    public static final String COLUMN_DRUG_PRICE_DOZEN = "pricePerDozen";
    public static final String COLUMN_DRUG_PRICE_BOX = "pricePerBox";
    public static final String COLUMN_DRUG_STATUS = "status";
    public static final String COLUMN_DRUG_TYPE = "type";
    public static final String COLUMN_DRUG_DOSAGE = "dosage";
    public static final String COLUMN_DRUG_SIDE_EFFECTS = "side_effect";


    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_OUT_OF_STOCK = "Out of stock";

    public static final String TYPE_TABLET = "Tablet";
    public static final String TYPE_SIRUP = "Sirup";
    public static final String TYPE_KAPSUL = "Kapsul";
    public static final String TYPE_SACHET = "Sachet";
    public static final String TYPE_SOLID = "Solid";

    public static final String QUANTITY_BOX = "Box";
    public static final String QUANTITY_DOZEN = "Dozen";
    public static final String QUANTITY_ITEM = "Item";

    // TODO: make validation method for status
    protected static final boolean isValidStatus(String status) {
      if (status.equals(STATUS_AVAILABLE) || status.equals(STATUS_OUT_OF_STOCK)) {
        return true;
      }
      return false;
    }
  }
}
