import { configureStore } from "@reduxjs/toolkit";
import accountReducer from "./accountSlice";
import accountBookReducer from "./accountBookSlice";
import userInformationReducer from "./userInformationSlice";

export const store = configureStore({
  reducer: {
    account: accountReducer,
    accountBook: accountBookReducer,
    userInformation: userInformationReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;
