package com.ironhack.manageAccountservice.model;

public class ThirdParty extends User{
    //Properties
    private String hashedKey;

    //Constructors
    public ThirdParty() {
    }

    public ThirdParty(String name, String username, String password, String hashedKey) {
        super(name, username, password);
        setHashedKey(hashedKey);
    }

    //Getters and setters
    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
