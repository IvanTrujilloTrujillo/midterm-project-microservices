package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.model.CreditCard;
import com.ironhack.bankingsystem.repository.CreditCardRepository;
import com.ironhack.bankingsystem.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CreditCardService implements ICreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    //Service to create an credit card by an admin
    public CreditCard createCreditCard(CreditCard creditCard) {

        //If the balance is null, set it to 0 because it's a credit card
        if(creditCard.getBalance() == null) {
            creditCard.setBalance(new Money(BigDecimal.valueOf(0)));
        }

        //Check if the balance is zero
        if(creditCard.getBalance().getAmount().compareTo(BigDecimal.valueOf(0)) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The balance must be 0");
        } else {

            //Check if the credit limit is set
            if(creditCard.getCreditLimit() != null) {

                //Check if the credit limit is a valid value
                if(creditCard.getCreditLimit().getAmount().compareTo(BigDecimal.valueOf(100)) < 0 ||
                        creditCard.getCreditLimit().getAmount().compareTo(BigDecimal.valueOf(100000)) > 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The credit limit must be between 100 and 100,000");
                }

            //If it isn't set, set the default value
            } else {
                creditCard.setCreditLimit(new Money(BigDecimal.valueOf(100), creditCard.getBalance().getCurrency()));
            }

            //Check if the interest rate is set
            if(creditCard.getInterestRate() != null) {

                //Check if the interest rate is a valid value
                if(creditCard.getInterestRate().compareTo(BigDecimal.valueOf(0.1)) < 0 ||
                        creditCard.getInterestRate().compareTo(BigDecimal.valueOf(0.2)) > 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The interest rate must be between 0.1 and 0.2");
                }

            //If it isn't set, set the default value
            } else {
                creditCard.setInterestRate(BigDecimal.valueOf(0.2));
            }

            //Set the penalty fee, the creation date, the last interest added date and the limit transaction
            creditCard.setPenaltyFee();
            creditCard.setCreationDate();
            creditCard.setLastInterestAddedDate(LocalDateTime.now());
            creditCard.setMaxLimitTransactions(new Money(BigDecimal.valueOf(0), creditCard.getBalance().getCurrency()));
            return creditCardRepository.save(creditCard);
        }
    }
}
