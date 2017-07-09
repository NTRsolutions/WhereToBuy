package com.mainframevampire.ryan.wheretobuy.adapters;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.ui.ProductsFragment;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnLoadListener {
        void onLoadData();
        void onLoadHeader();
    }

    private final ProductsFragment.onProductListSelectedInterface mListener;
    private String mItemName;
    private Context mContext;
    private ArrayList<ProductPrice> mProductPrices = new ArrayList<>();

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    //minimum amount of items to have below user current scroll position
    private int mVisibleThreshold = 2;
    private int mLastVisibleItem, mTotalItemCount;
    private boolean mLoading;
    private OnLoadListener mOnloadListener;


    public ListAdapter(ProductsFragment.onProductListSelectedInterface listener,
                       ArrayList<ProductPrice> productPrices,
                       String ItemName,
                       RecyclerView recyclerView) {
        mListener = listener;
        mProductPrices = productPrices;
        mItemName = ItemName;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean isUserScrolling = false;
                private boolean isListGoingUp = true;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //check if the top most is visible and user is scrolling
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        isUserScrolling = true;
                        if (isListGoingUp) {
                            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isListGoingUp) {
                                            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                                                if (mOnloadListener != null) {
                                                    mOnloadListener.onLoadHeader();
                                                }
                                            }
                                        }
                                    }
                                },50);
                            }
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mTotalItemCount = linearLayoutManager.getItemCount();
                    mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (isUserScrolling) {
                        if (dy > 0) {
                            isListGoingUp = false;
                        }
                        else {
                            isListGoingUp = true;
                        }
                    }

                    if (!mLoading && mTotalItemCount <= (mLastVisibleItem + mVisibleThreshold)) {
                        //reached the end
                        //do something
                        if (mOnloadListener != null) {
                            mOnloadListener.onLoadData();
                        }
                        mLoading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mProductPrices.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_item, parent, false);
            viewHolder = new TextViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_progress_item, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).bindView(position);
        } else {
            ((ProgressViewHolder) holder).mProgressBar.setIndeterminate(true);
        }

    }

    public void setLoaded() {
        mLoading = false;
    }

    @Override
    public int getItemCount() {
        return mProductPrices.size();
    }

    public void setOnLoadListener(OnLoadListener onloadListener) {
        mOnloadListener = onloadListener;
    }

    private class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mShortName;
        private TextView mCMWPrice;
        private TextView mPLPrice;
        private TextView mFLPrice;
        private TextView mTWPrice;
        private TextView mHWPrice;
        private ImageView mFavouriteImage;
        private int mIndex;
        private String mId;

        public TextViewHolder(View itemView) {
            super(itemView);

            mShortName = (TextView) itemView.findViewById(R.id.shortName);
            mCMWPrice = (TextView) itemView.findViewById(R.id.cmw_price);
            mPLPrice = (TextView) itemView.findViewById(R.id.pl_price);
            mFLPrice = (TextView) itemView.findViewById(R.id.fl_price);
            mTWPrice = (TextView) itemView.findViewById(R.id.tw_price);
            mHWPrice = (TextView) itemView.findViewById(R.id.hw_price);
            mFavouriteImage = (ImageView) itemView.findViewById(R.id.favoriteImage);

            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            mIndex = position;
            mId = mProductPrices.get(position).getID();
            String shortName = mProductPrices.get(position).getShortName();
            float lowestPrice = mProductPrices.get(position).getLowestPrice();
            float cmwPrice = mProductPrices.get(position).getCMWPrice();
            float plPrice = mProductPrices.get(position).getPLPrice();
            float flPrice = mProductPrices.get(position).getFLPrice();
            float twPrice = mProductPrices.get(position).getTWPrice();
            float hwPrice = mProductPrices.get(position).getHWPrice();
            String isFavourite = mProductPrices.get(position).getCustomiseFlag();

            mShortName.setText(shortName);
            //if price == 0, display NA;if not, display int with rounded value
            if (cmwPrice == 0) {
                mCMWPrice.setText(R.string.NA);
                mCMWPrice.setTextColor(Color.BLACK);
            } else {
                mCMWPrice.setText(String.valueOf(Math.round(cmwPrice)));
                mCMWPrice.setTextColor(Color.BLACK);
                if (cmwPrice == lowestPrice) mCMWPrice.setTextColor(Color.RED);
            }

            if (plPrice == 0) {
                mPLPrice.setText(R.string.NA);
                mPLPrice.setTextColor(Color.BLACK);
            } else {
                mPLPrice.setText(String.valueOf(Math.round(plPrice)));
                mPLPrice.setTextColor(Color.BLACK);
                if (plPrice == lowestPrice) mPLPrice.setTextColor(Color.RED);
            }

            if (flPrice == 0) {
                mFLPrice.setText(R.string.NA);
                mFLPrice.setTextColor(Color.BLACK);
            } else {
                mFLPrice.setText(String.valueOf(Math.round(flPrice)));
                mFLPrice.setTextColor(Color.BLACK);
                if (flPrice == lowestPrice) mFLPrice.setTextColor(Color.RED);
            }

            if (twPrice == 0) {
                mTWPrice.setText(R.string.NA);
                mTWPrice.setTextColor(Color.BLACK);
            } else {
                mTWPrice.setText(String.valueOf(Math.round(twPrice)));
                mTWPrice.setTextColor(Color.BLACK);
                if (twPrice == lowestPrice) mTWPrice.setTextColor(Color.RED);
            }

            if (hwPrice == 0) {
                mHWPrice.setText(R.string.NA);
                mHWPrice.setTextColor(Color.BLACK);
            } else {
                mHWPrice.setText(String.valueOf(Math.round(hwPrice)));
                mHWPrice.setTextColor(Color.BLACK);
                if (hwPrice == lowestPrice) mHWPrice.setTextColor(Color.RED);
            }

//            mCMWPrice.setText(String.valueOf(Math.round(cmwPrice)));
//            mPLPrice.setText(String.valueOf(Math.round(plPrice)));
//            mFLPrice.setText(String.valueOf(Math.round(flPrice)));
//            mTWPrice.setText(String.valueOf(Math.round(twPrice)));
//            mHWPrice.setText(String.valueOf(Math.round(hwPrice)));

            if (mItemName.equals("MyList")) {
                mFavouriteImage.setVisibility(View.VISIBLE);
            } else {
                if (isFavourite.equals("Y")) {
                    mFavouriteImage.setVisibility(View.VISIBLE);
                } else {
                    mFavouriteImage.setVisibility(View.INVISIBLE);
                }

            }

        }

        @Override
        public void onClick(View v) {
            mListener.onProductSelected(mId, mItemName);
        }
    }

    private class ProgressViewHolder extends  RecyclerView.ViewHolder {
        public ProgressBar mProgressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

}
