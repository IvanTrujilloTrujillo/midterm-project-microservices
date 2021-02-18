package com.ironhack.userservice.controller.impl;

import com.ironhack.userservice.controller.interfaces.IThirdPartyController;
import com.ironhack.userservice.model.ThirdParty;
import com.ironhack.userservice.repository.ThirdPartyRepository;
import com.ironhack.userservice.service.impl.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private ThirdPartyService thirdPartyService;

    //Request for admins. Create a third party
    @PostMapping("/admin/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody @Valid ThirdParty thirdParty) {
        return thirdPartyService.createThirdParty(thirdParty);
    }
}
