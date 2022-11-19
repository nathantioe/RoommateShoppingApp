package edu.uga.cs.roommateshoppingapp;

import java.util.Arrays;

public class RecentBuy {

    private String buyer;
    private ShoppingListItem[] itemsBought;

    public RecentBuy(String buyer, ShoppingListItem[] itemsbought) {
        this.buyer = buyer;
        this.itemsBought = itemsbought;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public ShoppingListItem[] getItemsBought() {
        return itemsBought;
    }

    public void setItemsBought(ShoppingListItem[] itemsBought) {
        this.itemsBought = itemsBought;
    }

    public String getTotPrice(){
        Double totPrice = 0.0;
        for (int i=0;i<itemsBought.length;i++){
            totPrice = totPrice + itemsBought[i].getPrice();
        }
        return String.valueOf(totPrice);
    }

    @Override
    public String toString() {
        return "Items bought by " + buyer + ": " + Arrays.toString(itemsBought)
                + "\nTotal price: " + getTotPrice() + " $";
    }
}

//"RecentBuy{" +
//        "buyer='" + buyer + '\'' +
//        ", ite  \n msBought= \n" + Arrays.toString(itemsBought) +
//        '}';
