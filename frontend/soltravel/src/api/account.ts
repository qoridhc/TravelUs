import { AccountInfo, Transaction } from "../types/account";

// 유저의 계좌 정보 받아오기
export const fetchAccountInfo = async (userId: string): Promise<AccountInfo> => {
    const response = await fetch(`/account/general/${userId}/all`);
    if (!response.ok) {
        throw new Error('Failed to fetch account info')
    }
    return response.json();
};

// 유저의 해당 계좌에 대한 거래 내역 받아오기
export const fetchTransactionHistory = async (accountNo: string): Promise<Transaction> => {
    const response = await fetch(`/transaction/${accountNo}/history`);
    if (!response.ok) {
        throw new Error('Failed to fetch transaction history')
    }
    return response.json();
}