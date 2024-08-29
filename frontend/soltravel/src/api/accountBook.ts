import api from "../lib/axios";
import { DayHistory, getAccountBookQuery } from "../types/accountBook";

export const accountBookApi = {
  // 월별 가계부 정보 가져오기
  fetchAccountBookInfo: (accountNo: string, data: getAccountBookQuery) => {
  return api.get(`/account-book/history/${accountNo}`, { params: data });
  }
}