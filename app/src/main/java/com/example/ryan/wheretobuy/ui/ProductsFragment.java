package com.example.ryan.wheretobuy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ryan.wheretobuy.R;
import com.example.ryan.wheretobuy.adapters.ProductsAdapter;
import com.example.ryan.wheretobuy.database.ProductsDataSource;
import com.example.ryan.wheretobuy.model.ProductPrice;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    public interface onProductSelectedInterface {
        void onProductSelected(String id, String listName);
        void onAddSelected(String id, String listName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onProductSelectedInterface listener = (onProductSelectedInterface) getActivity();
        String itemName = getArguments().getString(MainActivity.LIST_NAME);
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        ArrayList<ProductPrice> productPrices = new ArrayList<>();
        if (itemName.equals("SWISSE")) productPrices = dataSource.readProductsTableWithListName("SWS");
        if (itemName.equals("BLACKMORES")) productPrices = dataSource.readProductsTableWithListName("BKM");
        if (itemName.equals("BIOISLAND")) productPrices = dataSource.readProductsTableWithListName("BOI");
        if (itemName.equals("OSTELIN")) productPrices = dataSource.readProductsTableWithListName("OST");
        if (itemName.equals("MYLIST")) productPrices = dataSource.readProductsTableWithCustomisedFlag("Y");


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);
        ProductsAdapter productsAdapter = new ProductsAdapter(listener, productPrices, itemName, getActivity());
        recyclerView.setAdapter(productsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setTitle(getResources().getString(R.string.app_name));
    }
}
