import api from "../lib/axios";
import { ExchangeRateInfo, ExchangeRequest, ExchangeResponse } from "../types/exchange";

export const exchangeApi = {
  getExchangeRates: (): Promise<ExchangeRateInfo[]> => {
    return api.get('/exchange')
  },

  requestExchange: (data: ExchangeRequest): Promise<ExchangeResponse> => {
    return api.post(`/exchange`, data);
  }
};

