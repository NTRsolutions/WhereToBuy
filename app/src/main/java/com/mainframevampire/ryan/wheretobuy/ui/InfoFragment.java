package com.mainframevampire.ryan.wheretobuy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;

public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String productID = getArguments().getString(ProductDetailActivity.PRODUCT_ID);

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        final ProductPrice productPrice = dataSource.readProductsTableWithId(productID);
        String productInfo = productPrice.getInformation();

        TextView informationLabel = (TextView) view.findViewById(R.id.informationLabel);
        TextView informationText = (TextView) view.findViewById(R.id.informationText);
        TextView generalInfoLabel = (TextView) view.findViewById(R.id.generalInfoLabel);
        TextView generalInfoText = (TextView) view.findViewById(R.id.generalInfoText);
        TextView miscellaneousLabel = (TextView) view.findViewById(R.id.miscellaneousLabel);
        TextView miscellaneousText = (TextView) view.findViewById(R.id.miscellaneousText);
        TextView drugInteractionsLabel = (TextView) view.findViewById(R.id.drugInteractionsLabel);
        TextView drugInteractionsText = (TextView) view.findViewById(R.id.drugInteractionsText);
        TextView warningsLabel = (TextView) view.findViewById(R.id.warningsLabel);
        TextView warningsText = (TextView) view.findViewById(R.id.warningsText);
        TextView commonUsesLabel = (TextView) view.findViewById(R.id.commonUsesLabel);
        TextView commonUsesText = (TextView) view.findViewById(R.id.commonUsesText);
        TextView ingredientsLabel = (TextView) view.findViewById(R.id.ingredientsLabel);
        TextView ingredientsText = (TextView) view.findViewById(R.id.ingredientsText);
        TextView directionsLabel = (TextView) view.findViewById(R.id.directionsLabel);
        TextView directionsText = (TextView) view.findViewById(R.id.directionsText);
        TextView indicationsLabel = (TextView) view.findViewById(R.id.indicationsLabel);
        TextView indicationsText = (TextView) view.findViewById(R.id.indicationsText);
        TextView noInfoText = (TextView) view.findViewById(R.id.noInfoText);

        String[] productInfoArray = productInfo.split(ProductPrice.ARRAY_DIVIDER_SPILIT);
        if (productInfoArray[0].equals("none")) {
            informationLabel.setVisibility(View.GONE);
            informationText.setVisibility(View.GONE);
        } else {
            informationText.setText(productInfoArray[0]);
        }

        if (productInfoArray[1].equals("none")) {
            generalInfoLabel.setVisibility(View.GONE);
            generalInfoText.setVisibility(View.GONE);
        } else {
            generalInfoText.setText(productInfoArray[1]);
        }

        if (productInfoArray[2].equals("none")) {
            miscellaneousLabel.setVisibility(View.GONE);
            miscellaneousText.setVisibility(View.GONE);
        } else {
            miscellaneousText.setText(productInfoArray[2]);
        }

        if (productInfoArray[3].equals("none")) {
            drugInteractionsLabel.setVisibility(View.GONE);
            drugInteractionsText.setVisibility(View.GONE);
        } else {
            drugInteractionsText.setText(productInfoArray[3]);
        }

        if (productInfoArray[4].equals("none")) {
            warningsLabel.setVisibility(View.GONE);
            warningsText.setVisibility(View.GONE);
        } else {
            warningsText.setText(productInfoArray[4]);
        }

        if (productInfoArray[5].equals("none")) {
            commonUsesLabel.setVisibility(View.GONE);
            commonUsesText.setVisibility(View.GONE);
        } else {
            commonUsesText.setText(productInfoArray[5]);
        }

        if (productInfoArray[6].equals("none")) {
            ingredientsLabel.setVisibility(View.GONE);
            ingredientsText.setVisibility(View.GONE);
        } else {
            ingredientsText.setText(productInfoArray[6]);
        }

        if (productInfoArray[7].equals("none")) {
            directionsLabel.setVisibility(View.GONE);
            directionsText.setVisibility(View.GONE);
        } else {
            directionsText.setText(productInfoArray[7]);
        }

        if (productInfoArray[8].equals("none")) {
            indicationsLabel.setVisibility(View.GONE);
            indicationsText.setVisibility(View.GONE);
        } else {
            indicationsText.setText(productInfoArray[8]);
        }

        if(informationLabel.getVisibility() == View.GONE &&
                generalInfoLabel.getVisibility() == View.GONE &&
                miscellaneousLabel.getVisibility() == View.GONE &&
                drugInteractionsLabel.getVisibility() == View.GONE &&
                warningsLabel.getVisibility() == View.GONE &&
                commonUsesLabel.getVisibility() == View.GONE &&
                ingredientsLabel.getVisibility() == View.GONE &&
                directionsLabel.getVisibility() == View.GONE &&
                indicationsLabel.getVisibility() == View.GONE) {
            noInfoText.setVisibility(View.VISIBLE);
        } else {
            noInfoText.setVisibility(View.GONE);
        }

        return view;
    }
}
