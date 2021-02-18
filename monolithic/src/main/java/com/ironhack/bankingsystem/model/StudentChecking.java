package com.ironhack.bankingsystem.model;

import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.enums.Status;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotEmpty;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class StudentChecking extends Account {
    //Properties
    @NotEmpty
    private String secretKey;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    //Constructors
    public StudentChecking() {
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, String secretKey) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
