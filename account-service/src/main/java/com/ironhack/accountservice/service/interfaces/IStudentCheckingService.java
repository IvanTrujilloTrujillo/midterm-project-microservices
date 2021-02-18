package com.ironhack.accountservice.service.interfaces;

import com.ironhack.accountservice.controller.dtos.CheckingDTO;
import com.ironhack.accountservice.model.StudentChecking;

public interface IStudentCheckingService {
    CheckingDTO createStudentChecking(CheckingDTO checkingDTO);

}
