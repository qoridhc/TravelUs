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