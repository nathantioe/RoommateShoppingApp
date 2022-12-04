package edu.uga.cs.roommateshoppingapp;

/**
 * POJO class to represent an Item
 */
public class Item {

    private String key;
    private String itemName;
    private Double price;
    private String purchaser;

    public Item() {
        this.itemName = "";
        this.price = -1.0;
        this.purchaser = "";
    }

    public Item(String name){
        this.itemName = name;
        this.price = -1.0;
        this.purchaser = "";
    }

    public Item(String name, Double price) {
        this.itemName = name;
        this.price = price;
        this.purchaser = "";
    }

    public Item(String name, Double price, String purchaser) {
        this.itemName = name;
        this.price = price;
        this.purchaser = purchaser;
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

    public String getPurchaser() { return this.purchaser; }

    public void setPurchaser(String purchaser) { this.purchaser = purchaser; }

}