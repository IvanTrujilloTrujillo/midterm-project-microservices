package com.ironhack.accountservice.controller.impl;

import com.ironhack.accountservice.classes.Money;
import com.ironhack.accountservice.controller.dtos.BalanceDTO;
import com.ironhack.accountservice.controller.dtos.MoneyDTO;
import com.ironhack.accountservice.controller.interfaces.IAccountController;
import com.ironhack.accountservice.repository.AccountRepository;
import com.ironhack.accountservice.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccountController implements IAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    //Request for admins. Return the balance of an account given the id
    @GetMapping("/admin/account-balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getAccountBalance(@PathVariable("id") Long id) {
        return accountService.getAccountBalance(id);
    }

    //Request for admins. Change the balance of an account given the id
    @PatchMapping("/admin/account-balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setAccountBalance(@PathVariable("id") Long id, @RequestBody @Valid BalanceDTO balance) {
        accountService.setAccountBalance(id, balance);
    }

    //Request for account holders. Return the balance of an account given the id
    @GetMapping("/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getBalanceForAccount(@PathVariable("id") Long id) {

        return accountService.getBalanceForAccount(id);
    }

    /*//Request for account holders. Transfer money from one of the sender's account to another account
    //The information needed is inside of the object Transaction
    @PatchMapping("/transfer-money")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@RequestBody @Valid Transaction transaction) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        accountService.transferMoney(transaction, userDetails);
    }

    //Request for third parties. Receive money from an account given the id, the name of one of the owners,
    //the secret key of the account and the hashed key of the third party
    @PatchMapping("/receive-money/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void receiveMoney(@PathVariable("id") Long id, @RequestParam("secret-key") String secretKey,
                          @RequestBody @Valid MoneyDTO amount, @RequestHeader("Hashed-Key") String hashedKey) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        accountService.receiveMoney(id, secretKey, amount, hashedKey, userDetails);
    }

    //Request for third parties. Send money to an account given the id, the name of one of the owners,
    //the secret key of the account and the hashed key of the third party
    @PatchMapping("/send-money/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMoney(@PathVariable("id") Long id, @RequestParam("secret-key") String secretKey,
                             @RequestBody @Valid MoneyDTO amount, @RequestHeader("Hashed-Key") String hashedKey) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        accountService.sendMoney(id, secretKey, amount, hashedKey, userDetails);
    }*/

    //Request for admins. If an account is frozen (checking, student checking or saving), the admin can unfreeze it
    @PatchMapping("/admin/unfreeze-account/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfreezeAccount(@PathVariable("id") Long id) {
        accountService.unfreezeAccount(id);
    }
}
