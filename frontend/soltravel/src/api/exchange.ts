import api from "../lib/axios";
import { ExchangeRateInfo, ExchangeRequest, ExchangeResponse, ExchangeRateHistoryRequest, ExchangeRateHistoryResponse } from "../types/exchange";

export const exchangeApi = {
  getExchangeRates: async (): Promise<ExchangeRateInfo[]> => {
    const response = await api.get('/exchange');
    return response.data
  },

  requestExchange: (data: ExchangeRequest): Promise<ExchangeResponse> => {
    return api.post(`/exchange`, data);
  },

  getExchangeRateHistory: (data: ExchangeRateHistoryRequest): Promise<ExchangeRateHistoryResponse[]> => {
    return api.post(`/exchange/latest`, data);
  }
};

