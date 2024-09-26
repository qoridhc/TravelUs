package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  @Mapping(target = "transactionUniqueNo", source = "id")
  @Mapping(target = "accountNo", source = "transactionAccountNo")
  @Mapping(target = "transactionDate", source = "transactionAt")
  @Mapping(target = "transactionAmount", expression = "java(String.format(\"%.2f\", th.getAmount()))")
  @Mapping(target = "transactionBalance", expression = "java(String.format(\"%.2f\", th.getBalance()))")
  @Mapping(target = "transactionSummary", source = "summary")
  TransactionResponseDto convertTransactionHistoryToTransactionResponseDto(TransactionHistory th);

  List<TransactionResponseDto> convertTransactionHistoriesToResponseDtos(List<TransactionHistory> histories);
}
