package com.ironhack.accountservice.service.impl;

import com.ironhack.accountservice.classes.Money;
import com.ironhack.accountservice.controller.dtos.CheckingDTO;
import com.ironhack.accountservice.model.StudentChecking;
import com.ironhack.accountservice.repository.StudentCheckingRepository;
import com.ironhack.accountservice.service.interfaces.IStudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class StudentCheckingService implements IStudentCheckingService {

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    public CheckingDTO createStudentChecking(CheckingDTO checkingDTO) {

        StudentChecking studentChecking = new StudentChecking();
        studentChecking.setBalance(checkingDTO.getBalance());
        studentChecking.setPrimaryOwnerId(checkingDTO.getPrimaryOwner());
        if(checkingDTO.getSecondaryOwner() != null) {
            studentChecking.setSecondaryOwnerId(checkingDTO.getSecondaryOwner());
        }
        studentChecking.setSecretKey(checkingDTO.getSecretKey());

        //Check if the balance is smaller than
        if(studentChecking.getBalance().getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The balance must be greater or equals than 0");
        } else {

            //Set the creation date and the penalty fee
            studentChecking.setCreationDate();
            studentChecking.setPenaltyFee();

            studentChecking.setMaxLimitTransactions(new Money(BigDecimal.valueOf(0), studentChecking.getBalance().getCurrency()));
            }
        studentCheckingRepository.save(studentChecking);
        return checkingDTO;

    }
}
