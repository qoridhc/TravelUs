export interface SettlementParticipant {
  participantId: number;
  amount: number;
}

export interface SettlementRequest {
  groupId: number;
  accountNo: string;
  accountPassword: string;
  settlementType: string;
  amounts: [ number, number ];
  participants: Array<SettlementParticipant>;
}

export interface SettlementPersonalInfo {
  groupId: number;
  groupName: string;
  settlementId: number;
  settlementDetailId: number;
  participantId: number;
  profile: string;
  totalAmount: number;
  amount: number;
  remainingAmount: number;
  isSettled: string;
  settlementRequestTime: string;
  participantCount: number;
}

export interface SettlementTransferRequest {
  settlementDetailId:number;
  withdrawalAccountNo: string;
  depositAccountNo: string;
  transactionBalance: number;
  withdrawalTransactionSummary: string;
  accountPassword: string;
  depositTransactionSummary: string;
}

export interface ExpenditureSettlementRequest {
  groupId: number;
  totalAmount: number;
  participants: Array<SettlementParticipant>;
}