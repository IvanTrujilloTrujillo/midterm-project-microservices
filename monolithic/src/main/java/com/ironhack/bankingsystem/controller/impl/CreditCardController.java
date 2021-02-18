package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.ICreditCardController;
import com.ironhack.bankingsystem.model.CreditCard;
import com.ironhack.bankingsystem.repository.CreditCardRepository;
import com.ironhack.bankingsystem.service.impl.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CreditCardController implements ICreditCardController {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardService creditCardService;

    //Request for admins. Create a credit card account
    @PostMapping("/admin/credit-card")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard createCreditCard(@RequestBody @Valid CreditCard creditCard) {
        return creditCardService.createCreditCard(creditCard);
    }
}
