package com.ssafy.soltravel.dto.transaction.request;

import lombok.Data;

@Data
public class TransferRequestDto {

    String depositAccountNo;

    String depositTransactionSummary;

    Long transactionBalance;

    String withdrawalTransactionSummary;
}
