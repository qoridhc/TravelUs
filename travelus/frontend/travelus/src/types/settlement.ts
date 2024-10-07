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
  amount: number;
  groupId: number;
  groupName: string;
  participantCount: number;
  participantId: number;
  personalSettlementId: number;
  remainingAmount: number;
  settlementRequestTime: string;
}

export interface SettlementTransferRequest {
  personalSettlementId:number;
  participantId: number;
  withdrawalAccountNo: string;
  depositAccountNo: string;
  transactionBalance: number;
  withdrawalTransactionSummary: string;
  accountPassword: string;
  depositTransactionSummary: string;
}