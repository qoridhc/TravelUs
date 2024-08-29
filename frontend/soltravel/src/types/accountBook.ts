export interface getAccountBookQuery {
  startDate: string;
  endDate: string;
  transactionType: string;
  orderByType: string;
}

export interface DayHistory {
  totalExpenditure : number;
  totalIncome: number;
}
