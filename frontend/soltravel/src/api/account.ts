
import api from "../lib/axios";
import { AccountInfo, AccountParticipants } from "../types/account";

export const accountApi = {
    // 일반 계좌 정보 가져오기
    fetchAccountInfo: async (userId: number): Promise<AccountInfo[]> => {
      const response = await api.get(`/account/general/${userId}/all`)
      return response.data
    },
  
    // 외화 계좌 정보 가져오기
    fetchForeignAccountInfo: async (userId: number): Promise<AccountInfo[]> => {
      const response = await api.get(`/account/foreign/${userId}/all`)
      return response.data
    },

    fetchParticipantInfo: async (accountId: number): Promise<AccountParticipants> => {
      const response = await api.get(`/account/${accountId}/participants`)
      return response.data
    }
};