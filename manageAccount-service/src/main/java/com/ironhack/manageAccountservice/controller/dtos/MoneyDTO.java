package com.ironhack.manageAccountservice.controller.dtos;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

//Class to validate money on a patch request
//(similar to BalanceDTO but must be positive
public class MoneyDTO {
    //Properties
    private String currency = "USD";
    private BigDecimal amount;

    //Constructors
    public MoneyDTO() {
    }

    public MoneyDTO(String currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    //Getters and setters
    public MoneyDTO(BigDecimal amount) {
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
