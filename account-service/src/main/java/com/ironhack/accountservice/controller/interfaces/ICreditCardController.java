package com.ironhack.accountservice.controller.interfaces;

import com.ironhack.accountservice.controller.dtos.CreditCardDTO;
import com.ironhack.accountservice.model.CreditCard;

public interface ICreditCardController {
    CreditCardDTO createCreditCard(CreditCardDTO creditCard);
}
