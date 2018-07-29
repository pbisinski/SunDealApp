package com.example.bartoszxxx.sundeal.Products;

public class ProductFirebase extends ListProduct {

    private String item_lowercase;

    public ProductFirebase() {

    }

    public ProductFirebase(String owner, String item, String item_lowercase, String description, String location, Boolean oddam, Boolean zamienie, String key) {
        super(owner, item, description, location, oddam, zamienie, key);
        this.item_lowercase = item_lowercase;
    }

    public String getItem_lowercase() {
        return item_lowercase;
    }

    public void setItem_lowercase(String item_lowercase) {
        this.item_lowercase = item_lowercase;
    }

}