import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

export interface meetingAccountState {
  meetingName: string;
  meetingType: string;
}

const initialState: meetingAccountState = {
  meetingName: "",
  meetingType: "",
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
  },
});

export const { setMeetingName, setMeetingType } = meetingAccountSilce.actions;

export default meetingAccountSilce.reducer;