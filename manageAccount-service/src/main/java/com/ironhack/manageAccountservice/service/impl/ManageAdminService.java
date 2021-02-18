package com.ironhack.manageAccountservice.service.impl;

import com.ironhack.manageAccountservice.client.UserClient;
import com.ironhack.manageAccountservice.model.AccountHolder;
import com.ironhack.manageAccountservice.model.ThirdParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ManageAdminService {

    @Autowired
    private UserClient userClient;

    private CircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();

    public ThirdParty createThirdParty(ThirdParty thirdParty) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-service");

        ThirdParty thirdParty1 = circuitBreaker.run(() -> userClient.createThirdParty(thirdParty),
                throwable -> createThirdFallBack());

        if(thirdParty1 == null) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Third party not created");
        }

        return thirdParty1;
    }

    private ThirdParty createThirdFallBack() {
        return null;
    }

    public AccountHolder createAccountHolder(AccountHolder accountHolder) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-service");

        AccountHolder accountHolder1 =  circuitBreaker.run(() -> userClient.createAccountHolder(accountHolder),
                throwable -> createAccountHolderFallBack());

        if(accountHolder1 == null) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Account Holder not created");
        }

        return accountHolder1;
    }

    private AccountHolder createAccountHolderFallBack() {
        return null;
    }
}
