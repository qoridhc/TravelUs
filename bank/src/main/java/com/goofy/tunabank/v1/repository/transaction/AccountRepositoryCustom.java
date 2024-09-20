package com.goofy.tunabank.v1.repository.queryDSL;

import com.goofy.tunabank.v1.domain.Account;
import java.util.Optional;

public interface AccountRepositoryCustom {

    Optional<Account> findGroupAccountById(Long accountId);

    Optional<Account> fetchAccountWithMoneyBoxes(Long accountId);

}
