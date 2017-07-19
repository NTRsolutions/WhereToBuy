package com.mainframevampire.ryan.wheretobuy.util;

import android.text.TextUtils;

import com.mainframevampire.ryan.wheretobuy.model.BioIsland;
import com.mainframevampire.ryan.wheretobuy.model.Blackmores;
import com.mainframevampire.ryan.wheretobuy.model.Ostelin;
import com.mainframevampire.ryan.wheretobuy.model.ProductPrice;
import com.mainframevampire.ryan.wheretobuy.model.Swisse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetInfoFromWebsite {
    //call this method to parse the data from the URLs and get the products' price

    public static void getSwissePrice() {

        //chemist warehouse:Swisse
        String[] cmwURls = new String[] {
                "http://www.chemistwarehouse.com.au/Shop-OnLine/587/Swisse?size=120",
                "http://www.chemistwarehouse.com.au/Shop-OnLine/587/Swisse?size=120&page=2"};

        for (String url: cmwURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-container,span.price");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-container")) {
                            String id = element.attr("href").split("/")[2];
                            for (int i = 0; i < Swisse.id.length; i++) {
                                if (id.equals(Swisse.cmwID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Swisse.id.length;
                                    Swisse.cmwURL[index] = ("http://www.chemistwarehouse.com.au" + element.attr("href"));
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("Price")) {
                            Swisse.cmwPrice[index] = Float.parseFloat(element.text().substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get information from Chemist Warehouse swisse product url
        for(int i = 0; i < Swisse.id.length; i++) {
            try {
                Document doc = Jsoup.connect(Swisse.cmwURL[i]).get();
                Elements elements = doc.select("div.details");
                ArrayList<String> information = new ArrayList<>();
                for(Element element: elements) {
                    if (element.text().equals("")) {
                        information.add("none");
                    } else {
                        information.add(element.text());
                    }
                }
                Swisse.information[i] = TextUtils.join(ProductPrice.ARRAY_DIVIDER, information.toArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //priceline:Swisse
        String[] plURls = new String[] {
                "https://www.priceline.com.au/search/?limit=100&q=swisse",
                "https://www.priceline.com.au/search/p/2?limit=100&q=swisse"};

        for (String url: plURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-image,span.price,span.regular-price");
                int index = 0;
                boolean ifFound = false;
                String prev_URL = "";
                for (Element element: elements) {
                    if (!ifFound) {
                        if(element.className().equals("product-image")) {
                            prev_URL = element.attr("href");
                        }
                        if (element.className().equals("prev-price price") ||
                        element.className().equals("regular-price")) {
                            String id = element.attr("id").split("-")[2];
                            for (int i = 0; i < Swisse.id.length; i++) {
                                if (id.equals(Swisse.plID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Swisse.id.length;
                                    Swisse.plURL[index] = prev_URL;
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("price")) {
                            Swisse.plPrice[index] = Float.parseFloat(element.text().substring(1));
                            Swisse.plURL[index] = prev_URL;
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //for4Less:Swisse
        String[] flURls = new String[] {
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=2",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=3",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=4",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=5",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=6",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=7",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=8",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=9",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/swisse.html?p=10"};

        for (String url: flURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-image,div.price-box-hover");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-image")) {
                            String id = element.attr("id").split("-")[1];
                            for (int i = 0; i < Swisse.id.length; i++) {
                                if (id.equals(Swisse.flID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Swisse.id.length;
                                    Swisse.flURL[index] = element.attr("href");
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("price-box-hover")) {
                            Swisse.flPrice[index] = Float.parseFloat(element.text().split(" ")[0].substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //terrywhite:Swiise
        String[] twURls = new String[] {
                "https://shop.terrywhitechemmart.com.au/vitamins.html?brand=656&page=homepage&view=all"};

        for (String url: twURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-title,p.prod-price");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-title")) {
                            String[] idStrings = element.attr("href").split("/");
                            //some items have different "href" contents
                            if (idStrings.length > 4) {
                                String id = idStrings[7];
                                for (int i = 0; i < Swisse.id.length; i++) {
                                    if (id.equals(Swisse.twID[i].trim())) {
                                        ifFound = true;
                                        index = i;
                                        i = Swisse.id.length;
                                        Swisse.twURL[index] = element.attr("href");
                                    }
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("prod-price")) {
                            Swisse.twPrice[index] = Float.parseFloat(element.text().substring(1));
                            //ifFound = false;
                        }
                        ifFound = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //healthworld-part1 :Swiise
        String[] hwPart1URls = new String[] {
                "http://www.healthyworldpharmacy.com.au/category-s/2044.htm?searching=Y&sort=13&cat=2044&show=90&page=1"};

        for (String url: hwPart1URls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.productnamecolor,div.product_saleprice");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("productnamecolor colors_productname")) {
                            //one sample:<a href="http://www.healthyworldpharmacy.com.au/product-p/311294.htm" class="productnamecolor colors_productname" title="Swisse Children's Ultivite Multivitamin 60 tablets, 311294">
                            String id = element.attr("title").split(",")[1].substring(1);
                            for (int i = 0; i < Swisse.id.length; i++) {
                                if (id.equals(Swisse.hwID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Swisse.id.length;
                                    Swisse.hwURL[index] = element.attr("href");
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("product_saleprice")) {
                            //one sample of element.text(): Sale Price: $12.99
                            Swisse.hwPrice[index] = Float.parseFloat(element.text().substring(13));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //healthworld-part2 :Swiise
        String[] hwPart2URls = new String[] {
                "http://www.healthyworldpharmacy.com.au/category-s/2122.htm",
                "http://www.healthyworldpharmacy.com.au/category-s/2122.htm?searching=Y&sort=13&cat=2122&show=9&page=2"};

        for (String url: hwPart2URls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.productnamecolor,div.product_saleprice");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("v-product__title productnamecolor colors_productname")) {
                            //one sample:<a href="http://www.healthyworldpharmacy.com.au/Swisse-Products-p/256595.htm" class="v-product__title productnamecolor colors_productname" title="Swisse Women's Ultivite 50 + 60 tablets, 256595">
                            String id = element.attr("title").split(",")[1].substring(1);
                            for (int i = 0; i < Swisse.id.length; i++) {
                                if (id.equals(Swisse.hwID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Swisse.id.length;
                                    Swisse.hwURL[index] = element.attr("href");
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("product_saleprice")) {
                            //one sample of element.text(): Sale Price: $12.99
                            Swisse.hwPrice[index] = Float.parseFloat(element.text().substring(13));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get the lowest price and highest price
        for (int i = 0; i < Swisse.id.length; i++ ) {
            float lowest = 999;
            float highest = 0;
            if (Swisse.cmwPrice[i] != 0) {
                if (Swisse.cmwPrice[i] > highest) highest = Swisse.cmwPrice[i];
                if (Swisse.cmwPrice[i] < lowest) lowest = Swisse.cmwPrice[i];
            }

            if (Swisse.plPrice[i] != 0) {
                if (Swisse.plPrice[i] > highest) highest = Swisse.plPrice[i];
                if (Swisse.plPrice[i] < lowest) lowest = Swisse.plPrice[i];
            }

            if (Swisse.flPrice[i] != 0) {
                if (Swisse.flPrice[i] > highest) highest = Swisse.flPrice[i];
                if (Swisse.flPrice[i] < lowest) lowest = Swisse.flPrice[i];
            }

            if (Swisse.twPrice[i] != 0) {
                if (Swisse.twPrice[i] > highest) highest = Swisse.twPrice[i];
                if (Swisse.twPrice[i] < lowest) lowest = Swisse.twPrice[i];
            }

            if (Swisse.hwPrice[i] != 0) {
                if (Swisse.hwPrice[i] > highest) highest = Swisse.hwPrice[i];
                if (Swisse.hwPrice[i] < lowest) lowest = Swisse.hwPrice[i];
            }

            Swisse.lowestPrice[i] = lowest;
            Swisse.highestPrice[i] = highest;

            if (Swisse.cmwPrice[i] == lowest) Swisse.whichIsLowest[i] = "Chemist Warehouse";
            if (Swisse.plPrice[i] == lowest) Swisse.whichIsLowest[i] =  "Priceline Pharmacy";
            if (Swisse.flPrice[i] == lowest) Swisse.whichIsLowest[i] =  "Pharmacy 4Less";
            if (Swisse.twPrice[i] == lowest) Swisse.whichIsLowest[i] =  "TerryWhite Chemmart";
            if (Swisse.hwPrice[i] == lowest) Swisse.whichIsLowest[i] =  "HealthyWorld Pharmacy";

        }

    }

    public static void getBlackmoresPrice() {

        //chemist warehouse：Blackmores
        String[] cmwURls = new String[] {
                "http://www.chemistwarehouse.com.au/Shop-OnLine/513/Blackmores?size=120",
                "http://www.chemistwarehouse.com.au/Shop-OnLine/513/Blackmores?size=120&page=2"};

        for (String url: cmwURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-container,span.price");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-container")) {
                            String id = element.attr("href").split("/")[2];

                            for (int i = 0; i < Blackmores.id.length; i++) {
                                if (id.equals(Blackmores.cmwID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Blackmores.id.length;
                                    Blackmores.cmwURL[index] = ("http://www.chemistwarehouse.com.au" + element.attr("href"));
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("Price")) {
                            Blackmores.cmwPrice[index] = Float.parseFloat(element.text().substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get information from Chemist Warehouse Blackmores product url
        for(int i = 0; i < Blackmores.id.length; i++) {
            try {
                Document doc = Jsoup.connect(Blackmores.cmwURL[i]).get();
                Elements elements = doc.select("div.details");
                ArrayList<String> information = new ArrayList<>();
                for(Element element: elements) {
                    if (element.text().equals("")) {
                        information.add("none");
                    } else {
                        information.add(element.text());
                    }
                }
                Blackmores.information[i] = TextUtils.join(ProductPrice.ARRAY_DIVIDER, information.toArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //priceline：Blackmores
        String[] plURls = new String[] {
                "https://www.priceline.com.au/search/?limit=100&q=blackmores",
                "https://www.priceline.com.au/search/p/2?limit=100&q=blackmores"};

        for (String url: plURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-image,span.price,span.regular-price");
                int index = 0;
                boolean ifFound = false;
                String prev_URL = "";
                for (Element element: elements) {
                    if (!ifFound) {
                        if(element.className().equals("product-image")) {
                            prev_URL = element.attr("href");
                        }
                        if (element.className().equals("prev-price price") ||
                                element.className().equals("regular-price")) {
                            String id = element.attr("id").split("-")[2];
                            for (int i = 0; i < Blackmores.id.length; i++) {
                                if (id.equals(Blackmores.plID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Blackmores.id.length;
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("price")) {
                            Blackmores.plPrice[index] = Float.parseFloat(element.text().substring(1));
                            Blackmores.plURL[index] = prev_URL;
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //for4Less：Blackmores
//        String[] flURls = new String[] {
//                "http://www.pharmacy4less.com.au/blackmores-pregnancy-breast-feeding-gold-formula-180-caps-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-odourless-fish-oil-1000-capx400-new-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-omega-triple-concentrated-fish-oil-150-cap-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-odourless-fish-oil-1000-capx400-new-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-coq10-high-potency-150-mg-30-caps-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-coq10-75mg-capx90-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-glucosamine-sulfate-1500-x-180-tablets-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-joint-formula-x120-tablets-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-cranberry-15000-capx60-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-kids-immunities-60-tablets.html",
//                "http://www.pharmacy4less.com.au/blackmores-propolis-1000-x220-cap.html",
//                "http://www.pharmacy4less.com.au/blackmores-milk-thistle-tabx42-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-vitex-agnus-castus-tabx40-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-sugar-balance-tabx90-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-vitamin-e-cream-50g-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-evening-primrose-oil-1000mg-capx190-3.html",
//                "http://www.pharmacy4less.com.au/blackmores-celery-3000-x-50-tabs-3.html"
//        };
        String[] flURls = new String[]{
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=2",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=3",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=4",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=5",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=6",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=7",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=8",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=9",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=10",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=11",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=12",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=13",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=14",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=15",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=16",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=17",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/blackmores.html?p=18",
        };
        for (String url: flURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-image,div.price-box-hover");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-image")) {
                            String id = element.attr("id").split("-")[1];
                            for (int i = 0; i < Blackmores.id.length; i++) {
                                if (id.equals(Blackmores.flID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Blackmores.id.length;
                                    Blackmores.flURL[index] = element.attr("href");
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("price-box-hover")) {
                            Blackmores.flPrice[index] = Float.parseFloat(element.text().split(" ")[0].substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //terrywhite：Blackmores
        String[] twURls = new String[] {
                "https://shop.terrywhitechemmart.com.au/vitamins.html?brand=463&page=homepage&view=all"};

        for (String url: twURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-title,p.prod-price");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-title")) {
                            String[] idStrings = element.attr("href").split("/");
                            //some items have different "href" contents
                            if (idStrings.length > 4) {
                                String id = idStrings[7];
                                for (int i = 0; i < Blackmores.id.length; i++) {
                                    if (id.equals(Blackmores.twID[i].trim())) {
                                        ifFound = true;
                                        index = i;
                                        i = Blackmores.id.length;
                                        Blackmores.twURL[index] = element.attr("href");
                                    }
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("prod-price")) {
                            Blackmores.twPrice[index] = Float.parseFloat(element.text().substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //healthworld：Blackmores
        String[] hwURls = new String[] {
                "http://www.healthyworldpharmacy.com.au/searchresults.asp?searching=Y&sort=13&search=blackmores&show=90&page=1"};

        for (String url: hwURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.productnamecolor,div.product_saleprice");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("v-product__title productnamecolor colors_productname")) {
                            //one sample:<a href="http://www.healthyworldpharmacy.com.au/Swisse-Products-p/256595.htm" class="v-product__title productnamecolor colors_productname" title="Swisse Women's Ultivite 50 + 60 tablets, 256595">
                            String id = element.attr("title").split(",")[1].substring(1);
                            for (int i = 0; i < Blackmores.id.length; i++) {
                                if (id.equals(Blackmores.hwID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Blackmores.id.length;
                                    Blackmores.hwURL[index] = element.attr("href");
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("product_saleprice")) {
                            //one sample of element.text(): Sale Price: $12.99
                            Blackmores.hwPrice[index] = Float.parseFloat(element.text().substring(13));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get the lowest price and highest price
        for (int i = 0; i < Blackmores.id.length; i++ ) {
            float lowest = 999;
            float highest = 0;
            if (Blackmores.cmwPrice[i] != 0) {
                if (Blackmores.cmwPrice[i] > highest) highest = Blackmores.cmwPrice[i];
                if (Blackmores.cmwPrice[i] < lowest) lowest = Blackmores.cmwPrice[i];
            }

            if (Blackmores.plPrice[i] != 0) {
                if (Blackmores.plPrice[i] > highest) highest = Blackmores.plPrice[i];
                if (Blackmores.plPrice[i] < lowest) lowest = Blackmores.plPrice[i];
            }

            if (Blackmores.flPrice[i] != 0) {
                if (Blackmores.flPrice[i] > highest) highest = Blackmores.flPrice[i];
                if (Blackmores.flPrice[i] < lowest) lowest = Blackmores.flPrice[i];
            }

            if (Blackmores.twPrice[i] != 0) {
                if (Blackmores.twPrice[i] > highest) highest = Blackmores.twPrice[i];
                if (Blackmores.twPrice[i] < lowest) lowest = Blackmores.twPrice[i];
            }

            if (Blackmores.hwPrice[i] != 0) {
                if (Blackmores.hwPrice[i] > highest) highest = Blackmores.hwPrice[i];
                if (Blackmores.hwPrice[i] < lowest) lowest = Blackmores.hwPrice[i];
            }

            Blackmores.lowestPrice[i] = lowest;
            Blackmores.highestPrice[i] = highest;

            if (Blackmores.cmwPrice[i] == lowest) Blackmores.whichIsLowest[i] = "Chemist Warehouse";
            if (Blackmores.plPrice[i] == lowest) Blackmores.whichIsLowest[i] =  "Priceline Pharmacy";
            if (Blackmores.flPrice[i] == lowest) Blackmores.whichIsLowest[i] =  "Pharmacy 4Less";
            if (Blackmores.twPrice[i] == lowest) Blackmores.whichIsLowest[i] =  "TerryWhite Chemmart";
            if (Blackmores.hwPrice[i] == lowest) Blackmores.whichIsLowest[i] =  "HealthyWorld Pharmacy";

        }
    }

    public static void getBioIslandPrice() {

        //chemist warehouse：BioIsland
        String[] cmwURls = new String[]{
                "http://www.chemistwarehouse.com.au/Shop-Online/2128/Bio-Island"};

        for (String url : cmwURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-container,span.price");
                int index = 0;
                boolean ifFound = false;
                for (Element element : elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-container")) {
                            String id = element.attr("href").split("/")[2];
                            for (int i = 0; i < BioIsland.id.length; i++) {
                                if (id.equals(BioIsland.cmwID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = BioIsland.id.length;
                                    BioIsland.cmwURL[index] = ("http://www.chemistwarehouse.com.au" + element.attr("href"));
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("Price")) {
                            BioIsland.cmwPrice[index] = Float.parseFloat(element.text().substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get information from Chemist Warehouse swisse product url
        for(int i = 0; i < BioIsland.id.length; i++) {
            try {
                Document doc = Jsoup.connect(BioIsland.cmwURL[i]).get();
                Elements elements = doc.select("div.details");
                ArrayList<String> information = new ArrayList<>();
                for(Element element: elements) {
                    if (element.text().equals("")) {
                        information.add("none");
                    } else {
                        information.add(element.text());
                    }
                }
                BioIsland.information[i] = TextUtils.join(ProductPrice.ARRAY_DIVIDER, information.toArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //priceline no Bio Island products


        //for4Less: Bio Island
        String[] flURls = new String[]{
                "http://www.pharmacy4less.com.au/vitamins/by-brands/bio-island.html",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/bio-island.html?p=2"
        };

        for (String url : flURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-image,div.price-box-hover");
                int index = 0;
                boolean ifFound = false;
                for (Element element : elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-image")) {
                            String id = element.attr("id").split("-")[1];
                            for (int i = 0; i < BioIsland.id.length; i++) {
                                if (id.equals(BioIsland.flID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = BioIsland.id.length;
                                    BioIsland.flURL[index] = element.attr("href");
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("price-box-hover")) {
                            BioIsland.flPrice[index] = Float.parseFloat(element.text().split(" ")[0].substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //get the lowest price and highest price
        for (int i = 0; i < BioIsland.id.length; i++) {
            float lowest = 999;
            float highest = 0;
            if (BioIsland.cmwPrice[i] != 0) {
                if (BioIsland.cmwPrice[i] > highest) highest = BioIsland.cmwPrice[i];
                if (BioIsland.cmwPrice[i] < lowest) lowest = BioIsland.cmwPrice[i];
            }

            if (BioIsland.flPrice[i] != 0) {
                if (BioIsland.flPrice[i] > highest) highest = BioIsland.flPrice[i];
                if (BioIsland.flPrice[i] < lowest) lowest = BioIsland.flPrice[i];
            }

            BioIsland.lowestPrice[i] = lowest;
            BioIsland.highestPrice[i] = highest;

            if (BioIsland.cmwPrice[i] == lowest) BioIsland.whichIsLowest[i] = "Chemist Warehouse";
            if (BioIsland.flPrice[i] == lowest) BioIsland.whichIsLowest[i] = "Pharmacy 4Less";

        }
    }

    public static void getOstelinPrice() {
        //chemist warehouse：Ostelin
        String[] cmwURls = new String[] {
                "http://www.chemistwarehouse.com.au/Shop-Online/903/Ostelin"};

        for (String url: cmwURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-container,span.price");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-container")) {
                            String id = element.attr("href").split("/")[2];

                            for (int i = 0; i < Ostelin.id.length; i++) {
                                if (id.equals(Ostelin.cmwID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Ostelin.id.length;
                                    Ostelin.cmwURL[index] = ("http://www.chemistwarehouse.com.au" + element.attr("href"));
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("Price")) {
                            Ostelin.cmwPrice[index] = Float.parseFloat(element.text().substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get information from Chemist Warehouse swisse product url
        for(int i = 0; i < Ostelin.id.length; i++) {
            try {
                Document doc = Jsoup.connect(Ostelin.cmwURL[i]).get();
                Elements elements = doc.select("div.details");
                ArrayList<String> information = new ArrayList<>();
                for(Element element: elements) {
                    if (element.text().equals("")) {
                        information.add("none");
                    } else {
                        information.add(element.text());
                    }
                }
                Ostelin.information[i] = TextUtils.join(ProductPrice.ARRAY_DIVIDER, information.toArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //priceline：Ostelin
        String[] plURls = new String[] {
                "https://www.priceline.com.au/brand/ostelin"};

        for (String url: plURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-image,span.price,span.regular-price");
                int index = 0;
                boolean ifFound = false;
                String prev_URL = "";
                for (Element element: elements) {
                    if (!ifFound) {
                        if(element.className().equals("product-image")) {
                            prev_URL = element.attr("href");
                        }
                        if (element.className().equals("prev-price price") ||
                                element.className().equals("regular-price")) {
                            String id = element.attr("id").split("-")[2];
                            for (int i = 0; i < Ostelin.id.length; i++) {
                                if (id.equals(Ostelin.plID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Ostelin.id.length;
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("price")) {
                            Ostelin.plPrice[index] = Float.parseFloat(element.text().substring(1));
                            Ostelin.plURL[index] = prev_URL;
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //for4Less：Ostelin
        String[] flURls = new String[] {
                "http://www.pharmacy4less.com.au/vitamins/by-brands/ostelin.html",
                "http://www.pharmacy4less.com.au/vitamins/by-brands/ostelin.html?p=2"
        };

        for (String url: flURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-image,div.price-box-hover");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-image")) {
                            String id = element.attr("id").split("-")[1];
                            for (int i = 0; i < Ostelin.id.length; i++) {
                                if (id.equals(Ostelin.flID[i].trim())) {
                                    ifFound = true;
                                    index = i;
                                    i = Ostelin.id.length;
                                    Ostelin.flURL[index] = element.attr("href");
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("price-box-hover")) {
                            Ostelin.flPrice[index] = Float.parseFloat(element.text().split(" ")[0].substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //terrywhite：Ostelin
        String[] twURls = new String[] {
                "https://shop.terrywhitechemmart.com.au/vitamins.html?brand=104&page=homepage&view=all"};

        for (String url: twURls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a.product-title,p.prod-price");
                int index = 0;
                boolean ifFound = false;
                for (Element element: elements) {
                    if (!ifFound) {
                        if (element.className().equals("product-title")) {
                            String[] idStrings = element.attr("href").split("/");
                            //some items have different "href" contents
                            if (idStrings.length > 4) {
                                String id = idStrings[7];
                                for (int i = 0; i < Ostelin.id.length; i++) {
                                    if (id.equals(Ostelin.twID[i].trim())) {
                                        ifFound = true;
                                        index = i;
                                        i = Ostelin.id.length;
                                        Ostelin.twURL[index] = element.attr("href");
                                    }
                                }
                            }
                        }
                    } else {
                        if (element.className().equals("prod-price")) {
                            Ostelin.twPrice[index] = Float.parseFloat(element.text().substring(1));
                            ifFound = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

        //get the lowest price and highest price
        for (int i = 0; i < Ostelin.id.length; i++ ) {
            float lowest = 999;
            float highest = 0;
            if (Ostelin.cmwPrice[i] != 0) {
                if (Ostelin.cmwPrice[i] > highest) highest = Ostelin.cmwPrice[i];
                if (Ostelin.cmwPrice[i] < lowest) lowest = Ostelin.cmwPrice[i];
            }

            if (Ostelin.plPrice[i] != 0) {
                if (Ostelin.plPrice[i] > highest) highest = Ostelin.plPrice[i];
                if (Ostelin.plPrice[i] < lowest) lowest = Ostelin.plPrice[i];
            }

            if (Ostelin.flPrice[i] != 0) {
                if (Ostelin.flPrice[i] > highest) highest = Ostelin.flPrice[i];
                if (Ostelin.flPrice[i] < lowest) lowest = Ostelin.flPrice[i];
            }

            if (Ostelin.twPrice[i] != 0) {
                if (Ostelin.twPrice[i] > highest) highest = Ostelin.twPrice[i];
                if (Ostelin.twPrice[i] < lowest) lowest = Ostelin.twPrice[i];
            }


            Ostelin.lowestPrice[i] = lowest;
            Ostelin.highestPrice[i] = highest;

            if (Ostelin.cmwPrice[i] == lowest) Ostelin.whichIsLowest[i] = "Chemist Warehouse";
            if (Ostelin.plPrice[i] == lowest) Ostelin.whichIsLowest[i] =  "Priceline Pharmacy";
            if (Ostelin.flPrice[i] == lowest) Ostelin.whichIsLowest[i] =  "Pharmacy 4Less";
            if (Ostelin.twPrice[i] == lowest) Ostelin.whichIsLowest[i] =  "TerryWhite Chemmart";

        }



    }

    public static float getExchangeRate() {

        //chemist warehouse
        String url = "http://www.rba.gov.au/statistics/frequency/exchange-rates.html";
        float todayRate = 0;

        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("td");
            int i = 0;
            boolean ifFound = false;
            for (Element element : elements) {
                if (!ifFound) {
                    if (element.className().equals("rowBg")) {
                        if (element.text().equals("Chinese renminbi")) {
                            ifFound = true;
                        }
                    }
                } else {
                    if (i < 3) {
                        float tempRate = Float.parseFloat(element.text());
                        if (i == 2) {
                            todayRate = tempRate;
                            break;
                        } else {
                            i++;
                        }
                    } else {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return todayRate;
    }





}
