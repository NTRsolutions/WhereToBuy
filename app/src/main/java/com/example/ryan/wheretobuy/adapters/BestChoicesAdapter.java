package com.example.ryan.wheretobuy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ryan.wheretobuy.R;
import com.example.ryan.wheretobuy.model.BestChoice;
import com.example.ryan.wheretobuy.model.BioIsland;
import com.example.ryan.wheretobuy.model.Blackmores;
import com.example.ryan.wheretobuy.model.Ostelin;
import com.example.ryan.wheretobuy.model.Swisse;
import com.example.ryan.wheretobuy.ui.MainActivity;
import com.example.ryan.wheretobuy.ui.ProductsActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class BestChoicesAdapter extends RecyclerView.Adapter<BestChoicesAdapter.BestChoiceViewHolder> {

    private ArrayList<BestChoice> mBestChoices;
    private Context mContext;

    public BestChoicesAdapter(Context context, ArrayList<BestChoice> bestChoices) {
        mBestChoices = bestChoices;
        mContext = context;
    }


    @Override
    public BestChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.best_choices_item, parent, false);
        BestChoiceViewHolder viewHolder = new BestChoiceViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BestChoiceViewHolder holder, int position) {
        holder.bindBestChoice(mBestChoices.get(position));
    }



    @Override
    public int getItemCount() {
        return mBestChoices.size();
    }

    public class BestChoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mGridImageView;
        private TextView mGridName1;
        private TextView mGridName2;
        private TextView mGridLowestPrice;
        private TextView mGridSavePrice;
        private TextView mGridRrpPrice;
        private TextView mGridLongName;
        private String mId;

        public BestChoiceViewHolder(View itemView) {
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

        public void bindBestChoice(BestChoice bestChoice) {

            mId = bestChoice.getID();

            mGridLongName.setText(bestChoice.getLongName());

            if (mId.substring(0,3).equals("SWS")){

                Glide.with(mContext).load(Swisse.getSwisseImageId(mId)).into(mGridImageView);
            }
            if (mId.substring(0,3).equals("BKM")){
                Glide.with(mContext).load(Blackmores.getBlackmoresImageId(mId)).into(mGridImageView);
            }
            if (mId.substring(0,3).equals("BOI")){
                Glide.with(mContext).load(BioIsland.getBioIslandImageId(mId)).into(mGridImageView);
            }
            if (mId.substring(0,3).equals("OST")){
                //Drawable drawable = mContext.getResources().getDrawable(Ostelin.getOstelinImageId(mId));
                //mGridImageView.setImageDrawable(drawable);
                Glide.with(mContext).load(Ostelin.getOstelinImageId(mId)).into(mGridImageView);
            }

            String[] names = bestChoice.getWhichIsLowest().split(" ");
            mGridName1.setText(names[0]);
            mGridName2.setText(names[1]);

            String lowestPriceString = mContext.getString(R.string.dollar_sign) + String.valueOf(bestChoice.getLowestPrice());
            mGridLowestPrice.setText(lowestPriceString);

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            float savePrice = bestChoice.getHighestPrice() - bestChoice.getLowestPrice();
            String savePriceString = mContext.getString(R.string.save) +
                    " " +
                    mContext.getString(R.string.dollar_sign) +
                    String.valueOf(df.format(savePrice));
            mGridSavePrice.setText(savePriceString);

            String rrpPriceString = mContext.getString(R.string.rrp) + " " +
                    mContext.getString(R.string.dollar_sign) +
                    String.valueOf(bestChoice.getHighestPrice());
            mGridRrpPrice.setText(rrpPriceString);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ProductsActivity.class);
            intent.putExtra(MainActivity.FRAGMENT_NAME, "FRAGMENT_DETAIL");
            intent.putExtra(ProductsActivity.PRODUCT_ID, mId);
            mContext.startActivity(intent);
        }
    }

}
