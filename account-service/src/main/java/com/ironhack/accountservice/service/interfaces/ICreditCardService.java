package com.ironhack.accountservice.service.interfaces;

import com.ironhack.accountservice.controller.dtos.CreditCardDTO;
import com.ironhack.accountservice.model.CreditCard;

public interface ICreditCardService {
    CreditCardDTO createCreditCard(CreditCardDTO creditCardDTO);
}
