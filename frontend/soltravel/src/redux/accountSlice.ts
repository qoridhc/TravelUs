import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

export interface AccountState {
  isKeyboard?: boolean;
  accountPassword?: string;
  meetingAccountList: Array<{
    id: number;
    MeetingAccountName: string;
    MeetingAccountIcon: string;
    normalMeetingAccount: {
      accountNumber: string;
      accountMoney: string;
    };
    foreignMeetingAccount: {
      accountNumber: string;
      accountMoney: string;
      currencyType: string;
    };
  }>;
}

const initialState: AccountState = {
  isKeyboard: false,
  accountPassword: "",
  meetingAccountList: [
    {
      id: 1,
      MeetingAccountName: "모히또에서 몰디브 한 잔하는 모임",
      MeetingAccountIcon: "airplane",
      normalMeetingAccount: {
        accountNumber: "217-879928-13289",
        accountMoney: "3,481,900",
      },
      foreignMeetingAccount: {
        accountNumber: "212-123428-13289",
        accountMoney: "113,890",
        currencyType: "￥",
      },
    },
    {
      id: 2,
      MeetingAccountName: "신암고 1-3반 동창회",
      MeetingAccountIcon: "school",
      normalMeetingAccount: {
        accountNumber: "217-874218-12289",
        accountMoney: "481,900",
      },
      foreignMeetingAccount: {
        accountNumber: "212-123902-09281",
        accountMoney: "390",
        currencyType: "$",
      },
    },
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
    setMeetingAccountList: (state, action: PayloadAction<typeof initialState.meetingAccountList>) => {
      state.meetingAccountList = action.payload;
    },
  },
});

export const { setIsKeyboard, setMeetingAccountList, setAccountPassword } = userSilce.actions;

export default userSilce.reducer;