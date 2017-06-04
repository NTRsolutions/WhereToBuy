package com.example.ryan.wheretobuy.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ryan.wheretobuy.R;
import com.example.ryan.wheretobuy.model.BioIsland;
import com.example.ryan.wheretobuy.model.Blackmores;
import com.example.ryan.wheretobuy.model.Ostelin;
import com.example.ryan.wheretobuy.model.Swisse;
import com.example.ryan.wheretobuy.ui.ProductsFragment;
import com.example.ryan.wheretobuy.util.GetInfoFromModel;

public class ProductsAdapter extends RecyclerView.Adapter{
    private final ProductsFragment.onProductSelectedInterface mListener;
    private String mItemName;


    public ProductsAdapter(ProductsFragment.onProductSelectedInterface listener, String itemName) {
        mListener = listener;
        mItemName = itemName;
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
        int index = 0;
        if (mItemName.equals("SWISSE")) index = Swisse.id.length;
        if (mItemName.equals("BLACKMORES")) index = Blackmores.id.length;
        if (mItemName.equals("BIOISLAND")) index = BioIsland.id.length;
        if (mItemName.equals("OSTELIN")) index = Ostelin.id.length;

        return index;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mShortName;
        private TextView mCMWPrice;
        private TextView mPLPrice;
        private TextView mFLPrice;
        private TextView mTWPrice;
        private TextView mHWPrice;
        private int mIndex;

        public ListViewHolder(View itemView) {
            super(itemView);

            mShortName = (TextView) itemView.findViewById(R.id.shortName);
            mCMWPrice = (TextView) itemView.findViewById(R.id.cmw_price);
            mPLPrice = (TextView) itemView.findViewById(R.id.pl_price);
            mFLPrice = (TextView) itemView.findViewById(R.id.fl_price);
            mTWPrice = (TextView) itemView.findViewById(R.id.tw_price);
            mHWPrice = (TextView) itemView.findViewById(R.id.hw_price);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            mIndex = position;
            String shortName = GetInfoFromModel.getShortName(mItemName, mIndex);
            float lowestPrice = GetInfoFromModel.getLowestPrice(mItemName, mIndex);
            float cmwPrice = GetInfoFromModel.getCMWPrice(mItemName, mIndex);
            float plPrice = GetInfoFromModel.getPLPrice(mItemName, mIndex);
            float flPrice = GetInfoFromModel.getFLPrice(mItemName, mIndex);
            float twPrice = GetInfoFromModel.getTWPrice(mItemName, mIndex);
            float hwPrice = GetInfoFromModel.getHWPrice(mItemName, mIndex);

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

        }

        @Override
        public void onClick(View v) {
            mListener.onProductSelected(mIndex, mItemName);
        }
    }



}
