package com.mainframevampire.ryan.wheretobuy.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;

public class ProductsActivity extends AppCompatActivity
        implements ProductsFragment.onProductSelectedInterface {

    public static final String KEY_PRODUCT_INDEX = "KEY_PRODUCT_INDEX";
    public static final String PRODUCTS_FRAGMENT = "PRODUCTS_FRAGMENT" ;
    public static final String PRODUCT_DETAIL_FRAGMENT = "PRODUCT_DETAIL_FRAGMENT";
    public static final String PRODUCT_ID = "PRODUCT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_);


        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra(MainActivity.FRAGMENT_NAME);
        String itemName = intent.getStringExtra(MainActivity.LIST_NAME);
        String id = intent.getStringExtra(PRODUCT_ID);
        int index = intent.getIntExtra(MainActivity.INDEX, 0);

        if (fragmentName.equals("FRAGMENT_PRODUCTS")) {
            setTitle(getTitleFromItemName(itemName));

            ProductsFragment savedFragment = (ProductsFragment) getSupportFragmentManager().findFragmentByTag(PRODUCTS_FRAGMENT);
            if (savedFragment == null) {
                loadProductFragment(itemName, "add");
            }
        }
        if (fragmentName.equals("FRAGMENT_DETAIL")){
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
                loadProductFragment("SWISSE", "replace");
                return true;
            case R.id.blackmores:
                loadProductFragment("BLACKMORES", "replace");
                return true;
            case R.id.bioIsland:
                loadProductFragment("BIOISLAND", "replace");
                return true;
            case R.id.ostelin:
                loadProductFragment("OSTELIN", "replace");
                return true;
            case R.id.customise:
                //if no products in MYLIST, show alert
                ProductsDataSource dataSource = new ProductsDataSource(ProductsActivity.this);
                int countCustomisedProducts = dataSource.readProductsTableToGetCustomisedProduct();
                if (countCustomisedProducts == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                    builder.setTitle("No Products in MYLIST")
                            .setMessage("Please add your favourite products in each branch list");
                    builder.create().show();
                } else {
                    loadProductFragment("MYLIST", "replace");
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

    @Override
    public void onAddSelected(String id, String listName) {
        final String itemId = id;
        if (listName.equals("MYLIST")) {
            //delete alerdialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete item to my list")
                    .setMessage("please confirm you want to delete this product from my list")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProductsDataSource dataSource = new ProductsDataSource(ProductsActivity.this);
                            dataSource.updateCustomiseFlagInTable(itemId, "N");
                            //reload fragment again
                            loadProductFragment("MYLIST", "replace");
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.create().show();
        } else {
            //add alerdialog
            ProductsDataSource dataSource = new ProductsDataSource(ProductsActivity.this);
            ProductPrice productPrice = dataSource.readProductsTableWithId(itemId);
            if (productPrice.getCustomiseFlag().equals("Y")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                builder.setTitle("Product already exists")
                        .setMessage("This product already exists in MyList, no need to add again");
                builder.create().show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("add item to my list")
                        .setMessage("please confirm you want to add this product to my list")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ProductsDataSource dataSource = new ProductsDataSource(ProductsActivity.this);
                                dataSource.updateCustomiseFlagInTable(itemId, "Y");
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        }
    }

    private String getTitleFromItemName(String itemName) {
        String title = "";
        if (itemName.equals("SWISSE")) title = "Swisse";
        if (itemName.equals("BLACKMORES")) title = "Blackmores";
        if (itemName.equals("BIOISLAND")) title = "Bio Island";
        if (itemName.equals("OSTELIN")) title = "Ostelin";
        if (itemName.equals("MYLIST")) title = "My List";

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
