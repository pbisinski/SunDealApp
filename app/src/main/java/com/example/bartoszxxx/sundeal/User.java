package com.example.bartoszxxx.sundeal;

public class User {

    private String item;
    private String description;
    private String location;
    private String oddam;

    public User() {

    }

    public User(String item, String description, String location, String oddam) {
        this.item = item;
        this.description = description;
        this.location = location;
        this.oddam = oddam;
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
}