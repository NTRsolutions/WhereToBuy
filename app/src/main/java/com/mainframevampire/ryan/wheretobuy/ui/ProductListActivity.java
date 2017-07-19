package com.mainframevampire.ryan.wheretobuy.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.adapters.ListAdapter;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.BioIsland;
import com.mainframevampire.ryan.wheretobuy.model.Blackmores;
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.util.ConfigHelper;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private ArrayList<ProductPrice> mProductPrices;
    private ListAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private TextView mHeader;
    private Handler mHandler;

    private int mTotalCounts = 0;
    private int mNumRows = 0;
    private int mNumForOneQuery = 0;

    private String mClickProductID = " ";
    private int mClickPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //get List Name
        Intent intent = getIntent();
        String itemName = intent.getStringExtra(MainActivity.LIST_NAME);

        //set actionbar title
        setTitle(itemName);

        mHandler = new Handler();
        mHeader = (TextView) findViewById(R.id.products_header);
        mRecyclerView = (RecyclerView) findViewById(R.id.productsRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        //Load data
        refreshProductList(itemName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mClickProductID.equals(" ") && mClickPosition != -1) {
            ProductsDataSource dataSource = new ProductsDataSource(this);
            final ProductPrice productPrice = dataSource.readProductsTableWithId(mClickProductID);
            mProductPrices.get(mClickPosition).setCustomiseFlag(productPrice.getCustomiseFlag());
            mListAdapter.notifyDataSetChanged();
        }
    }

    //hide share menu item
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem shareItem = menu.findItem(R.id.share);
        shareItem.setVisible(false);
        MenuItem postFacebookItem = menu.findItem(R.id.post_facebook);
        postFacebookItem.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ProductsDataSource dataSource = new ProductsDataSource(ProductListActivity.this);
        int countCustomisedProducts = dataSource.readTableGetCustomisedCount();
        int countBlackmoresProducts = dataSource.readTableGetBrandCount("Blackmores");
        int countBioislandProducts = dataSource.readTableGetBrandCount("BioIsland");
        int countOsterlinProducts = dataSource.readTableGetBrandCount("Ostelin");
        switch (item.getItemId()) {
            case R.id.swisse:
                setTitle("Swisse");
                mHandler.removeCallbacksAndMessages(null);
                refreshProductList("Swisse");
                return true;
            case R.id.blackmores:
                if (countBlackmoresProducts != Blackmores.id.length) {
                    //if no Blackmores products, show alert
                    showBrandAlertDailog("Blackmores");
                } else {
                    setTitle("Blackmores");
                    mHandler.removeCallbacksAndMessages(null);
                    refreshProductList("Blackmores");
                }
                return true;
            case R.id.bioIsland:
                if (countBioislandProducts != BioIsland.id.length) {
                    //if no BioIsland products, show alert
                    showBrandAlertDailog("BioIsland");
                } else {
                    setTitle("BioIsland");
                    mHandler.removeCallbacksAndMessages(null);
                    refreshProductList("BioIsland");
                }
                return true;
            case R.id.ostelin:
                if (countOsterlinProducts != Ostelin.id.length) {
                    //if no Oetelin products, show alert
                    showBrandAlertDailog("Ostelin");
                } else {
                    setTitle("Ostelin");
                    mHandler.removeCallbacksAndMessages(null);
                    refreshProductList("Ostelin");
                }
                return true;
            case R.id.customise:
                if (countCustomisedProducts == 0) {
                    //if no products in MYLIST, show alert
                    showMyListAlertDailog();
                } else {
                    setTitle("MyList");
                    mHandler.removeCallbacksAndMessages(null);
                    refreshProductList("MyList");
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshProductList(final String itemName) {
        final ProductsDataSource dataSource = new ProductsDataSource(this);
        //get the total count
        if (itemName.equals("MyList")) {
            mTotalCounts = dataSource.readTableGetCustomisedCount();
        } else {
            mTotalCounts = dataSource.readTableGetBrandCount(itemName);
        }

        //get the item number of one page for different screen size and oritention.
        mNumRows = ConfigHelper.getNumberRowsForList(this);
        mNumForOneQuery = mNumRows + 5;

        //load first page data
        if (itemName.equals("MyList")) {
            mProductPrices = dataSource.readTableByCustomiseFlag("Y", mNumForOneQuery, " ");
        } else {
            mProductPrices = dataSource.readTableByBrand(itemName, mNumForOneQuery, " ");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mListAdapter = new ListAdapter(mProductPrices, itemName, mRecyclerView, this);
        mRecyclerView.setAdapter(mListAdapter);

        mListAdapter.setOnLoadListener(new ListAdapter.OnLoadListener() {
            @Override
            public void onLoadHeader() {
                if (mTotalCounts >= mNumRows) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHeader.setVisibility(View.VISIBLE);
                        }
                    });

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mHeader.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onLoadData() {
                if (mTotalCounts >= mNumRows) {
                    //add progress item
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProductPrices.add(null);
                            mListAdapter.notifyItemInserted(mProductPrices.size() - 1);
                        }
                    });

                    final ProductsDataSource dataSource = new ProductsDataSource(ProductListActivity.this);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //remove progress item
                            mProductPrices.remove(mProductPrices.size() - 1);
                            mListAdapter.notifyItemRemoved(mProductPrices.size());

                            if (mProductPrices.size() == mTotalCounts) {
                                mListAdapter.setLoaded();
                            } else {
                                ArrayList<ProductPrice> productPrices;
                                String prevProductID = mProductPrices.get(mProductPrices.size() - 1).getID();
                                if (itemName.equals("MyList")) {
                                    productPrices = dataSource.readTableByCustomiseFlag("Y", mNumForOneQuery, prevProductID);
                                } else {
                                    productPrices = dataSource.readTableByBrand(itemName, mNumForOneQuery, prevProductID);
                                }
                                for (ProductPrice productPrice : productPrices) {
                                    mProductPrices.add(productPrice);
                                }
                                mListAdapter.notifyItemInserted(mProductPrices.size() - 1);
                                mListAdapter.setLoaded();
                            }
                        }
                    }, 2000);
                }
            }
        });
    }

    private void showBrandAlertDailog(String brand) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        String message = String.format("Please wait for %s products' price to be downloaded", brand);
        builder.setTitle("still downloading")
                .setMessage(message);
        builder.create().show();
    }

    private void showMyListAlertDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        builder.setTitle("No Products in MyList")
                .setMessage("Please add your favourite products in each branch list");
        builder.create().show();
    }

    public void onItemClick(String productID, int position) {
        mClickProductID = productID;
        mClickPosition = position;
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.PRODUCT_ID, productID);
        startActivity(intent);
    }




}
