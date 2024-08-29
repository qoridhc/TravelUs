import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { MeetingAccountListDetail, GeneralMeetingAccountDetail } from "../types/account";

export interface AccountState {
  isKeyboard: boolean;
  accountPassword: string;
  meetingAccountList: Array<MeetingAccountListDetail>;
  generalMeetingAccountDetail: GeneralMeetingAccountDetail;
}

const initialState: AccountState = {
  isKeyboard: false,
  accountPassword: "",
  meetingAccountList: [],
  generalMeetingAccountDetail: {
    generalMeetingAccountName: "",
    generalMeetingAccountIcon: "",
    generalMeetingAccountUserName: "",
    generalMeetingAccountUserResidentNumber: "",
    generalMeetingAccountPassword: "",
    generalMeetingAccountMemberList: [],
  },
};

export const userSilce = createSlice({
  name: "account",
  initialState,
  reducers: {
    setIsKeyboard: (state, action: PayloadAction<boolean>) => {
      state.isKeyboard = action.payload;
    },
    setAccountPassword: (state, action: PayloadAction<string>) => {
      state.accountPassword = action.payload;
    },
    editMeetingAccountList: (state, action: PayloadAction<Array<MeetingAccountListDetail>>) => {
      state.meetingAccountList = action.payload;
    },
    editGeneralMeetingAccountList: (state, action: PayloadAction<GeneralMeetingAccountDetail>) => {
      state.generalMeetingAccountDetail = action.payload;
    },
  },
});

export const { setIsKeyboard, setAccountPassword, editMeetingAccountList, editGeneralMeetingAccountList } = userSilce.actions;

export default userSilce.reducer;