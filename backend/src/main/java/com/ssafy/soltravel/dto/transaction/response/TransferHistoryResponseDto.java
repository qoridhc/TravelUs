package com.ssafy.soltravel.dto.transaction.response;

import lombok.Data;

@Data
public class TransferHistoryResponseDto {
    String transactionUniqueNo;
    String accountNo;
    String transactionDate;
    String transactionType;
    String transactionTypeName;
    String transactionAccountNo;
}
