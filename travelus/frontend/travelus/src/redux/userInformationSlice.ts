import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { SignUpUserInfo, UserInfo } from "../types/userInformation";

export interface UserInformationState {
  SignUpUserInformation: SignUpUserInfo;
  UserInfo: UserInfo;
}

const initialState: UserInformationState = {
  SignUpUserInformation: {
    id: "",
    password: "",
    confirmPassword: "",
    name: "",
    birthday: "",
    phone: "",
    verificationCode: "",
  },
  UserInfo: {
    name: "",
    birth: "",
    phone: "",
    email: "",
    address: "",
    registerAt: "",
    isExit: false,
    userId: 0,
    userKey: null,
  },
};
    
export const userInfoSilce = createSlice({
  name: "userInformation",
  initialState,
  reducers: {
    editSignUpUserInformation: (state, action: PayloadAction<SignUpUserInfo>) => {
      state.SignUpUserInformation = action.payload;
    },
    editUserInformation: (state, action: PayloadAction<UserInfo>) => {
      state.UserInfo = action.payload;
    }
  },
});

export const { editSignUpUserInformation, editUserInformation } = userInfoSilce.actions;

export default userInfoSilce.reducer;