package com.example.bartoszxxx.sundeal;

public class Product {

    private String title;
    private String titleLowerCase;
    private String description;
    private String location;
    private String key;
    private Boolean giveaway;
    private String owner;
    private String photoUrl;

    public Product() {

    }

    public Product(String owner, String title, String titleLowerCase, String description, String location, Boolean giveaway, String photoUrl) {
        this.owner = owner;
        this.title = title;
        this.titleLowerCase = titleLowerCase;
        this.description = description;
        this.location = location;
        this.giveaway = giveaway;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleLowerCase() {
        return titleLowerCase;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
