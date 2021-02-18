package com.ironhack.accountservice.controller.dtos;

import com.ironhack.accountservice.classes.Money;
import com.ironhack.accountservice.model.Account;

import java.math.BigDecimal;

public class CreditCardDTO {

    private Money balance;
    private Long primaryOwner;
    private Long secondaryOwner;
    private Money creditLimit;
    private BigDecimal interestRate = new BigDecimal("0.2000");

    public CreditCardDTO() {
    }

    public CreditCardDTO(Money balance, Long primaryOwner, Long secondaryOwner, Money creditLimit, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

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

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
