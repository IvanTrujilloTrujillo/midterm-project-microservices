package com.ironhack.accountservice.service.impl;

import com.ironhack.accountservice.classes.Money;
import com.ironhack.accountservice.controller.dtos.CheckingDTO;
import com.ironhack.accountservice.model.Checking;
import com.ironhack.accountservice.repository.CheckingRepository;
import com.ironhack.accountservice.repository.StudentCheckingRepository;
import com.ironhack.accountservice.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CheckingService implements ICheckingService {

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    //Service to create a checking or student checking account
    public CheckingDTO createChecking(CheckingDTO checkingDTO) {

        Checking checking = new Checking();
        checking.setBalance(checkingDTO.getBalance());
        checking.setPrimaryOwnerId(checkingDTO.getPrimaryOwner());
        if(checkingDTO.getSecondaryOwner() != null) {
            checking.setSecondaryOwnerId(checkingDTO.getSecondaryOwner());
        }
        checking.setSecretKey(checkingDTO.getSecretKey());

        //Check if the balance is smaller than
        if(checking.getBalance().getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The balance must be greater or equals than 0");
        } else {

            //Set the creation date and the penalty fee
            checking.setCreationDate();
            checking.setPenaltyFee();

            //Set the minimum balance, the maintenance fee, the limit transaction and the last maintenance fee added date
            checking.setMinimumBalance(checking.getBalance().getCurrency());
            checking.setMonthlyMaintenanceFee(checking.getBalance().getCurrency());
            checking.setMaxLimitTransactions(new Money(BigDecimal.valueOf(0), checking.getBalance().getCurrency()));
            checking.setLastMaintenanceFeeAddedDate(LocalDateTime.now());
            checkingRepository.save(checking);
            return checkingDTO;
        }
    }
}
