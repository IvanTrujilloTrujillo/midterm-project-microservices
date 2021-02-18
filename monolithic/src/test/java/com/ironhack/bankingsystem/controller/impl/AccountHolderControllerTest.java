package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.bankingsystem.classes.Address;
import com.ironhack.bankingsystem.model.AccountHolder;
import com.ironhack.bankingsystem.repository.AccountHolderRepository;
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

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

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

        accountHolderRepository.save(accountHolder);
    }

    @AfterEach
    public void tearDown() {
        accountHolderRepository.deleteAll();
    }

    @Test
    public void createAccountHolder_ValidAccountHolder_AccountHolderSaved() throws Exception {
        AccountHolder newAccountHolder = new AccountHolder("Melissa Miérez", "melissam", "1234",
                LocalDateTime.of(1990, 3, 10, 0, 0),
                new Address("Calle Alfredo Krauss, 13, 3B", "43270", "Madrid", "Spain")
        );
        String body = objectMapper.writeValueAsString(newAccountHolder);

        MvcResult result = mockMvc.perform(post("/admin/account-holder").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("Melissa Miérez"));
    }

    @Test
    public void createAccountHolder_NotValidAccountHolder_BadRequest() throws Exception {
        AccountHolder newAccountHolder = new AccountHolder("Melissa Miérez", "melissam", "1234",
                LocalDateTime.of(2023, 3, 10, 0, 0),
                new Address("Calle Alfredo Krauss, 13, 3B", "43270", "Madrid", "Spain")
        );
        String body = objectMapper.writeValueAsString(newAccountHolder);

        MvcResult result1 = mockMvc.perform(post("/admin/account-holder").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andReturn();

        newAccountHolder = new AccountHolder("", "melissam", "1234",
                LocalDateTime.of(1990, 3, 10, 0, 0),
                new Address("Calle Alfredo Krauss, 13, 3B", "43270", "Madrid", "Spain")
        );
        body = objectMapper.writeValueAsString(newAccountHolder);

        MvcResult result2 = mockMvc.perform(post("/admin/account-holder").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}