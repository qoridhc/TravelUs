import api from "../lib/axios";
import { ExchangeRateInfo } from "../types/exchange";

export const exchangeApi = {
  getExchangeRates: async (): Promise<ExchangeRateInfo[]> => {
    try {
      const response = await api.get<ExchangeRateInfo[]>('/exchange');
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch exchange rates');
    }
  },
};