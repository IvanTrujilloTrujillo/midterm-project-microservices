package com.ironhack.bankingsystem.model;

import com.ironhack.bankingsystem.classes.Money;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends Account{
    //Properties
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="credit_limit_currency")),
            @AttributeOverride(name="amount",column=@Column(name="credit_limit_amount")),
    })
    private Money creditLimit;
    @Digits(integer = 2, fraction = 4, message = "The interest rate must have four decimals")
    @DecimalMax(value = "0.2000", message = "The interest rate must be smaller or equal than 0.2000")
    @DecimalMin(value = "0.1000", message = "The interest rate must be greater or equal than 0.1000")
    private BigDecimal interestRate = new BigDecimal("0.2000");
    private LocalDateTime lastInterestAddedDate;

    //Constructors
    public CreditCard() {
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner, credit limit and interest rate
     **/
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        //If the credit limit isn't a valid value, throws an exception and put the default value
        try {
            setCreditLimit(creditLimit);
        } catch (IllegalArgumentException e) {
            setCreditLimit(new Money(BigDecimal.valueOf(100), creditLimit.getCurrency()));
        }
        //If the interest rate isn't a valid value, throws an exception and put the default value
        try {
            setInterestRate(interestRate);
        } catch (IllegalArgumentException e) {
            setInterestRate(BigDecimal.valueOf(0.2));
        }
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner and credit limit
     **/
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit) {
        super(balance, primaryOwner, secondaryOwner);
        //If the credit limit isn't a valid value, throws an exception and put the default value
        try {
            setCreditLimit(creditLimit);
        } catch (IllegalArgumentException e) {
            setCreditLimit(new Money(BigDecimal.valueOf(100), creditLimit.getCurrency()));
        }
        //Set interest rate on default value
        setInterestRate(BigDecimal.valueOf(0.2));
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner and interest rate
     **/
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        //Set credit limit on default value
        setCreditLimit(new Money(BigDecimal.valueOf(100), balance.getCurrency()));
        //If the interest rate isn't a valid value, throws an exception and put the default value
        try {
            setInterestRate(interestRate);
        } catch (IllegalArgumentException e) {
            setInterestRate(BigDecimal.valueOf(0.2));
        }
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner and secondary owner
     **/
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        //Set credit limit on default value
        setCreditLimit(new Money(BigDecimal.valueOf(100), balance.getCurrency()));
        //Set interest rate on default value
        setInterestRate(BigDecimal.valueOf(0.2));
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, credit limit and interest rate
     **/
    public CreditCard(Money balance, AccountHolder primaryOwner, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner);
        //If the credit limit isn't a valid value, throws an exception and put the default value
        try {
            setCreditLimit(creditLimit);
        } catch (IllegalArgumentException e) {
            setCreditLimit(new Money(BigDecimal.valueOf(100), creditLimit.getCurrency()));
        }
        //If the interest rate isn't a valid value, throws an exception and put the default value
        try {
            setInterestRate(interestRate);
        } catch (IllegalArgumentException e) {
            setInterestRate(BigDecimal.valueOf(0.2));
        }
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner and credit limit
     **/
    public CreditCard(Money balance, AccountHolder primaryOwner, Money creditLimit) {
        super(balance, primaryOwner);
        //If the credit limit isn't a valid value, throws an exception and put the default value
        try {
            setCreditLimit(creditLimit);
        } catch (IllegalArgumentException e) {
            setCreditLimit(new Money(BigDecimal.valueOf(100), creditLimit.getCurrency()));
        }
        //Set interest rate on default value
        setInterestRate(BigDecimal.valueOf(0.2));
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner and interest rate
     **/
    public CreditCard(Money balance, AccountHolder primaryOwner, BigDecimal interestRate) {
        super(balance, primaryOwner);
        //Set credit limit on default value
        setCreditLimit(new Money(BigDecimal.valueOf(100), balance.getCurrency()));
        //If the interest rate isn't a valid value, throws an exception and put the default value
        try {
            setInterestRate(interestRate);
        } catch (IllegalArgumentException e) {
            setInterestRate(BigDecimal.valueOf(0.2));
        }
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance and primary owner
     **/
    public CreditCard(Money balance, AccountHolder primaryOwner) {
        super(balance, primaryOwner);
        //Set credit limit on default value
        setCreditLimit(new Money(BigDecimal.valueOf(100), balance.getCurrency()));
        //Set interest rate on default value
        setInterestRate(BigDecimal.valueOf(0.2));
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    //Getters and setters
    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        if(creditLimit.getAmount().compareTo(BigDecimal.valueOf(100)) < 0 ||
                creditLimit.getAmount().compareTo(BigDecimal.valueOf(100000)) > 0) {
            throw new IllegalArgumentException("The credit limit must be between 100 and 100,000");
        } else {
            this.creditLimit = creditLimit;
        }
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if(interestRate.compareTo(BigDecimal.valueOf(0.1)) < 0 ||
                interestRate.compareTo(BigDecimal.valueOf(0.2)) > 0) {
            throw new IllegalArgumentException("The interest rate must be between 0.1 and 0.2");
        } else {
            this.interestRate = interestRate;
        }

    }

    public LocalDateTime getLastInterestAddedDate() {
        return lastInterestAddedDate;
    }

    public void setLastInterestAddedDate(LocalDateTime lastInterestAddedDate) {
        this.lastInterestAddedDate = lastInterestAddedDate;
    }
}
