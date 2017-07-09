package com.mainframevampire.ryan.wheretobuy.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.adapters.EndLessRecyclerViewScrollListener;
import com.mainframevampire.ryan.wheretobuy.adapters.GridAdapter;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = SearchResultsActivity.class.getSimpleName();
    private EndLessRecyclerViewScrollListener mScrollListener;
    private ArrayList<ProductPrice> mProductPrices = new ArrayList<>();
    private GridAdapter mGridAdapter;
    private RecyclerView mSearchRecyclerView;
    private ProgressBar mLoadingProgressBar;
    private int mNumberOfOnePage = 0;
    private int mTotalPages = 0;
    private String mFormattedQueryString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_search);

        mSearchRecyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        TextView resultLabel = (TextView) findViewById(R.id.resultLabel);

        //get search input
        Intent intent = getIntent();
        String queryString = "";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryString = intent.getStringExtra(SearchManager.QUERY);
        }
        Log.d(TAG, "Search input: " + queryString);

        mFormattedQueryString = queryString.trim().toLowerCase();

        ProductsDataSource dataSource = new ProductsDataSource(this);
        int totalCounts = dataSource.readTableGetSearchCount(mFormattedQueryString);
        Log.d(TAG, "total Counts: " + totalCounts);

        if (totalCounts == 0) {
            resultLabel.setVisibility(View.VISIBLE);
            mSearchRecyclerView.setVisibility(View.GONE);
        } else {
            resultLabel.setVisibility(View.GONE);
            mSearchRecyclerView.setVisibility(View.VISIBLE);

            //get the item number of one page for different screen size and oritention.
            mNumberOfOnePage = getItemNumbersOfOnePageForSearchList();
            Log.d(TAG, "number of one page: " + mNumberOfOnePage);
            //get the total pages
            mTotalPages = getTotalPagesForSearchList(totalCounts);
            Log.d(TAG, "total pages: " + mTotalPages);

            //load first page data
            mProductPrices = dataSource.readTableBySearchQuery(mFormattedQueryString, mNumberOfOnePage, " ");

            mGridAdapter = new GridAdapter(this, mProductPrices);
            mSearchRecyclerView.setAdapter(mGridAdapter);
            int numberColumn = getNumberColumn();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberColumn);
            mSearchRecyclerView.setLayoutManager(gridLayoutManager);
            mScrollListener = new EndLessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int currentPage, int totalItemCount, RecyclerView recyclerView) {
                    //triggered only when new data needs to be loaded
                    if (currentPage == mTotalPages) {
                        //end of the pages
                        Log.d(TAG, "Pages: end of the pages");
                    } else {
                        Log.d(TAG, "Pages: " + currentPage);
                        loadNextSearchDataFromDatabase(mFormattedQueryString);
                    }
                }
            };
            mSearchRecyclerView.addOnScrollListener(mScrollListener);
        }

    }


    public int getItemNumbersOfOnePageForSearchList() {
        int numberOfOnePage = 0;

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        //get the height of device
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //get the height of action bar
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        float dpActionBarSize = actionBarSize / displayMetrics.density;

        int numRows = 0;
        if (!isTablet) {
            numRows = (int) ((dpHeight - dpActionBarSize) / 140);
        } else {
            numRows = (int) ((dpHeight - dpActionBarSize) / 280);
        }

        //get number of columns
        int numColumns = getNumberColumn();
        numberOfOnePage = numRows * numColumns + 6;

        return numberOfOnePage;
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
            numColumns = (int) (dpWidth / 320);
        }

        return numColumns;
    }

    //append the next page of data into the adapter
    private void loadNextSearchDataFromDatabase(final String formattedQueryString) {
        final ProductsDataSource dataSource = new ProductsDataSource(this);
        final ArrayList<ProductPrice> productPrices;

        final String lastIdInPreviousPage = mProductPrices.get(mProductPrices.size() - 1).getID();

        productPrices = dataSource.readTableBySearchQuery(formattedQueryString, mNumberOfOnePage, lastIdInPreviousPage);

        mSearchRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (ProductPrice productPrice: productPrices) {
                    mProductPrices.add(productPrice);
                }
                mGridAdapter.notifyDataSetChanged();
                Log.d(TAG, "ListAdapter changed ");
            }
        },2000);
    }
}
