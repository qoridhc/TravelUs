package com.goofy.tunabank.repository;

import com.goofy.tunabank.domain.Account;
import com.goofy.tunabank.domain.AccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, AccountId> {

    @Query("SELECT MAX(a.id) FROM Account a")
    Long findMaxAccountId();

}
