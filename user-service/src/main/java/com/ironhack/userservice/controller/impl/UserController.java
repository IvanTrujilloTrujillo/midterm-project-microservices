package com.ironhack.userservice.controller.impl;

import com.ironhack.userservice.model.User;
import com.ironhack.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user-by-username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User getByUsername(@PathVariable("username") String username) {
        if(userRepository.findByUsername(username).isPresent()) {
            return userRepository.findByUsername(username).get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User nor found");
        }
    }
}
