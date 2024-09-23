package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.Account;
import java.util.Optional;

public interface AccountRepositoryCustom {

    Optional<Account> findAccountByAccountNo(String accountNo);

    Optional<Account> fetchAccountWithMoneyBoxes(Long accountId);

}
