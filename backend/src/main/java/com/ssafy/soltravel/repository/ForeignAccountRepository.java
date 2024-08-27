package com.ssafy.soltravel.repository;

import com.ssafy.soltravel.domain.ForeignAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ForeignAccountRepository extends JpaRepository<ForeignAccount, Long> {

    @Transactional
    void deleteByAccountNo(String accountNo);

    Optional<ForeignAccount> findByAccountNo(String accountNo);

}
