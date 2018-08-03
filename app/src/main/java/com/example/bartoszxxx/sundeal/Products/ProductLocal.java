package com.example.bartoszxxx.sundeal.Products;

public class ProductLocal {

    private String title;
    private String description;
    private String location;
    private String key;
    private Boolean giveaway;
    private String owner;

    public ProductLocal() {

    }

    public ProductLocal(String owner, String item, String description, String location, Boolean giveaway, String key) {
        this.title = item;
        this.description = description;
        this.location = location;
        this.key = key;
        this.owner = owner;
        this.giveaway = giveaway;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getKey() {
        return key;
    }

    public Boolean getGiveaway() {
        return giveaway;
    }

    public String getOwner() {
        return owner;
    }

}
