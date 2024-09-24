package com.goofy.tunabank.v1.repository.account;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.QAccount;
import com.goofy.tunabank.v1.domain.QMoneyBox;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Account> findByAccountNo(String accountNo) {
        QAccount account = QAccount.account;
        QMoneyBox moneyBox = QMoneyBox.moneyBox;

        Account result = queryFactory
            .selectFrom(account)
            .leftJoin(account.moneyBoxes, moneyBox).fetchJoin()
            .where(account.accountNo.eq(accountNo))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<Account>> findAllAccountsByUserId(Long userId) {

        QAccount account = QAccount.account;
        QMoneyBox moneyBox = QMoneyBox.moneyBox;

        List<Account> result = queryFactory.selectFrom(account)
            .leftJoin(account.moneyBoxes, moneyBox).fetchJoin()
            .where(account.user.userId.eq(userId))
            .fetch();

        return Optional.ofNullable(result);
    }

}
