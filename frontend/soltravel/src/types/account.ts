// 계좌 정보
export interface AccountInfo {
  id: number;
  bankCode: number;
  dailyTransferLimit: string;
  accountNo: string;
  accountName: string;
  accountType: string;
  gropuName: string;
  iconName: string;
  travelStartDate: string;
  travelEndDate: string;
  currency: {
    currencyCode: string;
    currencyName: string;
  }
  createdAt: string;
  updatedAt: string;
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

// 입금 요청
export interface DepositRequest {
  transactionBalance: number;
  transactionSummary: string;
}

// 입금 응답
export interface DepositResponse {
  transactionUniqueNo: string;
  transactionDate: string;
}

// 모임통장 목록 조회 정보
export interface MeetingAccountListDetail {
  id: number;
  meetingAccountName: string;
  meetingAccountIcon: string;
  normalMeetingAccount: {
    accountNumber: string;
    accountMoney: string;
  };
  foreignMeetingAccount: {
    accountNumber: string;
    accountMoney: string;
    currencyType: string;
  };
}

// 모임통장 개설 정보
export interface MeetingAccountDetail {
  meetingAccountName: string;
  meetingAccountIcon: string;
  meetingAccountUserName: string;
  meetingAccountUserResidentNumber: string;
  meetingAccountPassword: string;
  meetingAccountMemberList: Array<string>;
}