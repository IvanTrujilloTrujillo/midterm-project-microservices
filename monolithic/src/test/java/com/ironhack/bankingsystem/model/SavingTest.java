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
class SavingTest {

    private AccountHolder accountHolder;

    private Saving saving;

    @BeforeEach
    public void setUp() {
        accountHolder = new AccountHolder("Manuel Gómez", "manuelg", "1234",
                LocalDateTime.of(1995, 2, 5, 0, 0),
                new Address("Calle Benito Pérez, 10, 2A", "30254", "Madrid", "Spain")
        );

        saving = new Saving(new Money(BigDecimal.valueOf(1500)), accountHolder, "A1B2C3");
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void setMinimumBalance_MinimumBalanceBetween100And1000_MinimumBalanceSet() {
        saving.setMinimumBalance(new Money(BigDecimal.valueOf(800), saving.getBalance().getCurrency()));

        assertEquals(BigDecimal.valueOf(800).setScale(2), saving.getMinimumBalance().getAmount());
    }

    @Test
    public void setMinimumBalance_MinimumBalanceNotBetween100And1000_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> saving.setMinimumBalance(new Money(BigDecimal.valueOf(2000), saving.getBalance().getCurrency())));
    }
}