package com.example.ryan.wheretobuy.ui;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.system.Os;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ryan.wheretobuy.R;
import com.example.ryan.wheretobuy.model.BioIsland;
import com.example.ryan.wheretobuy.model.Blackmores;
import com.example.ryan.wheretobuy.model.Ostelin;
import com.example.ryan.wheretobuy.model.Swisse;
import com.example.ryan.wheretobuy.util.GetInfoFromModel;


public class ProductDetailFragment extends Fragment {
    private TextView mDetailLongName;
    private  ImageView mDetailImageView;
    private TextView mDetailCMWPrice;
    private TextView mDetailPLPrice;
    private TextView mDetailFLPrice;
    private TextView mDetailTWPrice;
    private TextView mDetailHWPrice;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int index = getArguments().getInt(ProductsActivity.KEY_PRODUCT_INDEX);
        String itemName = getArguments().getString(MainActivity.ITEM_NAME);
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        mDetailLongName = (TextView) view.findViewById(R.id.detailLongName);
        mDetailImageView = (ImageView) view.findViewById(R.id.detailImageView);
        mDetailCMWPrice = (TextView) view.findViewById(R.id.detailCMWPrice);
        mDetailPLPrice = (TextView) view.findViewById(R.id.detailPLPrice);
        mDetailFLPrice = (TextView) view.findViewById(R.id.detailFLPrice);
        mDetailTWPrice = (TextView) view.findViewById(R.id.detailTWPrice);
        mDetailHWPrice = (TextView) view.findViewById(R.id.detailHWPrice);

        String shortName = GetInfoFromModel.getShortName(itemName, index);
        String longName = GetInfoFromModel.getLongName(itemName, index);
        float lowestPrice = GetInfoFromModel.getLowestPrice(itemName, index);
        float cmwPrice = GetInfoFromModel.getCMWPrice(itemName, index);
        float plPrice = GetInfoFromModel.getPLPrice(itemName, index);
        float flPrice = GetInfoFromModel.getFLPrice(itemName, index);
        float twPrice = GetInfoFromModel.getTWPrice(itemName, index);
        float hwPrice = GetInfoFromModel.getHWPrice(itemName, index);

        if (itemName.equals("SWISSE")) {
            Glide.with(getActivity()).load(Swisse.getSwisseImageId(Swisse.id[index])).into(mDetailImageView);
        }
        if (itemName.equals("BLACKMORES")){
        Glide.with(getActivity()).load(Blackmores.getBlackmoresImageId(Blackmores.id[index])).into(mDetailImageView);
        }
        if (itemName.equals("BIOISLAND")){
            Glide.with(getActivity()).load(BioIsland.getBioIslandImageId(BioIsland.id[index])).into(mDetailImageView);
        }
        if (itemName.equals("OSTELIN")){
            Glide.with(getActivity()).load(Ostelin.getOstelinImageId(Ostelin.id[index])).into(mDetailImageView);
        }

        getActivity().setTitle(shortName);
        mDetailLongName.setText(longName);

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
    public void onStop() {
        super.onStop();
        getActivity().setTitle(getResources().getString(R.string.app_name));
    }


}
