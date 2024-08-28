// 환율 타입
export interface ExchangeRateInfo {
  currency: string;
  exchangeRate: number;
  exchangeMin: number;
  created: string;
}

// 환전 요청 타입
export interface ExchangeRequest {
  accountid: number;
  accountNo: string;
  exchangeCurrency: string;
  exchangeAmount: number;
  exchangeRate: number;
}

// 환전 요청에 관한 Response
export interface ExchangeResponse {
  exchangeCurrencyDto: {
    amount: number;
    exchangeRate: number;
    currency: string;
  },
  accountInfoDto: {
    accountNo: string;
    accountId: number;
    amount: number;
    balance: number;
  },
  executed_at: string;
}