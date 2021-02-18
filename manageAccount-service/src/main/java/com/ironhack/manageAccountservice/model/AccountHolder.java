package com.ironhack.manageAccountservice.model;

import com.ironhack.manageAccountservice.classes.Address;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

public class AccountHolder extends User{

    private LocalDateTime birthDate;
    private Address primaryAddress;
    private Address mailingAddress;

    //Constructors
    public AccountHolder() {
    }

    /**
     *  Class constructor specifying name, username, password, birth date, primary address and mailing address
     **/
    public AccountHolder(String name, String username, String password, LocalDateTime birthDate, Address primaryAddress, Address mailingAddress) {
        super(name, username, password);
        setBirthDate(birthDate);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(mailingAddress);
    }

    /**
     *  Class constructor specifying name, username, password, birth date and primary address
     **/
    public AccountHolder(String name, String username, String password, LocalDateTime birthDate, Address primaryAddress) {
        super(name, username, password);
        setBirthDate(birthDate);
        setPrimaryAddress(primaryAddress);
    }

    //Getters and setters
    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
