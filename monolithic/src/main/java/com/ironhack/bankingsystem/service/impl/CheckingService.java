package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.controller.dtos.CheckingDTO;
import com.ironhack.bankingsystem.model.Checking;
import com.ironhack.bankingsystem.model.StudentChecking;
import com.ironhack.bankingsystem.repository.CheckingRepository;
import com.ironhack.bankingsystem.repository.StudentCheckingRepository;
import com.ironhack.bankingsystem.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class CheckingService implements ICheckingService {

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    //Service to create a checking or student checking account
    public void createChecking(CheckingDTO checkingDTO) {

        Checking checking = new Checking();
        checking.setBalance(checkingDTO.getBalance());
        checking.setPrimaryOwner(checkingDTO.getPrimaryOwner());
        if(checkingDTO.getSecondaryOwner() != null) {
            checking.setSecondaryOwner(checkingDTO.getSecondaryOwner());
        }
        checking.setSecretKey(checkingDTO.getSecretKey());

        //Check if the balance is smaller than
        if(checking.getBalance().getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The balance must be greater or equals than 0");
        } else {

            //Set the creation date and the penalty fee
            checking.setCreationDate();
            checking.setPenaltyFee();

            //If the primary owner is more than 24 years old, create a checking account
            if(ChronoUnit.YEARS.between(checking.getPrimaryOwner().getBirthDate(), LocalDateTime.now()) > 24) {

                //Set the minimum balance, the maintenance fee, the limit transaction and the last maintenance fee added date
                checking.setMinimumBalance(checking.getBalance().getCurrency());
                checking.setMonthlyMaintenanceFee(checking.getBalance().getCurrency());
                checking.setMaxLimitTransactions(new Money(BigDecimal.valueOf(0), checking.getBalance().getCurrency()));
                checking.setLastMaintenanceFeeAddedDate(LocalDateTime.now());
                checkingRepository.save(checking);

            //If the primary owner is less than 24 years old, create a student checking account
            } else {

                //We have to build the student checking from the checking properties
                StudentChecking studentChecking = new StudentChecking(checking.getBalance(),
                        checking.getPrimaryOwner(), checking.getSecondaryOwner(), checking.getSecretKey());
                studentChecking.setMaxLimitTransactions(new Money(BigDecimal.valueOf(0), checking.getBalance().getCurrency()));
                studentCheckingRepository.save(studentChecking);
            }

        }
    }
}
