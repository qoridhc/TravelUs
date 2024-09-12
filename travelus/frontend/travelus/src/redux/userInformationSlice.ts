import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { SignUpUserInfo } from "../types/userInformation";

export interface UserInformationState {
  SignUpUserInformation: SignUpUserInfo;
}

const initialState: UserInformationState = {
  SignUpUserInformation: {
    id: "",
    password: "",
    passwordConfirm: "",
    name: "",
    birthday: "",
  },
};
    
export const userInfoSilce = createSlice({
  name: "userInformation",
  initialState,
  reducers: {
    editSignUpUserInformation: (state, action: PayloadAction<SignUpUserInfo>) => {
      state.SignUpUserInformation = action.payload;
    }
  },
});

export const { editSignUpUserInformation } = userInfoSilce.actions;

export default userInfoSilce.reducer;