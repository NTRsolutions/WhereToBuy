package com.example.ryan.wheretobuy.util;

import com.example.ryan.wheretobuy.model.BioIsland;
import com.example.ryan.wheretobuy.model.Blackmores;
import com.example.ryan.wheretobuy.model.Ostelin;
import com.example.ryan.wheretobuy.model.Swisse;

public class GetInfoFromModel {

    public static String getLongName(String itemName, int index) {
        String longName = "";
        if (itemName.equals("SWISSE")) longName = Swisse.longName[index];
        if (itemName.equals("BLACKMORES")) longName = Blackmores.longName[index];
        if (itemName.equals("BIOISLAND")) longName = BioIsland.longName[index];
        if (itemName.equals("OSTELIN")) longName = Ostelin.longName[index];

        return longName;
    }

    public static String getShortName(String itemName, int index) {
        String shortName = "";
        if (itemName.equals("SWISSE")) shortName = Swisse.shortName[index];
        if (itemName.equals("BLACKMORES")) shortName = Blackmores.shortName[index];
        if (itemName.equals("BIOISLAND")) shortName = BioIsland.shortName[index];
        if (itemName.equals("OSTELIN")) shortName = Ostelin.shortName[index];

        return shortName;
    }

    public static float getLowestPrice(String itemName, int index) {
        float lowestPrice = 0;
        if (itemName.equals("SWISSE")) lowestPrice = Swisse.lowestPrice[index];
        if (itemName.equals("BLACKMORES")) lowestPrice = Blackmores.lowestPrice[index];
        if (itemName.equals("BIOISLAND")) lowestPrice = BioIsland.lowestPrice[index];
        if (itemName.equals("OSTELIN")) lowestPrice = Ostelin.lowestPrice[index];

        return lowestPrice;
    }

    public static float getCMWPrice(String itemName, int index) {
        float cmwPrice = 0;
        if (itemName.equals("SWISSE")) cmwPrice = Swisse.cmwPrice[index];
        if (itemName.equals("BLACKMORES")) cmwPrice = Blackmores.cmwPrice[index];
        if (itemName.equals("BIOISLAND")) cmwPrice = BioIsland.cmwPrice[index];
        if (itemName.equals("OSTELIN")) cmwPrice = Ostelin.cmwPrice[index];

        return cmwPrice;
    }

    public static float getPLPrice(String itemName, int index) {
        float plPrice = 0;
        if (itemName.equals("SWISSE")) plPrice = Swisse.plPrice[index];
        if (itemName.equals("BLACKMORES")) plPrice = Blackmores.plPrice[index];
        if (itemName.equals("BIOISLAND")) plPrice = BioIsland.plPrice[index];
        if (itemName.equals("OSTELIN")) plPrice = Ostelin.plPrice[index];

        return plPrice;
    }

    public static float getFLPrice(String itemName, int index) {
        float flPrice = 0;
        if (itemName.equals("SWISSE")) flPrice = Swisse.flPrice[index];
        if (itemName.equals("BLACKMORES")) flPrice = Blackmores.flPrice[index];
        if (itemName.equals("BIOISLAND")) flPrice = BioIsland.flPrice[index];
        if (itemName.equals("OSTELIN")) flPrice = Ostelin.flPrice[index];

        return flPrice;
    }

    public static float getTWPrice(String itemName, int index) {
        float twPrice = 0;
        if (itemName.equals("SWISSE")) twPrice = Swisse.twPrice[index];
        if (itemName.equals("BLACKMORES")) twPrice = Blackmores.twPrice[index];
        if (itemName.equals("BIOISLAND")) twPrice = BioIsland.twPrice[index];
        if (itemName.equals("OSTELIN")) twPrice = Ostelin.twPrice[index];

        return twPrice;
    }

    public static float getHWPrice(String itemName, int index) {
        float hwPrice = 0;
        if (itemName.equals("SWISSE")) hwPrice = Swisse.hwPrice[index];
        if (itemName.equals("BLACKMORES")) hwPrice = Blackmores.hwPrice[index];
        if (itemName.equals("BIOISLAND")) hwPrice = BioIsland.hwPrice[index];
        if (itemName.equals("OSTELIN")) hwPrice = Ostelin.hwPrice[index];

        return hwPrice;
    }
}
