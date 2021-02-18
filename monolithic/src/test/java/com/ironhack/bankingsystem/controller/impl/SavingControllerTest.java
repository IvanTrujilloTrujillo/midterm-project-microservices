package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.bankingsystem.classes.Address;
import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.model.AccountHolder;
import com.ironhack.bankingsystem.model.Saving;
import com.ironhack.bankingsystem.repository.AccountHolderRepository;
import com.ironhack.bankingsystem.repository.SavingRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SavingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private SavingRepository savingRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Saving saving;

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

        saving = new Saving(new Money(BigDecimal.valueOf(1500)), accountHolder, "A1B2C3");

        accountHolderRepository.save(accountHolder);
        savingRepository.save(saving);
    }

    @AfterEach
    public void tearDown() {
        savingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    public void createSaving_ValidSaving_SavingSaved() throws Exception {
        Saving newSaving = new Saving(new Money(BigDecimal.valueOf(2000)), accountHolder, "Z9Y8X7");
        String body = objectMapper.writeValueAsString(newSaving);

        MvcResult result = mockMvc.perform(post("/admin/saving").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("Z9Y8X7"));
    }

    @Test
    public void createSaving_NotValidSaving_NotAcceptable() throws Exception {
        Saving newSaving = new Saving(new Money(BigDecimal.valueOf(-50)), accountHolder, "Z9Y8X7");
        String body = objectMapper.writeValueAsString(newSaving);

        MvcResult result1 = mockMvc.perform(post("/admin/saving").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        newSaving = new Saving(new Money(BigDecimal.valueOf(2000)), accountHolder, "");
        body = objectMapper.writeValueAsString(newSaving);

        MvcResult result2 = mockMvc.perform(post("/admin/saving").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}