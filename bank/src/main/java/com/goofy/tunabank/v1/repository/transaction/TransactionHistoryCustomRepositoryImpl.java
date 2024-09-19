package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.QTransactionHistory;
import com.goofy.tunabank.v1.domain.TransactionHistory;
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
  public Optional<List<TransactionHistory>> findByCustomOrder(
      TransactionHistoryRequestDto requestDto) {
    QTransactionHistory qTransactionHistory = QTransactionHistory.transactionHistory;

    List<TransactionHistory> transactionHistories = queryFactory
        .selectFrom(qTransactionHistory)
        .where(
            accountIdEq(qTransactionHistory, requestDto.getAccountId()),
            accountTypeEq(qTransactionHistory, requestDto.getAccountType()),
            transactionTypeEq(qTransactionHistory, requestDto.getTransactionType()),
            transactionDateRangeEq(qTransactionHistory, requestDto.getStartDate(),
                requestDto.getEndDate())
        )
        .orderBy(getOrderByExpression(qTransactionHistory, requestDto.getOrderByType()))
        .fetch();

    return transactionHistories.isEmpty() ? Optional.empty() : Optional.of(transactionHistories);
  }

  private BooleanBuilder accountIdEq(QTransactionHistory qTransactionHistory, Long accountId) {
    return accountId != null ? new BooleanBuilder(qTransactionHistory.account.id.eq(accountId))
        : new BooleanBuilder();
  }

  private BooleanBuilder accountTypeEq(QTransactionHistory qTransactionHistory,
      AccountType accountType) {
    return accountType != null ? new BooleanBuilder(
        qTransactionHistory.account.accountType.eq(accountType)) : new BooleanBuilder();
  }

  private BooleanBuilder transactionTypeEq(QTransactionHistory qTransactionHistory,
      TransactionType transactionType) {
    return transactionType != null ? new BooleanBuilder(
        qTransactionHistory.transactionType.eq(transactionType)) : new BooleanBuilder();
  }

  private BooleanBuilder transactionDateRangeEq(QTransactionHistory qTransactionHistory,
      LocalDate startDate, LocalDate endDate) {
    if (startDate != null && endDate != null) {
      LocalDateTime startDateTime = startDate.atStartOfDay();
      LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
      return new BooleanBuilder(
          qTransactionHistory.transactionAt.between(startDateTime, endDateTime));
    }
    return new BooleanBuilder();
  }

  private OrderSpecifier<LocalDateTime> getOrderByExpression(
      QTransactionHistory qTransactionHistory, OrderByType orderByType) {
    return orderByType == OrderByType.ASC ? qTransactionHistory.transactionAt.asc()
        : qTransactionHistory.transactionAt.desc();
  }
}
