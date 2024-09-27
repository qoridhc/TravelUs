package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.QAccount;
import com.goofy.tunabank.v1.domain.QCard;
import com.goofy.tunabank.v1.domain.QMoneyBox;
import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.domain.history.QAbstractHistory;
import com.goofy.tunabank.v1.domain.history.QCardHistory;
import com.goofy.tunabank.v1.domain.history.QTransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryListRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ap.shaded.freemarker.template.utility.OptimizerUtil;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionHistoryCustomRepositoryImpl implements TransactionHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<List<AbstractHistory>> findHistoryByAccountNo(String accountNo) {
        QAbstractHistory abstractHistory = QAbstractHistory.abstractHistory;
        QTransactionHistory transactionHistory = QTransactionHistory.transactionHistory;
        QCardHistory cardHistory = QCardHistory.cardHistory;
        QAccount account = QAccount.account; // 단일 QAccount 객체 사용

        List<AbstractHistory> result = queryFactory
            .select(abstractHistory)
            .from(abstractHistory)
            .leftJoin(transactionHistory).on(
                abstractHistory.id.eq(transactionHistory.id)
                    .and(abstractHistory.transactionType.eq(transactionHistory.transactionType))
                    .and(transactionHistory.moneyBox.account.accountNo.eq(accountNo))
            )
            .leftJoin(cardHistory).on(
                abstractHistory.id.eq(cardHistory.id)
                    .and(abstractHistory.transactionType.eq(cardHistory.transactionType))
                    .and(cardHistory.card.account.accountNo.eq(accountNo))
            )
            .where(
                transactionHistory.moneyBox.account.accountNo.eq(accountNo)
                    .or(cardHistory.card.account.accountNo.eq(accountNo))
            )
            .orderBy(abstractHistory.transactionAt.asc())
            .fetch();

        return Optional.ofNullable(result.isEmpty() ? null : result);
    }




//    @Override
//    public Optional<List<AbstractHistory>> findByCustomOrder(TransactionHistoryListRequestDto requestDto) {
//        QAbstractHistory qAbstractHistory = QAbstractHistory.abstractHistory;
//
//        List<AbstractHistory> transactionHistories = queryFactory
//            .selectFrom(qAbstractHistory)
//            .where(
//                accountNoAndCurrencyCodeEq(qAbstractHistory, requestDto.getAccountNo(), requestDto.getCurrencyCode()),
//                transactionTypeEq(qAbstractHistory, requestDto.getTransactionType()),
//                transactionDateRangeEq(qAbstractHistory, requestDto.getStartDate(), requestDto.getEndDate())
//            )
//            .orderBy(getOrderByExpression(qAbstractHistory, requestDto.getOrderByType()))
//            .fetch();
//
//        return transactionHistories.isEmpty() ? Optional.empty() : Optional.of(transactionHistories);
//    }
//
//    private BooleanBuilder accountNoAndCurrencyCodeEq(QAbstractHistory qAbstractHistory, String accountNo,
//        CurrencyType currencyCode) {
//        BooleanBuilder builder = new BooleanBuilder();
//        if(qAbstractHistory.transactionType.eq(TransactionType.CD).) {
//
//        }
//
//        if (accountNo != null && currencyCode != null) {
//            builder.and(qAbstractHistory.moneyBox.account.accountNo.eq(accountNo))
//                .and(qAbstractHistory.moneyBox.currency.currencyCode.eq(currencyCode));
//        }
//        return builder;
//    }
//
//    private BooleanBuilder transactionTypeEq(QAbstractHistory qAbstractHistory, TransactionType transactionType) {
//        return transactionType != null ? new BooleanBuilder(qAbstractHistory.transactionType.eq(transactionType))
//            : new BooleanBuilder();
//    }
//
//    private BooleanBuilder transactionDateRangeEq(QAbstractHistory qAbstractHistory, LocalDate startDate,
//        LocalDate endDate) {
//        if (startDate != null && endDate != null) {
//            LocalDateTime startDateTime = startDate.atStartOfDay();
//            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
//            return new BooleanBuilder(qAbstractHistory.transactionAt.between(startDateTime, endDateTime));
//        }
//        return new BooleanBuilder();
//    }
//
//    private OrderSpecifier<LocalDateTime> getOrderByExpression(QAbstractHistory qAbstractHistory, OrderByType orderByType) {
//        return orderByType == OrderByType.ASC ? qAbstractHistory.transactionAt.asc()
//            : qAbstractHistory.transactionAt.desc();
//    }
//
//    public BooleanExpression isTransactionTypeCD(QAbstractHistory abstractHistory) {
//        return abstractHistory.transactionType.eq(com.goofy.tunabank.v1.domain.Enum.TransactionType.CD);
//    }
}
