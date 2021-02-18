package com.ironhack.bankingsystem.repository;

import com.ironhack.bankingsystem.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User("Manolo Hernández", "manoloh", "1234");
        user2 = new User("Ana Martín", "anam", "9876");

        userRepository.saveAll(List.of(user1, user2));
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void save_User_SavedCorrectly() {
        User user = new User("Raquel Govea", "raquelg", "5555");
        userRepository.save(user);

        assertEquals(3, userRepository.count());
    }

    @Test
    public void save_UserWithAnExistingUsername_SavedCorrectly() {
        User user = new User("Ana Moreno", "anam", "5555");

        assertThrows(DataIntegrityViolationException.class, ()->userRepository.save(user));
    }

    @Test
    public void findByUsername_ExistingUsername_User() {
        assertEquals("Manolo Hernández", userRepository.findByUsername("manoloh").get().getName());
    }
}