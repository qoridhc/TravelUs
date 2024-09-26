import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

export interface meetingAccountState {
  meetingName: string;
  meetingType: string;
  individualAccountNo: string;
}

const initialState: meetingAccountState = {
  meetingName: "",
  meetingType: "",
  individualAccountNo: "",
}

export const meetingAccountSilce = createSlice({
  name: "meetingAccount",
  initialState,
  reducers: {
    setMeetingName: (state, action: PayloadAction<string>) => {
      state.meetingName = action.payload;
    },
    setMeetingType: (state, action: PayloadAction<string>) => {
      state.meetingType = action.payload;
    },
    setindividualAccountNo: (state, action: PayloadAction<string>) => {
      state.individualAccountNo = action.payload;
    },
  },
});

export const { setMeetingName, setMeetingType, setindividualAccountNo } = meetingAccountSilce.actions;

export default meetingAccountSilce.reducer;