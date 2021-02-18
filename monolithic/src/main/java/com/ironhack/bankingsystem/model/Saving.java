package com.ironhack.bankingsystem.model;

import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Saving extends Account{
    //Properties
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
    private LocalDateTime lastInterestAddedDate;

    //Constructors
    public Saving() {
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner, secret key, minimum balance and interest rate
     **/
    public Saving(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, Money minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
        //If the minimum balance isn't a valid value, throws an exception and put the default value
        try {
            setMinimumBalance(minimumBalance);
        } catch (IllegalArgumentException e) {
            setMinimumBalance(new Money(BigDecimal.valueOf(1000), minimumBalance.getCurrency()));
        }
        //If the interest rate isn't a valid value, throws an exception and put the default value
        try {
            setInterestRate(interestRate);
        } catch (IllegalArgumentException e) {
            setInterestRate(BigDecimal.valueOf(0.0025));
        }
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner, secret key and minimum balance
     **/
    public Saving(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, Money minimumBalance) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
        //If the minimum balance isn't a valid value, throws an exception and put the default value
        try {
            setMinimumBalance(minimumBalance);
        } catch (IllegalArgumentException e) {
            setMinimumBalance(new Money(BigDecimal.valueOf(1000), minimumBalance.getCurrency()));
        }
        //Set interest rate on default value
        setInterestRate(BigDecimal.valueOf(0.0025));
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner, secret key and interest rate
     **/
    public Saving(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
        //Set minimum balance on default value
        setMinimumBalance(new Money(BigDecimal.valueOf(1000), balance.getCurrency()));
        //If the interest rate isn't a valid value, throws an exception and put the default value
        try {
            setInterestRate(interestRate);
        } catch (IllegalArgumentException e) {
            setInterestRate(BigDecimal.valueOf(0.0025));
        }
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, secondary owner and secret key
     **/
    public Saving(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
        //Set minimum balance on default value
        setMinimumBalance(new Money(BigDecimal.valueOf(1000), balance.getCurrency()));
        //Set interest rate on default value
        setInterestRate(BigDecimal.valueOf(0.0025));
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, secret key, minimum balance and interest rate
     **/
    public Saving(Money balance, AccountHolder primaryOwner, String secretKey, Money minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
        //If the minimum balance isn't a valid value, throws an exception and put the default value
        try {
            setMinimumBalance(minimumBalance);
        } catch (IllegalArgumentException e) {
            setMinimumBalance(new Money(BigDecimal.valueOf(1000), minimumBalance.getCurrency()));
        }
        //If the interest rate isn't a valid value, throws an exception and put the default value
        try {
            setInterestRate(interestRate);
        } catch (IllegalArgumentException e) {
            setInterestRate(BigDecimal.valueOf(0.0025));
        }
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, secret key and minimum balance
     **/
    public Saving(Money balance, AccountHolder primaryOwner, String secretKey, Money minimumBalance) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
        //If the minimum balance isn't a valid value, throws an exception and put the default value
        try {
            setMinimumBalance(minimumBalance);
        } catch (IllegalArgumentException e) {
            setMinimumBalance(new Money(BigDecimal.valueOf(1000), minimumBalance.getCurrency()));
        }
        //Set interest rate on default value
        setInterestRate(BigDecimal.valueOf(0.0025));
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner, secret key and interest rate
     **/
    public Saving(Money balance, AccountHolder primaryOwner, String secretKey, BigDecimal interestRate) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
        //Set minimum balance on default value
        setMinimumBalance(new Money(BigDecimal.valueOf(1000), balance.getCurrency()));
        //If the interest rate isn't a valid value, throws an exception and put the default value
        try {
            setInterestRate(interestRate);
        } catch (IllegalArgumentException e) {
            setInterestRate(BigDecimal.valueOf(0.0025));
        }
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    /**
     *  Class constructor specifying balance, primary owner and secret key
     **/
    public Saving(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
        //Set minimum balance on default value
        setMinimumBalance(new Money(BigDecimal.valueOf(1000), balance.getCurrency()));
        //Set interest rate on default value
        setInterestRate(BigDecimal.valueOf(0.0025));
        //Set the last interest added date on current date
        setLastInterestAddedDate(LocalDateTime.now());
    }

    //Getters and setters
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
        if(minimumBalance.getAmount().compareTo(BigDecimal.valueOf(100)) < 0 ||
                minimumBalance.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
            throw new IllegalArgumentException("The minimum balance must be between 100 and 1,000");
        } else {
            this.minimumBalance = minimumBalance;
        }
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
        if(interestRate.compareTo(BigDecimal.valueOf(0.500)) > 0) {
            throw new IllegalArgumentException("The interest rate must be smaller than 0.5");
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
