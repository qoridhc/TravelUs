import api from "../lib/axios";
import { SettleRequest, SettleResponse } from "../types/settle";

export const settleApi = {
  // 정산하기
  SettleInfo: async (data: SettleRequest): Promise<SettleResponse> => {
    const response = await api.post<SettleResponse>(`/settlement`)
    return response.data
  },
};