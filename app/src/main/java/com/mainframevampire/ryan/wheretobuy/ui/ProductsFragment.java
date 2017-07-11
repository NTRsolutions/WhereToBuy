package com.mainframevampire.ryan.wheretobuy.ui;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.adapters.ListAdapter;
import com.mainframevampire.ryan.wheretobuy.adapters.EndLessRecyclerViewScrollListener;
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
    private ListAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private TextView mHeader;
    private int mNumberOfOnePage = 0;
    private int mTotalPages = 0;
    private int mTotalCounts = 0;
    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onProductListSelectedInterface listener = (onProductListSelectedInterface) getActivity();
        mItemName = getArguments().getString(MainActivity.LIST_NAME);
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        mHandler = new Handler();
        final ProductsDataSource dataSource = new ProductsDataSource(getActivity());
        mTotalCounts = 0;
        if (mItemName.equals("MyList")) {
            mTotalCounts = dataSource.readTableGetCustomisedCount();
        } else {
            mTotalCounts = dataSource.readTableGetBrandCount(mItemName);
        }
        Log.d(TAG, mItemName + " totalCounts: " + mTotalCounts);

        //get the item number of one page for different screen size and oritention.
        mNumberOfOnePage = getItemNumbersOfOnePageForProductsList();
        Log.d(TAG, mItemName + " number of one page: " + mNumberOfOnePage);
        //get the total pages
        mTotalPages = getTotalPagesForProductsList(mTotalCounts);
        Log.d(TAG, mItemName + " total pages: " + mTotalPages);

        //load first page data
        if (mItemName.equals("MyList")) {
            mProductPrices = dataSource.readTableByCustomiseFlag("Y", mNumberOfOnePage, " ");
        } else {
            mProductPrices = dataSource.readTableByBrand(mItemName, mNumberOfOnePage, " ");
        }

        mHeader = (TextView) view.findViewById(R.id.products_header);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //mProductsAdapter = new ProductsAdapter(listener, mProductPrices, mItemName, getActivity());
        mListAdapter = new ListAdapter(listener, mProductPrices, mItemName, mRecyclerView);
        //mRecyclerView.setAdapter(mProductsAdapter);
        mRecyclerView.setAdapter(mListAdapter);

        mListAdapter.setOnLoadListener(new ListAdapter.OnLoadListener() {

            @Override
            public void onLoadHeader() {
                if (mTotalCounts >= mNumberOfOnePage) {
                    Log.d(TAG, "you've reached the top");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHeader.setVisibility(View.VISIBLE);
                        }
                    });

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mHeader.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onLoadData() {
                if (mTotalCounts >= mNumberOfOnePage) {
                    //add progress item
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProductPrices.add(null);
                            mListAdapter.notifyItemInserted(mProductPrices.size() - 1);
                            Log.d(TAG, " mAdapter progress bar ");
                        }
                    });

                    final ProductsDataSource dataSource = new ProductsDataSource(getActivity());

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //remove progress item
                            mProductPrices.remove(mProductPrices.size() - 1);
                            mListAdapter.notifyItemRemoved(mProductPrices.size());
                            Log.d(TAG, "mProductPrices.size(): " + mProductPrices.size());

                            if (mProductPrices.size() == mTotalCounts) {
                                Log.d(TAG, " reached the end");
                                mListAdapter.setLoaded();
                            } else {
                                ArrayList<ProductPrice> productPrices;
                                String lastIdInPreviousPage = mProductPrices.get(mProductPrices.size() - 1).getID();
                                if (mItemName.equals("MyList")) {
                                    productPrices = dataSource.readTableByCustomiseFlag("Y", mNumberOfOnePage, lastIdInPreviousPage);
                                    Log.d(TAG, mItemName + " Last ID in previous page " + lastIdInPreviousPage);
                                } else {
                                    productPrices = dataSource.readTableByBrand(mItemName, mNumberOfOnePage, lastIdInPreviousPage);
                                    Log.d(TAG, mItemName + " Last ID in previous page " + lastIdInPreviousPage);
                                }
                                for (ProductPrice productPrice : productPrices) {
                                    mProductPrices.add(productPrice);
                                }
                                mListAdapter.notifyItemInserted(mProductPrices.size() - 1);
                                mListAdapter.setLoaded();
                            }
                        }
                    }, 2000);
                }
            }
        });

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
                mListAdapter.notifyDataSetChanged();
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
        numberOfOnePage = numColumns;

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
