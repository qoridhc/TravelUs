package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.domain.history.QAbstractHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryListRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionHistoryCustomRepositoryImpl implements TransactionHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<AbstractHistory>> findByCustomOrder(TransactionHistoryListRequestDto requestDto) {
        QAbstractHistory qAbstractHistory = QAbstractHistory.abstractHistory;

        List<AbstractHistory> transactionHistories = queryFactory
            .selectFrom(qAbstractHistory)
            .where(
                accountNoAndCurrencyCodeEq(qAbstractHistory, requestDto.getAccountNo(), requestDto.getCurrencyCode()),
                transactionTypeEq(qAbstractHistory, requestDto.getTransactionType()),
                transactionDateRangeEq(qAbstractHistory, requestDto.getStartDate(), requestDto.getEndDate())
            )
            .orderBy(getOrderByExpression(qAbstractHistory, requestDto.getOrderByType()))
            .fetch();

        return transactionHistories.isEmpty() ? Optional.empty() : Optional.of(transactionHistories);
    }

    private BooleanBuilder accountNoAndCurrencyCodeEq(QAbstractHistory qAbstractHistory, String accountNo,
        CurrencyType currencyCode) {
        BooleanBuilder builder = new BooleanBuilder();
        if (accountNo != null && currencyCode != null) {
            builder.and(qAbstractHistory.moneyBox.account.accountNo.eq(accountNo))
                .and(qAbstractHistory.moneyBox.currency.currencyCode.eq(currencyCode));
        }
        return builder;
    }

    private BooleanBuilder transactionTypeEq(QAbstractHistory qAbstractHistory, TransactionType transactionType) {
        return transactionType != null ? new BooleanBuilder(qAbstractHistory.transactionType.eq(transactionType))
            : new BooleanBuilder();
    }

    private BooleanBuilder transactionDateRangeEq(QAbstractHistory qAbstractHistory, LocalDate startDate,
        LocalDate endDate) {
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            return new BooleanBuilder(qAbstractHistory.transactionAt.between(startDateTime, endDateTime));
        }
        return new BooleanBuilder();
    }

    private OrderSpecifier<LocalDateTime> getOrderByExpression(QAbstractHistory qAbstractHistory, OrderByType orderByType) {
        return orderByType == OrderByType.ASC ? qAbstractHistory.transactionAt.asc()
            : qAbstractHistory.transactionAt.desc();
    }
}
