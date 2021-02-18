package com.ironhack.bankingsystem.repository;

import com.ironhack.bankingsystem.model.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {
}
