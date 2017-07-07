package com.mainframevampire.ryan.wheretobuy.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.BioIsland;
import com.mainframevampire.ryan.wheretobuy.model.Blackmores;
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;

public class ProductsActivity extends AppCompatActivity
        implements ProductsFragment.onProductListSelectedInterface {

    public static final String KEY_PRODUCT_INDEX = "KEY_PRODUCT_INDEX";
    public static final String PRODUCTS_FRAGMENT = "PRODUCTS_FRAGMENT" ;
    public static final String PRODUCT_DETAIL_FRAGMENT = "PRODUCT_DETAIL_FRAGMENT";
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private String mFragmentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_);


        Intent intent = getIntent();
        mFragmentName = intent.getStringExtra(MainActivity.FRAGMENT_NAME);
        String itemName = intent.getStringExtra(MainActivity.LIST_NAME);
        String id = intent.getStringExtra(PRODUCT_ID);

        if (mFragmentName.equals("FRAGMENT_PRODUCTS")) {
            setTitle(getTitleFromItemName(itemName));
            ProductsFragment savedFragment = (ProductsFragment) getSupportFragmentManager().findFragmentByTag(PRODUCTS_FRAGMENT);
            if (savedFragment == null) {
                loadProductFragment(itemName, "add");
            }
        }
        if (mFragmentName.equals("FRAGMENT_DETAIL")) {
            ProductDetailFragment savedFragment = (ProductDetailFragment) getSupportFragmentManager().findFragmentByTag(PRODUCT_DETAIL_FRAGMENT);
            if (savedFragment == null) {
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_ID, id);
                bundle.putString(MainActivity.LIST_NAME, itemName);
                productDetailFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.products_placeholder, productDetailFragment, PRODUCT_DETAIL_FRAGMENT);
                fragmentTransaction.commit();
            }
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
        ProductsDataSource dataSource = new ProductsDataSource(ProductsActivity.this);
        int countCustomisedProducts = dataSource.readTableGetCustomisedCount();
        int countBlackmoresProducts = dataSource.readTableGetBrandCount("Blackmores");
        int countBioislandProducts = dataSource.readTableGetBrandCount("BioIsland");
        int countOsterlinProducts = dataSource.readTableGetBrandCount("Ostelin");
        switch (item.getItemId()) {
            case R.id.swisse:
                loadProductFragment("Swisse", "replace");
                return true;
            case R.id.blackmores:
                if (countBlackmoresProducts != Blackmores.id.length) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                    builder.setTitle("still downloading")
                            .setMessage("Please wait for Blackmores products' price to be downloaded");
                    builder.create().show();
                } else {
                    loadProductFragment("Blackmores", "replace");
                }
                return true;
            case R.id.bioIsland:
                if (countBioislandProducts != BioIsland.id.length) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                    builder.setTitle("still downloading")
                            .setMessage("Please wait for BioIsland products' price to be downloaded");
                    builder.create().show();
                } else {
                    loadProductFragment("BioIsland", "replace");
                }
                return true;
            case R.id.ostelin:
                if (countOsterlinProducts != Ostelin.id.length) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                    builder.setTitle("still downloading")
                            .setMessage("Please wait for Ostelin products' price to be downloaded");
                    builder.create().show();
                } else {
                    loadProductFragment("Ostelin", "replace");
                }
                return true;
            case R.id.customise:
                //if no products in MYLIST, show alert
                if (countCustomisedProducts == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                    builder.setTitle("No Products in MYLIST")
                            .setMessage("Please add your favourite products in each branch list");
                    builder.create().show();
                } else {
                    loadProductFragment("MyList", "replace");
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductSelected(String id, String listName) {

        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, id);
        bundle.putString(MainActivity.LIST_NAME, listName);
        productDetailFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.products_placeholder, productDetailFragment, PRODUCT_DETAIL_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private String getTitleFromItemName(String itemName) {
        String title = "";
        if (itemName.equals("Swisse")) title = "Swisse";
        if (itemName.equals("Blackmores")) title = "Blackmores";
        if (itemName.equals("BioIsland")) title = "Bio Island";
        if (itemName.equals("Ostelin")) title = "Ostelin";
        if (itemName.equals("MyList")) title = "My List";

        return title;
    }

    private void loadProductFragment(String itemName, String operation) {
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.LIST_NAME, itemName);
        productsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (operation.equals("add")) fragmentTransaction.add(R.id.products_placeholder, productsFragment, PRODUCTS_FRAGMENT);
        if (operation.equals("replace")) fragmentTransaction.replace(R.id.products_placeholder, productsFragment, PRODUCTS_FRAGMENT);
        fragmentTransaction.commit();
    }


}
