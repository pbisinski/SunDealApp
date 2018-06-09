package com.example.bartoszxxx.sundeal;

public class User extends Product{

    private String owner;
    private Boolean oddam;
    private Boolean zamienie;

    public User() {

    }

    public User(String owner, String item, String description, String location, Boolean oddam, Boolean zamienie) {
        super(item,description,location);
        this.owner = owner;
        this.oddam = oddam;
        this.zamienie = zamienie;
    }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }

    public Boolean getOddam() { return oddam; }

    public void setOddam(Boolean oddam) { this.oddam = oddam; }

    public Boolean getZamienie() { return zamienie; }

    public void setZamienie(Boolean zamienie) { this.zamienie = zamienie; }
}