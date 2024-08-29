// 계좌 정보
export interface AccountInfo {
  id: number;
  bankCode: number;
  dailyTransferLimit: string;
  accountNo: string;
  balance: number;
  accountName: string;
  accountType: string;
  groupName: string;
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
  bankCode: number;
  accountPassword: string;
  accountNo: string;
  balance: number;
  accountName: string;
  accountType: string;
  currency: {
    currencyCode: string;
    currencyName: string;
  };
  createdAt: string;
  updatedAt: string;
}

// 일반모임통장 개설 정보
export interface GeneralMeetingAccountDetail {
  generalMeetingAccountName: string;
  generalMeetingAccountIcon: string;
  generalMeetingAccountUserName: string;
  generalMeetingAccountUserResidentNumber: string;
  generalMeetingAccountPassword: string;
  generalMeetingAccountMemberList: Array<string>;
}

// 모임통장 모임종류 아이콘 
export const meetingAccountIconList: Array<{text: string, value: string}> = [
  { text: "선택안함", value: "none" },
  { text: "여행", value: "airplane" },
  { text: "학교", value: "school" },
];