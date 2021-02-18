package com.ironhack.manageAccountservice.controller.impl;

import com.ironhack.manageAccountservice.client.AccountClient;
import com.ironhack.manageAccountservice.client.UserClient;
import com.ironhack.manageAccountservice.model.AccountHolder;
import com.ironhack.manageAccountservice.model.ThirdParty;
import com.ironhack.manageAccountservice.service.impl.ManageAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManageAdminController {

    @Autowired
    private ManageAdminService manageAdminService;



    @PostMapping("/admin/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody ThirdParty thirdParty) {

        return manageAdminService.createThirdParty(thirdParty);
    }

    @PostMapping("/admin/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@RequestBody AccountHolder accountHolder) {

        return manageAdminService.createAccountHolder(accountHolder);
    }
}
