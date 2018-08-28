package com.sundeal.sundeal;

public class Item {

    private String name;
    private String description;
    private String location;
    private String owner;
    private int category;
    private String photoUrl;
    private String nameLowerCase;
    private String key;

    public Item() {

    }

    public Item(String name, String description, String location, String owner, int category, String photoUrl, String nameLowerCase, String key) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.owner = owner;
        this.category = category;
        this.photoUrl = photoUrl;
        this.nameLowerCase = nameLowerCase;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getOwner() {
        return owner;
    }

    public int getCategory() {
        return category;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getNameLowerCase() {
        return nameLowerCase;
    }

    public String getKey() {
        return key;
    }

}