package com.mainframevampire.ryan.wheretobuy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;

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
        productValues.put(ProductsSQLiteHelper.COLUMN_CMW_URL, productPrice.getCMWUrl());
        productValues.put(ProductsSQLiteHelper.COLUMN_PL_URL, productPrice.getPLUrl());
        productValues.put(ProductsSQLiteHelper.COLUMN_FL_URL, productPrice.getFLUrl());
        productValues.put(ProductsSQLiteHelper.COLUMN_TW_URL, productPrice.getTWUrl());
        productValues.put(ProductsSQLiteHelper.COLUMN_HW_URL, productPrice.getHWUrl());
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
        String cmwUrl = "";
        String plUrl = "";
        String flUrl = "";
        String twUrl = "";
        String hwUrl = "";
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
                cmwUrl = getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CMW_URL);
                plUrl = getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_PL_URL);
                flUrl = getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_FL_URL);
                twUrl = getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_TW_URL);
                hwUrl = getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_HW_URL);
                customiseFlag = getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_CUSTOMISE_FLAG);
                recommendationFlag = getStringFromColumnName(cursor,ProductsSQLiteHelper.COLUMN_RECOMMENDATION_FLAG);
                lastUpdateDateString = getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_LAST_UPDATE_DATE);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        ProductPrice productPrice = new ProductPrice(id, shortName, longName, lowestPrice, highestPrice, whichIsLowest,
                cmwPrice, plPrice, flPrice, twPrice, hwPrice,
                cmwUrl, plUrl, flUrl, twUrl, hwUrl,
                customiseFlag, recommendationFlag, lastUpdateDateString);

        return productPrice;
    }

    public ArrayList<ProductPrice> readProductsTableWithCondition(String ColumnName, String condition) {
        String queryString = getQueryWithCondition(ColumnName, condition);

        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(queryString, null);


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
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_CMW_URL),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_PL_URL),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_FL_URL),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_TW_URL),
                        getStringFromColumnName(cursor, ProductsSQLiteHelper.COLUMN_PL_URL),
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

    private String getQueryWithCondition(String columnName, String condition) {
        String queryString = "";
        if (columnName.equals("ID")) {
            String listName = condition.substring(0,3);
            queryString = "SELECT * FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                    " WHERE SUBSTR(ID,1,3) = " + "'" + listName + "'";
        }
        if (columnName.equals("CUSTOMISE_FLAG")) {
            queryString = "SELECT * FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                    " WHERE CUSTOMISE_FLAG = " + "'" + condition + "'";
        }
        if (columnName.equals("RECOMMENDATION_FLAG")) {
            queryString = "SELECT * FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                    " WHERE RECOMMENDATION_FLAG = " + "'" + condition + "'";
        }

        return queryString;
    }

    public int readProductsTableToGetBrandProduct(String listName) {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                " WHERE SUBSTR(ID,1,3) = " + "'" + listName + "'", null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        close(database);

        return count;
    }

    public int readProductsTableToGetCustomisedProduct() {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE +
                " WHERE CUSTOMISE_FLAG = " + "'Y'", null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        close(database);

        return count;
    }

    public int readProductsTableGetCount() {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ProductsSQLiteHelper.PRODUCTS_TABLE, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        close(database);

        return count;
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
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_CMW_URL, productPrice.getCMWUrl());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_PL_URL, productPrice.getPLUrl());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_FL_URL, productPrice.getFLUrl());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_TW_URL, productPrice.getTWUrl());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_HW_URL, productPrice.getHWUrl());
        updateProductValue.put(ProductsSQLiteHelper.COLUMN_RECOMMENDATION_FLAG, productPrice.getRecommendationFlag());
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
