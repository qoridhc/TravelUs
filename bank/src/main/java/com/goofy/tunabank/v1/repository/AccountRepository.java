package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.repository.queryDSL.AccountRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

    // 간단한 JPA 쿼리 메소드
    Optional<Account> findById(Long accountId);
}