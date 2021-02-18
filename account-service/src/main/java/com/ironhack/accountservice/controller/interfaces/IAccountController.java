package com.ironhack.accountservice.controller.interfaces;

import com.ironhack.accountservice.classes.Money;
import com.ironhack.accountservice.controller.dtos.BalanceDTO;
import com.ironhack.accountservice.controller.dtos.MoneyDTO;

public interface IAccountController {
    Money getAccountBalance(Long id);
    void setAccountBalance(Long id, BalanceDTO balance);
    Money getBalanceForAccount(Long id);
    //void transferMoney(Transaction transaction);
    //void receiveMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey);
    //void sendMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey);
    void unfreezeAccount(Long id);
}
