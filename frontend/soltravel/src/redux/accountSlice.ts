import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { accountList, GeneralMeetingAccountDetail } from "../types/account";

export interface AccountState {
  isKeyboard: boolean;
  accountPassword: string;
  accountList: Array<accountList>;
  generalMeetingAccountDetail: GeneralMeetingAccountDetail;
}

const initialState: AccountState = {
  isKeyboard: false,
  accountPassword: "",
  accountList: [],
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
    editAccountList: (state, action: PayloadAction<Array<accountList>>) => {
      state.accountList = action.payload;
    },
    editGeneralMeetingAccountList: (state, action: PayloadAction<GeneralMeetingAccountDetail>) => {
      state.generalMeetingAccountDetail = action.payload;
    },
  },
});

export const { setIsKeyboard, setAccountPassword, editAccountList, editGeneralMeetingAccountList } = userSilce.actions;

export default userSilce.reducer;