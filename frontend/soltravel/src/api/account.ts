
import api from "../lib/axios";
import { AccountInfo, AccountParticipants, MeetingAccountCreate } from "../types/account";

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

    // 모임통장 참여자 정보 가져오기
    fetchParticipantInfo: async (accountId: number): Promise<AccountParticipants> => {
      const response = await api.get(`/account/${accountId}/participants`)
      return response.data
    },

    // 모임통장 생성
  fetchCreateMeetingAccount: async (userId: number, data: MeetingAccountCreate): Promise<MeetingAccountCreate> => {
    const response = await api.post(`/account/general/${userId}`, data);
    return response.data;
  },
};