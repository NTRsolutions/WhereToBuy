package com.mainframevampire.ryan.wheretobuy.model;


import android.text.TextUtils;

import java.io.Serializable;

public class ProductPrice implements Serializable {
    private String mID;
    private String mShortName;
    private String mLongName;
    private String mBrand;
    private float mLowestPrice;
    private float mHighestPrice;
    private String mWhichIsLowest;
    private String mInformation;
    private float mCMWPrice;
    private float mPLPrice;
    private float mFLPrice;
    private float mTWPrice;
    private float mHWPrice;
    private String mCMWUrl;
    private String mPLUrl;
    private String mFLUrl;
    private String mTWUrl;
    private String mHWUrl;
    private String mCustomiseFlag;
    private String mRecommendationFlag;
    private String mLastUpdateDateString;

    public final static String ARRAY_DIVIDER = "02959$3213";
    public final static String ARRAY_DIVIDER_SPILIT = "02959\\$3213";


    //constructor for insert value in table
    public ProductPrice(String id,
                        String shortName,
                        String longName,
                        String brand,
                        float lowestPrice,
                        float highestPrice,
                        String whichIsLowest,
                        String information,
                        float CMWPrice,
                        float PLPrice,
                        float FLPrice,
                        float TWPrice,
                        float HWPrice,
                        String CMWUrl,
                        String PLUrl,
                        String FLUrl,
                        String TWUrl,
                        String HWUrl,
                        String customiseFlag,
                        String recommendationFlag,
                        String lastUpdateDateString) {
        mID = id;
        mShortName = shortName;
        mLongName = longName;
        mBrand = brand;
        mLowestPrice = lowestPrice;
        mHighestPrice = highestPrice;
        mWhichIsLowest = whichIsLowest;
        mInformation = information;
        mCMWPrice = CMWPrice;
        mPLPrice = PLPrice;
        mFLPrice = FLPrice;
        mTWPrice = TWPrice;
        mHWPrice = HWPrice;
        mCMWUrl = CMWUrl;
        mPLUrl = PLUrl;
        mFLUrl = FLUrl;
        mTWUrl = TWUrl;
        mHWUrl = HWUrl;
        mCustomiseFlag = customiseFlag;
        mRecommendationFlag = recommendationFlag;
        mLastUpdateDateString = lastUpdateDateString;
    }

    //constructor for update value in table
    public ProductPrice(String id,
                        float lowestPrice,
                        float highestPrice,
                        String whichIsLowest,
                        String information,
                        float CMWPrice,
                        float PLPrice,
                        float FLPrice,
                        float TWPrice,
                        float HWPrice,
                        String CMWUrl,
                        String PLUrl,
                        String FLUrl,
                        String TWUrl,
                        String HWUrl,
                        String recommendationFlag,
                        String lastUpdateDateString) {
        mID = id;
        mLowestPrice = lowestPrice;
        mHighestPrice = highestPrice;
        mWhichIsLowest = whichIsLowest;
        mInformation = information;
        mCMWPrice = CMWPrice;
        mPLPrice = PLPrice;
        mFLPrice = FLPrice;
        mTWPrice = TWPrice;
        mHWPrice = HWPrice;
        mCMWUrl = CMWUrl;
        mPLUrl = PLUrl;
        mFLUrl = FLUrl;
        mTWUrl = TWUrl;
        mHWUrl = HWUrl;
        mRecommendationFlag = recommendationFlag;
        mLastUpdateDateString = lastUpdateDateString;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public float getLowestPrice() {
        return mLowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        mLowestPrice = lowestPrice;
    }

    public float getHighestPrice() {
        return mHighestPrice;
    }

    public void setHighestPrice(float highestPrice) {
        mHighestPrice = highestPrice;
    }

    public String getWhichIsLowest() {
        return mWhichIsLowest;
    }

    public void setWhichIsLowest(String whichIsLowest) {
        mWhichIsLowest = whichIsLowest;
    }

    public float getCMWPrice() {
        return mCMWPrice;
    }

    public void setCMWPrice(float CMWPrice) {
        mCMWPrice = CMWPrice;
    }

    public float getPLPrice() {
        return mPLPrice;
    }

    public void setPLPrice(float PLPrice) {
        mPLPrice = PLPrice;
    }

    public float getFLPrice() {
        return mFLPrice;
    }

    public void setFLPrice(float FLPrice) {
        mFLPrice = FLPrice;
    }

    public float getTWPrice() {
        return mTWPrice;
    }

    public void setTWPrice(float TWPrice) {
        mTWPrice = TWPrice;
    }

    public float getHWPrice() {
        return mHWPrice;
    }

    public void setHWPrice(float HWPrice) {
        mHWPrice = HWPrice;
    }

    public String getLastUpdateDateString() {
        return mLastUpdateDateString;
    }

    public void setLastUpdateDateString(String lastUpdateDateString) {
        mLastUpdateDateString = lastUpdateDateString;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        mShortName = shortName;
    }

    public String getLongName() {
        return mLongName;
    }

    public void setLongName(String longName) {
        mLongName = longName;
    }

    public String getCustomiseFlag() {
        return mCustomiseFlag;
    }

    public void setCustomiseFlag(String customiseFlag) {
        mCustomiseFlag = customiseFlag;
    }

    public String getRecommendationFlag() {
        return mRecommendationFlag;
    }

    public void setRecommendationFlag(String recommendationFlag) {
        mRecommendationFlag = recommendationFlag;
    }

    public String getCMWUrl() {
        return mCMWUrl;
    }

    public void setCMWUrl(String CMWUrl) {
        mCMWUrl = CMWUrl;
    }

    public String getPLUrl() {
        return mPLUrl;
    }

    public void setPLUrl(String PLUrl) {
        mPLUrl = PLUrl;
    }

    public String getFLUrl() {
        return mFLUrl;
    }

    public void setFLUrl(String FLUrl) {
        mFLUrl = FLUrl;
    }

    public String getTWUrl() {
        return mTWUrl;
    }

    public void setTWUrl(String TWUrl) {
        mTWUrl = TWUrl;
    }

    public String getHWUrl() {
        return mHWUrl;
    }

    public void setHWUrl(String HWUrl) {
        mHWUrl = HWUrl;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getInformation() {
        return mInformation;
    }

    public void setInformation(String information) {
        mInformation = information;
    }


}
