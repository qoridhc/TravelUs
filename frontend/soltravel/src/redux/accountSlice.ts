import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

export interface AccountState {
  isKeyboard?: boolean;
  accountPassword?: string;
}

const initialState: AccountState = {
  isKeyboard: false,
  accountPassword: ""
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
    }
  },
});

export const { setIsKeyboard, setAccountPassword } = userSilce.actions;

export default userSilce.reducer;