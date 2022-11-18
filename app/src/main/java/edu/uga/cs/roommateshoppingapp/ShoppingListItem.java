package edu.uga.cs.roommateshoppingapp;

import java.io.Serializable;

public class ShoppingListItem implements Serializable {

    private String itemName;
    private Double price = 0.0;
    private boolean active;

    public ShoppingListItem(String name){
        this.itemName = name;
        this.active = false;
    }

    public ShoppingListItem(String name, boolean Active){
        this.itemName = name;
        this.active = Active;
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemname){
        this.itemName = itemname;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean Active){
        this.active = Active;
    }

    public void setPrice(Double price){
        this.price = price;
    }



    @Override
    public String toString(){
        return this.itemName;
    }


}
