package com.ironhack.manageAccountservice.client;

import com.ironhack.manageAccountservice.classes.Money;
import com.ironhack.manageAccountservice.controller.dtos.BalanceDTO;
import com.ironhack.manageAccountservice.controller.dtos.CheckingDTO;
import com.ironhack.manageAccountservice.controller.dtos.CreditCardDTO;
import com.ironhack.manageAccountservice.controller.dtos.SavingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient("account-service")
public interface AccountClient {
    @GetMapping("/admin/account-balance/{id}")
    Money getAccountBalance(@PathVariable("id") Long id);

    @PatchMapping("/admin/account-balance/{id}")
    void setAccountBalance(@PathVariable("id") Long id, @RequestBody BalanceDTO balance);

    @GetMapping("/account/{id}")
    Money getBalanceForAccount(@PathVariable("id") Long id);

    @PostMapping("/admin/checking")
    CheckingDTO createChecking(@RequestBody CheckingDTO checking);

    @PostMapping("/admin/credit-card")
    CreditCardDTO createCreditCard(@RequestBody CreditCardDTO creditCard);

    @PostMapping("/admin/saving")
    SavingDTO createSaving(@RequestBody SavingDTO saving);

    @PostMapping("/admin/student-checking")
    CheckingDTO createStudentChecking(@RequestBody CheckingDTO checking);
}
