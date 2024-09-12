import api from "../lib/axios";
import { TransferRequest, TransferResponse } from "../types/transaction";

export const transactionApi = {
  // 이체하기
  TransferInfo: async (accountNo: string, data: TransferRequest): Promise<TransferResponse> => {
    const response = await api.post<TransferResponse>(`/transaction/${accountNo}/transfer`, data)
    return response.data
  },
};