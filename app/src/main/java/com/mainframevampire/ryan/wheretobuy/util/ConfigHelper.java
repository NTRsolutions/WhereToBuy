package com.mainframevampire.ryan.wheretobuy.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

import com.mainframevampire.ryan.wheretobuy.R;

public class ConfigHelper {
    //call this method to get the number of rows for different screen size
    public static int getNumberRows(Context context) {
        boolean isTablet = context.getResources().getBoolean(R.bool.is_tablet);
        //get the height of device
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //get the height of action bar
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        float dpActionBarSize = actionBarSize / displayMetrics.density;

        int numRows = 0;
        if (!isTablet) {
            numRows = (int) ((dpHeight - dpActionBarSize) / 140);
        } else {
            numRows = (int) ((dpHeight - dpActionBarSize) / 280);
        }

        return numRows;
    }

    //call this method to get the number of culumns for different screen size
    public static int getNumberColumn(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numColumns = 0;
        boolean isTablet = context.getResources().getBoolean(R.bool.is_tablet);
        if (!isTablet) {
            numColumns = (int) (dpWidth / 170);
        } else {
            numColumns = (int) (dpWidth / 260);
        }

        return numColumns;
    }
}
