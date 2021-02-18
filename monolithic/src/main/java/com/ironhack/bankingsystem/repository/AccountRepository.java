package com.ironhack.bankingsystem.repository;

import com.ironhack.bankingsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    public List<Account> findByPrimaryOwnerUsernameOrSecondaryOwnerUsername(String username1, String username2);
    public List<Account> findByPrimaryOwnerNameOrSecondaryOwnerName(String name1, String name2);
}
