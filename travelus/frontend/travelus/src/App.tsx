import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainPage from "../src/pages/MainPage";
import Login from "./pages/user/Login";
import SignUp from "./pages/user/SignUp";
import SignUpBasicInformation from "./pages/user/SignUpBasicInformation";
import SignUpAddress from "./pages/user/SignUpAddress";
import MyPage from "./pages/user/MyPage";
import UserUpdate from "./pages/user/UserUpdate";
import UserPhoneUpdate from "./pages/user/UserPhoneUpdate";
import UserAddressUpdate from "./pages/user/UserAddressUpdate";
import UserPasswordUpdate from "./pages/user/UserPasswordUpdate";
import MeetingAccountList from "./pages/account/meetingAccount/MeetingAccountList";
import MeetingAccountDetail from "./pages/account/meetingAccount/MeetingAccountDetail";
import MeetingAccountManagement from "./pages/account/meetingAccount/MeetingAccountManagement";
import MeetingAccountGroupMember from "./pages/account/meetingAccount/MeetingAccountGroupMember";
import JoinedMeetingAccountDetail from "./pages/account/meetingAccount/JoinedMeetingAccountDetail";
import AccountTransaction from "./pages/account/AccountTransaction";
import AccountHistory from "./pages/accountHistory/AccountHistory";
import TransferSelectBank from "./pages/transfer/TransferSelectBank";
import TransferSetMoney from "./pages/transfer/TransferSetMoney";
import TransferPassword from "./pages/transfer/TransferPassword";
import TransferConfirm from "./pages/transfer/TransferConfirm";
import TransferSuccess from "./pages/transfer/TransferSuccess";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";
import AccountCreate from "./pages/ver1/AccountCreateOld";
import ExchangeRate from "./pages/exchange/ExchangeRate";
import ExchangeDetail from "./components/exchange/ExchangeDetail";
import Exchange from "./pages/exchange/Exchange";
import ExchangeKRWFlow from "./pages/exchange/ExchangeKRW";
import ExchangeCompletion from "./pages/exchange/ExchangeCompletion";
import AccountPasswordInput from "./pages/exchange/ExchangeConfirmation";
import SelectAccount from "./pages/exchange/SelectAccount";
import AccountCreateComplete from "./pages/ver1/AccountCreateComplete";
import GeneralMeetingAccountCreate from "./pages/ver1/GeneralMeetingAccountCreate";
import MeetingAccountCreatePrepare from "./pages/ver1/MeetingAccountCreatePrepare";
import MeetingAccountCreateComplete from "./pages/ver1/MeetingAccountCreateComplete";
import AccountBookDetail from "./pages/accountBook/AccountBookDetail";
import Transaction from "./pages/transaction/Transaction";
import PrivateRoute from "./pages/user/PrivateRoute";
import { Sign } from "crypto";
import EditMembers from "./pages/settlement/EditMembers";
import UserInfoOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/UserInfoOfCreateAccount";
import PasswordOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/PasswordOfCreateAccount";
import CheckPasswordOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/CheckPasswordOfCreateAccount";
import IDVerificationOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/IDVerificationOfCreateAccount";
import UserInfoOfCreateMeetingAccount from "./pages/meetingAccount/UserInfoOfCreateMeetingAccount";
import CompletedOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/CompletedOfCreateAccount";
import MeetingInfoOfCreateMeetingAccount from "./pages/meetingAccount/MeetingInfoOfCreateMeetingAccount";
import PasswordOfCreateMeetingAccount from "./pages/meetingAccount/PasswordOfCreateMeetingAccount";
import CheckPasswordOfCreateMeetingAccount from "./pages/meetingAccount/CheckPasswordOfCreateMeetingAccount";
import CompletedOfCreateMeetingAccount from "./pages/meetingAccount/CompletedOfCreateMeetingAccount";
import IDVerificationOfCreateMeetingAccount from "./pages/meetingAccount/IDVerificationOfCreateMeetingAccount";
import TravelBoxTransaction from "./pages/travelBox/TravelBoxTransactionDetail";
import MeetingTransaction from "./pages/meetingAccount/MeetingTransaction";
import CurrencyInfoOfCreateTravelBox from "./pages/travelBox/CurrencyInfoOfCreateTravelBox";
import AutoCurrencyExchangeOfCreateTravelBox from "./pages/travelBox/AutoCurrencyExchangeOfCreateTravelBox";
import SelectSettlementAmount from "./pages/settlement/balanceSettlement/SelectSettlementAmount";
import ForeignCurrencyExchange from "./pages/settlement/balanceSettlement/ForeignCurrencyExchange";
import BalanceSettlementCompleted from "./pages/settlement/balanceSettlement/BalanceSettlementCompleted";
import SettlementInfo from "./pages/settlement/balanceSettlement/SettlementInfo";
import ExpenditureTransactionDetail from "./pages/settlement/expenditureSettlement/ExpenditureTransactionDetail";
import ExpenditureSettlementInfo from "./pages/settlement/expenditureSettlement/ExpenditureSettlementInfo";
import ExpenditureSettlementCompleted from "./pages/settlement/expenditureSettlement/ExpenditureSettlementCompleted";
import TravelBoxCreatePrepare from "./pages/travelBox/TravelBoxCreatePrepare";
import InvitationOfMeeting from "./pages/meetingAccount/InvitationOfMeeting";
import InviteInfoOfMeeting from "./pages/meetingAccount/InviteInfoOfMeeting";
import AlreadyInviteOfMeeting from "./pages/meetingAccount/AlreadyInviteOfMeeting";
import SelectAccountOfMeeting from "./pages/meetingAccount/SelectAccountOfMeeting";
import EnglishNameOfCreateCard from "./pages/card/cardCreate/EnglishNameOfCreateCard";
import PasswordOfCreateCard from "./pages/card/cardCreate/PasswordOfCreateCard";
import CheckPasswordOfCreateCard from "./pages/card/cardCreate/CheckPasswordOfCreateCard";
import PasswordOfBalanceSettlement from "./pages/settlement/balanceSettlement/PasswordOfBalanceSettlement";

