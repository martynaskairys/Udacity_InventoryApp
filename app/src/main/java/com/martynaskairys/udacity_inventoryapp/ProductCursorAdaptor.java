package com.martynaskairys.udacity_inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.martynaskairys.udacity_inventoryapp.data.ProductContract;
import com.martynaskairys.udacity_inventoryapp.data.ProductDbHelper;

/**
 * Created by martynaskairys on 01/07/2017.
 */

public class ProductCursorAdaptor extends CursorAdapter {
    public ProductCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent,
                false);
    }

//    ProductDbHelper mDbHelper = new ProductDbHelper(getContext());

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView productNameTextView = (TextView) view.findViewById(R.id.productName);
        final TextView productQuantityTextView = (TextView) view.findViewById(R.id.productQuantity);
        TextView productPriceTextView = (TextView) view.findViewById(R.id.productPrice);
        Button productSaleButton = (Button) view.findViewById(R.id.sale_button);
//        ImageView productImageView = (ImageView) view.findViewById(R.id.thumbnail);

        int productNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int productQuantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int productPriceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
//        int productThumbnailColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_THUMBNAIL);

        String productName = cursor.getString(productNameColumnIndex);
        String productQuantity = cursor.getString(productQuantityColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);
//        String productThumbnail = cursor.getString(productThumbnailColumnIndex);

        productNameTextView.setText(productName);
        productQuantityTextView.setText(productQuantity);
        productPriceTextView.setText(productPrice);
//        productImageView.setImageURI(productThumbnail);

        productSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int leftProducts = Integer.parseInt(productQuantityTextView.getText().toString());
                if(leftProducts>0){
                    leftProducts--;
                    productQuantityTextView.setText(Integer.toString(leftProducts));
//                    mDbHelper.onUpgrade().

                }

            }
        });
    }
}
