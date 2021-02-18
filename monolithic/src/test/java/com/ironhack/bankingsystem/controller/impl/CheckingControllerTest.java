package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.bankingsystem.classes.Address;
import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.controller.dtos.CheckingDTO;
import com.ironhack.bankingsystem.model.AccountHolder;
import com.ironhack.bankingsystem.model.Checking;
import com.ironhack.bankingsystem.repository.AccountHolderRepository;
import com.ironhack.bankingsystem.repository.AccountRepository;
import com.ironhack.bankingsystem.repository.CheckingRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CheckingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private CheckingRepository checkingRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Checking checking;

    private AccountHolder accountHolder;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        accountHolder = new AccountHolder("Manuel Gómez", "manuelg", "1234",
                LocalDateTime.of(1991, 2, 5, 0, 0),
                new Address("Calle Benito Pérez, 10, 2A", "30254", "Madrid", "Spain")
        );

        checking = new Checking(new Money(BigDecimal.valueOf(1000)), accountHolder, "A1B2C3");

        accountHolderRepository.save(accountHolder);
        checkingRepository.save(checking);
    }

    @AfterEach
    public void tearDown() {
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    public void createChecking_ValidChecking_CheckingSaved() throws Exception {
        CheckingDTO newChecking = new CheckingDTO(new Money(BigDecimal.valueOf(2000)), accountHolder, "Z9Y8X7");
        String body = objectMapper.writeValueAsString(newChecking);

        /**
         * I have some problems with these test. The object mapper gives bad request because of 'JSON parse error:
         * Cannot deserialize instance of `java.util.Currency` out of START_OBJECT token'. But if I build the JSON
         * string manually, the test passes.
         *
         * In Credit card and saving test I haven't any problem with the object mapper.
         **/

        MvcResult result = mockMvc.perform(post("/admin/checking").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(2, checkingRepository.findAll().size());
    }

    @Test
    public void createChecking_NotValidChecking_NotAcceptable() throws Exception {
        CheckingDTO newChecking = new CheckingDTO(new Money(BigDecimal.valueOf(-50)), accountHolder, "Z9Y8X7");
        String body = objectMapper.writeValueAsString(newChecking);

        MvcResult result1 = mockMvc.perform(post("/admin/checking").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        newChecking = new CheckingDTO(new Money(BigDecimal.valueOf(5000)), accountHolder, "");
        body = objectMapper.writeValueAsString(newChecking);

        MvcResult result2 = mockMvc.perform(post("/admin/checking").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}