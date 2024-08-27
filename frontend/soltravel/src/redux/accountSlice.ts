import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

export interface AccountState {
  isKeyboard?: boolean;
}

const initialState: AccountState = {
  isKeyboard: false,
};

export const userSilce = createSlice({
  name: "account",
  initialState,
  reducers: {
    setIsKeyboard: (state, action: PayloadAction<boolean>) => {
      state.isKeyboard = action.payload;
    },
  },
});

export const { setIsKeyboard } = userSilce.actions;

export default userSilce.reducer;