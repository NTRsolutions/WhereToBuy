package com.mainframevampire.ryan.wheretobuy.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.BioIsland;
import com.mainframevampire.ryan.wheretobuy.model.Blackmores;
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.model.Swisse;


public class ProductDetailFragment extends Fragment {
    private TextView mDetailLongName;
    private  ImageView mDetailImageView;
    private CheckBox mFavouriteCheckBox;
    private TextView mDetailCMWPrice;
    private TextView mDetailPLPrice;
    private TextView mDetailFLPrice;
    private TextView mDetailTWPrice;
    private TextView mDetailHWPrice;

    private String mId;


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

        ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        ProductPrice productPrice = dataSource.readProductsTableWithId(mId);

        String shortName = productPrice.getShortName();
        String longName = productPrice.getLongName();
        final String customiseFlag = productPrice.getCustomiseFlag();
        float lowestPrice = productPrice.getLowestPrice();
        float cmwPrice = productPrice.getCMWPrice();
        float plPrice = productPrice.getPLPrice();
        float flPrice = productPrice.getFLPrice();
        float twPrice = productPrice.getTWPrice();
        float hwPrice = productPrice.getHWPrice();


        if (mId.substring(0,3).equals("SWS")) {
            Glide.with(getActivity()).load(Swisse.getSwisseImageId(mId)).into(mDetailImageView);
        }
        if (mId.substring(0,3).equals("BKM")){
        Glide.with(getActivity()).load(Blackmores.getBlackmoresImageId(mId)).into(mDetailImageView);
        }
        if (mId.substring(0,3).equals("BOI")){
            Glide.with(getActivity()).load(BioIsland.getBioIslandImageId(mId)).into(mDetailImageView);
        }
        if (mId.substring(0,3).equals("OST")){
            Glide.with(getActivity()).load(Ostelin.getOstelinImageId(mId)).into(mDetailImageView);
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
        } else {
            String detailCMWPriceString = getString(R.string.dollar_sign) + String.valueOf(cmwPrice);
            mDetailCMWPrice.setText(detailCMWPriceString);
            if (cmwPrice == lowestPrice) mDetailCMWPrice.setTextColor(Color.RED);
        }

        if (plPrice == 0){
            mDetailPLPrice.setText(R.string.NA);
        } else {
            String detailPLPriceString = getString(R.string.dollar_sign) + String.valueOf(plPrice);
            mDetailPLPrice.setText(detailPLPriceString);
            if (plPrice == lowestPrice) mDetailPLPrice.setTextColor(Color.RED);
        }

        if (flPrice == 0 ) {
            mDetailFLPrice.setText(R.string.NA);
        } else {
            String detailFLPriceString = getString(R.string.dollar_sign) + String.valueOf(flPrice);
            mDetailFLPrice.setText(detailFLPriceString);
            if (flPrice == lowestPrice) mDetailFLPrice.setTextColor(Color.RED);
        }

        if (twPrice == 0) {
            mDetailTWPrice.setText(R.string.NA);
        } else {
            String detailTWPriceString = getString(R.string.dollar_sign) + String.valueOf(twPrice);
            mDetailTWPrice.setText(detailTWPriceString);
            if (twPrice == lowestPrice) mDetailTWPrice.setTextColor(Color.RED);
        }

        if (hwPrice == 0){
            mDetailHWPrice.setText(R.string.NA);
        } else {
            String detailHWPriceString = getString(R.string.dollar_sign) + String.valueOf(hwPrice);
            mDetailHWPrice.setText(detailHWPriceString);
            if (hwPrice == lowestPrice)
                mDetailHWPrice.setTextColor(Color.RED);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setTitle(getResources().getString(R.string.app_name));
    }


}
