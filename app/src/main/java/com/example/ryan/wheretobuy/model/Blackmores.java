package com.example.ryan.wheretobuy.model;

import com.example.ryan.wheretobuy.R;

public class Blackmores {
    public static final String[] id = new String[]{
            "BKM001",
            "BKM002",
            "BKM003",
            "BKM004",
            "BKM005",
            "BKM006",
            "BKM007",
            "BKM008",
            "BKM009",
            "BKM010",
            "BKM011",
            "BKM012",
            "BKM013",
            "BKM014",
            "BKM015",
            "BKM016",
            "BKM017",
            "BKM018",
    };

    public static final String[] shortName = new String[]{
            "BKM Preg&Breastfeeding 180c",
            "BKM Fish Oil 1000mg 400 Caps",
            "BKM 3* Fish Oil 150 Caps",
            "BKM Odourless Fishoil 400c",
            "BKM CoQ10 150mg 30 Caps",
            "BKM CoQ10 150mg 125 Caps",
            "BKM CoQ10 75mg 90 Caps",
            "BKM Glucosamine Sulfate 180t",
            "BKM Joint Formula 120 Tabs",
            "BKM Cranberry 60 Caps",
            "BKM Kids Immunities 60 Caps",
            "BKM Propolis 1000mg 220 Caps",
            "BKM Milk Thistle 42 Tabs",
            "BKM Vitex Angus Castus 40t",
            "BKM Sugar Balance 90 Tabs",
            "BKM Vitamin E cream 50g",
            "BKM evening primrose 190c",
            "BKM Celery 50 tabs"
    };

    public static final String[] longName = new String[] {
            "Blackmores Pregnancy&Breastfeeding Gold 180 Caps",
            "Blackmores Fish Oil 1000mg 400 Capsules",
            "Blackmores Omega 3* Fish Oil 150 Capsules",
            "Blackmores Odourless Fish Oil 1000mg 400 Capsules",
            "Blackmores CoQ10 150mg High Potency 30 Capsules",
            "Blackmores CoQ10 150mg 125 Capsules Exclusive",
            "Blackmores CoQ10 75mg 90 Capsules",
            "Blackmores Glucosamine Sulfate 1500mg 180 Tabs",
            "Blackmores Joint Formula 120 Tablets",
            "Blackmores Cranberry 15000mg 60 Capsules",
            "Blackmores Kids Immunities 60 Capsules",
            "Blackmores Propolis 1000mg 220 Capsules",
            "Blackmores Milk Thistle 42 Tablets",
            "Blackmores Vitex Angus Castus 40 Tablets",
            "Blackmores Sugar Balance 90 Tablets",
            "Blackmores Natural Vitamin E Cream Skin Barrier 50g",
            "Blackmores Evening Primrose Oil 190 Capsules",
            "Blackmores Celery 3000 50 Tablets"
    };

    public static float[] lowestPrice = new float[id.length];
    public static float[] highestPrice = new float[id.length];
    public static String[] whichIsLowest = new String[id.length];

    public static final String[] cmwID = new String[]{
            "50953",
            "50010",
            "65960",
            "55300",
            "51821",
            "73360",
            "58007",
            "58934",
            "58936",
            "53338",
            "60191",
            "76853",
            "41057",
            "44786",
            "61715",
            "80184",
            "53030",
            "42131"
    };

    public static float[] cmwPrice = new float[id.length];

    public static final String[] plID = new String[] {
            "14200",
            "13896",
            "13890",
            "13893",
            "14058",
            "     ",
            "14063",
            "13528",
            "13557",
            "14194",
            "13636",
            "13505",
            "13770",
            "     ",
            "     ",
            "17634",
            "14201",
            "13526"
    };

    public static float[] plPrice = new float[id.length];

    public static final String[] flID = new String[] {
            "966  ",
            "6014 ",
            "9933 ",
            "9214 ",
            "6394 ",
            "     ",
            "7506 ",
            "6366 ",
            "6015 ",
            "890  ",
            "14390",
            "10636",
            "949  ",
            "999  ",
            "981  ",
            "998  ",
            "905  ",
            "883  "
    };

    public static float[] flPrice = new float[id.length];

    public static final String[] twID = new String[] {
            "41765",
            "41760",
            "41764",
            "41763",
            "30926",
            "     ",
            "30264",
            "46128",
            "40756",
            "31041",
            "31076",
            "43639",
            "30939",
            "30929",
            "33724",
            "46696",
            "41759",
            "30855"
    };

    public static float[] twPrice = new float[id.length];

    public static final String[] hwID = new String[] {
            "     ",
            "23138",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "375  ",
            "     ",
            "     ",
            "     ",
            "     "
    };

    public static float[] hwPrice = new float[id.length];

    public static int getBlackmoresImageId(String id){
        int blackmoresImageId = R.drawable.sws001;

        switch (id) {
            case "BKM001":
                blackmoresImageId = R.drawable.bkm001;
                break;
            case "BKM002":
                blackmoresImageId = R.drawable.bkm002;
                break;
            case "BKM003":
                blackmoresImageId = R.drawable.bkm003;
                break;
            case "BKM004":
                blackmoresImageId = R.drawable.bkm004;
                break;
            case "BKM005":
                blackmoresImageId = R.drawable.bkm005;
                break;
            case "BKM006":
                blackmoresImageId = R.drawable.bkm006;
                break;
            case "BKM007":
                blackmoresImageId = R.drawable.bkm007;
                break;
            case "BKM008":
                blackmoresImageId = R.drawable.bkm008;
                break;
            case "BKM009":
                blackmoresImageId = R.drawable.bkm009;
                break;
            case "BKM010":
                blackmoresImageId = R.drawable.bkm010;
                break;
            case "BKM011":
                blackmoresImageId = R.drawable.bkm011;
                break;
            case "BKM012":
                blackmoresImageId = R.drawable.bkm012;
                break;
            case "BKM013":
                blackmoresImageId = R.drawable.bkm013;
                break;
            case "BKM014":
                blackmoresImageId = R.drawable.bkm014;
                break;
            case "BKM015":
                blackmoresImageId = R.drawable.bkm015;
                break;
            case "BKM016":
                blackmoresImageId = R.drawable.bkm016;
                break;
            case "BKM017":
                blackmoresImageId = R.drawable.bkm017;
                break;
            case "BKM018":
                blackmoresImageId = R.drawable.bkm018;
                break;
        }
        return blackmoresImageId;
    }

}
