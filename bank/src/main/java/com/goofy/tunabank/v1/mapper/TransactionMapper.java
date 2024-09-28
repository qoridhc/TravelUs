package com.goofy.tunabank.v1.mapper;


import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.domain.history.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  @Mapping(target = "transactionUniqueNo", source = "id")
  @Mapping(target = "transactionType", source = "transactionType")
  @Mapping(target = "transactionDate", source = "transactionAt")
  @Mapping(target = "transactionAmount", expression = "java(String.format(\"%.2f\", transactionHistory.getAmount()))")
  @Mapping(target = "transactionBalance", expression = "java(String.format(\"%.2f\", transactionHistory.getBalance()))")
  @Mapping(target = "accountNo", source = "moneyBox.account.accountNo")
  @Mapping(target="ownerName", source="moneyBox.account.user.name")
  @Mapping(target = "transactionSummary", source = "summary")
  TransactionResponseDto toTransactionResponseDto(TransactionHistory transactionHistory);

  // AbstractHistory 리스트를 DTO 리스트로 변환
  List<TransactionResponseDto> toTransactionResponseDtos(List<AbstractHistory> transactionHistories);
}
