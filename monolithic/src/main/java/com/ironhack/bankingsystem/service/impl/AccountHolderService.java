package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.AccountHolder;
import com.ironhack.bankingsystem.model.Role;
import com.ironhack.bankingsystem.repository.AccountHolderRepository;
import com.ironhack.bankingsystem.repository.RoleRepository;
import com.ironhack.bankingsystem.repository.UserRepository;
import com.ironhack.bankingsystem.service.interfaces.IAccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountHolderService implements IAccountHolderService {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //Service to create an account holder by an admin
    public AccountHolder createAccountHolder(AccountHolder accountHolder) {
        //If the username already exists, throws conflict
        if(userRepository.findByUsername(accountHolder.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The username already exists");
        } else {
            //Encrypt password
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword()));

            //Set role and save the account holder
            accountHolderRepository.save(accountHolder);
            roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder));
            return accountHolder;
        }
    }
}
