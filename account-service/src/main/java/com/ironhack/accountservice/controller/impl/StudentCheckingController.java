package com.ironhack.accountservice.controller.impl;

import com.ironhack.accountservice.controller.dtos.CheckingDTO;
import com.ironhack.accountservice.controller.interfaces.IStudentCheckingController;
import com.ironhack.accountservice.model.StudentChecking;
import com.ironhack.accountservice.service.impl.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class StudentCheckingController implements IStudentCheckingController {

    @Autowired
    private StudentCheckingService studentCheckingService;

    //Request for admins. Create a student checking account
    @PostMapping("/admin/student-checking")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingDTO createStudentChecking(@RequestBody @Valid CheckingDTO checking) {
        return studentCheckingService.createStudentChecking(checking);
    }
}
