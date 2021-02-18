package com.ironhack.accountservice.controller.impl;

import com.ironhack.accountservice.controller.dtos.SavingDTO;
import com.ironhack.accountservice.controller.interfaces.ISavingController;
import com.ironhack.accountservice.model.Saving;
import com.ironhack.accountservice.repository.SavingRepository;
import com.ironhack.accountservice.service.impl.SavingService;
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
    public SavingDTO createSaving(@RequestBody @Valid SavingDTO saving) {
        return savingService.createSaving(saving);
    }
}
