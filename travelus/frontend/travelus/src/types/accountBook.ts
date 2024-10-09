export interface getAccountBookQuery {
  startDate: string;
  endDate: string;
  transactionType: string;
  orderByType: string;
}

export interface getAccountBookDayQuery {
  date: string;
  transactionType: string;
}

export interface DayHistory {
  totalExpenditureForeign : number;
  totalExpenditureKRW: number;
  totalIncomeForeign : number;
  totalIncomeKRW: number;
}

export interface DayHistoryDetail {
  amount: string;
  transactionType: string;
  transactionAt: string;
  balance: string;
  store: string;
}

export interface DayHistoryCreateInfo {
  accountNo: string;
  store: string;
  paid: number;
  transactionAt: string;
  address: string;
  items: BuyItemInfo[];
  currency: string;
}

export interface ReceiptInfo {
  store: string;
  paid: number;
  transactionAt: string;
  address: string;
  currency: string;
  items: BuyItemInfo[];
}

export interface BuyItemInfo {
  item: string;
  price: number;
  quantity: number;
}