package com.ironhack.manageAccountservice.controller.dtos;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

//Class to validate balance on a patch request
public class BalanceDTO {
    //Properties
    private String currency = "USD";
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
