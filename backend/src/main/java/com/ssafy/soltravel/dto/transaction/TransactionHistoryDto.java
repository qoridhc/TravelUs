package com.ssafy.soltravel.dto.transaction;

import lombok.Data;

@Data
public class TransactionHistoryDto{
	private String transactionType;
	private String transactionAfterBalance;
	private String transactionUniqueNo;
	private String transactionAccountNo;
	private String transactionBalance;
	private String transactionSummary;
	private String transactionDate;
	private String transactionTime;
	private String transactionMemo;
	private String transactionTypeName;
}