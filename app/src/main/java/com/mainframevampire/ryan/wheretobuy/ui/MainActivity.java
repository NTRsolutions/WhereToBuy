package com.mainframevampire.ryan.wheretobuy.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.adapters.GridAdapter;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.BioIsland;
import com.mainframevampire.ryan.wheretobuy.model.Blackmores;
import com.mainframevampire.ryan.wheretobuy.model.ListName;
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.model.Swisse;
import com.mainframevampire.ryan.wheretobuy.util.GetInfoFromWebsite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public static final String LIST_NAME = "LIST_NAME";
    public static final String FRAGMENT_NAME = "FRAGMENT_NAME";
    public static final String INDEX = "INDEX";
    public static final String IS_FIRST_RUN = "IS_FIRST_RUN";
    private ProgressDialog mProgressDialogFirstTime;
    private float mFloat = 0;
    private String mLastUpdateDate;
    private String mCurrentDate;
    private boolean mIsTablet;

    private ProgressBar mProgressBar;
    private ImageView mRefreshImageView;
    private TextView mRate;
    private TextView mLastUpdateDateTextView;
    private RecyclerView mBestChoiceRecyclerView;

    //define a custom intent action
    public static final String BROADCAST_ACTION = "com.mainframevampire.ryan.wheretobuy.BROADCAST";
    public static final String KEY_MESSAGE = "com.mainframevampire.ryan.wheretobuy.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsTablet = getResources().getBoolean(R.bool.is_tablet);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRefreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        mLastUpdateDateTextView = (TextView) findViewById(R.id.lastUpdateDate);
        mBestChoiceRecyclerView = (RecyclerView) findViewById(R.id.bestChoiceRecyclerView);
        mRate = (TextView) findViewById(R.id.rate);

        mProgressBar.setVisibility(View.INVISIBLE);

        ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
        mLastUpdateDate = dataSource.readProductsTableWithId("OST004").getLastUpdateDateString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //mCurrentDate = dateFormat.format(new Date());
        mCurrentDate = "2017-06-18";

        String lastUpdateSummary = getString(R.string.last_update_date_is) + " " + mLastUpdateDate;
        mLastUpdateDateTextView.setText(lastUpdateSummary);

        int countOfTable = dataSource.readProductsTableGetCount();
        if (isNetworkAvailable()) {
            new getRateInBackGround().execute();
            if (countOfTable == 0) {
                //if count == 0, then app first run
                toggleRefresh();
                new DownloadPriceFirstTime().execute();
            } else {
                //if last update date is not today, download the data in the background
                if(!lastUpdateIsToday()){
                    toggleRefresh();
                    //new DownloadPriceInBackground().execute();
                    String message = String.format("Last update is %s, the latest price is downloading", mLastUpdateDate);
                    mLastUpdateDateTextView.setText(message);
                    for (String brand : ListName.Brands) {
                        Intent intent = new Intent(this, DownloadService.class);
                        boolean isFirstRun = false;
                        intent.putExtra(IS_FIRST_RUN, isFirstRun);
                        intent.putExtra(LIST_NAME, brand);
                        startService(intent);
                    }
                }
                //load recommations to the list
                loadDataToGridList();
            }
        } else {
            if (countOfTable == 0) {
                mProgressDialogFirstTime = new ProgressDialog(MainActivity.this);
                mProgressDialogFirstTime.setCancelable(false);
                mProgressDialogFirstTime.setIndeterminate(false);
                mProgressDialogFirstTime.setTitle("please open your network");
                mProgressDialogFirstTime.setMessage("This is the app's first run, products' info needs to be downloaded");
                mProgressDialogFirstTime.show();
            }
            else {
                loadDataToGridList();
            }
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(KEY_MESSAGE);
            Log.d("MainActivity", "Receive Message: " + message);
            //load recommations to the list
            loadDataToGridList();
            if (message.equals("OSTELIN")) {
                toggleRefresh();
                String lastUpdateSummary = getString(R.string.last_update_date_is) + " " + mCurrentDate;
                mLastUpdateDateTextView.setText(lastUpdateSummary);
                mLastUpdateDateTextView.setTextColor(Color.BLACK);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ProductsActivity.class);
        ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
        int countCustomisedProducts = dataSource.readProductsTableToGetCustomisedProduct();
        int countBlackmoresProducts = dataSource.readProductsTableToGetBrandProduct("BKM");
        int countBioislandProducts = dataSource.readProductsTableToGetBrandProduct("BOI");
        int countOsterlinProducts = dataSource.readProductsTableToGetBrandProduct("OST");
        switch (item.getItemId()) {
            case R.id.swisse:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(LIST_NAME, "SWISSE");
                startActivity(intent);
                return true;
            case R.id.blackmores:
                if (countBlackmoresProducts != Blackmores.id.length) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("still downloading")
                            .setMessage("Please wait for Blackmores products' price to be downloaded");
                    builder.create().show();
                } else {
                    intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                    intent.putExtra(LIST_NAME, "BLACKMORES");
                    startActivity(intent);
                }
                return true;
            case R.id.bioIsland:
                if (countBioislandProducts != BioIsland.id.length) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("still downloading")
                            .setMessage("Please wait for BioIsland products' price to be downloaded");
                    builder.create().show();
                } else {
                    intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                    intent.putExtra(LIST_NAME, "BIOISLAND");
                    startActivity(intent);
                }
                return true;
            case R.id.ostelin:
                if (countOsterlinProducts != Ostelin.id.length) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("still downloading")
                            .setMessage("Please wait for Ostelin products' price to be downloaded");
                    builder.create().show();
                } else {
                    intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                    intent.putExtra(LIST_NAME, "OSTELIN");
                    startActivity(intent);
                }
                return true;
            case R.id.customise:
                if (countCustomisedProducts == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("No Products in MYLIST")
                            .setMessage("Please add your favourite products in each branch list");
                    builder.create().show();
                } else {
                    intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                    intent.putExtra(LIST_NAME, "MYLIST");
                    startActivity(intent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class DownloadPriceFirstTime extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialogFirstTime = new ProgressDialog(MainActivity.this);
            mProgressDialogFirstTime.setTitle("Downloading info from Websites");
            mProgressDialogFirstTime.setMessage("Products'information needs to be downloaded for app's first run");
            //mProgressDialogFirstTime.show();
            //mLastUpdateDateTextView.setText(R.string.text_for_first_run);
            //mLastUpdateDateTextView.setTextColor(Color.RED);
            mProgressDialogFirstTime.setCancelable(false);
            mProgressDialogFirstTime.setIndeterminate(false);
            mProgressDialogFirstTime.show();
            mLastUpdateDateTextView.setText(R.string.text_for_first_run);
            mLastUpdateDateTextView.setTextColor(Color.RED);
        }

        @Override
        protected Void doInBackground(Void... params) {
            GetInfoFromWebsite.getSwissePrice();
            createSwisseValueInTable();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialogFirstTime.dismiss();
            //load recommations to the list
            loadDataToGridList();
            for (String brand : ListName.Brands) {
                if (!brand.equals("SWISSE")) {
                    Intent intent = new Intent(MainActivity.this, DownloadService.class);
                    intent.putExtra(IS_FIRST_RUN, true);
                    intent.putExtra(LIST_NAME, brand);
                    startService(intent);
                }
            }
            mLastUpdateDateTextView.setText("Other products' price is still downloading");
        }
    }

    private class getRateInBackGround extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mFloat = GetInfoFromWebsite.getExchangeRate();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRate.setText("AUD/CNY:" + mFloat);
        }

    }

    private void createSwisseValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < Swisse.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(Swisse.lowestPrice[i], Swisse.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    Swisse.id[i],
                    Swisse.shortName[i],
                    Swisse.longName[i],
                    Swisse.lowestPrice[i],
                    Swisse.highestPrice[i],
                    Swisse.whichIsLowest[i],
                    Swisse.cmwPrice[i],
                    Swisse.plPrice[i],
                    Swisse.flPrice[i],
                    Swisse.twPrice[i],
                    Swisse.hwPrice[i],
                    "N",
                    recommendationFlag,
                    currentDateString);
            dataSource.createContents(productPrice);
        }
    }



    private String getRecomendationFlag(Float lowestPrice, Float highestPrice) {
        Float savePrice = highestPrice - lowestPrice;
        if (savePrice/highestPrice >= 0.4) {
            return "Y";
        } else {
            return "N";
        }
    }

    private boolean lastUpdateIsToday() {
        return mCurrentDate.equals(mLastUpdateDate);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void toggleRefresh(){
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void loadDataToGridList() {
        //get best choices
        ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
        ArrayList<ProductPrice> recommendedProcutPrices = dataSource.readProductsTableWithCondition("RECOMMENDATION_FLAG", "Y");
        GridAdapter RecommendedProductsAdapter = new GridAdapter(this, recommendedProcutPrices);

        mBestChoiceRecyclerView.setAdapter(RecommendedProductsAdapter);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numColumns = 0;
        if (!mIsTablet) {
            numColumns = (int) (dpWidth / 120);
        } else {
            numColumns = (int) (dpWidth / 240);
        }

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numColumns);
        mBestChoiceRecyclerView.setLayoutManager(layoutManager);
    }

}
