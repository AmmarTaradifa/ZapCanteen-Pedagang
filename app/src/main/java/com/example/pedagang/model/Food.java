package com.example.pedagang.model;

import com.google.firebase.database.Exclude;

public class Food {
    private String id_makanan, name, price, seller, image, mkey;


    public Food(String id_makanan, String name, String price, String seller, String image){
        this.id_makanan = id_makanan;
        this.name = name;
        this.price = price;
        this.seller = seller;
        this.image = image;
    }
    public Food() {}

    public String getId_makanan() {
        return id_makanan;
    }

    public void setId_makanan(String id_makanan) {
        this.id_makanan = id_makanan;
    }

    public String getName() {
        return name;
    }
    public String setName(String name)
    {
        this.name = name;
        return name;
    }
    public String getimage() {
        return image;
    }
    public String setimage(String image)
    {
        this.image = image;
        return image;
    }
    public String getprice()
    {
        return price;
    }
    public String setprice(String price) {
        this.price = price;
        return price;
    }

    public String getSeller()
    {
        return seller;
    }

    public String setSeller(String seller)
    {
        this.seller = seller;
        return seller;
    }
//
    @Exclude
    public String getkey() {
       return mkey;
    }
//
    @Exclude
    public void setkey(String mkey) {
        this.mkey = mkey;
    }

}

