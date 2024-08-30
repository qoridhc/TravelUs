import React, { useEffect, useState } from "react";
import DropdownInput from "../../components/accountBook/DropdownInput";
import MeetingAccount from "../../components/account/MeetingAccount";
import AccountBookCalendar from "../../components/accountBook/AccountBookCalendar";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import AccountBookDayDetail from "../../components/accountBook/AccountBookDayDetail";

const AccountBookDetail = () => {
  const [accountName, setAccountName] = useState("");
  const [accountIndex, setAccountIndex] = useState<number | null>(null);
  const [accountNo, setAccountNo] = useState("");

  const accountList = useSelector((state: RootState) => state.account.accountList);
  const foreignAccountList = useSelector((state: RootState) => state.account.foreignAccountList);

  const handleDropdownChange = (accountName: string, accountIndex: number, accountNo: string) => {
    setAccountName(accountName);
    setAccountIndex(accountIndex);
    setAccountNo(accountNo);
  };

  return (
    <div>
      <div className="p-5 flex flex-col items-center space-y-8">
        <p className="w-full text-xl text-left font-bold">모임통장 가계부</p>
        <div className="w-full flex flex-col space-y-3">
          <DropdownInput accountList={accountList} selectedOption={accountName} onChange={handleDropdownChange} />
          {accountName === "" || accountIndex === null ? (
            <></>
          ) : (
            <MeetingAccount
              index={accountIndex ? accountIndex - 1 : -1}
              account={accountList[accountIndex]}
              foreignAccount={foreignAccountList[accountIndex - 1]}
            />
          )}
          <AccountBookCalendar accountNo={accountNo} />
          <AccountBookDayDetail />
        </div>
      </div>
    </div>
  );
};

export default AccountBookDetail;
