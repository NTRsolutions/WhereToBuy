package com.example.ryan.wheretobuy.model;

import java.io.Serializable;

public class BestChoice implements Serializable {
    private String mID;
    private String mLongName;
    private float mLowestPrice;
    private String mWhichIsLowest;
    private float mHighestPrice;

    public BestChoice(String id, String longName, float lowestPrice, String whichIsLowest, float highestPrice) {
        mID = id;
        mLongName = longName;
        mLowestPrice = lowestPrice;
        mWhichIsLowest = whichIsLowest;
        mHighestPrice = highestPrice;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getLongName() {
        return mLongName;
    }

    public void setLongName(String longName) {
        mLongName = longName;
    }

    public float getLowestPrice() {
        return mLowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        mLowestPrice = lowestPrice;
    }

    public String getWhichIsLowest() {
        return mWhichIsLowest;
    }

    public void setWhichIsLowest(String whichIsLowest) {
        mWhichIsLowest = whichIsLowest;
    }

    public float getHighestPrice() {
        return mHighestPrice;
    }

    public void setHighestPrice(float highestPrice) {
        mHighestPrice = highestPrice;
    }
}
