export interface MeetingAccountInfo {
  groupAccountPassword: string;
  groupName: string;
  icon: string;
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