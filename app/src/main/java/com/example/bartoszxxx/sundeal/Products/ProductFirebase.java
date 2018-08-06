package com.example.bartoszxxx.sundeal.Products;

public class ProductFirebase extends ProductLocal {

    private String item_lowercase;

    public ProductFirebase() {

    }

    public ProductFirebase(String owner, String item, String item_lowercase, String description, String location, Boolean giveaway, String key, String photoUrl) {
        super(owner, item, description, location, giveaway, key, photoUrl);
        this.item_lowercase = item_lowercase;
    }

    public String getItem_lowercase() {
        return item_lowercase;
    }

}