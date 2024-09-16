package com.goofy.tunabank.v1.repository.queryDSL;

import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.QTransactionHistory;
import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    BooleanBuilder whereClause = new BooleanBuilder();

    // 복합키 필드 (accountId와 transactionType) 조건 추가
    if (requestDto.getAccountId() != null) {
      whereClause.and(qTransactionHistory.account.id.eq(requestDto.getAccountId()));
    }

    if (requestDto.getTransactionType() != null) {
      whereClause.and(qTransactionHistory.transactionType.eq(requestDto.getTransactionType()));
    }

    // 거래 일시 범위 조건 추가
    if (requestDto.getStartDate() != null && requestDto.getEndDate() != null) {

      LocalDateTime startDateTime = requestDto.getStartDate().atStartOfDay();
      LocalDateTime endDateTime = requestDto.getEndDate().atTime(LocalTime.MAX);

      whereClause.and(qTransactionHistory.transactionAt.between(startDateTime,
          endDateTime));
    }

    // 정렬 기준 추가
    List<TransactionHistory> transactionHistories = queryFactory
        .selectFrom(qTransactionHistory)
        .where(whereClause)
        .orderBy(
            requestDto.getOrderByType() == OrderByType.ASC ? qTransactionHistory.transactionAt.asc()
                : qTransactionHistory.transactionAt.desc())
        .fetch();

    return
        transactionHistories.isEmpty() ? Optional.empty() : Optional.of(transactionHistories);
  }
}
