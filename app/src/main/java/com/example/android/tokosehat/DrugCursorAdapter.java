package com.example.android.tokosehat;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.tokosehat.data.DrugContract.DrugEntry;

public class DrugCursorAdapter extends CursorAdapter {

    public DrugCursorAdapter (Context c, Cursor cursor) {
        super(c, cursor, 0);
    }
    
    
    @Override
    public View newView(Context c, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(c).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = (TextView) view.findViewById(R.id.list_name);
        TextView statusView = (TextView) view.findViewById(R.id.list_status);
        TextView priceView = (TextView) view.findViewById(R.id.list_price);
        TextView diseasesView = (TextView) view.findViewById(R.id.list_diseases);

        int nameIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_NAME);
        int statusIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_STATUS);
        int priceIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_PRICE_ITEM);
        int diseasesIndex = cursor.getColumnIndex(DrugEntry.COLUMN_DRUG_BENEFITS);

        String name = cursor.getString(nameIndex);
        String status = cursor.getString(statusIndex);
        int price = cursor.getInt(priceIndex);
        String diseases = cursor.getString(diseasesIndex);

        if (status.equals(DrugEntry.STATUS_AVAILABLE)) {
            statusView.setBackgroundColor(view.getResources().getColor(R.color.status_available));
        }
        else {
            statusView.setBackgroundColor(view.getResources().getColor(R.color.status_out_of_stock));
        }

        nameView.setText(name);
        statusView.setText(status);
        priceView.setText(String.valueOf(price));
        diseasesView.setText(diseases);
    }
}
