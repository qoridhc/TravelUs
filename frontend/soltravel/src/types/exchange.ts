// 환율 타입
export interface ExchangeRateInfo {
  currencyCode: string;
  exchangeRate: number;
  exchangeMin: number;
  created: string;
}

// 환율 그래프를 위한 타입
export interface ExchangeRateHistoryRequest {
  currencyCode: string;
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

// 환전 진행 관련 요청 / 응답
export interface ExchangeRequest {
  accountid: number;
  accountNo: string;
  exchangeCurrency: string;
  exchangeAmount: number;
  exchangeRate: number;
}

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

// 통화 종류
export const currencyNames: { [key: string]: string } = {
  USD: '미국 달러',
  JPY: '일본 엔',
  EUR: '유로',
  GBP: '영국 파운드',
  CHF: '스위스 프랑',
  CAD: '캐나다 달러',
  CNY: '중국 위안',
};