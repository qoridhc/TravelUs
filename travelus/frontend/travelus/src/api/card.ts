import api from "../lib/axios";
import { CardCreateInfo } from "../types/card";

export const cardApi = {
  // 모임카드 개설
  createCard: (data: CardCreateInfo) => {
    return api.post(`/card/issue`, data);
  },
};