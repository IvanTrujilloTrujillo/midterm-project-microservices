package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.Role;
import com.ironhack.bankingsystem.model.ThirdParty;
import com.ironhack.bankingsystem.repository.RoleRepository;
import com.ironhack.bankingsystem.repository.ThirdPartyRepository;
import com.ironhack.bankingsystem.repository.UserRepository;
import com.ironhack.bankingsystem.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ThirdPartyService implements IThirdPartyService {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //Service to create a third party by an admin
    public ThirdParty createThirdParty(ThirdParty thirdParty) {

        //Check if the username already exists
        if(userRepository.findByUsername(thirdParty.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The username already exists");
        } else {

            //Encrypt the password
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            thirdParty.setPassword(passwordEncoder.encode(thirdParty.getPassword()));

            //Save the third party and add the role
            thirdPartyRepository.save(thirdParty);
            roleRepository.save(new Role("THIRD_PARTY", thirdParty));
            return thirdParty;
        }
    }
}
