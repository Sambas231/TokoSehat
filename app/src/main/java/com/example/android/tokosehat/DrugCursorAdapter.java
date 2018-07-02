package com.example.android.tokosehat;

import android.view.LayoutInflater;
import com.example.android.tokosehat.data.DrugContract.DrugEntry;

public class DrugCursorAdapter extends CursorAdapter {

    public DrugCursorAdapter (Context c, Cursor cursor) {
        super(c, cursor, null);
    }
    
    
    @Override
    public View newView(Context c, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(c).inflate()
    }
}
