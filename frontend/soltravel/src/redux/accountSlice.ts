import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { AccountInfo, GeneralMeetingAccountDetail } from "../types/account";

export interface AccountState {
  isKeyboard: boolean;
  accountPassword: string;
  accountList: Array<AccountInfo>;
  foreingAccountList: Array<AccountInfo>;
  generalMeetingAccountDetail: GeneralMeetingAccountDetail;
}

const initialState: AccountState = {
  isKeyboard: false,
  accountPassword: "",
  accountList: [],
  foreingAccountList: [],
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
    editAccountList: (state, action: PayloadAction<Array<AccountInfo>>) => {
      state.accountList = action.payload;
    },
    editForeingAccountList: (state, action: PayloadAction<Array<AccountInfo>>) => {
      state.foreingAccountList = action.payload;
    },
    editGeneralMeetingAccountList: (state, action: PayloadAction<GeneralMeetingAccountDetail>) => {
      state.generalMeetingAccountDetail = action.payload;
    },
  },
});

export const { setIsKeyboard, setAccountPassword, editAccountList, editForeingAccountList, editGeneralMeetingAccountList } = userSilce.actions;

export default userSilce.reducer;