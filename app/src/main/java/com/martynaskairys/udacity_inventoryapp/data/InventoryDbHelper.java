package com.martynaskairys.udacity_inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by martynaskairys on 30/06/2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 1;


    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE "+ InventoryContract.InventoryEntry.TABLE_NAME +" ("
                + InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.InventoryEntry.PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryContract.InventoryEntry.PRODUCT_PRICE + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
