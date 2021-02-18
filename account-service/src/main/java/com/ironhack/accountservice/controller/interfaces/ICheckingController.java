package com.ironhack.accountservice.controller.interfaces;

import com.ironhack.accountservice.controller.dtos.CheckingDTO;
import com.ironhack.accountservice.model.Checking;

public interface ICheckingController {
    CheckingDTO createChecking(CheckingDTO checking);
}
