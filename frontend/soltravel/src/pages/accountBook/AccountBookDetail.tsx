import React, { useState } from "react";
import DropdownInput from "../../components/accountBook/DropdownInput";
import MeetingAccount from "../../components/account/MeetingAccount";
import AccountBookCalendar from "../../components/accountBook/AccountBookCalendar";

const AccountBookDetail = () => {
  const [accountName, setAccountName] = useState("");
  // const accountDetail = {
  //   id: 1,
  //   meetingAccountName: "모히또에서 몰디브 한 잔하는 모임",
  //   meetingAccountIcon: "airplane",
  //   normalMeetingAccount: {
  //     accountNumber: "217-879928-13289",
  //     accountMoney: "3,481,900",
  //   },
  //   foreignMeetingAccount: {
  //     accountNumber: "212-123428-13289",
  //     accountMoney: "113,890",
  //     currencyType: "￥",
  //   },
  // };

  return (
    <div>
      <div className="p-5 flex flex-col items-center space-y-8">
        <p className="w-full text-xl text-left font-bold">모임통장 가계부</p>

        <div className="w-full flex flex-col space-y-3">
          <DropdownInput selectedOption={accountName} onChange={setAccountName} />
          {/* <MeetingAccount account={accountDetail} /> */}
          <AccountBookCalendar />
        </div>
      </div>
    </div>
  );
};

export default AccountBookDetail;
