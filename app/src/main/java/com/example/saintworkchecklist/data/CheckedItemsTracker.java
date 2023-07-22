package com.example.saintworkchecklist.data;

import java.util.HashMap;
import java.util.Map;

import android.util.SparseBooleanArray;

public class CheckedItemsTracker {
    private SparseBooleanArray checkedItems;

    public CheckedItemsTracker() {
        checkedItems = new SparseBooleanArray();
    }

    public void setChecked(int itemId, boolean isChecked) {
        checkedItems.put(itemId, isChecked);
    }

    public boolean isChecked(int itemId) {
        return checkedItems.get(itemId, false);
    }

    public void clear() {
        checkedItems.clear();
    }
}


