// 환율 타입
export interface ExchangeRateInfo {
  currency: string;
  exchangeRate: number;
  exchangeMin: number;
  created: string;
}

// 환율 그래프를 위한 타입
export interface ExchangeRateHistoryRequest {
  currency: string;
  startDate: string;
  endDate: string;
}

export interface ExchangeRateHistoryResponse {
  id: number;
  postAt: string;
  dealBasR: number;
  ttb: number;
  tts: number;
  cashBuying: number;
  cashSelling: number;
  tcBuying: number;
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