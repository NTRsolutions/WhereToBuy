package com.mainframevampire.ryan.wheretobuy.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mainframevampire.ryan.wheretobuy.R;

public class DualPaneFragment extends Fragment {

    private static final String PRICE_FRAGMENT = "price_fragment";
    private static final String INFO_FRAGMENT = "info_fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String productID = getArguments().getString(ProductDetailActivity.PRODUCT_ID);

        View view = inflater.inflate(R.layout.fragment_dual_pane, container, false);

        FragmentManager fragmentManager = getChildFragmentManager();

        PriceFragment savePriceFragment = (PriceFragment) fragmentManager.findFragmentByTag(PRICE_FRAGMENT);
        if (savePriceFragment == null) {
            final PriceFragment priceFragment = new PriceFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ProductDetailActivity.PRODUCT_ID, productID);
            priceFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.leftPlaceholder, priceFragment, PRICE_FRAGMENT)
                    .commit();
        }

        InfoFragment saveInfoFragment = (InfoFragment) fragmentManager.findFragmentByTag(INFO_FRAGMENT);
        if (saveInfoFragment == null) {
            final InfoFragment infoFragment = new InfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ProductDetailActivity.PRODUCT_ID, productID);
            infoFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.rightPlaceholder, infoFragment, INFO_FRAGMENT)
                    .commit();
        }


        return view;
    }


}
