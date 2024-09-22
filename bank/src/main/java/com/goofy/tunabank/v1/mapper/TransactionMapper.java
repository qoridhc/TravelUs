package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  @Mapping(target = "transactionHistoryId", source = "id")
  @Mapping(target = "accountNo", source = "transactionAccountNo")
  @Mapping(target = "amount", expression = "java(String.format(\"%.0f\", th.getAmount()))")
  @Mapping(target = "balance", expression = "java(String.format(\"%.0f\", th.getBalance()))")
  TransactionResponseDto convertTransactionHistoryToTransactionResponseDto(TransactionHistory th);

  List<TransactionResponseDto> convertTransactionHistoriesToResponseDtos(List<TransactionHistory> histories);
}
