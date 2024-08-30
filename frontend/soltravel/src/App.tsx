import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainPage from "../src/pages/MainPage";
import Login from "./pages/user/Login";
import SignUp from "./pages/user/SignUp";
import MyPage from "./pages/user/MyPage";
import UserUpdate from "./pages/user/UserUpdate";
import MeetingAccountList from "./pages/account/MeetingAccountList";
import MeetingAccountDetail from "./pages/account/MeetingAccountDetail";
import MyAccount from "./pages/viewaccount/MyAccount";
import GeneralAccount from "./pages/viewaccount/GeneralAccount";
import ForeignAccount from "./pages/viewaccount/ForeignAccount";
import ViewAccount from "./pages/viewaccount/Account";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";
import AccountCreate from "./pages/account/AccountCreate";
import ExchangeRate from "./pages/exchange/ExchangeRate";
import Exchange from "./pages/exchange/Exchange";
import SelectAccount from "./pages/exchange/SelectAccount";
import Settlement from "./pages/settle/Settlement";
import Detail from "./pages/viewaccount/Detail";
import GroupAccountPage from "./pages/viewaccount/ViewAccount";
import AccountCreateComplete from "./pages/account/AccountCreateComplete";
import GeneralMeetingAccountCreate from "./pages/account/GeneralMeetingAccountCreate";
import MeetingAccountCreatePrepare from "./pages/account/MeetingAccountCreatePrepare";
import ForeignMeetingAccountCreate from "./pages/account/ForeignMeetingAccountCreate";
import MeetingAccountCreateComplete from "./pages/account/MeetingAccountCreateComplete";
import AccountBookDetail from "./pages/accountBook/AccountBookDetail";
import Transaction from "./pages/transaction/Transaction"
import PrivateRoute from "./pages/user/PrivateRoute";

function App() {
  return (
    <div className="h-full">
      <BrowserRouter>
        <Routes>
          {/* 페이지에 Header와 Footer가 모두 포함된 경로 */}
          <Route
            path="/*"
            element={
              <>
                <Header />
                <div style={{ paddingBottom: "64px", backgroundColor: "#EFEFF5", minHeight: "100vh" }}>
                  <Routes>
                    <Route element={<PrivateRoute />}>
                      <Route path="/" element={<MainPage />} />
                      <Route path="/meetingaccountlist" element={<MeetingAccountList />} />
                      <Route path="/accountbookdetail" element={<AccountBookDetail />} />
                      {/* Add other protected routes here */}
                    </Route>
                  </Routes>
                </div>
                <Footer />
              </>
            }
          />

          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/userupdate" element={<UserUpdate />} />

          <Route path="/accountcreate" element={<AccountCreate />} />
          <Route path="/meetingaccountcreateprepare" element={<MeetingAccountCreatePrepare />} />
          <Route path="/generalmeetingaccountcreate" element={<GeneralMeetingAccountCreate />} />
          <Route path="/foreignmeetingaccountcreate" element={<ForeignMeetingAccountCreate />} />
          <Route path="/accountcreatecomplete" element={<AccountCreateComplete />} />
          <Route path="/meetingaccountcreatecomplete" element={<MeetingAccountCreateComplete />} />

          <Route path="/account/:userId" element={<GroupAccountPage />} />
          <Route path="/myaccount" element={<MyAccount />} />
          <Route path="/meetingaccount/:id" element={<MeetingAccountDetail />} />
          <Route path="/generalaccount" element={<GeneralAccount />} />
          <Route path="/foreignaccount" element={<ForeignAccount />} />
          <Route path="/account" element={<ViewAccount />} />

          <Route path="/exchangerate" element={<ExchangeRate />} />
          <Route path="/exchange" element={<Exchange />}></Route>
          <Route path="/selectaccount/:userId" element={<SelectAccount />}></Route>
          <Route path="/settlement" element={<Settlement />}></Route>
          <Route path="/detail" element={<Detail />}></Route>
          <Route path="/transaction" element={<Transaction />}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
