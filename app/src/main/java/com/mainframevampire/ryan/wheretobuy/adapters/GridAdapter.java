package com.mainframevampire.ryan.wheretobuy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.model.BioIsland;
import com.mainframevampire.ryan.wheretobuy.model.Blackmores;
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.model.Swisse;
import com.mainframevampire.ryan.wheretobuy.ui.MainActivity;
import com.mainframevampire.ryan.wheretobuy.ui.ProductDetailActivity;
import com.mainframevampire.ryan.wheretobuy.ui.ProductListActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnLoadListener {
        void onLoadData();
        void onLoadHeader();
    }

    private ArrayList<ProductPrice> mProductPrices;
    private Context mContext;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    //minimum amount of items to have below user current scroll position, get it from constructor
    private int mVisibleThreshold = 0;
    private int mLastVisibleItem, mTotalItemCount;
    private boolean mLoading;
    private OnLoadListener mOnloadListener;

    public GridAdapter(Context context, ArrayList<ProductPrice> productPrices, RecyclerView recyclerView, int numRows) {
        mProductPrices = productPrices;
        mContext = context;
        mVisibleThreshold = 2 * numRows;

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
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
                            if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isListGoingUp) {
                                            if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
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
                    mTotalItemCount = gridLayoutManager.getItemCount();
                    mLastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
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
                        Log.d("GridAdapter", "mLoading before load:" + mLoading);
                        if (mOnloadListener != null) {
                            mOnloadListener.onLoadData();
                        }
                        mLoading = true;
                        Log.d("GridAdapter", "mLoading before after:" + mLoading);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_choices_item, parent, false);
            viewHolder = new GridViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_progress_item, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GridViewHolder) {
            ((GridViewHolder) holder).bindView(position);
        } else {
            ((ProgressViewHolder) holder).mProgressBar.setIndeterminate(true);
        }
    }



    @Override
    public int getItemCount() {
        return mProductPrices.size();
    }

    public void setLoaded() {
        mLoading = false;
    }

    public void setOnLoadListener(OnLoadListener onloadListener) {
        mOnloadListener = onloadListener;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mGridImageView;
        private TextView mGridName1;
        private TextView mGridName2;
        private TextView mGridLowestPrice;
        private TextView mGridSavePrice;
        private TextView mGridRrpPrice;
        private TextView mGridLongName;
        private String mId;
        private String mListName;

        public GridViewHolder(View itemView) {
            super(itemView);

            mGridLongName = (TextView) itemView.findViewById(R.id.gridLongName);
            mGridImageView = (ImageView) itemView.findViewById(R.id.gridImageView);
            mGridName1 = (TextView) itemView.findViewById(R.id.gridName1);
            mGridName2 = (TextView) itemView.findViewById(R.id.gridName2);
            mGridLowestPrice = (TextView) itemView.findViewById(R.id.gridLowestPrice);
            mGridSavePrice = (TextView) itemView.findViewById(R.id.gridSavePrice);
            mGridRrpPrice = (TextView) itemView.findViewById(R.id.gridRrpPrice);


            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {

            mId = mProductPrices.get(position).getID();

            mGridLongName.setText(mProductPrices.get(position).getLongName());

            if (mId.substring(0,3).equals("SWS")){
                Glide.with(mContext).load(Swisse.getSwisseImageId(mId)).into(mGridImageView);
                mListName = "Swisse";
            }
            if (mId.substring(0,3).equals("BKM")){
                Glide.with(mContext).load(Blackmores.getBlackmoresImageId(mId)).into(mGridImageView);
                mListName = "Blackmores";
            }
            if (mId.substring(0,3).equals("BOI")){
                Glide.with(mContext).load(BioIsland.getBioIslandImageId(mId)).into(mGridImageView);
                mListName = "BioIsland";
            }
            if (mId.substring(0,3).equals("OST")){
                //Drawable drawable = mContext.getResources().getDrawable(Ostelin.getOstelinImageId(mId));
                //mGridImageView.setImageDrawable(drawable);
                Glide.with(mContext).load(Ostelin.getOstelinImageId(mId)).into(mGridImageView);
                mListName = "Ostelin";
            }

            String[] names = mProductPrices.get(position).getWhichIsLowest().split(" ");
            Log.d("name", mProductPrices.get(position).getWhichIsLowest());
            mGridName1.setText(names[0]);
            mGridName2.setText(names[1]);

            String lowestPriceString = mContext.getString(R.string.dollar_sign) + String.valueOf(mProductPrices.get(position).getLowestPrice());
            mGridLowestPrice.setText(lowestPriceString);

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            float savePrice = mProductPrices.get(position).getHighestPrice() - mProductPrices.get(position).getLowestPrice();
            String savePriceString = mContext.getString(R.string.save) +
                    " " +
                    mContext.getString(R.string.dollar_sign) +
                    String.valueOf(df.format(savePrice));
            mGridSavePrice.setText(savePriceString);

            String rrpPriceString = mContext.getString(R.string.rrp) + " " +
                    mContext.getString(R.string.dollar_sign) +
                    String.valueOf(mProductPrices.get(position).getHighestPrice());
            mGridRrpPrice.setText(rrpPriceString);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.PRODUCT_ID, mId);
            mContext.startActivity(intent);
        }
    }

    private class ProgressViewHolder extends  RecyclerView.ViewHolder {
        public ProgressBar mProgressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public void updateData(ArrayList<ProductPrice> productPrices) {
        mProductPrices = productPrices;
    }

}
