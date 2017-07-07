package com.mainframevampire.ryan.wheretobuy.ui;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.adapters.EndLessRecyclerViewScrollListener;
import com.mainframevampire.ryan.wheretobuy.adapters.ProductsAdapter;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    private static final String TAG = ProductsFragment.class.getSimpleName() ;

    public interface onProductListSelectedInterface {
        void onProductSelected(String id, String listName);
    }

    private String mItemName = "";
    private EndLessRecyclerViewScrollListener mScrollListener;
    private ArrayList<ProductPrice> mProductPrices = new ArrayList<>();
    private ProductsAdapter mProductsAdapter;
    private RecyclerView mRecyclerView;
    private int mNumberOfOnePage = 0;
    private int mTotalPages = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onProductListSelectedInterface listener = (onProductListSelectedInterface) getActivity();
        mItemName = getArguments().getString(MainActivity.LIST_NAME);
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        int totalCounts = 0;
        if (mItemName.equals("MyList")) {
            totalCounts = dataSource.readTableGetCustomisedCount();
        } else {
            totalCounts = dataSource.readTableGetBrandCount(mItemName);
        }
        Log.d(TAG, mItemName + " totalCounts: " + totalCounts);

        //get the item number of one page for different screen size and oritention.
        mNumberOfOnePage = getItemNumbersOfOnePageForProductsList();
        Log.d(TAG, mItemName + " number of one page: " + mNumberOfOnePage);
        //get the total pages
        mTotalPages = getTotalPagesForProductsList(totalCounts);
        Log.d(TAG, mItemName + " total pages: " + mTotalPages);

        //load first page data
        if (mItemName.equals("MyList")) {
            mProductPrices = dataSource.readTableByCustomiseFlag("Y", mNumberOfOnePage, " ");
        } else {
            mProductPrices = dataSource.readTableByBrand(mItemName, mNumberOfOnePage, " ");
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);
        mProductsAdapter = new ProductsAdapter(listener, mProductPrices, mItemName, getActivity());
        mRecyclerView.setAdapter(mProductsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mScrollListener = new EndLessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount, RecyclerView recyclerView) {
                //triggered only when new data needs to be loaded
                if (currentPage == mTotalPages) {
                    //end of the pages
                    Log.d(TAG, mItemName + " Pages: end of the pages");
                } else {
                    Log.d(TAG, mItemName + " Pages: " + currentPage);
                    loadNextDataFromDatabase();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

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

    //append the next page of data into the adapter
    private void loadNextDataFromDatabase() {
        ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        ArrayList<ProductPrice> productPrices;

        String lastIdInPreviousPage = mProductPrices.get(mProductPrices.size() - 1).getID();

        if (mItemName.equals("MyList")) {
            productPrices = dataSource.readTableByCustomiseFlag("Y", mNumberOfOnePage, lastIdInPreviousPage);
            Log.d(TAG, mItemName + " Last ID in previous page " + lastIdInPreviousPage);
        } else {
            productPrices = dataSource.readTableByBrand(mItemName, mNumberOfOnePage, lastIdInPreviousPage);
            Log.d(TAG, mItemName + " Last ID in previous page " + lastIdInPreviousPage);
        }

        for (ProductPrice productPrice: productPrices) {
            mProductPrices.add(productPrice);
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mProductsAdapter.notifyDataSetChanged();
            }
        });
    }

    public int getItemNumbersOfOnePageForProductsList() {
        int numberOfOnePage = 0;

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        //get the height of device
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //get the height of action bar
        final TypedArray styledAttributes = getActivity().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        float dpActionBarSize = actionBarSize / displayMetrics.density;

        int numColumns = 0;
        if (!isTablet) {
            numColumns = (int) ((dpHeight - dpActionBarSize - 30) / 30);
        } else {
            numColumns = (int) ((dpHeight - dpActionBarSize - 60) / 60);
        }
        numberOfOnePage = numColumns + 5;

        return numberOfOnePage;
    }

    private int getTotalPagesForProductsList(int totalCounts) {
        int totalPages = 0;
        if (totalCounts <= mNumberOfOnePage) {
            totalPages = 1;
        } else {
            totalPages = totalCounts / mNumberOfOnePage + 1;
        }

        return totalPages;
    }


}
