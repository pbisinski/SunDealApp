package com.example.bartoszxxx.sundeal;

public class ProductFirebase extends Product{

    private String owner;
    //rozwiazanie problemu wyszukiwania
    private String item_lowercase;
    private Boolean oddam;
    private Boolean zamienie;

    public ProductFirebase() {

    }

    public ProductFirebase(String owner, String item, String item_lowercase, String description, String location, Boolean oddam, Boolean zamienie, String key) {
        super(item,description,location,key);
        this.item_lowercase=item_lowercase;
        this.owner = owner;
        this.oddam = oddam;
        this.zamienie = zamienie;
    }

    public String getItem_lowercase() { return item_lowercase; }

    public void setItem_lowercase(String item_lowercase) { this.item_lowercase = item_lowercase; }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }

    public Boolean getOddam() { return oddam; }

    public void setOddam(Boolean oddam) { this.oddam = oddam; }

    public Boolean getZamienie() { return zamienie; }

    public void setZamienie(Boolean zamienie) { this.zamienie = zamienie; }
}