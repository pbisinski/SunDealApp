package com.example.bartoszxxx.sundeal.Products;

public class ListProduct extends Product {

    private Boolean oddam;
    private Boolean zamienie;
    private String owner;

    public ListProduct() {

    }

    public ListProduct(String owner, String item, String description, String location, Boolean oddam, Boolean zamienie, String key) {
        super(item, description, location, key);
        this.owner = owner;
        this.oddam = oddam;
        this.zamienie = zamienie;
    }

    public Boolean getOddam() {
        return oddam;
    }

    public void setOddam(Boolean oddam) {
        this.oddam = oddam;
    }

    public Boolean getZamienie() {
        return zamienie;
    }

    public void setZamienie(Boolean zamienie) {
        this.zamienie = zamienie;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
