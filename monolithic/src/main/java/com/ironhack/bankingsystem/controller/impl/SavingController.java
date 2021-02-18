package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.ISavingController;
import com.ironhack.bankingsystem.model.Saving;
import com.ironhack.bankingsystem.repository.SavingRepository;
import com.ironhack.bankingsystem.service.impl.SavingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SavingController implements ISavingController {

    @Autowired
    private SavingRepository savingRepository;

    @Autowired
    private SavingService savingService;

    //Request for admins. Create a saving account
    @PostMapping("/admin/saving")
    @ResponseStatus(HttpStatus.CREATED)
    public Saving createSaving(@RequestBody @Valid Saving saving) {
        return savingService.createSaving(saving);
    }
}
