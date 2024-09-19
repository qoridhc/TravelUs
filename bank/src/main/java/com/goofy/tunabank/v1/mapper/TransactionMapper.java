package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransferResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  @Mapping(target = "transactionHistoryId", source = "id")
  @Mapping(target = "accountNo", source = "transactionAccountNo")
  TransactionResponseDto convertTransactionHistoryToTransactionResponseDto(TransactionHistory th);

  List<TransactionResponseDto> convertTransactionHistoriesToResponseDtos(List<TransactionHistory> histories);

  default TransferResponseDto convertToTransferResponseDto(TransactionHistory withdrawalTh,
      TransactionHistory depositTh) {

    return TransferResponseDto.builder().Rec(
        List.of(convertTransactionHistoryToTransactionResponseDto(withdrawalTh),
            convertTransactionHistoryToTransactionResponseDto(depositTh))).build();
  }
}
