import React, { useState } from "react";
import DropdownInput from "../../components/accountBook/DropdownInput";

const AccountBookDetail = () => {
  const [accountName, setAccountName] = useState("");

  return (
    <div className="pb-16 bg-[#EFEFF5]">
      <div className="w-full p-5 flex flex-col items-center space-y-8">
        <p className="w-full text-xl text-left font-bold">모임통장 가계부</p>

        <DropdownInput selectedOption={accountName} onChange={setAccountName} />
      </div>
    </div>
  );
};

export default AccountBookDetail;
