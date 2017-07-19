package com.mainframevampire.ryan.wheretobuy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mainframevampire.ryan.wheretobuy.R;

public class ViewPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String productID = getArguments().getString(ProductDetailActivity.PRODUCT_ID);

        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);

        final PriceFragment priceFragment = new PriceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProductDetailActivity.PRODUCT_ID, productID);
        priceFragment.setArguments(bundle);

        final InfoFragment infoFragment = new InfoFragment();
        bundle = new Bundle();
        bundle.putString(ProductDetailActivity.PRODUCT_ID, productID);
        infoFragment.setArguments(bundle);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position == 0 ? priceFragment : infoFragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return position == 0 ? "Price" : "Information";
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }



}
