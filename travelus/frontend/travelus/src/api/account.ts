
import api from "../lib/axios";
import { AccountInfo, AccountInfoNew, MeetingAccountInfo, AccountParticipants, MeetingAccountCreate, meetingInvitationCode } from "../types/account";

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
  
  // 사용자의 모든 계좌 조회
  fetchAllAccountInfo: async (): Promise<AccountInfoNew[]> => {
    const response = await api.get(`/accounts/inquireAccountList`);
    return response.data;
  },

  // 특정 계좌 조회
  fetchSpecificAccountInfo: async (accountNo: string): Promise<AccountInfoNew> => {
    const response = await api.post(`/accounts/inquireAccount`, { accountNo });
    return response.data;
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

  // 생성한 모임통장 정보 가져오기 (모임원인 경우)
  fetchJoinedMeetingAccount: async (): Promise<MeetingAccountInfo[]> => {
    const response = await api.get(`/groups/joined`);
    return response.data;
  },

  // 가입한 모임통장 정보 가져오기 (모임원인 경우)
  fetchCreatedMeetingAccount: async (): Promise<MeetingAccountInfo[]> => {
    const response = await api.get(`/groups/created`);
    return response.data;
  },

  // 특정 모임 조회
  fetchSpecificMeetingAccount: (groupId: number) => {
    return api.get(`/groups/${groupId}`);
  },


  // 특정 외화모임통장 조회
  fetchForeignMeetingAccount: async (accountId: number): Promise<AccountInfo> => {
    const response = await api.get(`/account/foreign/accountId/${accountId}`);
    return response.data;
  },

  // 모임 초대 코드 발급
  fetchInvitationCode: (groupId: number) => {
    return api.post(`/groups/create/groupCode`, { "groupId": groupId });
  }
};