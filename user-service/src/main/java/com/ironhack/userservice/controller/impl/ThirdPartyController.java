package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IThirdPartyController;
import com.ironhack.bankingsystem.model.ThirdParty;
import com.ironhack.bankingsystem.repository.ThirdPartyRepository;
import com.ironhack.bankingsystem.service.impl.ThirdPartyService;
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
