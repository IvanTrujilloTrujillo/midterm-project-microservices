package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.bankingsystem.model.ThirdParty;
import com.ironhack.bankingsystem.repository.ThirdPartyRepository;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThirdPartyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ThirdParty thirdParty;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        thirdParty = new ThirdParty("Manuel Gómez", "manuelg", "1234", "A1B2C3");

        thirdPartyRepository.save(thirdParty);
    }

    @AfterEach
    public void tearDown() {
        thirdPartyRepository.deleteAll();
    }

    @Test
    public void createThirdParty_ValidThirdParty_ThirdPartySaved() throws Exception {
        ThirdParty newThirdParty = new ThirdParty("Melissa Miérez", "melissam", "1234", "Z9Y8X7");
        String body = objectMapper.writeValueAsString(newThirdParty);

        MvcResult result = mockMvc.perform(post("/admin/third-party").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("Melissa Miérez"));
    }

    @Test
    public void createThirdParty_NotValidThirdParty_BadRequest() throws Exception {
        ThirdParty newThirdParty = new ThirdParty("", "melissam", "1234", "");
        String body = objectMapper.writeValueAsString(newThirdParty);

        MvcResult result = mockMvc.perform(post("/admin/third-party").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}