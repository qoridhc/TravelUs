import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { DayHistoryDetail } from "../types/accountBook";

export interface AccountBookState {
  dayHistoryDetail: Array<DayHistoryDetail>;
}

const initialState: AccountBookState = {
  dayHistoryDetail: [],
};

export const accountBookSlice = createSlice({
  name: "accountBook",
  initialState,
  reducers: {
    setDayHistoryDetailList: (state, action: PayloadAction<Array<DayHistoryDetail>>) => {
      state.dayHistoryDetail = action.payload;
    },
  }
});

export const { setDayHistoryDetailList } = accountBookSlice.actions;

export default accountBookSlice.reducer;