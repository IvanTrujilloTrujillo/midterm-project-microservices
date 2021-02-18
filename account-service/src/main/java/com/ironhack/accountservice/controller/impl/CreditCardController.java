package com.ironhack.accountservice.controller.impl;

import com.ironhack.accountservice.controller.dtos.CreditCardDTO;
import com.ironhack.accountservice.controller.interfaces.ICreditCardController;
import com.ironhack.accountservice.model.CreditCard;
import com.ironhack.accountservice.repository.CreditCardRepository;
import com.ironhack.accountservice.service.impl.CreditCardService;
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
    public CreditCardDTO createCreditCard(@RequestBody @Valid CreditCardDTO creditCard) {
        return creditCardService.createCreditCard(creditCard);
    }
}
