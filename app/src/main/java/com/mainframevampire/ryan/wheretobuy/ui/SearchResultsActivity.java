package com.mainframevampire.ryan.wheretobuy.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.adapters.GridAdapter;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.util.ConfigHelper;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = SearchResultsActivity.class.getSimpleName();
    private ArrayList<ProductPrice> mProductPrices = new ArrayList<>();
    private GridAdapter mGridAdapter;
    private RecyclerView mSearchRecyclerView;
    private TextView mHeader;
    private int mNumberOfOnePage = 0;
    private int mTotalPages = 0;
    private int mTotalCounts = 0;
    //number of columns for grid view
    private int mNumCulomns = 0;
    private int mNumRows = 0;
    private String mFormattedQueryString;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_search);

        mHandler = new Handler();

        mSearchRecyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        TextView resultLabel = (TextView) findViewById(R.id.resultLabel);

        //get search input
        Intent intent = getIntent();
        String queryString = "";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryString = intent.getStringExtra(SearchManager.QUERY);
            setTitle("Search Results for " + queryString);
        }
        Log.d(TAG, "Search input: " + queryString);

        mFormattedQueryString = queryString.trim().toLowerCase();

        ProductsDataSource dataSource = new ProductsDataSource(this);
        mTotalCounts = dataSource.readTableGetSearchCount(mFormattedQueryString);
        Log.d(TAG, "total Counts: " + mTotalCounts);

        if (mTotalCounts == 0) {
            resultLabel.setVisibility(View.VISIBLE);
            mSearchRecyclerView.setVisibility(View.GONE);
        } else {
            resultLabel.setVisibility(View.GONE);
            mSearchRecyclerView.setVisibility(View.VISIBLE);

            //get the item number of one page for different screen size and oritention.
            mNumCulomns = ConfigHelper.getNumberColumn(this);
            mNumRows = ConfigHelper.getNumberRows(this);
            mNumberOfOnePage = mNumCulomns * (mNumRows + 1);
            Log.d(TAG, "number of one page: " + mNumberOfOnePage);
            //get the total pages
            mTotalPages = getTotalPagesForSearchList(mTotalCounts);
            Log.d(TAG, "total pages: " + mTotalPages);

            //load first page data
            mProductPrices = dataSource.readTableBySearchQuery(mFormattedQueryString, mNumberOfOnePage, " ");
            mHeader = (TextView) findViewById(R.id.search_header);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, mNumCulomns, GridLayoutManager.VERTICAL, false);
            mSearchRecyclerView.setLayoutManager(gridLayoutManager);
            mSearchRecyclerView.setHasFixedSize(true);

            mGridAdapter = new GridAdapter(this, mProductPrices, mSearchRecyclerView, mNumRows);
            mSearchRecyclerView.setAdapter(mGridAdapter);

            mGridAdapter.setOnLoadListener(new GridAdapter.OnLoadListener() {
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
                                mGridAdapter.notifyItemInserted(mProductPrices.size() - 1);
                                Log.d(TAG, " mAdapter progress bar ");
                            }
                        });

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //remove progress item
                                mProductPrices.remove(mProductPrices.size() - 1);
                                mGridAdapter.notifyItemRemoved(mProductPrices.size());
                                Log.d(TAG, "mProductPrices.size(): " + mProductPrices.size());

                                if (mProductPrices.size() == mTotalCounts) {
                                    Log.d(TAG, " reached the end");
                                    mGridAdapter.setLoaded();
                                } else {
                                    loadNextSearchDataFromDatabase(mFormattedQueryString);
                                }
                            }
                        }, 2000);
                    }
                }
            });

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (mGridAdapter.getItemViewType(position)) {
                        case 1: //item view
                            return 1;
                        case 0: //progress bar
                            return mNumCulomns; //number of columns of the grid
                        default:
                            return -1;
                    }
                }
            });

//            mScrollListener = new EndLessRecyclerViewScrollListener(gridLayoutManager) {
//                @Override
//                public void onLoadMore(int currentPage, int totalItemCount, RecyclerView recyclerView) {
//                    //triggered only when new data needs to be loaded
//                    if (currentPage == mTotalPages) {
//                        //end of the pages
//                        Log.d(TAG, "Pages: end of the pages");
//                    } else {
//                        Log.d(TAG, "Pages: " + currentPage);
//                        loadNextSearchDataFromDatabase(mFormattedQueryString);
//                    }
//                }
//            };
//            mSearchRecyclerView.addOnScrollListener(mScrollListener);
        }

    }

    private int getTotalPagesForSearchList(int totalCounts) {
        int totalPages = 0;
        if (totalCounts <= mNumberOfOnePage) {
            totalPages = 1;
        } else {
            totalPages = totalCounts / mNumberOfOnePage + 1;
        }

        return totalPages;
    }

    private int getNumberColumn() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numColumns = 0;
        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (!isTablet) {
            numColumns = (int) (dpWidth / 170);
        } else {
            numColumns = (int) (dpWidth / 270);
        }

        return numColumns;
    }

    //append the next page of data into the adapter
    private void loadNextSearchDataFromDatabase(final String formattedQueryString) {
        final ProductsDataSource dataSource = new ProductsDataSource(this);
        final ArrayList<ProductPrice> productPrices;

        final String lastIdInPreviousPage = mProductPrices.get(mProductPrices.size() - 1).getID();

        productPrices = dataSource.readTableBySearchQuery(formattedQueryString, mNumberOfOnePage, lastIdInPreviousPage);

        for (ProductPrice productPrice: productPrices) {
            mProductPrices.add(productPrice);
        }
        mGridAdapter.notifyItemInserted(mProductPrices.size() - 1);
        mGridAdapter.setLoaded();
    }


}
