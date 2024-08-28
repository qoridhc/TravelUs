import api from "../lib/axios";
import { ExchangeRateInfo, ExchangeRequest, ExchangeResponse, ExchangeRateHistoryRequest, ExchangeRateHistoryResponse } from "../types/exchange";

export const exchangeApi = {
  getExchangeRates: async (): Promise<ExchangeRateInfo[]> => {
    const response = await api.get('/exchange');
    console.log(response.data)
    return response.data
  },

  requestExchange: async (data: ExchangeRequest): Promise<ExchangeResponse> => {
    const response = await api.post<ExchangeResponse>('/exchange', data)
    return response.data
  },

  getExchangeRateHistory: async (data: ExchangeRateHistoryRequest): Promise<ExchangeRateHistoryResponse> => {
    const response = await api.post<ExchangeRateHistoryResponse>('/exchange/latest', data);
    console.log(response.data)
    return response.data
  }
};

