package com.ironhack.bankingsystem.controller.dtos;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

//Class to validate balance on a patch request
public class BalanceDTO {
    //Properties
    @Size(max = 3)
    private String currency = "USD";
    @Digits(integer = 19, fraction = 2, message = "The amount must have two decimals")
    private BigDecimal amount;

    //Constructors
    public BalanceDTO() {
    }

    public BalanceDTO(String currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    //Getters and setters
    public BalanceDTO(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
