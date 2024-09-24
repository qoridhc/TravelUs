package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.repository.transaction.AccountRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

    // 간단한 JPA 쿼리 메소드
    Optional<Account> findById(Long accountId);

    // 계좌번호로 조회
    Optional<Account> findByAccountNo(String accountNo);

    @Query("SELECT a.accountPassword FROM Account a WHERE a.accountNo = :accountNo")
    Optional<String> findPasswordByAccountNo(@Param("accountNo") String accountNo);
}