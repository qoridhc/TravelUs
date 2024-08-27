// 계좌 정보
export interface AccountInfo {
  bankCode: string;
  dailyTransferLimit: string;
  accountName: string;
  accountTypeCode: string;
  bankName: string;
  accountTypeName: string;
  accountExpiryDate: string;
  userName: string;
  oneTimeTransferLimit: string;
  accountCreatedDate: string;
  lastTransactionDate: string;
  accountNo: string;
  currency: string;
  accountBalance: number;
}

// 거래 내역 조회
export interface Transaction {
  transactionType: string;
  transactionAfterBalance: string;
  transactionUniqueNo: string;
  transactionAccountNo: string;
  transactionBalance: string;
  transactionSummary: string;
  transactionDate: string;
  transactionTime: string;
  transactionMemo: string;
  transactionTypeName: string;
}

// 입금
export interface DepositRequest {
  transactionBalance: number;
  transactionSummary: string;
}