import api from "../lib/axios";
import { SettlementParticipant, SettlementRequest } from "../types/settlement";

export const settlementApi = {
  // 정산 수행
  fetchSettlement: (data: SettlementRequest) => {
    return api.post(`/settlement`, data);
  },

  // 개별 정산 요청
  fetchSettlementPersonal: (participants: SettlementParticipant[]) => {
    return api.post(`/settlement/personal`, {participants});
  },

  // 개별 정산 요청 내역 개인별 조회
  fetchSettlementPersonalList: (settlementStatus: string) => {
    return api.get(`/settlement/personal/transfer?settlementStatus=${settlementStatus}`)
  },
};