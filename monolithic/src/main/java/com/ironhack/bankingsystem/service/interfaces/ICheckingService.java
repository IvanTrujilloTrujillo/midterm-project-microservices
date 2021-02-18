package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.controller.dtos.CheckingDTO;

public interface ICheckingService {
    void createChecking(CheckingDTO checking);
}
