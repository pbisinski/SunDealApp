package com.example.bartoszxxx.sundeal;

public class User {

    private String owner;
    private String item;
    private String description;
    private String location;
    private Boolean oddam;
    private Boolean zamienie;

    public User() {

    }

    public User(String owner, String item, String description, String location, Boolean oddam, Boolean zamienie) {
        this.owner = owner;
        this.item = item;
        this.description = description;
        this.location = location;
        this.oddam = oddam;
        this.zamienie = zamienie;
    }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }

    public String getItem() { return item; }

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

    public Boolean getOddam() { return oddam; }

    public void setOddam(Boolean oddam) { this.oddam = oddam; }

    public Boolean getZamienie() { return zamienie; }

    public void setZamienie(Boolean zamienie) { this.zamienie = zamienie; }
}