package edu.uga.cs.roommateshoppingapp;

/**
 * POJO class to represent a user
 */
public class User {

    private String key;
    private String email;
    private double amountOwed;

    public User() {
        this.email = "";
        this.amountOwed = 0.0;
    }

    public User(String email) {
        this.email = email;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAmountOwed() { return this.amountOwed; }

    public void setAmountOwed(double amountOwed) { this.amountOwed = amountOwed; }
}
