package com.mainframevampire.ryan.wheretobuy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.adapters.ProductsAdapter;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    public interface onProductListSelectedInterface {
        void onProductSelected(String id, String listName);
    }

    private String mItemName = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onProductListSelectedInterface listener = (onProductListSelectedInterface) getActivity();
        mItemName = getArguments().getString(MainActivity.LIST_NAME);
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        ArrayList<ProductPrice> productPrices = new ArrayList<>();
        if (mItemName.equals("SWISSE")) productPrices=dataSource.readProductsTableWithCondition("ID", "SWS");
        if (mItemName.equals("BLACKMORES")) productPrices=dataSource.readProductsTableWithCondition("ID", "BKM");
        if (mItemName.equals("BIOISLAND")) productPrices=dataSource.readProductsTableWithCondition("ID", "BOI");
        if (mItemName.equals("OSTELIN")) productPrices=dataSource.readProductsTableWithCondition("ID", "OST");
        if (mItemName.equals("MYLIST")) productPrices=dataSource.readProductsTableWithCondition("CUSTOMISE_FLAG", "Y");


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);
        ProductsAdapter productsAdapter = new ProductsAdapter(listener, productPrices, mItemName, getActivity());
        recyclerView.setAdapter(productsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(mItemName);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setTitle(getResources().getString(R.string.app_name));
    }


}
