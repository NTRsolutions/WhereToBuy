package com.example.ryan.wheretobuy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ryan.wheretobuy.model.ProductPrice;

import java.util.ArrayList;

public class ProductsDataSource {
    private Context mContext;
    private ProductsSQLiteHelper mProductsSQLiteHelper;

    public ProductsDataSource(Context context){
        mContext = context;
        mProductsSQLiteHelper = new ProductsSQLiteHelper(context);
    }


    private SQLiteDatabase open() {
        return mProductsSQLiteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public void createContents(ProductPrice productPrice) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues productValues = new ContentValues();
        productValues.put(ProductsSQLiteHelper.COLUMN_ID, productPrice.getID());
        productValues.put(ProductsSQLiteHelper.COLUMN_SHORT_NAME, productPrice.getShortName());
        productValues.put(ProductsSQLiteHelper.COLUMN_LONG_NAME, productPrice.getLongName());
        productValues.put(ProductsSQLiteHelper.COLUMN_LOWEST_PRICE, productPrice.getLowestPrice());
        productValues.put(ProductsSQLiteHelper.COLUMN_HIGHEST_PRICE, productPrice.getHighestPrice());
        productValues.put(ProductsSQLiteHelper.COLUMN_WHICH_IS_LOWEST, productPrice.getWhichIsLowest());
        productValues.put(ProductsSQLiteHelper.COLUMN_CMW_PRICE, productPrice.getCMWPrice());
        productValues.put(ProductsSQLiteHelper.COLUMN_PL_PRICE, productPrice.getPLPrice());
        productValues.put(ProductsSQLiteHelper.COLUMN_FL_PRICE, productPrice.getFLPrice());
        productValues.put(ProductsSQLiteHelper.COLUMN_TW_PRICE, productPrice.getTWPrice());
        productValues.put(ProductsSQLiteHelper.COLUMN_HW_PRICE, productPrice.getHWPrice());
        productValues.put(ProductsSQLiteHelper.COLUMN_CUSTOMISE_FLAG, productPrice.getCustomiseFlag());
        productValues.put(ProductsSQLiteHelper.COLUMN_RECOMMENDATION_FLAG, productPrice.getRecommendationFlag());
        productValues.put(ProductsSQLiteHelper.COLUMN_LAST_UPDATE_DATE, productPrice.getLastUpdateDateString());

        database.insert(ProductsSQLiteHelper.PRODUCTS_TABLE, null, productValues);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public ProductPrice readProductsTableWithId(String id) {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                        " WHERE ID = " + "'" + id + "'", null);
        String shortName = "";
        String longName = "";
        float lowestPrice = 0;
        float highestPrice = 0;
        String whichIsLowest = "";
        float cmwPrice = 0;
        float plPrice = 0;
        float flPrice = 0;
        float twPrice = 0;
        float hwPrice = 0;
        String customiseFlag = "";
        String recommendationFlag = "";
        String lastUpdateDateString = "";
        if (cursor.moveToFirst()){
            do {
                shortName = getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_SHORT_NAME);
                longName = getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_LONG_NAME);
                lowestPrice = getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LOWEST_PRICE);
                highestPrice = getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HIGHEST_PRICE);
                whichIsLowest = getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_WHICH_IS_LOWEST);
                cmwPrice = getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CMW_PRICE);
                plPrice = getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_PL_PRICE);
                flPrice = getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_FL_PRICE);
                twPrice = getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_TW_PRICE);
                hwPrice = getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HW_PRICE);
                customiseFlag = getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_CUSTOMISE_FLAG);
                recommendationFlag = getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_RECOMMENDATION_FLAG);
                lastUpdateDateString = getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LAST_UPDATE_DATE);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        ProductPrice productPrice = new ProductPrice(id, shortName, longName, lowestPrice, highestPrice, whichIsLowest,
                cmwPrice, plPrice, flPrice, twPrice, hwPrice, customiseFlag, recommendationFlag, lastUpdateDateString);

        return productPrice;
    }

    public ArrayList<ProductPrice> readProductsTableWithListName(String listName) {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                        " WHERE SUBSTR(ID,1,3) = " + "'" + listName + "'", null);


        ArrayList<ProductPrice> productPrices = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ProductPrice productPrice = new ProductPrice(
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_ID),
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_SHORT_NAME),
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_LONG_NAME),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LOWEST_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HIGHEST_PRICE),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_WHICH_IS_LOWEST),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CMW_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_PL_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_FL_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_TW_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HW_PRICE),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CUSTOMISE_FLAG),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_RECOMMENDATION_FLAG),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LAST_UPDATE_DATE));
                productPrices.add(productPrice);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return productPrices;
    }

    public ArrayList<ProductPrice> readProductsTableWithCustomisedFlag(String CustomisedFlag) {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                    "SELECT * FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                            " WHERE CUSTOMISE_FLAG = " + "'" + CustomisedFlag + "'", null);

        ArrayList<ProductPrice> productPrices = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ProductPrice productPrice = new ProductPrice(
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_ID),
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_SHORT_NAME),
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_LONG_NAME),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LOWEST_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HIGHEST_PRICE),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_WHICH_IS_LOWEST),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CMW_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_PL_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_FL_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_TW_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HW_PRICE),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CUSTOMISE_FLAG),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_RECOMMENDATION_FLAG),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LAST_UPDATE_DATE));
                productPrices.add(productPrice);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return productPrices;
    }

    public ArrayList<ProductPrice> readProductsTableWithRecommendationFlag(String recommendationFlag) {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                        " WHERE RECOMMENDATION_FLAG = " + "'" + recommendationFlag + "'", null);

        ArrayList<ProductPrice> productPrices = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ProductPrice productPrice = new ProductPrice(
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_ID),
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_SHORT_NAME),
                        getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_LONG_NAME),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LOWEST_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HIGHEST_PRICE),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_WHICH_IS_LOWEST),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CMW_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_PL_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_FL_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_TW_PRICE),
                        getFloatFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HW_PRICE),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CUSTOMISE_FLAG),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_RECOMMENDATION_FLAG),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LAST_UPDATE_DATE));
                productPrices.add(productPrice);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return productPrices;
    }

    private String getStringFromColumnName(Cursor cursor, String ColumnName) {
        int columnIndex = cursor.getColumnIndex(ColumnName);
        return cursor.getString(columnIndex);
    }

    private float getFloatFromColumnName(Cursor cursor, String ColumnName) {
        int columnIndex = cursor.getColumnIndex(ColumnName);
        return cursor.getFloat(columnIndex);
    }

    public void updatePriceInTable(ProductPrice productPrice) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues updateProductValue = new ContentValues();
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_LOWEST_PRICE, productPrice.getLowestPrice());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_HIGHEST_PRICE, productPrice.getHighestPrice());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_WHICH_IS_LOWEST, productPrice.getWhichIsLowest());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_CMW_PRICE, productPrice.getCMWPrice());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_PL_PRICE, productPrice.getPLPrice());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_FL_PRICE, productPrice.getFLPrice());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_TW_PRICE, productPrice.getTWPrice());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_HW_PRICE, productPrice.getHWPrice());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_LAST_UPDATE_DATE, productPrice.getLastUpdateDateString());


        database.update(ProductsSQLiteHelper.PRODUCTS_TABLE,
                updateProductValue,
                " ID = " + "'" + productPrice.getID() + "'",
                null);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void updateCustomiseFlagInTable(String id, String customiseFlag) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues updateProductValue = new ContentValues();
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_CUSTOMISE_FLAG, customiseFlag);


        database.update(ProductsSQLiteHelper.PRODUCTS_TABLE,
                updateProductValue,
                " ID = " + "'" + id + "'",
                null);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
