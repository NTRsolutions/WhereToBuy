package com.mainframevampire.ryan.wheretobuy.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.model.Swisse;
import com.mainframevampire.ryan.wheretobuy.util.GetInfoFromWebsite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.id.progress;


public class MainActivity extends AppCompatActivity {
    public static final String LIST_NAME = "LIST_NAME";
    public static final String FRAGMENT_NAME = "FRAGMENT_NAME";
    public static final String INDEX = "INDEX";
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
        mLastUpdateDate = dataSource.readProductsTableWithId("SWS001").getLastUpdateDateString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = dateFormat.format(new Date());

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
                    if (isNetworkAvailable()) {
                        toggleRefresh();
                        new DownloadPriceInBackground().execute();
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
        switch (item.getItemId()) {
            case R.id.swisse:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(LIST_NAME, "SWISSE");
                startActivity(intent);
                return true;
            case R.id.blackmores:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(LIST_NAME, "BLACKMORES");
                startActivity(intent);
                return true;
            case R.id.bioIsland:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(LIST_NAME, "BIOISLAND");
                startActivity(intent);
                return true;
            case R.id.ostelin:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(LIST_NAME, "OSTELIN");
                startActivity(intent);
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

        int countOfProducts = Swisse.id.length + Blackmores.id.length + BioIsland.id.length + Ostelin.id.length;

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
            mProgressDialogFirstTime.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialogFirstTime.setMax(100);
            mProgressDialogFirstTime.show();
            mLastUpdateDateTextView.setText(R.string.text_for_first_run);
            mLastUpdateDateTextView.setTextColor(Color.RED);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialogFirstTime.setProgress(progress[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            GetInfoFromWebsite.GetSwissePrice();
            publishProgress((int) ((Swisse.id.length
                    / (float) countOfProducts) * 100));
            GetInfoFromWebsite.GetBlackmoresPrice();
            publishProgress((int) ((Blackmores.id.length + Swisse.id.length
                    / (float) countOfProducts) * 100));
            GetInfoFromWebsite.GetBioIslandPrice();
            publishProgress((int) ((BioIsland.id.length + Blackmores.id.length + Swisse.id.length
                    / (float) countOfProducts) * 100));
            GetInfoFromWebsite.GetOstelinPrice();
            publishProgress((int) ((Ostelin.id.length + BioIsland.id.length + Blackmores.id.length + Swisse.id.length
                    / (float) countOfProducts) * 100));
            createValueInTable();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialogFirstTime.dismiss();
            //app first run, load all products' price to database
            toggleRefresh();
            //load recommations to the list
            loadDataToGridList();
            String lastUpdateSummary = getString(R.string.last_update_date_is) + " " + mCurrentDate;
            mLastUpdateDateTextView.setText(lastUpdateSummary);
            mLastUpdateDateTextView.setTextColor(Color.BLACK);
        }
    }

    private class DownloadPriceInBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String message = String.format("Last update is %s, the latest price is downloading", mLastUpdateDate);
            mLastUpdateDateTextView.setText(message);
        }

        @Override
        protected Void doInBackground(Void... params) {
            GetInfoFromWebsite.GetSwissePrice();
            GetInfoFromWebsite.GetBlackmoresPrice();
            GetInfoFromWebsite.GetBioIslandPrice();
            GetInfoFromWebsite.GetOstelinPrice();
            //update all products' price to database
            updateValueInTable();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            toggleRefresh();
            //load recommations to the list
            loadDataToGridList();
            Toast.makeText(MainActivity.this, "lastest price information was updated", Toast.LENGTH_SHORT).show();
            String lastUpdateSummary = getString(R.string.last_update_date_is) + " " + mCurrentDate;
            mLastUpdateDateTextView.setText(lastUpdateSummary);
            mLastUpdateDateTextView.setTextColor(Color.BLACK);
        }

    }

    private class getRateInBackGround extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mFloat = GetInfoFromWebsite.GetExchangeRate();
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

