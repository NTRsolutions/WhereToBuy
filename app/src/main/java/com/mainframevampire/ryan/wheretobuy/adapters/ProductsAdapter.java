package com.mainframevampire.ryan.wheretobuy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.ui.DownloadService;
import com.mainframevampire.ryan.wheretobuy.ui.ProductsFragment;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter{
    private final ProductsFragment.onProductListSelectedInterface mListener;
    private String mItemName;
    private Context mContext;
    private ArrayList<ProductPrice> mProductPrices = new ArrayList<>();


    public ProductsAdapter(ProductsFragment.onProductListSelectedInterface listener,
                           ArrayList<ProductPrice> productPrices,
                           String ItemName,
                           Context context) {
        mListener = listener;
        mProductPrices = productPrices;
        mItemName = ItemName;
        mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_item, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {

        return mProductPrices.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mShortName;
        private TextView mCMWPrice;
        private TextView mPLPrice;
        private TextView mFLPrice;
        private TextView mTWPrice;
        private TextView mHWPrice;
        private ImageView mFavouriteImage;
        private int mIndex;
        private String mId;

        public ListViewHolder(View itemView) {
            super(itemView);

            mShortName = (TextView) itemView.findViewById(R.id.shortName);
            mCMWPrice = (TextView) itemView.findViewById(R.id.cmw_price);
            mPLPrice = (TextView) itemView.findViewById(R.id.pl_price);
            mFLPrice = (TextView) itemView.findViewById(R.id.fl_price);
            mTWPrice = (TextView) itemView.findViewById(R.id.tw_price);
            mHWPrice = (TextView) itemView.findViewById(R.id.hw_price);
            mFavouriteImage = (ImageView) itemView.findViewById(R.id.favoriteImage);

            mFavouriteImage.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
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
                if (isFavourite.equals("Y")){
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



}
