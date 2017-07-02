package com.martynaskairys.udacity_inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.martynaskairys.udacity_inventoryapp.data.ProductContract;

//import android.support.v7.app.LoaderManager;
//import android.support.v7.content.CursorLoader;
//import android.support.v7.content.Loader;
//import android.support.v7.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER = 0;
    ProductCursorAdaptor mProductCursorAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView inventoryListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_text);
        inventoryListView.setEmptyView(emptyView);

        mProductCursorAdaptor = new ProductCursorAdaptor(this, null);
        inventoryListView.setAdapter(mProductCursorAdaptor);

        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    private void insertProduct() {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Macbooko Pro");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 0);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, 0);

        Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.insert_data:
                insertProduct();
                return true;

            case R.id.delete_entry:
                deleteProductInfo();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteProductInfo() {

        int rowsDeleted = getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from products database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE
        };

        return new CursorLoader(this,
                ProductContract.ProductEntry.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mProductCursorAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mProductCursorAdaptor.swapCursor(null);
    }
}
