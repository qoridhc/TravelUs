import api from "../lib/axios";
import { SettlementRequest } from "../types/settlement";

export const settlementApi = {
  // 정산 수행
  fetchSettlement: (data: SettlementRequest) => {
    return api.post(`/settlement`, data);
  }
};