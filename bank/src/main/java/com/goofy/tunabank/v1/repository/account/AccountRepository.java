package com.goofy.tunabank.v1.repository.account;

import com.goofy.tunabank.v1.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

    // 간단한 JPA 쿼리 메소드
    Optional<Account> findById(Long accountId);

}