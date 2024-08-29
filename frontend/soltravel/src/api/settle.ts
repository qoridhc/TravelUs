import api from "../lib/axios";
import { SettlementRequest, SettlementResponse } from "../types/settle";

export const settlementApi = {
  // 정산하기
  SettleInfo: async (data: SettlementRequest): Promise<SettlementResponse> => {
    const response = await api.post<SettlementResponse>(`/settlement`, data)
    return response.data
  },
};