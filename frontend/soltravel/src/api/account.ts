
import api from "../lib/axios";
import { AccountInfo, Transaction } from "../types/account";

// 계좌 및 거래내역 조회
export const accountApi = {
    // 유저의 계좌 정보 받아오기
    fetchAccountInfo: async (userId: string): Promise<AccountInfo> => {
        try {
            const response = await api.get<AccountInfo>(`/account/general/${userId}/all`);
            return response.data;
        }   catch (error) {
            throw new Error('Failed to fetch account info')
        }
    },

    // 유저의 해당 계좌에 대한 거래 내역 받아오기
    // fetchTransactionHistory: async (accountNo: string): Promise<Transaction[]> => {
    //     try {
    //         const response = await api.get<Transaction[]>(`/transaction/${accountNo}/history`);
    //         return response.data;
    //     }   catch (error) {
    //         throw new Error('Failed to fetch transaction history');
    //     }
    // },
}


