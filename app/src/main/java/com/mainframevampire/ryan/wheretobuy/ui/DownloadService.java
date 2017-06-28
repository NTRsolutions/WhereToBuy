package com.mainframevampire.ryan.wheretobuy.ui;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mainframevampire.ryan.wheretobuy.R;
import com.mainframevampire.ryan.wheretobuy.database.ProductsDataSource;
import com.mainframevampire.ryan.wheretobuy.model.BioIsland;
import com.mainframevampire.ryan.wheretobuy.model.Blackmores;
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.model.Swisse;
import com.mainframevampire.ryan.wheretobuy.util.GetInfoFromWebsite;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DownloadService extends IntentService{

    private static final String TAG = DownloadService.class.getSimpleName();
    private static final int REQEUST_OPEN = 93;
    private static final int REQEUST_CLOSE = 193;
    private static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE" ;

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 76;

    public DownloadService() {
        super("DownloadService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean isFirstRun = intent.getBooleanExtra(MainActivity.IS_FIRST_RUN, false);
        String brand = intent.getStringExtra(MainActivity.LIST_NAME);

        Log.d("DownloadService", "Send Message: " + brand);

        //create pending intent for user to click notification to go to MainActivity
        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent pStartMain = PendingIntent.getActivity(this,
                REQEUST_OPEN, mainIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon24)
                .setContentTitle("Downloading")
                .setContentText(brand)
                .setContentIntent(pStartMain)
                .setProgress(0, 0, true);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        downloadPrice(brand, isFirstRun);

        //send download result to activity
        Intent messageIntent = new Intent(MainActivity.BROADCAST_ACTION);
        messageIntent.putExtra(MainActivity.KEY_MESSAGE, brand);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

    }

    private void downloadPrice(String brand, boolean isFirstRun) {
        if (isFirstRun) {
            if (brand.equals("BLACKMORES")) {
                GetInfoFromWebsite.getBlackmoresPrice();
                createBlackmoresValueInTable();
            }
            if (brand.equals("BIOISLAND")) {
                GetInfoFromWebsite.getBioIslandPrice();
                createBioIslandValueInTable();
            }
            if (brand.equals("OSTELIN")) {
                GetInfoFromWebsite.getOstelinPrice();
                createOstelinValueInTable();
                mNotificationManager.cancel(NOTIFICATION_ID);
            }
        } else {
            if (brand.equals("SWISSE")) {
                GetInfoFromWebsite.getSwissePrice();
                updateSwisseValueInTable();
            }
            if (brand.equals("BLACKMORES")) {
                GetInfoFromWebsite.getBlackmoresPrice();
                updateBlackmoresValueInTable();
            }
            if (brand.equals("BIOISLAND")) {
                GetInfoFromWebsite.getBioIslandPrice();
                updateBioIslandValueInTable();
            }
            if (brand.equals("OSTELIN")) {
                GetInfoFromWebsite.getOstelinPrice();
                updateOstelinValueInTable();
                mNotificationManager.cancel(NOTIFICATION_ID);
            }
        }
    }

    private void createBlackmoresValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < Blackmores.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(Blackmores.lowestPrice[i], Blackmores.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    Blackmores.id[i],
                    Blackmores.shortName[i],
                    Blackmores.longName[i],
                    Blackmores.lowestPrice[i],
                    Blackmores.highestPrice[i],
                    Blackmores.whichIsLowest[i],
                    Blackmores.cmwPrice[i],
                    Blackmores.plPrice[i],
                    Blackmores.flPrice[i],
                    Blackmores.twPrice[i],
                    Blackmores.hwPrice[i],
                    Blackmores.cmwURL[i],
                    Blackmores.plURL[i],
                    Blackmores.flURL[i],
                    Blackmores.twURL[i],
                    Blackmores.hwURL[i],
                    "N",
                    recommendationFlag,
                    currentDateString);
            dataSource.createContents(productPrice);
        }
    }
    private void createBioIslandValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < BioIsland.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(BioIsland.lowestPrice[i], BioIsland.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    BioIsland.id[i],
                    BioIsland.shortName[i],
                    BioIsland.longName[i],
                    BioIsland.lowestPrice[i],
                    BioIsland.highestPrice[i],
                    BioIsland.whichIsLowest[i],
                    BioIsland.cmwPrice[i],
                    BioIsland.plPrice[i],
                    BioIsland.flPrice[i],
                    BioIsland.twPrice[i],
                    BioIsland.hwPrice[i],
                    BioIsland.cmwURL[i],
                    BioIsland.plURL[i],
                    BioIsland.flURL[i],
                    BioIsland.twURL[i],
                    BioIsland.hwURL[i],
                    "N",
                    recommendationFlag,
                    currentDateString);
            dataSource.createContents(productPrice);
        }
    }
    private void createOstelinValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < Ostelin.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(Ostelin.lowestPrice[i], Ostelin.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    Ostelin.id[i],
                    Ostelin.shortName[i],
                    Ostelin.longName[i],
                    Ostelin.lowestPrice[i],
                    Ostelin.highestPrice[i],
                    Ostelin.whichIsLowest[i],
                    Ostelin.cmwPrice[i],
                    Ostelin.plPrice[i],
                    Ostelin.flPrice[i],
                    Ostelin.twPrice[i],
                    Ostelin.hwPrice[i],
                    Ostelin.cmwURL[i],
                    Ostelin.plURL[i],
                    Ostelin.flURL[i],
                    Ostelin.twURL[i],
                    Ostelin.hwURL[i],
                    "N",
                    recommendationFlag,
                    currentDateString);
            dataSource.createContents(productPrice);
        }
    }

    private String getRecomendationFlag(Float lowestPrice, Float highestPrice) {
        Float savePrice = highestPrice - lowestPrice;
        if (savePrice/highestPrice >= 0.4) {
            return "Y";
        } else {
            return "N";
        }
    }

    private void updateSwisseValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < Swisse.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(Swisse.lowestPrice[i], Swisse.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    Swisse.id[i],
                    Swisse.lowestPrice[i],
                    Swisse.highestPrice[i],
                    Swisse.whichIsLowest[i],
                    Swisse.cmwPrice[i],
                    Swisse.plPrice[i],
                    Swisse.flPrice[i],
                    Swisse.twPrice[i],
                    Swisse.hwPrice[i],
                    Swisse.cmwURL[i],
                    Swisse.plURL[i],
                    Swisse.flURL[i],
                    Swisse.twURL[i],
                    Swisse.hwURL[i],
                    recommendationFlag,
                    currentDateString
            );
            dataSource.updatePriceInTable(productPrice);
        }
    }
    private void updateBlackmoresValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < Blackmores.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(Blackmores.lowestPrice[i], Blackmores.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    Blackmores.id[i],
                    Blackmores.lowestPrice[i],
                    Blackmores.highestPrice[i],
                    Blackmores.whichIsLowest[i],
                    Blackmores.cmwPrice[i],
                    Blackmores.plPrice[i],
                    Blackmores.flPrice[i],
                    Blackmores.twPrice[i],
                    Blackmores.hwPrice[i],
                    Blackmores.cmwURL[i],
                    Blackmores.plURL[i],
                    Blackmores.flURL[i],
                    Blackmores.twURL[i],
                    Blackmores.hwURL[i],
                    recommendationFlag,
                    currentDateString
            );
            dataSource.updatePriceInTable(productPrice);
        }
    }

    private void updateBioIslandValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < BioIsland.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(BioIsland.lowestPrice[i], BioIsland.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    BioIsland.id[i],
                    BioIsland.lowestPrice[i],
                    BioIsland.highestPrice[i],
                    BioIsland.whichIsLowest[i],
                    BioIsland.cmwPrice[i],
                    BioIsland.plPrice[i],
                    BioIsland.flPrice[i],
                    BioIsland.twPrice[i],
                    BioIsland.hwPrice[i],
                    BioIsland.cmwURL[i],
                    BioIsland.plURL[i],
                    BioIsland.flURL[i],
                    BioIsland.twURL[i],
                    BioIsland.hwURL[i],
                    recommendationFlag,
                    currentDateString
            );
            dataSource.updatePriceInTable(productPrice);
        }
    }

    private void updateOstelinValueInTable() {
        ProductsDataSource dataSource = new ProductsDataSource(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(new Date());
        for (int i = 0; i < Ostelin.id.length; i++) {
            String recommendationFlag = getRecomendationFlag(Ostelin.lowestPrice[i], Ostelin.highestPrice[i]);
            ProductPrice productPrice = new ProductPrice(
                    Ostelin.id[i],
                    Ostelin.lowestPrice[i],
                    Ostelin.highestPrice[i],
                    Ostelin.whichIsLowest[i],
                    Ostelin.cmwPrice[i],
                    Ostelin.plPrice[i],
                    Ostelin.flPrice[i],
                    Ostelin.twPrice[i],
                    Ostelin.hwPrice[i],
                    Ostelin.cmwURL[i],
                    Ostelin.plURL[i],
                    Ostelin.flURL[i],
                    Ostelin.twURL[i],
                    Ostelin.hwURL[i],
                    recommendationFlag,
                    currentDateString
            );
            dataSource.updatePriceInTable(productPrice);
        }
    }
}
