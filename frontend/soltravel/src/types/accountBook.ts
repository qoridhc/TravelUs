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
  totalExpenditure : number;
  totalIncome: number;
}

export interface DayHistoryDetail {
  amount: string;
  transactionType: string;
  transactionAt: string;
  balance: string;
  store: string;
}

export interface BuyItemInfo {
  item: string;
  price: number;
  quantity: number
}

export interface DayHistoryCreateInfo {
  accountNo: string;
  store: string;
  paid: number;
  items: Array<BuyItemInfo>;
}