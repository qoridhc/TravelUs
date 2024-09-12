// 계좌 내역 타입 선언
export interface AccountHistoryRequest {
  startDate: string;
  endDate: string;
  // M: 입금, D: 출금, A: 전체
  transactionType: string;
  orderByType: string;
}

export interface AccountHistoryResponse {
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