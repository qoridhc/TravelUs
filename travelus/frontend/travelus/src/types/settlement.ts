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