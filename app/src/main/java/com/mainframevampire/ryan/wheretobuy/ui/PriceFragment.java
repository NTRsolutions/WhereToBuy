package com.mainframevampire.ryan.wheretobuy.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;


public class PriceFragment extends Fragment {

    public interface OnPriceFragmentWebsiteListener {
        void onOpenWebsite(String Url);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String productID = getArguments().getString(ProductDetailActivity.PRODUCT_ID);
        View view = inflater.inflate(R.layout.fragment_price, container, false);

        TextView detailCMWPrice = (TextView) view.findViewById(R.id.detailCMWPrice);
        TextView detailPLPrice = (TextView) view.findViewById(R.id.detailPLPrice);
        TextView detailFLPrice = (TextView) view.findViewById(R.id.detailFLPrice);
        TextView detailTWPrice = (TextView) view.findViewById(R.id.detailTWPrice);
        TextView detailHWPrice = (TextView) view.findViewById(R.id.detailHWPrice);
        Button cmwButton = (Button) view.findViewById(R.id.cwh_button);
        Button plButton = (Button) view.findViewById(R.id.pl_button);
        Button flButton = (Button) view.findViewById(R.id.fl_button);
        Button twButton = (Button) view.findViewById(R.id.tw_button);
        Button hwButton = (Button) view.findViewById(R.id.hw_button);

        ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        final ProductPrice productPrice = dataSource.readProductsTableWithId(productID);

        if (productPrice.getCMWPrice() == 0) {
            detailCMWPrice.setText(R.string.NA);
            cmwButton.setVisibility(View.INVISIBLE);
        } else {
            String detailCMWPriceString = getString(R.string.dollar_sign) + String.valueOf(productPrice.getCMWPrice());
            detailCMWPrice.setText(detailCMWPriceString);
            if (productPrice.getCMWPrice() == productPrice.getLowestPrice()) detailCMWPrice.setTextColor(Color.RED);
        }

        if (productPrice.getPLPrice() == 0){
            detailPLPrice.setText(R.string.NA);
            plButton.setVisibility(View.INVISIBLE);
        } else {
            String detailPLPriceString = getString(R.string.dollar_sign) + String.valueOf(productPrice.getPLPrice());
            detailPLPrice.setText(detailPLPriceString);
            if (productPrice.getPLPrice() == productPrice.getLowestPrice()) detailPLPrice.setTextColor(Color.RED);
        }

        if (productPrice.getFLPrice() == 0 ) {
            detailFLPrice.setText(R.string.NA);
            flButton.setVisibility(View.INVISIBLE);
        } else {
            String detailFLPriceString = getString(R.string.dollar_sign) + String.valueOf(productPrice.getFLPrice());
            detailFLPrice.setText(detailFLPriceString);
            if (productPrice.getFLPrice() == productPrice.getLowestPrice()) detailFLPrice.setTextColor(Color.RED);
        }

        if (productPrice.getTWPrice() == 0) {
            detailTWPrice.setText(R.string.NA);
            twButton.setVisibility(View.INVISIBLE);
        } else {
            String detailTWPriceString = getString(R.string.dollar_sign) + String.valueOf(productPrice.getTWPrice());
            detailTWPrice.setText(detailTWPriceString);
            if (productPrice.getTWPrice() == productPrice.getLowestPrice()) detailTWPrice.setTextColor(Color.RED);
        }

        if (productPrice.getHWPrice() == 0){
            detailHWPrice.setText(R.string.NA);
            hwButton.setVisibility(View.INVISIBLE);
        } else {
            String detailHWPriceString = getString(R.string.dollar_sign) + String.valueOf(productPrice.getHWPrice());
            detailHWPrice.setText(detailHWPriceString);
            if (productPrice.getHWPrice() == productPrice.getLowestPrice()) detailHWPrice.setTextColor(Color.RED);
        }

        cmwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnPriceFragmentWebsiteListener) getActivity()).onOpenWebsite(productPrice.getCMWUrl());
            }
        });

        plButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnPriceFragmentWebsiteListener) getActivity()).onOpenWebsite(productPrice.getPLUrl());
            }
        });

        flButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnPriceFragmentWebsiteListener) getActivity()).onOpenWebsite(productPrice.getFLUrl());
            }
        });

        twButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnPriceFragmentWebsiteListener) getActivity()).onOpenWebsite(productPrice.getTWUrl());
            }
        });

        hwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnPriceFragmentWebsiteListener) getActivity()).onOpenWebsite(productPrice.getHWUrl());
            }
        });

        return view;
    }


}
