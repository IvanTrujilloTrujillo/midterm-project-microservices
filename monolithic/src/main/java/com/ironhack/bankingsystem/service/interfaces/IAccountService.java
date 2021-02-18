package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.controller.dtos.BalanceDTO;
import com.ironhack.bankingsystem.controller.dtos.MoneyDTO;
import com.ironhack.bankingsystem.model.Transaction;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAccountService {
    Money getAccountBalance(Long id);
    void setAccountBalance(Long id, BalanceDTO balance);
    Money getBalanceForAccount(Long id, UserDetails userDetails);
    void transferMoney(Transaction transaction, UserDetails userDetails);
    void receiveMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey, UserDetails userDetails);
    void sendMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey, UserDetails userDetails);
    void unfreezeAccount(Long id);
}
