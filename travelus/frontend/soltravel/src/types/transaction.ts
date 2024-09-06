// 이체 필요 타입
export interface TransferRequest {
  depositAccountNo: string;
  depositTransactionSummary: string;
  transactionBalance: number;
  withdrawlTransactionSummary: string;
}

export interface TransferResponse {
  transactionUniqueNo: string;
  accountNo: string;
  transactionDate: string;
  transactionType: string;
  transactionTypeName: string;
  transactionAccountNo: string;
}