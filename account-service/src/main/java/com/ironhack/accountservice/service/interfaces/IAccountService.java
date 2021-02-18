package com.ironhack.accountservice.service.interfaces;

import com.ironhack.accountservice.classes.Money;
import com.ironhack.accountservice.controller.dtos.BalanceDTO;

public interface IAccountService {
    Money getAccountBalance(Long id);
    void setAccountBalance(Long id, BalanceDTO balance);
    Money getBalanceForAccount(Long id);
    //void transferMoney(Transaction transaction, UserDetails userDetails);
    //void receiveMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey, UserDetails userDetails);
    //void sendMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey, UserDetails userDetails);
    void unfreezeAccount(Long id);
}
