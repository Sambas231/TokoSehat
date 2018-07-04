package com.example.android.tokosehat;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

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
    }
}
