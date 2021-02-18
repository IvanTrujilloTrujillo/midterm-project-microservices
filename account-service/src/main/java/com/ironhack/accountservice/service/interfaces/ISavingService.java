package com.ironhack.accountservice.service.interfaces;

import com.ironhack.accountservice.controller.dtos.SavingDTO;
import com.ironhack.accountservice.model.Saving;

public interface ISavingService {
    SavingDTO createSaving(SavingDTO savingDTO);
}
