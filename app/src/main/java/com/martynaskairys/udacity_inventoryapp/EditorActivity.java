package com.martynaskairys.udacity_inventoryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.martynaskairys.udacity_inventoryapp.data.ProductContract;
import com.martynaskairys.udacity_inventoryapp.data.ProductDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private int PICK_IMAGE_REQUEST = 1;

    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Uri mCurrentProductUri;
    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;
    private boolean mProductHasChanged = false;

    private Button mIncreaseButton;
    private Button mDecreaseButton;
    private Button mBtnAddImage;
    private ImageView mImageView;

    private ProductDbHelper dbHelper;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mProductHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle("Add a product");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Product");
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mIncreaseButton = (Button) findViewById(R.id.increase_button);
        mDecreaseButton = (Button) findViewById(R.id.decrease_button);
        mBtnAddImage = (Button) findViewById(R.id.add_image);
        mImageView = (ImageView) findViewById(R.id.image_view);

        dbHelper = new ProductDbHelper(this);




        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mIncreaseButton.setOnTouchListener(mTouchListener);
        mDecreaseButton.setOnTouchListener(mTouchListener);


        mIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOneToQuantity();
                mProductHasChanged = true;
            }
        });

        mDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractOneToQuantity();
                mProductHasChanged = true;
            }
        });

        mBtnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(
                        intent, "Select your picture"
                ), PICK_IMAGE_REQUEST);
            }
            });
        }

    private void subtractOneToQuantity() {
        String previousValue = mQuantityEditText.getText().toString();
        int previousValueInt;
        if (previousValue.isEmpty()) {
            return;
        } else if (previousValue.equals("0")) {
            return;
        } else {
            previousValueInt = Integer.parseInt(previousValue);
            mQuantityEditText.setText(String.valueOf(previousValueInt - 1));
        }
    }

    private void addOneToQuantity() {
        String previousValue = mQuantityEditText.getText().toString();
        int previousValueInt;
        if (previousValue.isEmpty()) {
            previousValueInt = 0;
        } else {
            previousValueInt = Integer.parseInt(previousValue);
        }
        mQuantityEditText.setText(String.valueOf(previousValueInt + 1));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE
        };

        return new CursorLoader(this,
                mCurrentProductUri,
                projection, null, null, null);

    }

    private void saveProduct() {

        String nameString = mNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String picture = mImageView.getDrawable().toString().trim();

        if (mCurrentProductUri == null &&
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(quantityString) &&
                TextUtils.isEmpty(priceString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameString);

        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);

        int image=Integer.parseInt(picture);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE, image);

        if (mCurrentProductUri == null) {

            Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "Saving product error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Product info saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values,
                    null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, "updating product error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "product updated", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                finish();
                return true;

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;

            case R.id.contact_provider:
                contactProvider();
                return true;

            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void contactProvider() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Need extra products");
        intent.putExtra(Intent.EXTRA_TEXT, "Dear Provider, please send us more stuff");
        Intent mail = Intent.createChooser(intent, null);
        startActivity(mail);

    }


    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);

            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);

            mNameEditText.setText(name);
            mQuantityEditText.setText(Integer.toString(quantity));
            mPriceEditText.setText(Integer.toString(price));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText("");
        mQuantityEditText.setText("");
        mPriceEditText.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to discard your changes?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Eiting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void deleteProduct() {

        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowsDeleted == 0) {

                Toast.makeText(this, "Error with deleting product", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
            }
        }
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==PICK_IMAGE_REQUEST&&resultCode== Activity.RESULT_OK){

            if (data!=null){
                Uri uri = data.getData();
                mImageView.setImageURI(uri);
                mImageView.invalidate();
            }
        }

    }
}
