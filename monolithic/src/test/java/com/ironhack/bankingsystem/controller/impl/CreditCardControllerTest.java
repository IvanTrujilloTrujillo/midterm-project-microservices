package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.bankingsystem.classes.Address;
import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.model.AccountHolder;
import com.ironhack.bankingsystem.model.CreditCard;
import com.ironhack.bankingsystem.repository.AccountHolderRepository;
import com.ironhack.bankingsystem.repository.CreditCardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CreditCardControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private CreditCard creditCard;

    private AccountHolder accountHolder;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        accountHolder = new AccountHolder("Manuel Gómez", "manuelg", "1234",
                LocalDateTime.of(1995, 2, 5, 0, 0),
                new Address("Calle Benito Pérez, 10, 2A", "30254", "Madrid", "Spain")
        );

        creditCard = new CreditCard(new Money(BigDecimal.valueOf(1000)), accountHolder);

        accountHolderRepository.save(accountHolder);
        creditCardRepository.save(creditCard);
    }

    @AfterEach
    public void tearDown() {
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    public void createCreditCard_ValidCreditCard_CreditCardSaved() throws Exception {
        CreditCard newCreditCard = new CreditCard(new Money(BigDecimal.valueOf(2000)), accountHolder);
        String body = objectMapper.writeValueAsString(newCreditCard);

        MvcResult result = mockMvc.perform(post("/admin/credit-card").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("2000"));
    }

    @Test
    public void createCreditCard_NotValidCreditCard_NotAcceptable() throws Exception {
        CreditCard newCreditCard = new CreditCard(new Money(BigDecimal.valueOf(-50)), accountHolder);
        String body = objectMapper.writeValueAsString(newCreditCard);

        MvcResult result = mockMvc.perform(post("/admin/credit-card").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }
}