import React, { useState } from "react";
import DropdownInput from "../../components/accountBook/DropdownInput";
import MeetingAccount from "../../components/account/MeetingAccount";
import AccountBookCalendar from "../../components/accountBook/AccountBookCalendar";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";

const AccountBookDetail = () => {
  const [accountName, setAccountName] = useState("");
  const account = useSelector((state: RootState) => state.account.accountList);
  const foreignAccountList = useSelector((state: RootState) => state.account.foreignAccountList);

  return (
    <div>
      <div className="p-5 flex flex-col items-center space-y-8">
        <p className="w-full text-xl text-left font-bold">모임통장 가계부</p>

        <div className="w-full flex flex-col space-y-3">
          <DropdownInput selectedOption={accountName} onChange={setAccountName} />
          {accountName === "" ? (
            <></>
          ) : (
            <MeetingAccount index={0} account={account[1]} foreignAccount={foreignAccountList[0]} />
          )}
          <AccountBookCalendar />
        </div>
      </div>
    </div>
  );
};

export default AccountBookDetail;
