package com.example.bartoszxxx.sundeal.Products;

public class ProductLocal {

    private String title;
    private String description;
    private String location;
    private String key;
    private Boolean giveaway;
    private String owner;
    private String photoUrl;

    public ProductLocal() {

    }

    public ProductLocal(String owner, String title, String description, String location, Boolean giveaway, String key, String photoUrl) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.location = location;
        this.giveaway = giveaway;
        this.key = key;
        this.photoUrl = photoUrl;
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

    public String getPhotoUrl() { return photoUrl; }

}
