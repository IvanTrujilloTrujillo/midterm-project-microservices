package com.ironhack.manageAccountservice.client;

import com.ironhack.manageAccountservice.model.AccountHolder;
import com.ironhack.manageAccountservice.model.Role;
import com.ironhack.manageAccountservice.model.ThirdParty;
import com.ironhack.manageAccountservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient("user-service")
public interface UserClient {
    @PostMapping("/admin/account-holder")
    AccountHolder createAccountHolder(@RequestBody AccountHolder accountHolder);

    @PostMapping("/admin/third-party")
    ThirdParty createThirdParty(@RequestBody ThirdParty thirdParty);

    @GetMapping("/user-by-username/{username}")
    User getByUsername(@PathVariable("username") String username);

    @GetMapping("/roles/{username}")
    public Set<Role> getRoles(@PathVariable("username") String username);
}
