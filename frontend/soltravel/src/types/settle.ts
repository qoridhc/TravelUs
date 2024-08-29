// 정산 요청
export interface SettlementRequest {
  accountId: number;
  accountNo: string;
}

// 정산
export interface SettlementResponse {
  accountId: number;
  accounNo: string;
  userId: number;
  amountPerPerson: number;
  message: string;
}