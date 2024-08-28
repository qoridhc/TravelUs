import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { MeetingForeignAccount } from "../types/account";

export interface AccountState {
  isKeyboard?: boolean;
  accountPassword?: string;
  meetingAccountList: Array<MeetingForeignAccount>;
}

const initialState: AccountState = {
  isKeyboard: false,
  accountPassword: "",
  meetingAccountList: [
  ],
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
    editMeetingAccountList: (state, action: PayloadAction<Array<MeetingForeignAccount>>) => {
      state.meetingAccountList = action.payload;
    },
  },
});

export const { setIsKeyboard, editMeetingAccountList, setAccountPassword } = userSilce.actions;

export default userSilce.reducer;