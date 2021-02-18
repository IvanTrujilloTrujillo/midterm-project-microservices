package com.ironhack.accountservice.controller.dtos;

import com.ironhack.accountservice.classes.Money;
import com.ironhack.accountservice.model.Account;

import javax.validation.constraints.NotEmpty;

public class CheckingDTO {
    //Properties
    private Money balance;
    private Long primaryOwner;
    private Long secondaryOwner;
    @NotEmpty
    private String secretKey;

    //Constructors
    public CheckingDTO() {
    }

    public CheckingDTO(Money balance, Long primaryOwner, Long secondaryOwner, String secretKey) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.secretKey = secretKey;
    }

    //Getters and setters


    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Long getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(Long primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public Long getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(Long secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
