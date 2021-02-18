package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.controller.dtos.BalanceDTO;
import com.ironhack.bankingsystem.controller.dtos.MoneyDTO;
import com.ironhack.bankingsystem.model.Transaction;

public interface IAccountController {
    Money getAccountBalance(Long id);
    void setAccountBalance(Long id, BalanceDTO balance);
    Money getBalanceForAccount(Long id);
    void transferMoney(Transaction transaction);
    void receiveMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey);
    void sendMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey);
    void unfreezeAccount(Long id);
}
