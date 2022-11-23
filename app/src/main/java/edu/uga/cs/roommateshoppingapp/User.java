package edu.uga.cs.roommateshoppingapp;

public class User {

    private String key;
    private String email;

    public User() {
        this.email = "";
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
}
