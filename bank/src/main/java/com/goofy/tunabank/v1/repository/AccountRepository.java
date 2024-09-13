package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT MAX(a.id) FROM Account a")
    Long findMaxAccountId();

    Optional<Account> findByIdAndAccountType(Long accountId, AccountType accountType);
}
