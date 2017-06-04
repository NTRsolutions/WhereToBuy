package com.example.ryan.wheretobuy.model;

import com.example.ryan.wheretobuy.R;

public class Ostelin {

    public static final String[] id = new String[]{
            "OST001",
            "OST002",
            "OST003",
            "OST004"
    };

    public static final String[] shortName = new String[]{
            "OST VD&Calcium Kids 90c",
            "OST VD&Calcium Kids 50c",
            "OST Vitamin D Kids 20ml",
            "OST Vit D & Cal 300 Tabs"
    };

    public static final String[] longName = new String[] {
            "Ostelin Vitamin D and Calcium Kids Chewable 90",
            "Ostelin Vitamin D Calcium Kids Chewable 50 Tabs",
            "Ostelin Vitamin D Kids Liquid 20ml",
            "Ostelin Vitamin D Calcium 300 Tablets"
    };

    public static float[] lowestPrice = new float[id.length];
    public static float[] highestPrice = new float[id.length];
    public static String[] whichIsLowest = new String[id.length];

    public static final String[] cmwID = new String[]{
            "80322",
            "73151",
            "64807",
            "68621"
    };

    public static float[] cmwPrice = new float[id.length];

    public static final String[] plID = new String[] {
            "21418",
            "16318",
            "13625",
            ""
    };

    public static float[] plPrice = new float[id.length];

    public static final String[] flID = new String[] {
            "15248",
            "14949",
            "10089",
            ""
    };

    public static float[] flPrice = new float[id.length];

    public static final String[] twID = new String[] {
            "",
            "42752",
            "30574",
            ""
    };

    public static float[] twPrice = new float[id.length];

    public static float[] hwPrice = new float[id.length];

    public static int getOstelinImageId(String id){
        int ostelinImageId = R.drawable.sws001;

        switch (id) {
            case "OST001":
                ostelinImageId = R.drawable.ost001;
                break;
            case "OST002":
                ostelinImageId = R.drawable.ost002;
                break;
            case "OST003":
                ostelinImageId = R.drawable.ost003;
                break;
            case "OST004":
                ostelinImageId = R.drawable.ost004;
                break;
        }
        return ostelinImageId;
    }

}
