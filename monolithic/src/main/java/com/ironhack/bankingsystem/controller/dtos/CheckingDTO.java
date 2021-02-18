package com.ironhack.bankingsystem.controller.dtos;

import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.model.Account;
import com.ironhack.bankingsystem.model.AccountHolder;

import javax.validation.constraints.NotEmpty;

public class CheckingDTO extends Account {
    //Properties
    @NotEmpty
    private String secretKey;

    //Constructors
    public CheckingDTO() {
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner and secret key
     **/
    public CheckingDTO(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner and secret key
     **/
    public CheckingDTO(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
    }

    //Getters and setters
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
