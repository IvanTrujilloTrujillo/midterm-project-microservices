package com.ironhack.userservice.controller.impl;

import com.ironhack.userservice.model.Role;
import com.ironhack.userservice.model.User;
import com.ironhack.userservice.repository.RoleRepository;
import com.ironhack.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
public class RoleController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/roles/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Role> getRoles(@PathVariable("username") String username) {
        if(userRepository.findByUsername(username).isPresent()) {
            return userRepository.findByUsername(username).get().getRoles();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User nor found");
        }
    }
}
