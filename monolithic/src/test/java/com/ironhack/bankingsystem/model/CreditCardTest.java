package com.ironhack.bankingsystem.model;

import com.ironhack.bankingsystem.classes.Address;
import com.ironhack.bankingsystem.classes.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditCardTest {

    private AccountHolder accountHolder;

    private CreditCard creditCard;

    @BeforeEach
    public void setUp() {
        accountHolder = new AccountHolder("Manuel Gómez", "manuelg", "1234",
                LocalDateTime.of(1995, 2, 5, 0, 0),
                new Address("Calle Benito Pérez, 10, 2A", "30254", "Madrid", "Spain")
        );

        creditCard = new CreditCard(new Money(BigDecimal.valueOf(1000)), accountHolder);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void setCreditLimit_CreditLimitBetween100And100000_CreditLimitSet() {
        creditCard.setCreditLimit(new Money(BigDecimal.valueOf(8000), creditCard.getBalance().getCurrency()));

        assertEquals(BigDecimal.valueOf(8000).setScale(2), creditCard.getCreditLimit().getAmount());
    }

    @Test
    public void setCreditLimit_CreditLimitNotBetween100And100000_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> creditCard.setCreditLimit(new Money(BigDecimal.valueOf(200000), creditCard.getBalance().getCurrency())));
    }
}