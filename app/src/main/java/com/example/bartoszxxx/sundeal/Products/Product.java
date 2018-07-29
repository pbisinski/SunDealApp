package com.example.bartoszxxx.sundeal.Products;

public class Product {

    private String item;
    private String description;
    private String location;
    private String key;

    public Product() {

    }

    public Product(String item, String description, String location, String key) {
        this.item = item;
        this.description = description;
        this.location = location;
        this.key = key;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
