
import api from "../lib/axios";
import { AccountInfo } from "../types/account";

export const accountApi = {
    // 일반 계좌 정보 가져오기
    fetchAccountInfo: async (userId: string): Promise<AccountInfo[]> => {
      try {
        const response = await api.get(`/account/general/${userId}/all`);
        return response.data;
      } catch (error) {
        throw new Error('Failed to fetch account info');
      }
    },
  
    // 외화 계좌 정보 가져오기
    fetchForeignAccountInfo: async (userId: string): Promise<AccountInfo[]> => {
      try {
        const response = await api.get(`/account/foreign/${userId}/all`);
        return response.data;
      } catch (error) {
        throw new Error('Failed to fetch foreign account info');
      }
    },
}

