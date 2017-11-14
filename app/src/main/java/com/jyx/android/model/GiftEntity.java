package com.jyx.android.model;

/**
 * Created by Administrator on 2/26/2016.
 */
public class GiftEntity {
    /**
     *             "imagejson": "",
     "price": "9900",
     "gifts_id": "1",
     "theme": "test"
     */
    String imagejson;
    long price;
    String gifts_id;
    String theme;
    String flowers_id;

    public String getFlowers_id() {
        return flowers_id;
    }

    public void setFlowers_id(String flowers_id) {
        this.flowers_id = flowers_id;
    }

    public String getImagejson() {
        return imagejson;
    }

    public void setImagejson(String imagejson) {
        this.imagejson = imagejson;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getGifts_id() {
        return gifts_id;
    }

    public void setGifts_id(String gifts_id) {
        this.gifts_id = gifts_id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
