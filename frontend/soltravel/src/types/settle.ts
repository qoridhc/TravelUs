// 정산 요청
export interface SettleRequest {
  accountId: number;
  accountNo: string;
}

// 정산
export interface SettleResponse {
  accountId: number;
  accounNo: string;
  userId: number;
  amountPerPerson: number;
  message: string;
}