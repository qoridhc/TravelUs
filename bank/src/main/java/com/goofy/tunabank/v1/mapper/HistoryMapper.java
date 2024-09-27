package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.domain.history.CardHistory;
import com.goofy.tunabank.v1.domain.history.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

  @Mapping(target = "transactionUniqueNo", source = "id")
  @Mapping(target = "transactionType", source = "transactionType")
  @Mapping(target = "transactionDate", source = "transactionAt")
  @Mapping(target = "transactionAmount", source = "amount", qualifiedByName = "doubleToString")
  @Mapping(target = "transactionBalance", source = "balance", qualifiedByName = "doubleToString")
  @Mapping(target = "accountNo", source = "moneyBox.account.accountNo")
  @Mapping(target = "transactionSummary", source = "summary")
  TransactionResponseDto toTransactionResponseDto(TransactionHistory transactionHistory);

  @Mapping(target = "transactionUniqueNo", source = "id")
  @Mapping(target = "transactionType", source = "transactionType")
  @Mapping(target = "transactionDate", source = "transactionAt")
  @Mapping(target = "transactionAmount", source = "amount", qualifiedByName = "doubleToString")
  @Mapping(target = "transactionBalance", source = "balance", qualifiedByName = "doubleToString")
  @Mapping(target = "accountNo", source = "card.account.accountNo")
  @Mapping(target = "transactionSummary", source = "merchant.name")
  TransactionResponseDto toTransactionResponseDto(CardHistory cardHistory);

  List<TransactionResponseDto> toTransactionResponseDtos(List<AbstractHistory> transactionHistories);

  // Double을 String으로 변환하는 매핑 메서드
  @Named("doubleToString")
  default String map(Double value) {
    return value != null ? String.valueOf(value) : null;
  }
}
