package com.mainframevampire.ryan.wheretobuy.model;

import com.mainframevampire.ryan.wheretobuy.R;

public class BioIsland {
    public static final String[] id = new String[]{
            "BOI001",
            "BOI002",
            "BOI003",
            "BOI004",
            "BOI005",
            "BOI006"
    };

    public static final String[] shortName = new String[]{
            "Milk Cal BoneCare 300c",
            "Milk Cal Kids 90 Caps",
            "Zinc 120 Chewable Tabs",
            "Liver+Oil Kids 90 Caps",
            "DHA Kids 60 Caps",
            "Kangaroo Essence 90c"
    };

    public static final String[] longName = new String[] {
            "Bio Island Milk Calcium Bone Care 300 Caps",
            "Bio Island Milk Calcium Kids 90 Caps",
            "Bio Island Zinc 120 Chewable Tablets",
            "Bio Island Cod Liver Fish Oil Kids 90 Caps",
            "Bio Island DHA Kids 60 Caps",
            "Bio Island Kangaroo Essence 50000 90 Caps"
    };

    public static float[] lowestPrice = new float[id.length];
    public static float[] highestPrice = new float[id.length];
    public static String[] whichIsLowest = new String[id.length];

    public static final String[] cmwID = new String[]{
            "78272",
            "75444",
            "75447",
            "75439",
            "75441",
            "78228"
    };


    public static float[] cmwPrice = new float[id.length];

    public static float[] plPrice = new float[id.length];

    public static final String[] flID = new String[] {
            "14292",
            "13256",
            "11176",
            "13257",
            "",
            "14527"
    };

    public static float[] flPrice = new float[id.length];
    public static float[] twPrice = new float[id.length];
    public static float[] hwPrice = new float[id.length];

    public static String[] cmwURL = new String[id.length];
    public static String[] plURL = new String[id.length];
    public static String[] flURL = new String[id.length];
    public static String[] twURL = new String[id.length];
    public static String[] hwURL = new String[id.length];

    public static int getBioIslandImageId(String id){
        int bioIslandImageId = R.drawable.sws001;

        switch (id) {
            case "BOI001":
                bioIslandImageId = R.drawable.boi001;
                break;
            case "BOI002":
                bioIslandImageId = R.drawable.boi002;
                break;
            case "BOI003":
                bioIslandImageId = R.drawable.boi003;
                break;
            case "BOI004":
                bioIslandImageId = R.drawable.boi004;
                break;
            case "BOI005":
                bioIslandImageId = R.drawable.boi005;
                break;
            case "BOI006":
                bioIslandImageId = R.drawable.boi006;
                break;
        }
        return bioIslandImageId;
    }


}
