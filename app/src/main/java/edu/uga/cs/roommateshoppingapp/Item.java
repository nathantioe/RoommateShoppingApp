package edu.uga.cs.roommateshoppingapp;

import java.io.Serializable;

public class Item {

    private String key;
    private String itemName;
    private Double price;

    //private boolean active;

    public Item() {
        this.itemName = "";
        this.price = -1.0;
    }

    public Item(String name){
        this.itemName = name;
        this.price = -1.0;
    }

    public Item(String name, Double price) {
        this.itemName = name;
        this.price = price;
    }

    public String getKey() { return this.key; }

    public void setKey(String key) { this.key = key; }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public Double getPrice() { return this.price; }

    public void setPrice(Double price) { this.price = price; }

}

//public class Item implements Serializable {
//
//    private String key;
//    private String itemName;
//    private Double price = 0.0;
//    //private boolean active;
//
//    public Item(String name){
//        this.itemName = name;
//        //this.active = false;
//    }
//
//    public Item(String name, Double price){
//        this.itemName = name;
//        this.price = price;
//    }
//
//    public Item(String name, boolean Active){
//        this.itemName = name;
//        //this.active = Active;
//    }
//
//    public String getKey() { return this.key; }
//
//    public void setKey(String key) { this.key = key; }
//
//    public String getItemName(){
//        return itemName;
//    }
//
//    public void setItemName(String itemname){
//        this.itemName = itemname;
//    }
//
////    public boolean isActive(){
////        return active;
////    }
////
////    public void setActive(boolean Active){
////        this.active = Active;
////    }
//
//    public void setPrice(Double price){
//        this.price = price;
//    }
//
//    public Double getPrice(){
//        return price;
//    }
//
//
//
//    @Override
//    public String toString(){
//        return this.itemName;
//    }
//
//
//}
