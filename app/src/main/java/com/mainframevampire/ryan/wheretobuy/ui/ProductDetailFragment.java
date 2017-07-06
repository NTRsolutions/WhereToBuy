package com.mainframevampire.ryan.wheretobuy.ui;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
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


public class ProductDetailFragment extends Fragment {
    public static final String PRODUCT_URL = "PRODUCT_URL";
    private TextView mDetailLongName;
    private  ImageView mDetailImageView;
    private CheckBox mFavouriteCheckBox;
    private TextView mDetailCMWPrice;
    private TextView mDetailPLPrice;
    private TextView mDetailFLPrice;
    private TextView mDetailTWPrice;
    private TextView mDetailHWPrice;
    private Button mCMWButton;
    private Button mPLButton;
    private Button mFLButton;
    private Button mTWButton;
    private Button mHWButton;

    private String mId;

    private String shortName = "";
    private String longName = "";
    private String customiseFlag = "";
    private float lowestPrice = 0;
    private float cmwPrice = 0;
    private float plPrice = 0;
    private float flPrice = 0;
    private float twPrice = 0;
    private float hwPrice = 0;
    private String cmwUrl = "";
    private String plUrl = "";
    private String flUrl = "";
    private String twUrl = "";
    private String hwUrl = "";
    private String mWhichIsLowest = "";
    private Bitmap mBitmap;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mId = getArguments().getString(ProductsActivity.PRODUCT_ID);
        final String itemName = getArguments().getString(MainActivity.LIST_NAME);
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);


        mDetailLongName = (TextView) view.findViewById(R.id.detailLongName);
        mDetailImageView = (ImageView) view.findViewById(R.id.detailImageView);
        mFavouriteCheckBox = (CheckBox) view.findViewById(R.id.favouriteCheckBox);
        mDetailCMWPrice = (TextView) view.findViewById(R.id.detailCMWPrice);
        mDetailPLPrice = (TextView) view.findViewById(R.id.detailPLPrice);
        mDetailFLPrice = (TextView) view.findViewById(R.id.detailFLPrice);
        mDetailTWPrice = (TextView) view.findViewById(R.id.detailTWPrice);
        mDetailHWPrice = (TextView) view.findViewById(R.id.detailHWPrice);
        mCMWButton = (Button) view.findViewById(R.id.cwh_button);
        mPLButton = (Button) view.findViewById(R.id.pl_button);
        mFLButton = (Button) view.findViewById(R.id.fl_button);
        mTWButton = (Button) view.findViewById(R.id.tw_button);
        mHWButton = (Button) view.findViewById(R.id.hw_button);


        ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        ProductPrice productPrice = dataSource.readProductsTableWithId(mId);

        shortName = productPrice.getShortName();
        longName = productPrice.getLongName();
        customiseFlag = productPrice.getCustomiseFlag();
        mWhichIsLowest = productPrice.getWhichIsLowest();
        lowestPrice = productPrice.getLowestPrice();
        cmwPrice = productPrice.getCMWPrice();
        plPrice = productPrice.getPLPrice();
        flPrice = productPrice.getFLPrice();
        twPrice = productPrice.getTWPrice();
        hwPrice = productPrice.getHWPrice();
        cmwUrl = productPrice.getCMWUrl();
        plUrl = productPrice.getPLUrl();
        flUrl = productPrice.getFLUrl();
        twUrl = productPrice.getTWUrl();
        hwUrl = productPrice.getHWUrl();


        if (mId.substring(0,3).equals("SWS")) {
            Glide.with(getActivity()).load(Swisse.getSwisseImageId(mId)).into(mDetailImageView);
            mBitmap = BitmapFactory.decodeResource(getResources(),Swisse.getSwisseImageId(mId));
        }
        if (mId.substring(0,3).equals("BKM")){
            Glide.with(getActivity()).load(Blackmores.getBlackmoresImageId(mId)).into(mDetailImageView);
            mBitmap = BitmapFactory.decodeResource(getResources(),Blackmores.getBlackmoresImageId(mId));
        }
        if (mId.substring(0,3).equals("BOI")){
            Glide.with(getActivity()).load(BioIsland.getBioIslandImageId(mId)).into(mDetailImageView);
            mBitmap = BitmapFactory.decodeResource(getResources(),BioIsland.getBioIslandImageId(mId));
        }
        if (mId.substring(0,3).equals("OST")){
            Glide.with(getActivity()).load(Ostelin.getOstelinImageId(mId)).into(mDetailImageView);
            mBitmap = BitmapFactory.decodeResource(getResources(),Ostelin.getOstelinImageId(mId));
        }

        getActivity().setTitle(shortName);
        mDetailLongName.setText(longName);

        if (customiseFlag.equals("Y")) {
            mFavouriteCheckBox.setChecked(true);
        } else {
            mFavouriteCheckBox.setChecked(false);
        }

        mFavouriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ProductsDataSource dataSource = new ProductsDataSource(getActivity());
                if (customiseFlag.equals("Y")) {
                    dataSource.updateCustomiseFlagInTable(mId, "N");
                } else {
                    dataSource.updateCustomiseFlagInTable(mId, "Y");
                }
            }
        });

        if (cmwPrice == 0) {
            mDetailCMWPrice.setText(R.string.NA);
            mCMWButton.setVisibility(View.INVISIBLE);
        } else {
            String detailCMWPriceString = getString(R.string.dollar_sign) + String.valueOf(cmwPrice);
            mDetailCMWPrice.setText(detailCMWPriceString);
            if (cmwPrice == lowestPrice) mDetailCMWPrice.setTextColor(Color.RED);
        }

        if (plPrice == 0){
            mDetailPLPrice.setText(R.string.NA);
            mPLButton.setVisibility(View.INVISIBLE);
        } else {
            String detailPLPriceString = getString(R.string.dollar_sign) + String.valueOf(plPrice);
            mDetailPLPrice.setText(detailPLPriceString);
            if (plPrice == lowestPrice) mDetailPLPrice.setTextColor(Color.RED);
        }

        if (flPrice == 0 ) {
            mDetailFLPrice.setText(R.string.NA);
            mFLButton.setVisibility(View.INVISIBLE);
        } else {
            String detailFLPriceString = getString(R.string.dollar_sign) + String.valueOf(flPrice);
            mDetailFLPrice.setText(detailFLPriceString);
            if (flPrice == lowestPrice) mDetailFLPrice.setTextColor(Color.RED);
        }

        if (twPrice == 0) {
            mDetailTWPrice.setText(R.string.NA);
            mTWButton.setVisibility(View.INVISIBLE);
        } else {
            String detailTWPriceString = getString(R.string.dollar_sign) + String.valueOf(twPrice);
            mDetailTWPrice.setText(detailTWPriceString);
            if (twPrice == lowestPrice) mDetailTWPrice.setTextColor(Color.RED);
        }

        if (hwPrice == 0){
            mDetailHWPrice.setText(R.string.NA);
            mHWButton.setVisibility(View.INVISIBLE);
        } else {
            String detailHWPriceString = getString(R.string.dollar_sign) + String.valueOf(hwPrice);
            mDetailHWPrice.setText(detailHWPriceString);
            if (hwPrice == lowestPrice)
                mDetailHWPrice.setTextColor(Color.RED);
        }

        mCMWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite(cmwUrl);
            }
        });

        mPLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite(plUrl);
            }
        });

        mFLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite(flUrl);
            }
        });

        mTWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite(twUrl);
            }
        });

        mHWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite(hwUrl);
            }
        });

        return view;
    }

    private void openWebsite(String Url) {
        Intent intent = new Intent(getActivity(),WebActivity.class);
        intent.putExtra(PRODUCT_URL, Url);
        startActivity(intent);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
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
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String websiteUrl = "";
            if (mWhichIsLowest.equals("Chemist Warehouse")) websiteUrl = cmwUrl;
            if (mWhichIsLowest.equals("Priceline Pharmacy")) websiteUrl = plUrl;
            if (mWhichIsLowest.equals("Pharmacy 4Less")) websiteUrl = flUrl;
            if (mWhichIsLowest.equals("TerryWhite Chemmart")) websiteUrl = twUrl;
            if (mWhichIsLowest.equals("HealthyWorld Pharmacy")) websiteUrl = hwUrl;
            String summary = ("Check the lowest price for " + longName + ": $" + lowestPrice + " in "
                    + mWhichIsLowest + " " + websiteUrl);
            intent.putExtra(Intent.EXTRA_TEXT, summary);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.post_facebook) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(mBitmap)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            ShareDialog.show(ProductDetailFragment.this, content);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setTitle(getResources().getString(R.string.app_name));
    }

    private boolean isFacebookInstalled() {
        try {
            ApplicationInfo info = getActivity().getPackageManager()
                    .getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