function App() {
  return (
    <div className="h-full">
      <BrowserRouter>
        <Routes>
          <Route
            path="/*"
            element={
              <>
                {/* 페이지에 Header와 Footer가 모두 포함된 경로 */}
                <Header />
                <div style={{ paddingBottom: "64px", backgroundColor: "#F3F4F6", minHeight: "100vh" }}>
                  <Routes>
                    <Route element={<PrivateRoute />}>
                      {/* 메인페이지 */}
                      <Route path="/" element={<MainPage />} />

                      {/* 모임통장 목록 */}
                      <Route path="/meetingaccountlist" element={<MeetingAccountList />} />

                      {/* 가계부 */}
                      <Route path="/accountbookdetail" element={<AccountBookDetail />} />

                      {/* Add other protected routes here */}
                    </Route>
                  </Routes>
                </div>
                <Footer />
              </>
            }
          />

          {/* 회원 */}
          <Route path="/login" element={<Login />} />
          {/* <Route path="/signup" element={<SignUp />} /> */}
          <Route path="/signup" element={<SignUpBasicInformation />} />
          <Route path="/signup/address" element={<SignUpAddress />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/userupdate" element={<UserUpdate />} />
          <Route path="/userupdate/phone" element={<UserPhoneUpdate />} />
          <Route path="/userupdate/address" element={<UserAddressUpdate />} />
          <Route path="/userupdate/password" element={<UserPasswordUpdate />} />

          {/* 입출금통장 생성 */}
          <Route path="/account/create/userinfo" element={<UserInfoOfCreateAccount />} />
          <Route path="/account/create/password" element={<PasswordOfCreateAccount />} />
          <Route path="/account/create/password/check" element={<CheckPasswordOfCreateAccount />} />
          <Route path="/account/create/idverification" element={<IDVerificationOfCreateAccount />} />
          <Route path="/account/create/completed" element={<CompletedOfCreateAccount />} />

          {/* 모임통장 생성 */}
          <Route path="/meeting/create/prepare" element={<MeetingAccountCreatePrepare />} />
          <Route path="/meeting/create/userinfo" element={<UserInfoOfCreateMeetingAccount />} />
          <Route path="/meeting/create/meetinginfo" element={<MeetingInfoOfCreateMeetingAccount />} />
          <Route path="/meeting/create/select/account" element={<SelectAccountOfMeeting />} />
          <Route path="/meeting/create/password/:type" element={<PasswordOfCreateMeetingAccount />} />
          <Route path="/meeting/create/password/check" element={<CheckPasswordOfCreateMeetingAccount />} />
          <Route path="/meeting/create/idverificationo/:type" element={<IDVerificationOfCreateMeetingAccount />} />
          <Route path="/meeting/create/completed/:type" element={<CompletedOfCreateMeetingAccount />} />

          {/* 모임통장 상세 */}
          <Route path="/meetingaccount/:id" element={<MeetingAccountDetail />} />
          <Route path="/joinedmeetingaccount/:id" element={<JoinedMeetingAccountDetail />} />
          <Route path="/accounthistory/:accountNo" element={<AccountHistory />} />
          <Route path="/meetingaccount/management/:id" element={<MeetingAccountManagement />} />
          <Route path="/meetingaccount/management/:id/groupmember" element={<MeetingAccountGroupMember />} />
          <Route path="/meeting/invite/:code" element={<InvitationOfMeeting />} />
          <Route path="/meeting/invite/:code/info" element={<InviteInfoOfMeeting />} />
          <Route path="/meeting/invite/participated" element={<AlreadyInviteOfMeeting />} />

          {/* 통장 내역 */}
          <Route path="/transaction/:accountNo" element={<AccountTransaction />} />
          <Route path="/meetingtransaction/:id" element={<MeetingTransaction />} />
          <Route path="/transaction/detail/travelbox/:id" element={<TravelBoxTransaction />} />

          {/* 트래블박스 생성 */}
          <Route path="/travelbox/create/prepare" element={<TravelBoxCreatePrepare />} />
          <Route path="/currencyinfoofcreatetravelbox" element={<CurrencyInfoOfCreateTravelBox />} />
          <Route path="/autocurrencyexchangeofcreatetravelbox" element={<AutoCurrencyExchangeOfCreateTravelBox />} />

          {/* 카드 개설 */}
          <Route path="/card/:groupId/create/englishname" element={<EnglishNameOfCreateCard />} />
          <Route path="/card/:groupId/create/password/:type" element={<PasswordOfCreateCard />} />
          <Route path="/card/:groupId/create/password/check" element={<CheckPasswordOfCreateCard />} />

          {/* 이체 */}
          <Route path="/transfer/selectbank" element={<TransferSelectBank />} />
          <Route path="/transfer/setmoney" element={<TransferSetMoney />} />
          <Route path="/transfer/password" element={<TransferPassword />} />
          <Route path="/transfer/confirm" element={<TransferConfirm />} />
          <Route path="/transfer/success" element={<TransferSuccess />} />

          {/* 환전 */}
          <Route path="/exchange/foreign-currency" element={<Exchange />}></Route>
          <Route path="/exchange/korean-currency" element={<ExchangeKRWFlow />} />
          <Route path="/exchange/account-password-input" element={<AccountPasswordInput />} />
          <Route path="/exchange/exchange-completion" element={<ExchangeCompletion />} />
          {/* <Route path="/selectaccount/:userId" element={<SelectAccount />}></Route> */}
          <Route path="/transaction" element={<Transaction />}></Route>

          {/* 환율 */}
          <Route path="/exchangerate" element={<ExchangeRate />} />
          <Route path="/exchangerate/:currencyCode" element={<ExchangeDetail />} />

          {/* 잔액정산 */}
          <Route path="/settlement/balance/amount/:id" element={<SelectSettlementAmount />}></Route>
          <Route path="/settlement/balance/foreigncurrency/exchange/:id" element={<ForeignCurrencyExchange />}></Route>
          <Route path="/settlement/balance/participants/:id" element={<SettlementInfo />}></Route>
          <Route path="/settlement/editmembers/:type/:id" element={<EditMembers />}></Route>
          <Route path="/settlement/password" element={<PasswordOfBalanceSettlement />}></Route>
          <Route path="/settlement/balance/completed" element={<BalanceSettlementCompleted />}></Route>

          {/* 지출정산 */}
          <Route
            path="/settlement/expenditure/transaction/detail/:id"
            element={<ExpenditureTransactionDetail />}></Route>
          <Route path="/settlement/expenditure/info" element={<ExpenditureSettlementInfo />}></Route>
          <Route path="/settlement/expenditure/completed" element={<ExpenditureSettlementCompleted />}></Route>

          {/* ver1 */}
          <Route path="/accountcreate" element={<AccountCreate />} />
          <Route path="/accountcreatecomplete" element={<AccountCreateComplete />} />
          <Route path="/generalmeetingaccountcreate" element={<GeneralMeetingAccountCreate />} />
          <Route path="/meetingaccountcreatecomplete" element={<MeetingAccountCreateComplete />} />
          {/* <Route path="/settlement" element={<Settlement />}></Route> */}
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
