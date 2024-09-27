package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.history.QTransactionHistory;
import com.goofy.tunabank.v1.domain.history.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
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
    public Optional<List<TransactionHistory>> findByCustomOrder(TransactionHistoryRequestDto requestDto) {
        QTransactionHistory qTransactionHistory = QTransactionHistory.transactionHistory;

        List<TransactionHistory> transactionHistories = queryFactory
            .selectFrom(qTransactionHistory)
            .where(
                accountNoAndCurrencyCodeEq(qTransactionHistory, requestDto.getAccountNo(), requestDto.getCurrencyCode()),
                transactionTypeEq(qTransactionHistory, requestDto.getTransactionType()),
                transactionDateRangeEq(qTransactionHistory, requestDto.getStartDate(), requestDto.getEndDate())
            )
            .orderBy(getOrderByExpression(qTransactionHistory, requestDto.getOrderByType()))
            .fetch();

        return transactionHistories.isEmpty() ? Optional.empty() : Optional.of(transactionHistories);
    }

    private BooleanBuilder accountNoAndCurrencyCodeEq(QTransactionHistory qTransactionHistory, String accountNo,
        CurrencyType currencyCode) {
        BooleanBuilder builder = new BooleanBuilder();
        if (accountNo != null && currencyCode != null) {
            builder.and(qTransactionHistory.moneyBox.account.accountNo.eq(accountNo))
                .and(qTransactionHistory.moneyBox.currency.currencyCode.eq(currencyCode));
        }
        return builder;
    }

    private BooleanBuilder transactionTypeEq(QTransactionHistory qTransactionHistory, TransactionType transactionType) {
        return transactionType != null ? new BooleanBuilder(qTransactionHistory.transactionType.eq(transactionType))
            : new BooleanBuilder();
    }

    private BooleanBuilder transactionDateRangeEq(QTransactionHistory qTransactionHistory, LocalDate startDate,
        LocalDate endDate) {
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            return new BooleanBuilder(qTransactionHistory.transactionAt.between(startDateTime, endDateTime));
        }
        return new BooleanBuilder();
    }

    private OrderSpecifier<LocalDateTime> getOrderByExpression(QTransactionHistory qTransactionHistory, OrderByType orderByType) {
        return orderByType == OrderByType.ASC ? qTransactionHistory.transactionAt.asc()
            : qTransactionHistory.transactionAt.desc();
    }
}
