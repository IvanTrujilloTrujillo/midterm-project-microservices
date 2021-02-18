package com.ironhack.accountservice.controller.dtos;

import com.ironhack.accountservice.classes.Money;
import com.ironhack.accountservice.enums.Status;
import com.ironhack.accountservice.model.Account;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class SavingDTO {

    private Money balance;
    private Long primaryOwner;
    private Long secondaryOwner;
    @NotEmpty
    private String secretKey;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="minimum_balance_currency")),
            @AttributeOverride(name="amount",column=@Column(name="minimum_balance_amount")),
    })
    private Money minimumBalance;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    @Digits(integer = 2, fraction = 4, message = "The interest rate must have four decimals")
    @DecimalMax(value = "0.5", message = "The interest rate must be smaller or equal than 0.5")
    @DecimalMin(value = "0.0", message = "The interest rate must be greater or equal than 0.0")
    private BigDecimal interestRate = new BigDecimal("0.0025");

    public SavingDTO() {
    }

    public SavingDTO(Money balance, Long primaryOwner, Long secondaryOwner, String secretKey, Money minimumBalance, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
