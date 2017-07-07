package com.mainframevampire.ryan.wheretobuy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ProductsSQLiteHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "products.db";
    private static final int DB_VERSION = 1;
    //Products Table functionality
    public static final String PRODUCTS_TABLE = "PRODUCTS";
    public static final String COLUMN_ID ="ID";
    public static final String COLUMN_SHORT_NAME ="SHORT_NAME";
    public static final String COLUMN_LONG_NAME ="LONG_NAME";
    public static final String COLUMN_BRAND ="BRAND";
    public static final String COLUMN_LOWEST_PRICE = "LOWEST_PRICE";
    public static final String COLUMN_HIGHEST_PRICE = "HIGHEST_PRICE";
    public static final String COLUMN_WHICH_IS_LOWEST ="WHICH_IS_LOWEST";
    public static final String COLUMN_CMW_PRICE = "CMW_PRICE";
    public static final String COLUMN_PL_PRICE = "PL_PRICE";
    public static final String COLUMN_FL_PRICE = "FL_PRICE";
    public static final String COLUMN_TW_PRICE = "TW_PRICE";
    public static final String COLUMN_HW_PRICE = "HW_PRICE";
    public static final String COLUMN_CMW_URL = "CMW_URL";
    public static final String COLUMN_PL_URL = "PL_URL";
    public static final String COLUMN_FL_URL = "FL_URL";
    public static final String COLUMN_TW_URL = "TW_URL";
    public static final String COLUMN_HW_URL = "HW_URL";
    public static final String COLUMN_CUSTOMISE_FLAG = "CUSTOMISE_FLAG";
    public static final String COLUMN_RECOMMENDATION_FLAG = "RECOMMENDATION_FLAG";
    public static final String COLUMN_LAST_UPDATE_DATE = "LAST_UPDATE_DATE";
    public static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + PRODUCTS_TABLE + "("
            + COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_SHORT_NAME + " TEXT," +
                    COLUMN_LONG_NAME + " TEXT," +
                    COLUMN_BRAND + " TEXT," +
                    COLUMN_LOWEST_PRICE + " REAL," +
                    COLUMN_HIGHEST_PRICE + " REAL," +
                    COLUMN_WHICH_IS_LOWEST + " TEXT," +
                    COLUMN_CMW_PRICE + " REAL," +
                    COLUMN_PL_PRICE + " REAL," +
                    COLUMN_FL_PRICE + " REAL," +
                    COLUMN_TW_PRICE + " REAL," +
                    COLUMN_HW_PRICE + " REAL," +
                    COLUMN_CMW_URL + " TEXT," +
                    COLUMN_PL_URL + " TEXT," +
                    COLUMN_FL_URL + " TEXT," +
                    COLUMN_TW_URL + " TEXT," +
                    COLUMN_HW_URL + " TEXT," +
                    COLUMN_CUSTOMISE_FLAG + " TEXT," +
                    COLUMN_RECOMMENDATION_FLAG + " TEXT," +
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
