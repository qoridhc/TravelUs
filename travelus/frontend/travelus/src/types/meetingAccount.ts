export interface MeetingAccountInfo {
  groupAccountPassword: string;
  groupName: string;
  icon: string;
  groupId?: number;
}

export interface TravelboxInfo {
  accountPassword: string;
  accountNo: string;
  currencyCode: string;
}

export interface ExchangeTargetInfo {
  transactionBalance: number;
  targetRate: number;
}

export interface NewParticipant {
  groupId: number;
  personalAccountNo: string;
}

export interface ParticipantInfo {
  participantId: number;
  userId: number;
  personalAccountNo: string;
  createdAt: string;
  updatedAt: string;
  master: boolean;
}