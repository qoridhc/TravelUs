import api from "../lib/axios";
import { DepositRequest, DepositResponse } from "../types/transaction";

export const depositApi = {
  // 정산하기
  DepositInfo: async (accountNo: string, data: DepositRequest): Promise<DepositResponse> => {
    const response = await api.post<DepositResponse>(`/transaction/${accountNo}/deposit`, data)
    return response.data
  },
};