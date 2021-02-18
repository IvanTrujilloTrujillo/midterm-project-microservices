package com.ironhack.bankingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.bankingsystem.classes.Money;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private Account senderAccount;
    @NotEmpty
    private String receiverAccountHolderName;
    @NotNull
    @Min(1)
    private Long receiverAccountId;
    @Embedded
    @NotNull
    private Money amount;
    private LocalDateTime transactionDate;

    //Constructors
    public Transaction() {
    }

    public Transaction(Account senderAccount, String receiverAccountHolderName, Long receiverAccountId, Money amount) {
        setSenderAccount(senderAccount);
        setReceiverAccountHolderName(receiverAccountHolderName);
        setReceiverAccountId(receiverAccountId);
        setAmount(amount);
        this.transactionDate = LocalDateTime.now();
    }

    //Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public String getReceiverAccountHolderName() {
        return receiverAccountHolderName;
    }

    public void setReceiverAccountHolderName(String receiverAccountHolderName) {
        this.receiverAccountHolderName = receiverAccountHolderName;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
