package com.goofy.tunabank.v1.repository.queryDSL;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.QAccount;
import com.goofy.tunabank.v1.domain.QMoneyBox;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Account> findGroupAccountById(Long accountId) {
        QAccount account = QAccount.account;
        QMoneyBox moneyBox = QMoneyBox.moneyBox;

        Account result = queryFactory
            .selectFrom(account)
            .leftJoin(account.moneyBoxes, moneyBox).fetchJoin()
            .where(account.id.eq(accountId)
                .and(account.accountType.eq(AccountType.G)))
            .fetchOne();

        return Optional.ofNullable(result);
    }
}
