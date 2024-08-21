package com.ssafy.soltravel.repository;

import com.ssafy.soltravel.domain.GeneralAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GeneralAccountRepository extends JpaRepository<GeneralAccount, Long> {

    @Transactional
    void deleteByAccountNo(String accountNo);

}
