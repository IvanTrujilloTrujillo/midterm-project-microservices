package com.ironhack.userservice.controller.impl;

import com.ironhack.userservice.controller.interfaces.IAccountHolderController;
import com.ironhack.userservice.model.AccountHolder;
import com.ironhack.userservice.repository.AccountHolderRepository;
import com.ironhack.userservice.service.impl.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AccountHolderController implements IAccountHolderController {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountHolderService accountHolderService;

    //Request for admins. Create an account holder
    @PostMapping("/admin/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@RequestBody @Valid AccountHolder accountHolder) {
        return accountHolderService.createAccountHolder(accountHolder);
    }
}
