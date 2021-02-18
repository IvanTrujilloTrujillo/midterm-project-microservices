package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.bankingsystem.classes.Address;
import com.ironhack.bankingsystem.classes.Money;
import com.ironhack.bankingsystem.controller.dtos.BalanceDTO;
import com.ironhack.bankingsystem.controller.dtos.MoneyDTO;
import com.ironhack.bankingsystem.enums.Status;
import com.ironhack.bankingsystem.model.*;
import com.ironhack.bankingsystem.repository.*;
import com.ironhack.bankingsystem.security.CustomUserDetails;
import com.ironhack.bankingsystem.service.impl.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@SpringBootTest
class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

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

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Checking checking;
    private Saving saving;
    private Saving saving2;
    private CreditCard creditCard;

    private User admin;
    private CustomUserDetails customUserDetailsAdmin, customUserDetails, customUserDetails2,
            customUserDetails3, customUserDetails4, customUserDetailsThirdParty;

    private AccountHolder accountHolder, accountHolder2, accountHolder3, accountHolder4;

    private ThirdParty thirdParty;

    private Role adminRole, role, role2, role3, role4, thirdPartyRole;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        admin = new User("Iván Trujillo", "ivantllo", "1111");
        userRepository.save(admin);

        adminRole = new Role("ADMIN", admin);
        roleRepository.save(adminRole);

        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        customUserDetailsAdmin = new CustomUserDetails(admin);

        accountHolder = new AccountHolder("Manuel Gómez", "manuelg", "1234",
                LocalDateTime.of(1990, 2, 5, 0, 0),
                new Address("Calle Benito Pérez, 10, 2A", "30254", "Madrid", "Spain")
        );
        accountHolder2 = new AccountHolder("Ana Pérez", "anap", "9876",
                LocalDateTime.of(1980, 2, 5, 0, 0),
                new Address("Calle Benito Pérez, 10, 2A", "30254", "Madrid", "Spain")
        );
        accountHolder3 = new AccountHolder("Fernando Gómez", "fernandog", "5555",
                LocalDateTime.of(1992, 2, 5, 0, 0),
                new Address("Calle Benito Pérez, 10, 2A", "30254", "Madrid", "Spain")
        );
        accountHolder4 = new AccountHolder("Raquel Pérez", "raquelp", "5555",
                LocalDateTime.of(1992, 2, 5, 0, 0),
                new Address("Calle Benito Pérez, 10, 2A", "30254", "Madrid", "Spain")
        );

        accountHolderRepository.saveAll(List.of(accountHolder, accountHolder2, accountHolder3, accountHolder4));

        thirdParty = new ThirdParty("Pablo Méndez", "pablom", "2222", "TGHY8547");

        thirdPartyRepository.save(thirdParty);

        role = new Role("ACCOUNT_HOLDER", accountHolder);
        role2 = new Role("ACCOUNT_HOLDER", accountHolder2);
        role3 = new Role("ACCOUNT_HOLDER", accountHolder3);
        role4 = new Role("ACCOUNT_HOLDER", accountHolder4);
        thirdPartyRole = new Role("THIRD_PARTY", thirdParty);

        roleRepository.saveAll(List.of(role, role2, role3, role4, thirdPartyRole));

        accountHolder.setRoles(Set.of(role));
        accountHolder2.setRoles(Set.of(role2));
        accountHolder3.setRoles(Set.of(role3));
        accountHolder4.setRoles(Set.of(role4));
        thirdParty.setRoles(Set.of(thirdPartyRole));

        accountHolderRepository.saveAll(List.of(accountHolder, accountHolder2, accountHolder3, accountHolder4));

        customUserDetails = new CustomUserDetails((User) accountHolder);
        customUserDetails2 = new CustomUserDetails((User) accountHolder2);
        customUserDetails3 = new CustomUserDetails((User) accountHolder3);
        customUserDetails4 = new CustomUserDetails((User) accountHolder4);
        customUserDetailsThirdParty = new CustomUserDetails((User) thirdParty);

        checking = new Checking(new Money(BigDecimal.valueOf(500)), accountHolder, accountHolder3, "A1B2C3");
        //checking.setMaxLimitTransactions(new Money(BigDecimal.valueOf(0)));
        saving = new Saving(new Money(BigDecimal.valueOf(1500)), accountHolder, accountHolder3, "A1B2C3");
        saving2 = new Saving(new Money(BigDecimal.valueOf(3000)), accountHolder2, accountHolder4, "A1B2C3");
        creditCard = new CreditCard(new Money(BigDecimal.valueOf(1000)), accountHolder, accountHolder3);
        saving = new Saving(new Money(BigDecimal.valueOf(1500)), accountHolder, accountHolder3, "A1B2C3");

        checkingRepository.save(checking);
        savingRepository.saveAll(List.of(saving, saving2));
        creditCardRepository.save(creditCard);
    }

    @AfterEach
    public void tearDown() {
        roleRepository.deleteAll();
        creditCardRepository.deleteAll();
        savingRepository.deleteAll();
        checkingRepository.deleteAll();
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        transactionRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getAccountBalance_ExistingId_Balance() throws Exception {
        List<Checking> checkings = checkingRepository.findAll();
        Long id1 = checkings.get(0).getId();
        List<Saving> savings = savingRepository.findAll();
        Long id2 = savings.get(0).getId();
        List<CreditCard> creditCards = creditCardRepository.findAll();
        Long id3 = creditCards.get(0).getId();

        MvcResult result = mockMvc.perform(get("/admin/account-balance/" + id1)
                .characterEncoding("UTF-8").with(user((UserDetails) customUserDetailsAdmin)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("500"));

        result = mockMvc.perform(get("/admin/account-balance/" + id2)
                .characterEncoding("UTF-8").with(user((UserDetails) customUserDetailsAdmin)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("1500"));

        result = mockMvc.perform(get("/admin/account-balance/" + id3)
                .characterEncoding("UTF-8").with(user((UserDetails) customUserDetailsAdmin)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("1000"));
    }

    @Test
    public void getAccountBalance_NotExistingId_Balance() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/account-balance/100000")
                .characterEncoding("UTF-8").with(user((UserDetails) customUserDetailsAdmin)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getAccountBalance_ExistingIdButNotAdmin_Forbidden() throws Exception {
        List<Checking> checkings = checkingRepository.findAll();
        Long id1 = checkings.get(0).getId();

        MvcResult result = mockMvc.perform(get("/admin/account-balance/" + id1)
                .characterEncoding("UTF-8").with(user((UserDetails) customUserDetails)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void setAccountBalance_ExistingIdAndValidBalance_BalanceModified() throws Exception {
        List<Checking> checkings = checkingRepository.findAll();
        Long id1 = checkings.get(0).getId();
        List<Saving> savings = savingRepository.findAll();
        Long id2 = savings.get(0).getId();
        List<CreditCard> creditCards = creditCardRepository.findAll();
        Long id3 = creditCards.get(0).getId();

        BalanceDTO balance = new BalanceDTO(Currency.getInstance(("EUR")).getCurrencyCode(), BigDecimal.valueOf(5000));
        String body = objectMapper.writeValueAsString(balance);

        MvcResult result1 = mockMvc.perform(patch("/admin/account-balance/" + id1).content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsAdmin)).with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();

        MvcResult result2 = mockMvc.perform(patch("/admin/account-balance/" + id2).content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsAdmin)).with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();

        MvcResult result3 = mockMvc.perform(patch("/admin/account-balance/" + id3).content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsAdmin)).with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(BigDecimal.valueOf(5000).setScale(2), checkingRepository.findById(id1).get().getBalance().getAmount());
        assertEquals(BigDecimal.valueOf(5000).setScale(2), savingRepository.findById(id2).get().getBalance().getAmount());
        assertEquals(BigDecimal.valueOf(5000).setScale(2), creditCardRepository.findById(id3).get().getBalance().getAmount());
    }

    @Test
    public void setAccountBalance_NotExistingIdAndValidBalance_NotFound() throws Exception {
        BalanceDTO balance = new BalanceDTO("EUR", BigDecimal.valueOf(5000));
        String body = objectMapper.writeValueAsString(balance);

        MvcResult result = mockMvc.perform(patch("/admin/account-balance/1000000").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsAdmin)).with(csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void setAccountBalance_ExistingIdAndNotValidBalance_BalanceModified() throws Exception {
        List<Checking> checkings = checkingRepository.findAll();
        Long id1 = checkings.get(0).getId();

        BalanceDTO balance = new BalanceDTO("ACURRENCY", BigDecimal.valueOf(5000));
        String body = objectMapper.writeValueAsString(balance);

        MvcResult result = mockMvc.perform(patch("/admin/account-balance/" + id1).content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsAdmin)).with(csrf()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getBalanceForAccount_ExistingIdAndCorrectPrimaryUser_Balance() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerUsernameOrSecondaryOwnerUsername(
                customUserDetails.getUsername(), customUserDetails.getUsername());
        Long id1 = accounts.get(0).getId();

        MvcResult result = mockMvc.perform(get("/account/" + id1).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetails)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("500"));
    }

    @Test
    public void getBalanceForAccount_ExistingIdAndCorrectSecondaryUser_Balance() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerUsernameOrSecondaryOwnerUsername(
                customUserDetails3.getUsername(), customUserDetails3.getUsername());
        Long id1 = accounts.get(0).getId();

        MvcResult result = mockMvc.perform(get("/account/" + id1).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetails3)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("500"));
    }

    @Test
    public void getBalanceForAccount_NotExistingIdAndCorrectUser_NotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/account/100000").characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetails)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getBalanceForAccount_ExistingIdAndIncorrectUser_Forbidden() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerUsernameOrSecondaryOwnerUsername(
                "manuelg", "manuelg");
        Long id1 = accounts.get(0).getId();

        MvcResult result = mockMvc.perform(get("/account/" + id1).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetails2)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void transferMoney_ValidTransaction_BalancesModifiedAndTransactionSaved() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        Account account = accountRepository.findById(checking.getId()).get();

        transaction = new Transaction(account, "Ana Pérez", id,
                new Money(BigDecimal.valueOf(100), checking.getBalance().getCurrency()));
        String body = objectMapper.writeValueAsString(transaction);

        MvcResult result = mockMvc.perform(patch("/transfer-money").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetails)).with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(BigDecimal.valueOf(400).setScale(2),
                checkingRepository.findById(checking.getId()).get().getBalance().getAmount());
        assertEquals(BigDecimal.valueOf(3100).setScale(2),
                savingRepository.findById(id).get().getBalance().getAmount());
        assertEquals(BigDecimal.valueOf(100).setScale(2), transactionRepository.findAll().get(0).getAmount().getAmount());
    }

    @Test
    public void transferMoney_ValidTransactionButAnotherUser_Forbidden() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        Account account = accountRepository.findById(checking.getId()).get();

        transaction = new Transaction(account, "Ana Pérez", id,
                new Money(BigDecimal.valueOf(100), checking.getBalance().getCurrency()));
        String body = objectMapper.writeValueAsString(transaction);

        MvcResult result = mockMvc.perform(patch("/transfer-money").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetails2)).with(csrf()))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void transferMoney_ValidTransactionButNotEnoughBalance_NotAcceptable() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        Account account = accountRepository.findById(checking.getId()).get();

        transaction = new Transaction(account, "Ana Pérez", id,
                new Money(BigDecimal.valueOf(10000), checking.getBalance().getCurrency()));
        String body = objectMapper.writeValueAsString(transaction);

        MvcResult result = mockMvc.perform(patch("/transfer-money").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetails)).with(csrf()))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void transferMoney_ValidTransactionButNameDoesNotMatch_NotAcceptable() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        Account account = accountRepository.findById(checking.getId()).get();

        transaction = new Transaction(account, "María Pérez", id,
                new Money(BigDecimal.valueOf(100), checking.getBalance().getCurrency()));
        String body = objectMapper.writeValueAsString(transaction);

        MvcResult result = mockMvc.perform(patch("/transfer-money").content(body)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetails)).with(csrf()))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void receiveMoney_ValidIdSecretKeyAmountAndHashedKey_BalanceSubtracted() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/receive-money/" + id)
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(BigDecimal.valueOf(2950).setScale(2),
                savingRepository.findById(saving2.getId()).get().getBalance().getAmount());
    }

    @Test
    public void receiveMoney_NotValidIdButValidSecretKeyAmountAndHashedKey_NotFound() throws Exception {
        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/receive-money/10000")
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void receiveMoney_ValidIdNotValidSecretKeyButValidAmountAndHashedKey_NotAcceptable() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/receive-money/" + id)
                .param("secret-key", "TTTTT55555").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void receiveMoney_ValidIdValidSecretKeyNotValidAmountButValidHashedKey_NotAcceptable() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(500000));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/receive-money/" + id)
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void receiveMoney_ValidIdValidSecretKeyValidAmountAndNotValidHashedKey_NotFound() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/receive-money/" + id)
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "AAAA5555")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void receiveMoney_ValidIdSecretKeyAmountAndHashedKeyButFrozenAccount_NotAcceptable() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        saving2.setStatus(Status.FROZEN);
        savingRepository.save(saving2);

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/receive-money/" + id)
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void sendMoney_ValidIdSecretKeyAmountAndHashedKey_BalanceSubtracted() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/send-money/" + id)
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(BigDecimal.valueOf(3050).setScale(2),
                savingRepository.findById(saving2.getId()).get().getBalance().getAmount());
    }

    @Test
    public void sendMoney_NotValidIdButValidSecretKeyAmountAndHashedKey_NotFound() throws Exception {
        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/send-money/10000")
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void sendMoney_ValidIdNotValidSecretKeyButValidAmountAndHashedKey_NotAcceptable() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/send-money/" + id)
                .param("secret-key", "TTTTT55555").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void sendMoney_ValidIdValidSecretKeyValidAmountAndNotValidHashedKey_NotFound() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/send-money/" + id)
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "AAAA5555")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void sendMoney_ValidIdSecretKeyAmountAndHashedKeyButFrozenAccount_NotAcceptable() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        saving2.setStatus(Status.FROZEN);
        savingRepository.save(saving2);

        MoneyDTO amount = new MoneyDTO("USD", BigDecimal.valueOf(50));
        String body = objectMapper.writeValueAsString(amount);

        MvcResult result = mockMvc.perform(patch("/send-money/" + id)
                .param("secret-key", "A1B2C3").content(body)
                .header("Hashed-Key", "TGHY8547")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsThirdParty)).with(csrf()))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void unfreezeAccount_ValidIdOfAFrozenAccount_AccountUnfrozen() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();
        saving2.setStatus(Status.FROZEN);
        savingRepository.save(saving2);

        MvcResult result = mockMvc.perform(patch("/admin/unfreeze-account/" + id)
                .characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsAdmin)).with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(Status.ACTIVE, savingRepository.findById(id).get().getStatus());
    }

    @Test
    public void unfreezeAccount_NotValidId_NotFound() throws Exception {

        MvcResult result = mockMvc.perform(patch("/admin/unfreeze-account/100000")
                .characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsAdmin)).with(csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void unfreezeAccount_ValidIdButNotAFrozenAccount_Conflict() throws Exception {
        List<Account> accounts = accountRepository.findByPrimaryOwnerNameOrSecondaryOwnerName(
                "Ana Pérez", "Ana Pérez");
        Long id = accounts.get(0).getId();

        MvcResult result = mockMvc.perform(patch("/admin/unfreeze-account/" + id)
                .characterEncoding("UTF-8")
                .with(user((UserDetails) customUserDetailsAdmin)).with(csrf()))
                .andExpect(status().isConflict())
                .andReturn();
    }
}