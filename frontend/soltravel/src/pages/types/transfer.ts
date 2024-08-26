// 이체 관련 types
export interface TransferRequest {
  depositAccountNo: string;
  depostiTransactionSummary: string;
  transactionBalance: number;
  withdrawalTransactionSummary: string;
}

export interface TransferState {
  depositAccountNo: string;
  depostiTransactionSummary: string;
  transactionBalance: number;
  withdrawalTransactionSummary: string;
}