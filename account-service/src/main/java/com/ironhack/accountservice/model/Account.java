package com.ironhack.accountservice.model;

import com.ironhack.accountservice.classes.Money;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {
    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="balance_currency")),
            @AttributeOverride(name="amount",column=@Column(name="balance_amount")),
    })
    @NotNull
    @Valid
    private Money balance;
    @NotNull
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="penalty_fee_currency")),
            @AttributeOverride(name="amount",column=@Column(name="penalty_fee_amount")),
    })
    private Money penaltyFee;
    private LocalDateTime creationDate;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="max_limit_transactions_currency")),
            @AttributeOverride(name="amount",column=@Column(name="max_limit_transactions_amount")),
    })
    private Money maxLimitTransactions;

    /*@OneToMany(mappedBy = "senderAccount", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactions;*/

    //Constructors
    public Account() {
    }

    /**
    *  Class constructor specifying balance, primary owner and secondary owner
    **/
    public Account(Money balance, Long primaryOwnerId, Long secondaryOwnerId) {
        setBalance(balance);
        setPrimaryOwnerId(primaryOwnerId);
        setSecondaryOwnerId(secondaryOwnerId);
        //Set creation date on current date
        setCreationDate();
        //Set penalty fee on 40 and the currency of the balance
        setPenaltyFee();
        //Set limit transactions on 0 and the currency of the balance
        setMaxLimitTransactions(new Money(BigDecimal.valueOf(0), balance.getCurrency()));
    }

    /**
     *  Class constructor specifying balance and primary owner
     **/
    public Account(Money balance, Long primaryOwnerId) {
        setBalance(balance);
        setPrimaryOwnerId(primaryOwnerId);
        //Set creation date on current date
        setCreationDate();
        //Set penalty fee on 40 and the currency of the balance
        setPenaltyFee();
        //Set limit transactions on 0 and the currency of the balance
        setMaxLimitTransactions(new Money(BigDecimal.valueOf(0), balance.getCurrency()));
    }

    //Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Long getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(Long primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Long getSecondaryOwnerId() {
        return secondaryOwnerId;
    }

    public void setSecondaryOwnerId(Long secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public Money getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee() {
        this.penaltyFee = new Money(BigDecimal.valueOf(40), this.balance.getCurrency());
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate() {
        this.creationDate = LocalDateTime.now();
    }

    public Money getMaxLimitTransactions() {
        return maxLimitTransactions;
    }

    public void setMaxLimitTransactions(Money maxLimitTransactions) {
        this.maxLimitTransactions = maxLimitTransactions;
    }

    /*public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }*/
}
