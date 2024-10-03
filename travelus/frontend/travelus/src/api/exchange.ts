import api from "../lib/axios";
import { ExchangeRateInfo, ExchangeRequest, ExchangeResponse, ExchangeRateHistoryRequest, ExchangeRateHistoryResponse, TargetRate } from "../types/exchange";

import axios from "axios";

// API 응답 타입 정의
interface RecentRates {
  "3_months": { [date: string]: number };
  "1_month"?: { [date: string]: number };
  "1_week"?: { [date: string]: number };
}

interface ConfidenceInterval {
  lower: number;
  upper: number;
}

interface CurrencyPrediction {
  forecast: { [date: string]: number };
  average_forecast: number;
  confidence_interval: ConfidenceInterval;
  daily_changes: { [date: string]: number };
  recent_rates: RecentRates;
}

interface PredictionResponse {
  USD: CurrencyPrediction;
  JPY: CurrencyPrediction;
  EUR: { recent_rates: RecentRates };
  TWD: { recent_rates: RecentRates };
  last_updated: string;
}

const API_BASE_URL = "http://70.12.130.121:11209"; // GPU 서버

export const exchangeApi = {
  // GPU 서버 요청 api
  getPrediction: async (): Promise<PredictionResponse> => {
    try {
      const response = await axios.get<PredictionResponse>(`${API_BASE_URL}/prediction`);
      return response.data;
    } catch (error) {
      console.error("Failed to fetch prediction data:", error);
      throw error;
    }
  },

  // 특정 통화에 대한 최근 환율 데이터만 가져오는 메서드
  getRecentRates: async (currency: string): Promise<RecentRates> => {
    try {
      const response = await api.get<{ recent_rates: RecentRates }>(`/recent-rates/${currency}`);
      return response.data.recent_rates;
    } catch (error) {
      console.error(`Failed to fetch recent rates for ${currency}:`, error);
      throw error;
    }
  },
};

export const exchangeRateApi = {
  getExchangeRates: async (): Promise<ExchangeRateInfo[]> => {
    const response = await api.get<ExchangeRateInfo[]>('/exchange/rate');
    return response.data
  },

  getExchangeRate: async (currencyCode: string): Promise<ExchangeRateInfo> => {
    const response = await api.get<ExchangeRateInfo>(`/exchange/rate/${currencyCode}`);
    return response.data;
  },

  requestExchange: async (data: ExchangeRequest): Promise<ExchangeResponse[]> => {
    const response = await api.post<ExchangeResponse[]>('/transaction/transfer/moneybox', data)
    return response.data
  },

  getExchangeRateHistory: async (data: ExchangeRateHistoryRequest): Promise<ExchangeRateHistoryResponse> => {
    const response = await api.post<ExchangeRateHistoryResponse>('/exchange/latest', data);
    return response.data
  },

  // 환율 저장
  postExchangeTargetRate: (data: TargetRate) => {
    return api.post(`/exchange/rate/target-rate`, data);
  }
}