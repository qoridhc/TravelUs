import React from "react";
import { useNavigate } from "react-router";
import { IoSchool } from "react-icons/io5";
import { PiAirplaneTiltFill } from "react-icons/pi";
import path from "path";
import { AccountInfo } from "../../types/account";

interface Props {
  account: AccountInfo;
  foreignAccount: AccountInfo;
}

const MainMeetingAccount = ({ account, foreignAccount }: Props) => {
  const navigate = useNavigate();

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  return (
    <>
      {account && (
        <div
          onClick={() => {
            navigate(`/meetingaccount/${account.id}`);
          }}
          className="w-full py-5 pb-10 px-5 flex flex-col rounded-xl bg-white shadow-md">
          <div className="flex flex-col space-y-4">
            <div className="flex items-center space-x-1 mb-1">
              {account.iconName === "airplane" ? (
                <div className="w-6 h-6 bg-[#638ee4] rounded-full flex justify-center items-center">
                  <PiAirplaneTiltFill className="text-zinc-50" />
                </div>
              ) : (
                <div className="w-6 h-6 bg-[#61bb9d] rounded-full flex justify-center items-center">
                  <IoSchool className="text-zinc-50" />
                </div>
              )}
              <p className="font-bold">{account.groupName}</p>
            </div>
            <div className="rounded-md flex justify-between">
              <div>
                <p className="text-sm font-bold">올인원 일반모임통장</p>
                <p className="text-sm text-zinc-500">{account.accountNo}</p>
              </div>
              <div className="flex items-center space-x-1">
                <p className="text-[1.3rem] font-semibold">{formatCurrency(account.balance)}</p>
                <p className="text-[1rem]">원</p>
              </div>
            </div>
            <hr />
            {foreignAccount && (
              <div
                onClick={(e) => {
                  e.stopPropagation();
                  navigate("/foreignaccount");
                }}
                className="rounded-md flex justify-between">
                <div className="flex flex-col">
                  <p className="text-sm font-bold">올인원 외화모임통장</p>
                  <p className="text-sm text-zinc-500">{foreignAccount.accountNo}</p>
                </div>

                <div className="flex items-center space-x-1">
                  <p className="text-[1.3rem] font-semibold">{formatCurrency(foreignAccount.balance)}</p>
                  <p className="text-[1rem]">{foreignAccount.currency.currencyCode}</p>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </>
  );
};

export default MainMeetingAccount;
