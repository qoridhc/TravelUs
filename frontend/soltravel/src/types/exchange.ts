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
  accountId: number;
  accountNo: string;
  currencyCode: string;
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

// 통화 종류 목록
export const currencyTypeList: Array<{text: string, value: string}> = [
  { text: "USD(미국/$)", value: "USD" },
  { text: "JPY(일본/¥)", value: "JPY" },
  { text: "EUR(유로/€)", value: "EUR" },
  { text: "GBP(영국/£)", value: "GBP" },
  { text: "CHF(스위스/₣)", value: "CHF" },
  { text: "CAD(캐나다/$)", value: "CAD" },
  { text: "CNY(중국/¥)", value: "CNY" },
];