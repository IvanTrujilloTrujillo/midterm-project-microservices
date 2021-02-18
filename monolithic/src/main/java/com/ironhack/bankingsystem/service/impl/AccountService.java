package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.controller.dtos.BalanceDTO;
import com.ironhack.bankingsystem.controller.dtos.MoneyDTO;
import com.ironhack.bankingsystem.enums.Status;
import com.ironhack.bankingsystem.model.Account;
import com.ironhack.bankingsystem.model.Checking;
import com.ironhack.bankingsystem.model.StudentChecking;
import com.ironhack.bankingsystem.model.Transaction;
import com.ironhack.bankingsystem.repository.*;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private SavingRepository savingRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    //Service to get an account balance by an admin
    public Money getAccountBalance(Long id) {
        if(accountRepository.existsById(id)) {
            return accountRepository.findById(id).get().getBalance();

        //If the id doesn't match with any account, throws not found
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Account Id doesn't exist");
        }
    }

    //Service to set an account balance by an admin
    public void setAccountBalance(Long id, BalanceDTO balance) {
        //We have to ensure the account id exists
        if(accountRepository.existsById(id)) {
            Account account = accountRepository.findById(id).get();
            try {
                Money newBalance = new Money(balance.getAmount(), Currency.getInstance(balance.getCurrency()));
                account.setBalance(newBalance);
                accountRepository.save(account);

            //If the currency string isn't a valid format, throws bad request
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It isn't a supported ISO 4217 code");
            }

        //If the id doesn't match with any account, throws not found
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Account Id doesn't exist");
        }
    }

    //Service to get an account balance by an account holder
    public Money getBalanceForAccount(Long id, UserDetails userDetails) {
        //Check if the id exists
        if(accountRepository.existsById(id)) {

            //We need to check the status, but it's a property that only exists in checking, student checking and
            //saving accounts, so we must check first if the id correspond with any of this accounts
            Status status = Status.ACTIVE;
            if(checkingRepository.existsById(id)){
                status = checkingRepository.findById(id).get().getStatus();
            } else if (studentCheckingRepository.existsById(id)){
                status = studentCheckingRepository.findById(id).get().getStatus();
            } else if (savingRepository.existsById(id)) {
                status = savingRepository.findById(id).get().getStatus();
            }

            //Check if the username of the primary or secondary match with the username of the user details
            if(accountRepository.findById(id).get().getPrimaryOwner().getUsername().equals(userDetails.getUsername()) ||
                    (accountRepository.findById(id).get().getSecondaryOwner() != null &&
                     accountRepository.findById(id).get().getSecondaryOwner().getUsername().equals(userDetails.getUsername()))) {

                //Check if the status is active
                if(status == Status.ACTIVE) {

                    //Now, we must check if the conditions to add or subtract the maintenance fee
                    //and interest rate are met.

                    //First, the conditions to the checking account (if the account is this type)
                    if (checkingRepository.existsById(id)) {

                        //If it has passed more than one month since the last maintenance fee subtracted, we must
                        //subtract the fee
                        if (ChronoUnit.MONTHS.between(checkingRepository.findById(id).get().getLastMaintenanceFeeAddedDate(), LocalDateTime.now()) > 1) {

                            BigDecimal amount = checkingRepository.findById(id).get().getBalance().getAmount();
                            amount = amount.subtract(checkingRepository.findById(id).get().getMonthlyMaintenanceFee().getAmount());
                            checkingRepository.findById(id).get().setBalance(
                                    new Money(amount, checkingRepository.findById(id).get().getBalance().getCurrency()));

                            checkingRepository.findById(id).get().setLastMaintenanceFeeAddedDate(LocalDateTime.now());

                            checkingRepository.save(checkingRepository.findById(id).get());

                        }

                    //If it isn't a checking account, it could be a saving account
                    } else if (savingRepository.existsById(id)) {

                        //If it has passed more than one year since the last interest added, we must add the rate
                        if (ChronoUnit.YEARS.between(savingRepository.findById(id).get().getLastInterestAddedDate(), LocalDateTime.now()) > 1) {

                            BigDecimal amount = savingRepository.findById(id).get().getBalance().getAmount();
                            amount = amount.add(amount.multiply(savingRepository.findById(id).get().getInterestRate()));
                            savingRepository.findById(id).get().setBalance(
                                    new Money(amount, savingRepository.findById(id).get().getBalance().getCurrency()));

                            savingRepository.findById(id).get().setLastInterestAddedDate(LocalDateTime.now());

                            savingRepository.save(savingRepository.findById(id).get());

                        }

                    //If it isn't a checking or a saving account, it could be a credit card account
                    } else if (creditCardRepository.existsById(id)) {

                        //If it has passed more than one month since the last interest added, we must add a
                        //twelfth part of the rate
                        if (ChronoUnit.MONTHS.between(creditCardRepository.findById(id).get().getLastInterestAddedDate(), LocalDateTime.now()) > 1) {

                            BigDecimal amount = creditCardRepository.findById(id).get().getBalance().getAmount();
                            amount = amount.add(amount.multiply(
                                    creditCardRepository.findById(id).get().getInterestRate().divide(BigDecimal.valueOf(12))));
                            creditCardRepository.findById(id).get().setBalance(
                                    new Money(amount, creditCardRepository.findById(id).get().getBalance().getCurrency()));

                            creditCardRepository.findById(id).get().setLastInterestAddedDate(LocalDateTime.now());

                            creditCardRepository.save(creditCardRepository.findById(id).get());

                        }

                    }

                    //Finally, if it's a student checking account, we need to check if the primary owner is now more
                    //than 24 years old. In that case, we would convert the student checking account on a checking account
                    if (studentCheckingRepository.existsById(id)) {

                        if (ChronoUnit.YEARS.between(studentCheckingRepository.findById(id).get().getPrimaryOwner()
                                .getBirthDate(), LocalDateTime.now()) > 24) {

                            StudentChecking studentChecking = studentCheckingRepository.findById(id).get();
                            studentCheckingRepository.deleteById(id);
                            Checking checking = new Checking(studentChecking.getBalance(),
                                    studentChecking.getPrimaryOwner(), studentChecking.getSecretKey());
                            if(studentChecking.getSecondaryOwner() != null) {
                                checking.setSecondaryOwner(checking.getSecondaryOwner());
                            }

                            checking = checkingRepository.save(checking);
                            id = checking.getId();
                        }
                    }

                    //Once all the checks are done, return the balance
                    return accountRepository.findById(id).get().getBalance();
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is frozen. Contact with an admin");
                }
            }
            else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don´t have access to this account");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Account Id doesn't exist");
        }
    }

    //Service to transfer money by an account holder
    public void transferMoney(Transaction transaction, UserDetails userDetails) {
        //For simplicity, we define an account for the sender's account
        Account senderAccount;

        //Check if the if of the sender's account exists
        if (accountRepository.existsById(transaction.getSenderAccount().getId())) {
            senderAccount = accountRepository.findById(transaction.getSenderAccount().getId()).get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The sender account doesn't exist");
        }

        //For simplicity, we define a long to the receiver account id
        Long receiverAccountId = transaction.getReceiverAccountId();

        //Check if the username of the primary or secondary in the sender's account match with the username of the user details
        if(senderAccount.getPrimaryOwner().getUsername().equals(userDetails.getUsername()) ||
                (senderAccount.getSecondaryOwner() != null &&
                 senderAccount.getSecondaryOwner().getUsername().equals(userDetails.getUsername()))) {

            //We need to check the status, but it's a property that only exists in checking, student checking and
            //saving accounts, so we must check first if the id correspond with any of this accounts
            Status status = Status.ACTIVE;
            if(checkingRepository.existsById(senderAccount.getId())){
                status = checkingRepository.findById(senderAccount.getId()).get().getStatus();
            } else if (studentCheckingRepository.existsById(senderAccount.getId())){
                status = studentCheckingRepository.findById(senderAccount.getId()).get().getStatus();
            } else if (savingRepository.existsById(senderAccount.getId())) {
                status = savingRepository.findById(senderAccount.getId()).get().getStatus();
            }

            //Check if the status is active
            if(status == Status.ACTIVE) {

                //We must check if the last transaction have been made in less than one second, so, if there are
                //any past transaction, we save on 'lastTransaction' the date of last transaction
                LocalDateTime lastTransaction;
                if(transactionRepository.findLastTransactionBySenderAccount(senderAccount).isPresent()) {
                    lastTransaction = transactionRepository.findLastTransactionBySenderAccount(senderAccount).get();
                } else {
                    lastTransaction = LocalDateTime.MIN;
                }

                //Check if is has passed more than one second since last transaction
                if(ChronoUnit.SECONDS.between(lastTransaction, LocalDateTime.now()) > 0) {

                    //Take the transactions made on the last 24 hours
                    List<Transaction> lastTwentyFourHoursTransactions = transactionRepository.
                            findByTransactionDateBetweenAndSenderAccount(
                            LocalDateTime.now().minusHours(24), LocalDateTime.now(), senderAccount);
                    BigDecimal sumLastTransactions = BigDecimal.valueOf(0);

                    //If there are at least one, add the amount of each one
                    if(lastTwentyFourHoursTransactions.size() > 0) {
                        for (Transaction transactionFor : lastTwentyFourHoursTransactions) {
                            sumLastTransactions = sumLastTransactions.add(transactionFor.getAmount().getAmount());
                        }
                    }
                    //And add this one
                    sumLastTransactions = sumLastTransactions.add(transaction.getAmount().getAmount());

                    //If this sum is less than a 150% of the account's limit or the limit is 0, it can continue
                    if(sumLastTransactions.compareTo(
                            senderAccount.getMaxLimitTransactions().getAmount().multiply(
                                    BigDecimal.valueOf(1.5))) < 1 ||
                            senderAccount.getMaxLimitTransactions().getAmount().compareTo(
                                    BigDecimal.valueOf(0)) == 0) {

                        //If the limit is 0, it means there are very few transactions
                        if(senderAccount.getMaxLimitTransactions().getAmount().compareTo(
                                BigDecimal.valueOf(0)) == 0) {

                            //Take the transactions made before last 24 hours
                            List<Transaction> transactionsBeforeTwentyFourHour = transactionRepository.
                                    findByTransactionDateBetweenAndSenderAccount(
                                    LocalDateTime.MIN, LocalDateTime.now().minusHours(24), senderAccount);
                            //If there are some transactions in before this period, we want to sum the amount of these
                            //and set the limit with this sum
                            if(transactionsBeforeTwentyFourHour.size() > 0) {

                                BigDecimal sumTransactionsBeforeLastTwentyFourHours = BigDecimal.valueOf(0);
                                for (Transaction transactionFor : transactionsBeforeTwentyFourHour) {
                                    sumTransactionsBeforeLastTwentyFourHours = sumTransactionsBeforeLastTwentyFourHours
                                            .add(transactionFor.getAmount().getAmount());
                                }

                                //As we still don't have the limit on the account, we must compare if the transaction is legal
                                if(sumLastTransactions.compareTo(
                                        sumTransactionsBeforeLastTwentyFourHours.multiply(BigDecimal.valueOf(1.5))) > 0) {

                                    //If it's illegal, we must frozen the account
                                    if(checkingRepository.existsById(senderAccount.getId())){

                                        checkingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                                        checkingRepository.save(checkingRepository.findById(senderAccount.getId()).get());

                                    } else if (studentCheckingRepository.existsById(senderAccount.getId())){

                                        studentCheckingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                                        studentCheckingRepository.save(studentCheckingRepository.findById(senderAccount.getId()).get());

                                    } else if (savingRepository.existsById(senderAccount.getId())) {

                                        savingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                                        savingRepository.save(savingRepository.findById(senderAccount.getId()).get());
                                    }

                                    //And throw a forbidden response
                                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have tried to make a " +
                                            "transaction above your limit in the last 24 hours, your account would be " +
                                            "frozen. Contact with an admin");
                                }

                                //If it's legal, set the new limit
                                senderAccount.setMaxLimitTransactions(new Money(
                                        sumTransactionsBeforeLastTwentyFourHours, senderAccount.getBalance().getCurrency()));
                            }

                        //Here we know the sum is less than a 150%, but, if is greater than the limit, we must set
                        //the limit also
                        } else if(sumLastTransactions.compareTo(
                                senderAccount.getMaxLimitTransactions().getAmount()) > 0) {

                            senderAccount.setMaxLimitTransactions(new Money(
                                    sumLastTransactions, senderAccount.getBalance().getCurrency()));

                        }

                        //Check if the sender's account have enough balance
                        if (transaction.getAmount().getAmount().compareTo(
                                senderAccount.getBalance().getAmount()) < 0) {

                            //Check if the id of the receiver's account exists
                            if (accountRepository.existsById(receiverAccountId)) {

                                //Check if the name of the receiver match with the owners of the receiver's account
                                if (accountRepository.findById(receiverAccountId).get().getPrimaryOwner().getName().equals(
                                        transaction.getReceiverAccountHolderName()) ||
                                        (accountRepository.findById(receiverAccountId).get().getSecondaryOwner() != null &&
                                                accountRepository.findById(receiverAccountId).get().getSecondaryOwner().getName().equals(
                                                        transaction.getReceiverAccountHolderName()))) {

                                    //Check if the status of the receiver's account is active
                                    Status status2 = Status.ACTIVE;
                                    if(checkingRepository.existsById(receiverAccountId)){
                                        status2 = checkingRepository.findById(receiverAccountId).get().getStatus();
                                    } else if (studentCheckingRepository.existsById(receiverAccountId)){
                                        status2 = studentCheckingRepository.findById(receiverAccountId).get().getStatus();
                                    } else if (savingRepository.existsById(receiverAccountId)) {
                                        status2 = savingRepository.findById(receiverAccountId).get().getStatus();
                                    }

                                    if(status2 == Status.ACTIVE) {

                                        //If the sender's account is a credit card, we need to add the amount instead
                                        //of subtract
                                        if(creditCardRepository.existsById(senderAccount.getId())) {

                                            //Check if the currencies are the same
                                            if(transaction.getAmount().getCurrency().equals(creditCardRepository
                                                    .findById(senderAccount.getId()).get()
                                                    .getBalance().getCurrency())) {

                                                senderAccount.getBalance().increaseAmount(transaction.getAmount());

                                                //If they aren't the same, convert to the account currency
                                            } else {

                                                senderAccount.getBalance().increaseAmount(currencyConversion(
                                                        transaction.getAmount(), senderAccount.getBalance().getCurrency()));
                                            }
                                            accountRepository.save(senderAccount);
                                        } else {

                                            //Check if the currencies are the same
                                            if(transaction.getAmount().getCurrency().equals(accountRepository
                                                    .findById(senderAccount.getId()).get()
                                                    .getBalance().getCurrency())) {

                                                senderAccount.getBalance().decreaseAmount(transaction.getAmount());

                                            //If they aren't the same, convert to the account currency
                                            } else {

                                                senderAccount.getBalance().decreaseAmount(currencyConversion(
                                                        transaction.getAmount(), senderAccount.getBalance().getCurrency()));
                                            }
                                            accountRepository.save(senderAccount);
                                        }

                                        //If the receiver's account is a credit card, we need to subtract the amount instead
                                        //of add
                                        if(creditCardRepository.existsById(receiverAccountId)) {

                                            //Check if the currencies are the same
                                            if(transaction.getAmount().getCurrency().equals(creditCardRepository
                                                    .findById(receiverAccountId).get()
                                                    .getBalance().getCurrency())) {

                                                Account receiverAccount = accountRepository.findById(receiverAccountId).get();
                                                receiverAccount.getBalance().decreaseAmount(transaction.getAmount());
                                                accountRepository.save(receiverAccount);

                                            //If they aren't the same, convert to the account currency
                                            } else {

                                                Account receiverAccount = accountRepository.findById(receiverAccountId).get();
                                                receiverAccount.getBalance().decreaseAmount(currencyConversion(
                                                        transaction.getAmount(), receiverAccount.getBalance().getCurrency()));
                                                accountRepository.save(receiverAccount);
                                            }

                                        } else {

                                            //Check if the currencies are the same
                                            if(transaction.getAmount().getCurrency().equals(accountRepository
                                                    .findById(receiverAccountId).get()
                                                    .getBalance().getCurrency())) {

                                                Account receiverAccount = accountRepository.findById(receiverAccountId).get();
                                                receiverAccount.getBalance().increaseAmount(transaction.getAmount());
                                                accountRepository.save(receiverAccount);

                                            //If they aren't the same, convert to the account currency
                                            } else {

                                                Account receiverAccount = accountRepository.findById(receiverAccountId).get();
                                                receiverAccount.getBalance().increaseAmount(currencyConversion(
                                                        transaction.getAmount(), receiverAccount.getBalance().getCurrency()));
                                                accountRepository.save(receiverAccount);
                                            }
                                        }

                                        transaction.setTransactionDate(LocalDateTime.now());
                                        transactionRepository.save(transaction);

                                        //Now we must check if the sender's account is below the minimum balance in the
                                        //case the account is a checking or a saving. If it's a credit card, we must
                                        //check if it has passed the credit limit

                                        //If it's a checking account
                                        if(checkingRepository.existsById(senderAccount.getId())) {
                                            //If the balance is below the minimum, we must subtract the penalty fee
                                            if (checkingRepository.findById(senderAccount.getId()).get().getBalance().getAmount().compareTo(
                                                    checkingRepository.findById(senderAccount.getId()).get().getMinimumBalance().getAmount()) < 0) {

                                                checkingRepository.findById(senderAccount.getId()).get().getBalance().decreaseAmount(
                                                        checkingRepository.findById(senderAccount.getId()).get().getPenaltyFee());
                                                checkingRepository.save(checkingRepository.findById(senderAccount.getId()).get());
                                            }

                                        //If it's a saving account
                                        } else if(savingRepository.existsById(senderAccount.getId())) {
                                            //If the balance is below the minimum, we must subtract the penalty fee
                                            if (savingRepository.findById(senderAccount.getId()).get().getBalance().getAmount().compareTo(
                                                    savingRepository.findById(senderAccount.getId()).get().getMinimumBalance().getAmount()) < 0) {

                                                savingRepository.findById(senderAccount.getId()).get().getBalance().decreaseAmount(
                                                        savingRepository.findById(senderAccount.getId()).get().getPenaltyFee());
                                                savingRepository.save(savingRepository.findById(senderAccount.getId()).get());
                                            }

                                        //If the balance is above the limit credit, we must add the penalty fee
                                        } else if(creditCardRepository.existsById(senderAccount.getId())) {

                                            if (creditCardRepository.findById(senderAccount.getId()).get().getBalance().getAmount().compareTo(
                                                    creditCardRepository.findById(senderAccount.getId()).get().getCreditLimit().getAmount()) > 0) {

                                                //We need to ADD the penalty fee because the balance of the credit card is a credit
                                                creditCardRepository.findById(senderAccount.getId()).get().getBalance().increaseAmount(
                                                        creditCardRepository.findById(senderAccount.getId()).get().getPenaltyFee());
                                                creditCardRepository.save(creditCardRepository.findById(senderAccount.getId()).get());
                                            }
                                        }

                                    } else {
                                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The receiver account is frozen");
                                    }

                                } else {
                                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The receiver name doesn't match " +
                                            "with the owners of the Account Id");
                                }
                            } else {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Account Id of the receiver " +
                                        "doesn't exist");
                            }
                        } else {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You don´t have enough balance");
                        }

                    } else {
                        if(checkingRepository.existsById(senderAccount.getId())){
                            checkingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                            checkingRepository.save(checkingRepository.findById(senderAccount.getId()).get());
                        } else if (studentCheckingRepository.existsById(senderAccount.getId())){
                            studentCheckingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                            studentCheckingRepository.save(studentCheckingRepository.findById(senderAccount.getId()).get());
                        } else if (savingRepository.existsById(senderAccount.getId())) {
                            savingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                            savingRepository.save(savingRepository.findById(senderAccount.getId()).get());
                        }

                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have tried to make a transaction " +
                                "above your limit in the last 24 hours, your account would be frozen. Contact with an admin");
                    }
                } else {
                    if(checkingRepository.existsById(senderAccount.getId())){
                        checkingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                        checkingRepository.save(checkingRepository.findById(senderAccount.getId()).get());
                    } else if (studentCheckingRepository.existsById(senderAccount.getId())){
                        studentCheckingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                        studentCheckingRepository.save(studentCheckingRepository.findById(senderAccount.getId()).get());
                    } else if (savingRepository.existsById(senderAccount.getId())) {
                        savingRepository.findById(senderAccount.getId()).get().setStatus(Status.FROZEN);
                        savingRepository.save(savingRepository.findById(senderAccount.getId()).get());
                    }


                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have tried to make two transaction " +
                            "in 1 second, your account would be frozen. Contact with an admin");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is frozen. Contact with an admin");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don´t have access to the sender account");
        }
    }

    //Service to receive money by a third party
    public void receiveMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey, UserDetails userDetails) {
        //Check if the account's id exists
        if(accountRepository.existsById(id)) {

            //Check if the hashed key exists
            if(thirdPartyRepository.findByHashedKey(hashedKey).isPresent()) {

                //Check if the hashed key matches with the hashed key of the user logged
                if(userDetails.getUsername().equals(thirdPartyRepository.findByHashedKey(hashedKey).get().getUsername())) {

                    //Check if the status of the sender's account is active
                    Status status = Status.ACTIVE;
                    if(checkingRepository.existsById(id)){
                        status = checkingRepository.findById(id).get().getStatus();
                    } else if (studentCheckingRepository.existsById(id)){
                        status = studentCheckingRepository.findById(id).get().getStatus();
                    } else if (savingRepository.existsById(id)) {
                        status = savingRepository.findById(id).get().getStatus();
                    }

                    if(status == Status.ACTIVE) {

                        //Check if there are enough balance
                        if (accountRepository.findById(id).get().getBalance().getAmount().compareTo(amount.getAmount()) > 0) {

                            //Now, we must check the secret key, but it's a property of checking, student checking or
                            //saving accounts, so we need to check if the sender's account if one of this

                            //Checking account
                            if (checkingRepository.existsById(id)) {

                                //Check if the secret key matches
                                if (checkingRepository.findById(id).get().getSecretKey().equals(secretKey)) {

                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().decreaseAmount(amount.getAmount());
                                    accountRepository.save(account);

                                } else {
                                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The Secret Key doesn't match " +
                                            "with the Account");
                                }

                            //Student checking account
                            } else if (studentCheckingRepository.existsById(id)) {

                                //Check if the secret key matches
                                if (studentCheckingRepository.findById(id).get().getSecretKey().equals(secretKey)) {

                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().decreaseAmount(amount.getAmount());
                                    accountRepository.save(account);

                                } else {
                                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The Secret Key doesn't match " +
                                            "with the Account");
                                }

                            //Saving account
                            } else if (savingRepository.existsById(id)) {

                                //Check if the secret key matches
                                if (savingRepository.findById(id).get().getSecretKey().equals(secretKey)) {

                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().decreaseAmount(amount.getAmount());
                                    accountRepository.save(account);

                                    //Now we must check if the sender's account is below the minimum balance in the
                                    //case the account is a checking or a saving.

                                    //If it's a checking account
                                    if(checkingRepository.existsById(id)) {
                                        //If the balance is below the minimum, we must subtract the penalty fee
                                        if (checkingRepository.findById(id).get().getBalance().getAmount().compareTo(
                                                checkingRepository.findById(id).get().getMinimumBalance().getAmount()) < 0) {

                                            checkingRepository.findById(id).get().getBalance().decreaseAmount(
                                                    checkingRepository.findById(id).get().getPenaltyFee());
                                            checkingRepository.save(checkingRepository.findById(id).get());
                                        }
                                    //If it's a saving account
                                    } else if(savingRepository.existsById(id)) {
                                        //If the balance is below the minimum, we must subtract the penalty fee
                                        if (savingRepository.findById(id).get().getBalance().getAmount().compareTo(
                                                savingRepository.findById(id).get().getMinimumBalance().getAmount()) < 0) {

                                            savingRepository.findById(id).get().getBalance().decreaseAmount(
                                                    savingRepository.findById(id).get().getPenaltyFee());
                                            savingRepository.save(savingRepository.findById(id).get());
                                        }
                                    }

                                } else {
                                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The Secret Key doesn't match " +
                                            "with the Account");
                                }

                            //If it's a credit card, it's not possible to complete the transfer
                            } else {
                                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You can't receive from a Credit Card");
                            }
                        } else {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Not enough balance");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The sender's account is frozen");
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don´t have access to the Third Party account");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "It doesn't exist any Third Party with this hashed key");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Account Id of the sender doesn't exist");
        }
    }

    //Service to send money by a third party
    public void sendMoney(Long id, String secretKey, MoneyDTO amount, String hashedKey, UserDetails userDetails) {
        //Check if the account's id exists
        if(accountRepository.existsById(id)) {

            //Check if the hashed key exists
            if(thirdPartyRepository.findByHashedKey(hashedKey).isPresent()) {

                //Check if the hashed key matches with the hashed key of the user logged
                if(userDetails.getUsername().equals(thirdPartyRepository.findByHashedKey(hashedKey).get().getUsername())) {

                    //Check if the status of the sender's account is active
                    Status status = Status.ACTIVE;
                    if(checkingRepository.existsById(id)){
                        status = checkingRepository.findById(id).get().getStatus();
                    } else if (studentCheckingRepository.existsById(id)){
                        status = studentCheckingRepository.findById(id).get().getStatus();
                    } else if (savingRepository.existsById(id)) {
                        status = savingRepository.findById(id).get().getStatus();
                    }

                    if(status == Status.ACTIVE) {

                        //Now, we must check the secret key, but it's a property of checking, student checking or
                        //saving accounts, so we need to check if the sender's account if one of this

                        //Checking account
                        if (checkingRepository.existsById(id)) {

                            //Check if the secret key matches
                            if (checkingRepository.findById(id).get().getSecretKey().equals(secretKey)) {

                                //Check if the currencies are the same
                                if(amount.getCurrency().equals(checkingRepository.findById(id).get()
                                        .getBalance().getCurrency().getCurrencyCode())) {

                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().increaseAmount(amount.getAmount());
                                    accountRepository.save(account);

                                //If they aren't the same, convert to the account currency
                                } else {

                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().increaseAmount(currencyConversion(
                                            new Money(amount.getAmount(), Currency.getInstance(amount.getCurrency())),
                                            account.getBalance().getCurrency()));
                                    accountRepository.save(account);
                                }

                            } else {
                                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The Secret Key doesn't match " +
                                        "with the Account");
                            }

                        //Student checking account
                        } else if (studentCheckingRepository.existsById(id)) {

                            //Check if the secret key matches
                            if (studentCheckingRepository.findById(id).get().getSecretKey().equals(secretKey)) {

                                //Check if the currencies are the same
                                if(amount.getCurrency().equals(studentCheckingRepository.findById(id).get()
                                        .getBalance().getCurrency().getCurrencyCode())) {

                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().increaseAmount(amount.getAmount());
                                    accountRepository.save(account);

                                //If they aren't the same, convert to the account currency
                                } else {
                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().increaseAmount(currencyConversion(
                                            new Money(amount.getAmount(), Currency.getInstance(amount.getCurrency())),
                                            account.getBalance().getCurrency()));
                                    accountRepository.save(account);
                                }
                            } else {
                                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The Secret Key doesn't match " +
                                        "with the Account");
                            }

                        //Saving account
                        } else if (savingRepository.existsById(id)) {

                            //Check if the secret key matches
                            if (savingRepository.findById(id).get().getSecretKey().equals(secretKey)) {

                                //Check if the currencies are the same
                                if(amount.getCurrency().equals(savingRepository.findById(id).get()
                                        .getBalance().getCurrency().getCurrencyCode())) {

                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().increaseAmount(amount.getAmount());
                                    accountRepository.save(account);

                                //If they aren't the same, convert to the account currency
                                } else {
                                    Account account = accountRepository.findById(id).get();
                                    account.getBalance().increaseAmount(currencyConversion(
                                            new Money(amount.getAmount(), Currency.getInstance(amount.getCurrency())),
                                            account.getBalance().getCurrency()));
                                    accountRepository.save(account);
                                }
                            } else {
                                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The Secret Key doesn't match " +
                                        "with the Account");
                            }

                        //If it's a credit card, it's not possible to complete the transfer
                        } else {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You can't send to a Credit Card");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The receiver's account is frozen");
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don´t have access to the Third Party account");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "It doesn't exist any Third Party with this hashed key");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Account Id of the receiver doesn't exist");
        }
    }

    //Service to unfreeze an account by an admin
    public void unfreezeAccount(Long id) {
        //We have to ensure the account id exists
        if(accountRepository.existsById(id)) {

            Account account = accountRepository.findById(id).get();

            //Save the status on a variable
            Status status = Status.ACTIVE;
            if(checkingRepository.existsById(id)){
                status = checkingRepository.findById(id).get().getStatus();
            } else if (studentCheckingRepository.existsById(id)){
                status = studentCheckingRepository.findById(id).get().getStatus();
            } else if (savingRepository.existsById(id)) {
                status = savingRepository.findById(id).get().getStatus();
            }

            //Check if the status is frozen
            if(status == Status.FROZEN) {

                if(checkingRepository.existsById(id)){
                    checkingRepository.findById(id).get().setStatus(Status.ACTIVE);
                    checkingRepository.save(checkingRepository.findById(id).get());
                } else if (studentCheckingRepository.existsById(id)){
                    studentCheckingRepository.findById(id).get().setStatus(Status.ACTIVE);
                    studentCheckingRepository.save(studentCheckingRepository.findById(id).get());
                } else if (savingRepository.existsById(id)) {
                    savingRepository.findById(id).get().setStatus(Status.ACTIVE);
                    savingRepository.save(savingRepository.findById(id).get());
                }

            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "The account isn't frozen");
            }

        //If the id doesn't match with any account, throws not found
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Account Id doesn't exist");
        }
    }

    //A method to convert a Money to another currency
    public Money currencyConversion(Money initialAmount, Currency finalCurrency) {
        //Create a Monetary class from the initialAmount
        MonetaryAmount initialMoney = Monetary.getDefaultAmountFactory().setCurrency(initialAmount.getCurrency().getCurrencyCode())
                .setNumber(initialAmount.getAmount()).create();

        //Create a CurrencyConversion class from the finalCurrency
        CurrencyConversion currencyConversion = MonetaryConversions.getConversion(finalCurrency.getCurrencyCode());

        //Make the conversion
        MonetaryAmount convertedAmount = initialMoney.with(currencyConversion);

        //Return a Money class with the ner amount and currency
        return new Money(BigDecimal.valueOf(convertedAmount.getNumber().doubleValueExact()), finalCurrency);
    }
}