    private void createValueInTable() {
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
        for (int i = 0; i < Blackmores.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(Blackmores.lowestPrice[i], Blackmores.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    Blackmores.id[i],
                    Blackmores.shortName[i],
                    Blackmores.longName[i],
                    Blackmores.lowestPrice[i],
                    Blackmores.highestPrice[i],
                    Blackmores.whichIsLowest[i],
                    Blackmores.cmwPrice[i],
                    Blackmores.plPrice[i],
                    Blackmores.flPrice[i],
                    Blackmores.twPrice[i],
                    Blackmores.hwPrice[i],
                    "N",
                    recommendationFlag,
                    currentDateString);
            dataSource.createContents(productPrice);
        }
        for (int i = 0; i < BioIsland.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(BioIsland.lowestPrice[i], BioIsland.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    BioIsland.id[i],
                    BioIsland.shortName[i],
                    BioIsland.longName[i],
                    BioIsland.lowestPrice[i],
                    BioIsland.highestPrice[i],
                    BioIsland.whichIsLowest[i],
                    BioIsland.cmwPrice[i],
                    BioIsland.plPrice[i],
                    BioIsland.flPrice[i],
                    BioIsland.twPrice[i],
                    BioIsland.hwPrice[i],
                    "N",
                    recommendationFlag,
                    currentDateString);
            dataSource.createContents(productPrice);
        }
        for (int i = 0; i < Ostelin.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(Ostelin.lowestPrice[i], Ostelin.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    Ostelin.id[i],
                    Ostelin.shortName[i],
                    Ostelin.longName[i],
                    Ostelin.lowestPrice[i],
                    Ostelin.highestPrice[i],
                    Ostelin.whichIsLowest[i],
                    Ostelin.cmwPrice[i],
                    Ostelin.plPrice[i],
                    Ostelin.flPrice[i],
                    Ostelin.twPrice[i],
                    Ostelin.hwPrice[i],
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

    private void updateValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < Swisse.id.length; i++) {
            ProductPrice productPrice = new ProductPrice(
                    Swisse.id[i],
                    Swisse.lowestPrice[i],
                    Swisse.highestPrice[i],
                    Swisse.whichIsLowest[i],
                    Swisse.cmwPrice[i],
                    Swisse.plPrice[i],
                    Swisse.flPrice[i],
                    Swisse.twPrice[i],
                    Swisse.hwPrice[i],
                    currentDateString
            );
            dataSource.updatePriceInTable(productPrice);
        }

        for (int i = 0; i < Blackmores.id.length; i++) {
            ProductPrice productPrice = new ProductPrice(
                    Blackmores.id[i],
                    Blackmores.lowestPrice[i],
                    Blackmores.highestPrice[i],
                    Blackmores.whichIsLowest[i],
                    Blackmores.cmwPrice[i],
                    Blackmores.plPrice[i],
                    Blackmores.flPrice[i],
                    Blackmores.twPrice[i],
                    Blackmores.hwPrice[i],
                    currentDateString
            );
            dataSource.updatePriceInTable(productPrice);
        }

        for (int i = 0; i < BioIsland.id.length; i++) {
            ProductPrice productPrice = new ProductPrice(
                    BioIsland.id[i],
                    BioIsland.lowestPrice[i],
                    BioIsland.highestPrice[i],
                    BioIsland.whichIsLowest[i],
                    BioIsland.cmwPrice[i],
                    BioIsland.plPrice[i],
                    BioIsland.flPrice[i],
                    BioIsland.twPrice[i],
                    BioIsland.hwPrice[i],
                    currentDateString
            );
            dataSource.updatePriceInTable(productPrice);
        }

        for (int i = 0; i < Ostelin.id.length; i++) {
            ProductPrice productPrice = new ProductPrice(
                    Ostelin.id[i],
                    Ostelin.lowestPrice[i],
                    Ostelin.highestPrice[i],
                    Ostelin.whichIsLowest[i],
                    Ostelin.cmwPrice[i],
                    Ostelin.plPrice[i],
                    Ostelin.flPrice[i],
                    Ostelin.twPrice[i],
                    Ostelin.hwPrice[i],
                    currentDateString
            );
            dataSource.updatePriceInTable(productPrice);
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
