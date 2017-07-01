package com.martynaskairys.udacity_inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.martynaskairys.udacity_inventoryapp.data.InventoryContract;

/**
 * Created by martynaskairys on 01/07/2017.
 */

public class InventoryCursorAdaptor extends CursorAdapter {
    public InventoryCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView productNameTextView = (TextView) view.findViewById(R.id.productName);
        TextView productQuantityTextView = (TextView) view.findViewById(R.id.productQuantity);
        TextView productPriceTextView = (TextView) view.findViewById(R.id.productPrice);

        int productNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.PRODUCT_NAME);
        int productQuantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.PRODUCT_QUANTITY);
        int productPriceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.PRODUCT_PRICE);

        String productName = cursor.getString(productNameColumnIndex);
        String productQuantity = cursor.getString(productQuantityColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);

        if (TextUtils.isEmpty(productName)) {
            productName = "unknown product";
        }

        productNameTextView.setText(productName);
        productQuantityTextView.setText(productQuantity);
        productPriceTextView.setText(productPrice);
    }
}
