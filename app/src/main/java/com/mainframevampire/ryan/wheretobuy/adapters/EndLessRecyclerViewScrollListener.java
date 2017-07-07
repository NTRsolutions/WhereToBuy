package com.mainframevampire.ryan.wheretobuy.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndLessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    //the minimum amount of items to have below your current position or before mLoading more
    private int mVisibleThreshold = 5;
    //the current offset index of data you have loaded;
    private int mCurrentPage = 0;
    //the total number of items in the dataset after the last load
    private int mPreviousTotalItemCount = 0;
    //True if we are still waiting for the last set of data to load;
    private boolean mLoading = true;
    //Sets the starting page index;
    private int mStartingPageIndex = 0;

    RecyclerView.LayoutManager mLayoutManager;

    public EndLessRecyclerViewScrollListener(LinearLayoutManager linearLayoutManager) {
        mLayoutManager = linearLayoutManager;
    }

    public EndLessRecyclerViewScrollListener(GridLayoutManager gridLayoutManager) {
        mLayoutManager = gridLayoutManager;
        mVisibleThreshold = mVisibleThreshold * gridLayoutManager.getSpanCount();
    }

    public EndLessRecyclerViewScrollListener(StaggeredGridLayoutManager staggeredGridLayoutManager) {
        mLayoutManager = staggeredGridLayoutManager;
        mVisibleThreshold = mVisibleThreshold * staggeredGridLayoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    //this happens many times a second during a scroll, so be wary.
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        //get the total items of adapters bound to the RecyclerView.
        int totalItemCount = mLayoutManager.getItemCount();

        //get the last visible item position
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager)
                    .findLastVisibleItemPositions(null);
        }
        else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }
        else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        //if the total item count is 0 and the previous isn't, reset to the initial state
        if (totalItemCount < mPreviousTotalItemCount) {
            mCurrentPage = mStartingPageIndex;
            mPreviousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) mLoading = true;
        }

        //if it's still loading, we check to see if the dataset count has changed, if so then loading
        //is finished and we update the current page and total item count
        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        //if it isn't currently loading, we check to see if we have breached the visibleThreshold
        //and need to load more data, if we do, call onLoadMore() to load more data.
        if (!mLoading && (lastVisibleItemPosition + mVisibleThreshold) > totalItemCount) {
            mCurrentPage ++;
            onLoadMore(mCurrentPage, totalItemCount, recyclerView);
            mLoading = true;
        }
    }

    //call this method whenever performing new searches
    public void resetState() {
        mCurrentPage = mStartingPageIndex;
        mPreviousTotalItemCount = 0;
        mLoading = true;
    }

    //Load more data
    public abstract void onLoadMore(int currentPage, int totalItemCount, RecyclerView recyclerView);

}
