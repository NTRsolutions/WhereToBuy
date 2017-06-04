package com.example.ryan.wheretobuy.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import com.example.ryan.wheretobuy.R;
import com.example.ryan.wheretobuy.adapters.BestChoicesAdapter;
import com.example.ryan.wheretobuy.database.ProductsDataSource;
import com.example.ryan.wheretobuy.model.BestChoice;
import com.example.ryan.wheretobuy.model.BioIsland;
import com.example.ryan.wheretobuy.model.Blackmores;
import com.example.ryan.wheretobuy.model.Ostelin;
import com.example.ryan.wheretobuy.model.ProductPrice;
import com.example.ryan.wheretobuy.model.Swisse;
import com.example.ryan.wheretobuy.util.GetInfoFromWebsite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String ITEM_NAME = "ITEM_NAME";
    public static final String FRAGMENT_NAME = "FRAGMENT_NAME";
    public static final String INDEX = "INDEX";
    private ProgressDialog mProgressDialogFirstTime;
    private SharedPreferences prefs = null;
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
        ButterKnife.bind(this);

        mIsTablet = getResources().getBoolean(R.bool.is_tablet);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRefreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        mLastUpdateDateTextView = (TextView) findViewById(R.id.lastUpdateDate);
        mBestChoiceRecyclerView = (RecyclerView) findViewById(R.id.bestChoiceRecyclerView);
        mRate = (TextView) findViewById(R.id.rate);

        mProgressBar.setVisibility(View.INVISIBLE);

        ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
        mLastUpdateDate = dataSource.readProductsTable("SWS001").getLastUpdateDateString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = dateFormat.format(new Date());

        String lastUpdateSummary = getString(R.string.last_update_date_is) + " " + mLastUpdateDate;
        mLastUpdateDateTextView.setText(lastUpdateSummary);


        if (isNetworkAvailable()) {
            new getRateInBackGround().execute();
        } else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
        }

        prefs = getSharedPreferences("com.example.ryan.wheretobuy", MODE_PRIVATE);
        //if firsr run
        if (prefs.getBoolean("firstrun", true)){
            if (isNetworkAvailable()) {
                toggleRefresh();
                new DownloadPriceFirstTime().execute();
                prefs.edit().putBoolean("firstrun", false).apply();
            } else {
                mProgressDialogFirstTime = new ProgressDialog(MainActivity.this);
                mProgressDialogFirstTime.setTitle("please open your network");
                mProgressDialogFirstTime.setMessage("This is the app's first run, products' info needs to be downloaded");
                mProgressDialogFirstTime.show();
                prefs.edit().putBoolean("firstrun", true).apply();
            }
        } else {
            //if last update date is not today, download the data in the background
            if(!lastUpdateIsToday()){
                if (isNetworkAvailable()) {
                    toggleRefresh();
                    new DownloadPriceInBackground().execute();
                    getProductsPriceFromDatabase();
                    loadDataToGridList();
                }
            } else {
                //if last update date is today, get the data from the database
                getProductsPriceFromDatabase();
                //load recommations to the list
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
        switch (item.getItemId()) {
            case R.id.swisse:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(ITEM_NAME, "SWISSE");
                startActivity(intent);
                return true;
            case R.id.blackmores:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(ITEM_NAME, "BLACKMORES");
                startActivity(intent);
                return true;
            case R.id.bioIsland:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(ITEM_NAME, "BIOISLAND");
                startActivity(intent);
                return true;
            case R.id.ostelin:
                intent.putExtra(FRAGMENT_NAME, "FRAGMENT_PRODUCTS");
                intent.putExtra(ITEM_NAME, "OSTELIN");
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadPriceFirstTime extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialogFirstTime = new ProgressDialog(MainActivity.this);
            mProgressDialogFirstTime.setTitle("Downloading info from Websites");
            mProgressDialogFirstTime.setMessage("Products'information needs to be downloaded for app's first run");
            mProgressDialogFirstTime.show();
            mLastUpdateDateTextView.setText(R.string.text_for_first_run);
            mLastUpdateDateTextView.setTextColor(Color.RED);
        }

        @Override
        protected Void doInBackground(Void... params) {
            GetInfoFromWebsite.GetSwissePrice();
            GetInfoFromWebsite.GetBlackmoresPrice();
            GetInfoFromWebsite.GetBioIslandPrice();
            GetInfoFromWebsite.GetOstelinPrice();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialogFirstTime.dismiss();
            //app first run, load all products' price to database
            createValueInTable();
            toggleRefresh();
            //load recommations to the list
            loadDataToGridList();
            mLastUpdateDateTextView.setText("Last Update Date is " + mCurrentDate );
            mLastUpdateDateTextView.setTextColor(Color.BLACK);
        }
    }

    private class DownloadPriceInBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLastUpdateDateTextView.setText("Last update is " + mLastUpdateDate + ", the latest price is downloading");
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
            mLastUpdateDateTextView.setText("Last Update Date is " + mCurrentDate );
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
                    currentDateString);
            dataSource.createContents(productPrice);
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
                    currentDateString);
            dataSource.createContents(productPrice);
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
                    currentDateString);
            dataSource.createContents(productPrice);
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
                    currentDateString);
            dataSource.createContents(productPrice);
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
            dataSource.updateProductsTable(productPrice);
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
            dataSource.updateProductsTable(productPrice);
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
            dataSource.updateProductsTable(productPrice);
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
            dataSource.updateProductsTable(productPrice);
        }


    }

    private void getProductsPriceFromDatabase() {


        for (int i = 0; i < Swisse.id.length; i++) {
            ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
            Swisse.lowestPrice[i] = dataSource.readProductsTable(Swisse.id[i]).getLowestPrice();
            Swisse.highestPrice[i] = dataSource.readProductsTable(Swisse.id[i]).getHighestPrice();
            Swisse.whichIsLowest[i] = dataSource.readProductsTable(Swisse.id[i]).getWhichIsLowest();
            Swisse.cmwPrice[i] = dataSource.readProductsTable(Swisse.id[i]).getCMWPrice();
            Swisse.plPrice[i] = dataSource.readProductsTable(Swisse.id[i]).getPLPrice();
            Swisse.flPrice[i] = dataSource.readProductsTable(Swisse.id[i]).getFLPrice();
            Swisse.twPrice[i] = dataSource.readProductsTable(Swisse.id[i]).getTWPrice();
            Swisse.hwPrice[i] = dataSource.readProductsTable(Swisse.id[i]).getHWPrice();
        }

        for (int i = 0; i < Blackmores.id.length; i++) {
            ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
            Blackmores.lowestPrice[i] = dataSource.readProductsTable(Blackmores.id[i]).getLowestPrice();
            Blackmores.highestPrice[i] = dataSource.readProductsTable(Blackmores.id[i]).getHighestPrice();
            Blackmores.whichIsLowest[i] = dataSource.readProductsTable(Blackmores.id[i]).getWhichIsLowest();
            Blackmores.cmwPrice[i] = dataSource.readProductsTable(Blackmores.id[i]).getCMWPrice();
            Blackmores.plPrice[i] = dataSource.readProductsTable(Blackmores.id[i]).getPLPrice();
            Blackmores.flPrice[i] = dataSource.readProductsTable(Blackmores.id[i]).getFLPrice();
            Blackmores.twPrice[i] = dataSource.readProductsTable(Blackmores.id[i]).getTWPrice();
            Blackmores.hwPrice[i] = dataSource.readProductsTable(Blackmores.id[i]).getHWPrice();
        }

        for (int i = 0; i < BioIsland.id.length; i++) {
            ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
            BioIsland.lowestPrice[i] = dataSource.readProductsTable(BioIsland.id[i]).getLowestPrice();
            BioIsland.highestPrice[i] = dataSource.readProductsTable(BioIsland.id[i]).getHighestPrice();
            BioIsland.whichIsLowest[i] = dataSource.readProductsTable(BioIsland.id[i]).getWhichIsLowest();
            BioIsland.cmwPrice[i] = dataSource.readProductsTable(BioIsland.id[i]).getCMWPrice();
            BioIsland.plPrice[i] = dataSource.readProductsTable(BioIsland.id[i]).getPLPrice();
            BioIsland.flPrice[i] = dataSource.readProductsTable(BioIsland.id[i]).getFLPrice();
            BioIsland.twPrice[i] = dataSource.readProductsTable(BioIsland.id[i]).getTWPrice();
            BioIsland.hwPrice[i] = dataSource.readProductsTable(BioIsland.id[i]).getHWPrice();
        }

        for (int i = 0; i < Ostelin.id.length; i++) {
            ProductsDataSource dataSource = new ProductsDataSource(MainActivity.this);
            Ostelin.lowestPrice[i] = dataSource.readProductsTable(Ostelin.id[i]).getLowestPrice();
            Ostelin.highestPrice[i] = dataSource.readProductsTable(Ostelin.id[i]).getHighestPrice();
            Ostelin.whichIsLowest[i] = dataSource.readProductsTable(Ostelin.id[i]).getWhichIsLowest();
            Ostelin.cmwPrice[i] = dataSource.readProductsTable(Ostelin.id[i]).getCMWPrice();
            Ostelin.plPrice[i] = dataSource.readProductsTable(Ostelin.id[i]).getPLPrice();
            Ostelin.flPrice[i] = dataSource.readProductsTable(Ostelin.id[i]).getFLPrice();
            Ostelin.twPrice[i] = dataSource.readProductsTable(Ostelin.id[i]).getTWPrice();
            Ostelin.hwPrice[i] = dataSource.readProductsTable(Ostelin.id[i]).getHWPrice();
        }

    }

    private boolean lastUpdateIsToday() {
        return mCurrentDate.equals(mLastUpdateDate);
    }

    public static ArrayList<BestChoice> GetBestChoices() {
        ArrayList<BestChoice> bestChoices = new ArrayList<>();
        for (int i = 0; i < Swisse.id.length; i++) {
            float savePrice = Swisse.highestPrice[i] - Swisse.lowestPrice[i];
            if (savePrice/Swisse.highestPrice[i] >= 0.4) {
                BestChoice bestChoice = new BestChoice(
                        Swisse.id[i],
                        Swisse.longName[i],
                        Swisse.lowestPrice[i],
                        Swisse.whichIsLowest[i],
                        Swisse.highestPrice[i]);
                bestChoices.add(bestChoice);
            }
        }
        for (int i = 0; i < Blackmores.id.length; i++) {
            float savePrice = Blackmores.highestPrice[i] - Blackmores.lowestPrice[i];
            if (savePrice/Blackmores.highestPrice[i] >= 0.4) {
                BestChoice bestChoice = new BestChoice(
                        Blackmores.id[i],
                        Blackmores.longName[i],
                        Blackmores.lowestPrice[i],
                        Blackmores.whichIsLowest[i],
                        Blackmores.highestPrice[i]);
                bestChoices.add(bestChoice);
            }
        }

        for (int i = 0; i < BioIsland.id.length; i++) {
            float savePrice = BioIsland.highestPrice[i] - BioIsland.lowestPrice[i];
            if (savePrice/BioIsland.highestPrice[i] >= 0.4) {
                BestChoice bestChoice = new BestChoice(
                        BioIsland.id[i],
                        BioIsland.longName[i],
                        BioIsland.lowestPrice[i],
                        BioIsland.whichIsLowest[i],
                        BioIsland.highestPrice[i]);
                bestChoices.add(bestChoice);
            }
        }

        for (int i = 0; i < Ostelin.id.length; i++) {
            float savePrice = Ostelin.highestPrice[i] - Ostelin.lowestPrice[i];
            if (savePrice/Ostelin.highestPrice[i] >= 0.4) {
                BestChoice bestChoice = new BestChoice(
                        Ostelin.id[i],
                        Ostelin.longName[i],
                        Ostelin.lowestPrice[i],
                        Ostelin.whichIsLowest[i],
                        Ostelin.highestPrice[i]);
                bestChoices.add(bestChoice);
            }
        }
        
        
        return bestChoices;
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
        ArrayList<BestChoice> bestChoices = GetBestChoices();
        BestChoicesAdapter bestChoicesAdapter = new BestChoicesAdapter(this, bestChoices);

        mBestChoiceRecyclerView.setAdapter(bestChoicesAdapter);
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
