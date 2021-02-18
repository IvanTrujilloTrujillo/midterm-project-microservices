package com.ironhack.accountservice.controller.interfaces;

import com.ironhack.accountservice.controller.dtos.SavingDTO;
import com.ironhack.accountservice.model.Saving;

public interface ISavingController {
    SavingDTO createSaving(SavingDTO saving);
}
