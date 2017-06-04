package com.example.ryan.wheretobuy.ui;

import android.content.Context;
import android.net.Uri;
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

public class ProductsFragment extends Fragment {

    public interface onProductSelectedInterface {
        void onProductSelected(int index, String itemName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onProductSelectedInterface listener = (onProductSelectedInterface) getActivity();
        String itemName = getArguments().getString(MainActivity.ITEM_NAME);
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);
        ProductsAdapter productsAdapter = new ProductsAdapter(listener, itemName);
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
