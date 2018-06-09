package com.example.bartoszxxx.sundeal;

public class Product {

    private String item;
    private String description;
    private String location;

    public Product(){

    }

    public Product(String item, String description, String location) {
        this.item = item;
        this.description = description;
        this.location = location;
    }

    public String getItem() { return item; }

    public void setItem(String item) { this.item = item; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

}
