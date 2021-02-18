package com.ironhack.accountservice.controller.impl;

import com.ironhack.accountservice.controller.dtos.CheckingDTO;
import com.ironhack.accountservice.controller.interfaces.ICheckingController;
import com.ironhack.accountservice.model.Checking;
import com.ironhack.accountservice.repository.CheckingRepository;
import com.ironhack.accountservice.service.impl.CheckingService;
import com.ironhack.accountservice.service.impl.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CheckingController implements ICheckingController {

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private CheckingService checkingService;

    //Request for admins. Create a checking account
    @PostMapping("/admin/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingDTO createChecking(@RequestBody @Valid CheckingDTO checking) {
        return checkingService.createChecking(checking);
    }


}
