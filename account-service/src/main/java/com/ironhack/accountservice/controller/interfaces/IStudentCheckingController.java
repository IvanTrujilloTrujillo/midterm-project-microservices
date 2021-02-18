package com.ironhack.accountservice.controller.interfaces;

import com.ironhack.accountservice.controller.dtos.CheckingDTO;
import com.ironhack.accountservice.model.StudentChecking;

public interface IStudentCheckingController {
    CheckingDTO createStudentChecking(CheckingDTO checking);
}
