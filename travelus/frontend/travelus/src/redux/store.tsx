import { configureStore } from "@reduxjs/toolkit";
import accountReducer from "./accountSlice";
import accountBookReducer from "./accountBookSlice";

export const store = configureStore({
  reducer: {
    account: accountReducer,
    accountBook: accountBookReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;
