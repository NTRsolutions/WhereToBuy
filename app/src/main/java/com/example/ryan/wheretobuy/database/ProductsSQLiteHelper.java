package com.example.ryan.wheretobuy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2017/5/30.
 */

public class ProductsSQLiteHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "products.db";
    private static final int DB_VERSION = 1;
    //Products Table functionality
    public static final String PRODUCTS_TABLE = "PRODUCTS";
    public static final String COLUMN_ID ="ID";
    public static final String COLUMN_LOWEST_PRICE = "LOWEST_PRICE";
    public static final String COLUMN_HIGHEST_PRICE = "HIGHEST_PRICE";
    public static final String COLUMN_WHICH_IS_LOWEST ="WHICH_IS_LOWEST";
    public static final String COLUMN_CMW_PRICE = "CMW_PRICE";
    public static final String COLUMN_PL_PRICE = "PL_PRICE";
    public static final String COLUMN_FL_PRICE = "FL_PRICE";
    public static final String COLUMN_TW_PRICE = "TW_PRICE";
    public static final String COLUMN_HW_PRICE = "HW_PRICE";
    public static final String COLUMN_LAST_UPDATE_DATE = "LAST_UPDATE_DATE";
    public static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + PRODUCTS_TABLE + "("
            + COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_LOWEST_PRICE + " REAL," +
                    COLUMN_HIGHEST_PRICE + " REAL," +
                    COLUMN_WHICH_IS_LOWEST + " TEXT," +
                    COLUMN_CMW_PRICE + " REAL," +
                    COLUMN_PL_PRICE + " REAL," +
                    COLUMN_FL_PRICE + " REAL," +
                    COLUMN_TW_PRICE + " REAL," +
                    COLUMN_HW_PRICE + " REAL," +
                    COLUMN_LAST_UPDATE_DATE + " TEXT)";

    public ProductsSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
