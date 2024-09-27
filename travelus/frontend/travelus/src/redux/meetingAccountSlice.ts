import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { meetingAccountInfo, TravelboxInfo } from "../types/meetingAccount";

export interface meetingAccountState {
  meetingName: string;
  meetingType: string;
  meetingAccounInfo: meetingAccountInfo;
  travelboxInfo: TravelboxInfo;
}

const initialState: meetingAccountState = {
  meetingName: "",
  meetingType: "",
  meetingAccounInfo: {
    groupAccountPassword: "",
    groupName: "",
    icon: "",
  },
  travelboxInfo: {
    accountPassword: "",
    accountNo: "",
    currencyCode: "",
  },
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
    setMeetingAccountInfo: (state, action: PayloadAction<meetingAccountInfo>) => {
      state.meetingAccounInfo = action.payload;
    },
    setTravelboxInfo: (state, action: PayloadAction<TravelboxInfo>) => {
      state.travelboxInfo = action.payload;
    },
  },
});

export const { setMeetingName, setMeetingType, setMeetingAccountInfo, setTravelboxInfo } = meetingAccountSilce.actions;

export default meetingAccountSilce.reducer;