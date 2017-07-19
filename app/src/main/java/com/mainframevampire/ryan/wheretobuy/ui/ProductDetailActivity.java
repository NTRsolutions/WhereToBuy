package com.mainframevampire.ryan.wheretobuy.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.BioIsland;
import com.mainframevampire.ryan.wheretobuy.model.Blackmores;
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.model.Swisse;

public class ProductDetailActivity extends AppCompatActivity
        implements PriceFragment.OnPriceFragmentWebsiteListener {
    public final static String PRODUCT_ID = "product_id";
    private static final String VIEWPAGER_FRAGMENT = "viewpager_fragment" ;
    private static final String DUALPANE_FRAGMENT = "dualpane_fragment";
    public static final String PRODUCT_URL = "product_url";

    private TextView mDetailLongName;
    private ImageView mDetailImageView;
    private CheckBox mFavouriteCheckBox;
    private Bitmap mBitmap;

    private String mWhichIsLowest;
    private String mCMWUrl;
    private String mPLUrl;
    private String mFLUrl;
    private String mTWUrl;
    private String mHWUrl;
    private String mLongName;
    private Float mLowestPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        final String productID = intent.getStringExtra(PRODUCT_ID);

        ProductsDataSource dataSource = new ProductsDataSource(this);
        final ProductPrice productPrice = dataSource.readProductsTableWithId(productID);

        mWhichIsLowest = productPrice.getWhichIsLowest();
        mCMWUrl = productPrice.getCMWUrl();
        mPLUrl = productPrice.getPLUrl();
        mFLUrl = productPrice.getFLUrl();
        mTWUrl = productPrice.getTWUrl();
        mHWUrl = productPrice.getHWUrl();
        mLongName = productPrice.getLongName();
        mLowestPrice = productPrice.getLowestPrice();

        mDetailLongName = (TextView) findViewById(R.id.detailLongName);
        mDetailImageView = (ImageView) findViewById(R.id.detailImageView);
        mFavouriteCheckBox = (CheckBox) findViewById(R.id.favouriteCheckBox);

        mDetailLongName.setText(productPrice.getLongName());
        setDetailImageView(productPrice.getBrand(), productID);
        if (productPrice.getCustomiseFlag().equals("Y")) {
            mFavouriteCheckBox.setChecked(true);
        } else {
            mFavouriteCheckBox.setChecked(false);
        }

        mFavouriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ProductsDataSource dataSource = new ProductsDataSource(ProductDetailActivity.this);
                if (productPrice.getCustomiseFlag().equals("Y")) {
                    dataSource.updateCustomiseFlagInTable(productID, "N");
                } else {
                    dataSource.updateCustomiseFlagInTable(productID, "Y");
                }
            }
        });

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (!isTablet) {
            //add view pager fragment
            if (savedInstanceState == null) {
                ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_ID, productID);
                viewPagerFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.placeHolder, viewPagerFragment, VIEWPAGER_FRAGMENT);
                fragmentTransaction.commit();
            }
        } else {
            //add dual pane fragment
            if (savedInstanceState == null) {
                DualPaneFragment dualPaneFragment = new DualPaneFragment();
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_ID, productID);
                dualPaneFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.placeHolder, dualPaneFragment, DUALPANE_FRAGMENT);
                fragmentTransaction.commit();
            }
        }

    }

    //hide share menu item
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem swisseItem = menu.findItem(R.id.swisse);
        swisseItem.setVisible(false);
        MenuItem blackmoresItem = menu.findItem(R.id.blackmores);
        blackmoresItem.setVisible(false);
        MenuItem bioIslandItem = menu.findItem(R.id.bioIsland);
        bioIslandItem.setVisible(false);
        MenuItem ostelinItem = menu.findItem(R.id.ostelin);
        ostelinItem.setVisible(false);
        MenuItem customiseItem = menu.findItem(R.id.customise);
        customiseItem.setVisible(false);
        MenuItem shareItem = menu.findItem(R.id.share);
        shareItem.setVisible(true);
        if (isFacebookInstalled()) {
            MenuItem postFacebookItem = menu.findItem(R.id.post_facebook);
            postFacebookItem.setVisible(true);
        }
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
        if (item.getItemId() == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getSummary());
            startActivity(intent);
        }

        if(item.getItemId() == R.id.post_facebook) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(mBitmap)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            ShareDialog.show(this, content);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDetailImageView(String brand, String productID){
        switch (brand){
            case "Swisse":
                Glide.with(this).load(Swisse.getSwisseImageId(productID)).into(mDetailImageView);
                mBitmap = BitmapFactory.decodeResource(getResources(),Swisse.getSwisseImageId(productID));
                break;
            case "Blackmores":
                Glide.with(this).load(Blackmores.getBlackmoresImageId(productID)).into(mDetailImageView);
                mBitmap = BitmapFactory.decodeResource(getResources(),Blackmores.getBlackmoresImageId(productID));
                break;
            case "BioIsland":
                Glide.with(this).load(BioIsland.getBioIslandImageId(productID)).into(mDetailImageView);
                mBitmap = BitmapFactory.decodeResource(getResources(),BioIsland.getBioIslandImageId(productID));
                break;
            case "Ostelin":
                Glide.with(this).load(Ostelin.getOstelinImageId(productID)).into(mDetailImageView);
                mBitmap = BitmapFactory.decodeResource(getResources(),Ostelin.getOstelinImageId(productID));
                break;
        }
    }

    private boolean isFacebookInstalled() {
        try {
            ApplicationInfo info = getPackageManager()
                    .getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    public void onOpenWebsite(String Url) {
        Intent intent = new Intent(this,WebActivity.class);
        intent.putExtra(PRODUCT_URL, Url);
        startActivity(intent);
    }

    private String getSummary() {
        String websiteUrl = " ";
        switch (mWhichIsLowest) {
            case "Chemist Warehouse":
                websiteUrl = mCMWUrl;
                break;
            case "Priceline Pharmacy":
                websiteUrl = mPLUrl;
                break;
            case "Pharmacy 4Less":
                websiteUrl = mFLUrl;
                break;
            case "TerryWhite Chemmart":
                websiteUrl = mTWUrl;
                break;
            case "HealthyWorld Pharmacy":
                websiteUrl = mHWUrl;
                break;
        }

        return ("Check the lowest price for " + mLongName + ": $" + mLowestPrice + " in "
                + mWhichIsLowest + " " + websiteUrl);

    }
}
