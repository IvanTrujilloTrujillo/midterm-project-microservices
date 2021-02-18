package com.ironhack.accountservice.service.interfaces;

import com.ironhack.accountservice.controller.dtos.CheckingDTO;

public interface ICheckingService {
    CheckingDTO createChecking(CheckingDTO checking);
}
