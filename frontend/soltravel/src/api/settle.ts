import api from "../lib/axios";

export const settleApi = {
  // 정산하기
  SettleInfo: async (userId: string): Promise<AccountInfo[]> => {
    const response = await api.get(`/account/general/${userId}/all`)
    return response.data
  },
};