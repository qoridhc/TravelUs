
import api from "../lib/axios";
import { AccountInfo } from "../types/account";

export const accountApi = {
    // 일반 계좌 정보 가져오기
    fetchAccountInfo: (userId: string): Promise<AccountInfo[]> => {
      return api.get(`/account/general/${userId}/all`)
    },
  
    // 외화 계좌 정보 가져오기
    fetchForeignAccountInfo: (userId: string): Promise<AccountInfo[]> => {
      return api.get(`/account/foreign/${userId}/all`)
    }
};

