package com.example.ryan.wheretobuy.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import com.example.ryan.wheretobuy.R;
import com.example.ryan.wheretobuy.model.Swisse;

public class ProductsActivity extends AppCompatActivity
        implements ProductsFragment.onProductSelectedInterface {

    public static final String KEY_PRODUCT_INDEX = "KEY_PRODUCT_INDEX";
    public static final String PRODUCTS_FRAGMENT = "PRODUCTS_FRAGMENT" ;
    public static final String PRODUCT_DETAIL_FRAGMENT = "PRODUCT_DETAIL_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_);

        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra(MainActivity.FRAGMENT_NAME);
        String itemName = intent.getStringExtra(MainActivity.ITEM_NAME);
        int index = intent.getIntExtra(MainActivity.INDEX, 0);

        if (fragmentName.equals("FRAGMENT_PRODUCTS")) {
            if (itemName.equals("SWISSE")) setTitle("Swisse");
            if (itemName.equals("BLACKMORES")) setTitle("Blackmores");
            if (itemName.equals("BIOISLAND")) setTitle("Bio Island");
            if (itemName.equals("OSTELIN")) setTitle("Ostelin");

            ProductsFragment savedFragment = (ProductsFragment) getSupportFragmentManager().findFragmentByTag(PRODUCTS_FRAGMENT);
            if (savedFragment == null) {
                ProductsFragment productsFragment = new ProductsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.ITEM_NAME, itemName);
                productsFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.products_placeholder, productsFragment, PRODUCTS_FRAGMENT);
                fragmentTransaction.commit();
            }
        }
        if (fragmentName.equals("FRAGMENT_DETAIL")){
            ProductDetailFragment savedFragment = (ProductDetailFragment) getSupportFragmentManager().findFragmentByTag(PRODUCT_DETAIL_FRAGMENT);
            if (savedFragment == null) {
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_PRODUCT_INDEX, index);
                bundle.putString(MainActivity.ITEM_NAME, itemName);
                productDetailFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.products_placeholder, productDetailFragment, PRODUCT_DETAIL_FRAGMENT);
                fragmentTransaction.commit();
            }

        }

    }


    @Override
    public void onProductSelected(int index, String itemName) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PRODUCT_INDEX, index);
        bundle.putString(MainActivity.ITEM_NAME, itemName);
        productDetailFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.products_placeholder, productDetailFragment, PRODUCT_DETAIL_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
