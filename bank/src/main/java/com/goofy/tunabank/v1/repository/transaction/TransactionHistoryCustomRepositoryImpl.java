package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.domain.history.QAbstractHistory;
import com.goofy.tunabank.v1.domain.history.QCardHistory;
import com.goofy.tunabank.v1.domain.history.QTransactionHistory;
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

    public Optional<List<AbstractHistory>> findHistoryByAccountNo(TransactionHistoryListRequestDto requestDto) {
        QAbstractHistory abstractHistory = QAbstractHistory.abstractHistory;
        QTransactionHistory transactionHistory = QTransactionHistory.transactionHistory;
        QCardHistory cardHistory = QCardHistory.cardHistory;

        List<AbstractHistory> result = queryFactory
            .select(abstractHistory)
            .from(abstractHistory)
            .leftJoin(transactionHistory).on(
                abstractHistory.id.eq(transactionHistory.id)
                    .and(abstractHistory.transactionType.eq(transactionHistory.transactionType))
                    .and(transactionHistory.moneyBox.account.accountNo.eq(requestDto.getAccountNo()))
            )
            .leftJoin(cardHistory).on(
                abstractHistory.id.eq(cardHistory.id)
                    .and(abstractHistory.transactionType.eq(cardHistory.transactionType))
                    .and(cardHistory.card.account.accountNo.eq(requestDto.getAccountNo()))
            )
            .where(
                transactionHistory.moneyBox.account.accountNo.eq(requestDto.getAccountNo())
                    .or(cardHistory.card.account.accountNo.eq(requestDto.getAccountNo())),
                transactionTypeEq(abstractHistory, requestDto.getTransactionType()),
                transactionDateRangeEq(abstractHistory, requestDto.getStartDate(), requestDto.getEndDate())
//                accountNoAndCurrencyCodeEq(abstractHistory, requestDto.getAccountNo(), requestDto.getCurrencyCode())
            )
            .orderBy(getOrderByExpression(abstractHistory, requestDto.getOrderByType()))
            .fetch();

        return Optional.ofNullable(result.isEmpty() ? null : result);
    }
//
//    private BooleanBuilder accountNoAndCurrencyCodeEq(QAbstractHistory qAbstractHistory, String accountNo, CurrencyType currencyCode) {
//        BooleanBuilder builder = new BooleanBuilder();
//
//        if (accountNo != null) {
//            // TransactionHistory인 경우
//            builder.or(
//                new CaseBuilder()
//                    .when(qAbstractHistory.instanceOf(QTransactionHistory.class))
//                    .then(QTransactionHistory.transactionHistory.moneyBox.account.accountNo.eq(accountNo))
//                    .otherwise(false)
//            );
//
//            // CardHistory인 경우
//            builder.or(
//                new CaseBuilder()
//                    .when(qAbstractHistory.instanceOf(QCardHistory.class))
//                    .then(QCardHistory.cardHistory.card.account.accountNo.eq(accountNo))
//                    .otherwise(false)
//            );
//        }
//
//        if (currencyCode != null) {
//            // TransactionHistory에서만 currencyCode 필터 적용
//            builder.and(
//                new CaseBuilder()
//                    .when(qAbstractHistory.instanceOf(QTransactionHistory.class))
//                    .then(QTransactionHistory.transactionHistory.moneyBox.currency.currencyCode.eq(currencyCode))
//                    .otherwise(false)
//            );
//        }
//
//        return builder;
//    }





    private BooleanBuilder transactionTypeEq(QAbstractHistory qAbstractHistory, TransactionType transactionType) {
        return transactionType != null ? new BooleanBuilder(qAbstractHistory.transactionType.eq(transactionType)) : new BooleanBuilder();
    }

    private BooleanBuilder transactionDateRangeEq(QAbstractHistory qAbstractHistory, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            return new BooleanBuilder(qAbstractHistory.transactionAt.between(startDateTime, endDateTime));
        }
        return new BooleanBuilder();
    }

    private OrderSpecifier<LocalDateTime> getOrderByExpression(QAbstractHistory qAbstractHistory, OrderByType orderByType) {
        return orderByType == OrderByType.ASC ? qAbstractHistory.transactionAt.asc() : qAbstractHistory.transactionAt.desc();
    }
}
