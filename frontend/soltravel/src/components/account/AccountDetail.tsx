import React from "react";
import { useParams } from "react-router";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { useNavigate } from "react-router";
import { PiAirplaneTiltFill } from "react-icons/pi";
import { IoSchool } from "react-icons/io5";

const AccountDetail = () => {
  const navigate = useNavigate();

  const meetingAccountList = useSelector((state: RootState) => state.account.meetingAccountList);

  const { id } = useParams();
  const numberId = Number(id);

  return (
    <>
      <div
        // key={index}
        onClick={() => {
          navigate("/");
        }}
        className="w-full py-5 px-5 flex flex-col rounded-xl bg-white shadow-md">
        <div className="flex flex-col space-y-4">
          <div className="rounded-md flex justify-between">
            <div>
              <p className="text-sm font-bold">올인원 일반모임통장</p>
              <p className="text-sm text-zinc-500">
                {meetingAccountList[numberId - 1].normalMeetingAccount.accountNumber}
              </p>
            </div>
            <div className="flex items-center">
              <p className="text-[1.3rem] font-semibold">
                {meetingAccountList[numberId - 1].normalMeetingAccount.accountMoney}
              </p>
              <p className="text-[1rem]">원</p>
            </div>
          </div>
          <div>
            <button
              onClick={(e) => {
                e.stopPropagation();
                navigate("/exchange");
              }}
              className="w-full h-10 p-2 rounded-md bg-[#0046FF] text-white text-sm font-bold">
              환전하기
            </button>
          </div>
        </div>
      </div>

      <div
        // key={index}
        onClick={() => {
          navigate("/");
        }}
        className="w-full py-5 px-5 flex flex-col rounded-xl bg-white shadow-md">
        <div
          onClick={(e) => {
            e.stopPropagation();
            navigate("/foreignaccount");
          }}
          className="rounded-md flex justify-between mb-3">
          <div className="flex flex-col">
            <p className="text-sm font-bold">올인원 외화모임통장</p>
            <p className="text-sm text-zinc-500">
              {meetingAccountList[numberId - 1].foreignMeetingAccount.accountNumber}
            </p>
          </div>
          <div className="flex items-center space-x-[0.1rem]">
            <p className="text-[1.3rem] font-semibold">
              {meetingAccountList[numberId - 1].foreignMeetingAccount.accountMoney}
            </p>
            <p className="text-[1rem]">{meetingAccountList[numberId - 1].foreignMeetingAccount.currencyType}</p>
          </div>
        </div>
        <button
          onClick={(e) => {
            e.stopPropagation();
            navigate("/exchange");
          }}
          className="w-full h-10 p-2 rounded-md bg-[#0046FF] text-white text-sm font-bold">
          재환전하기
        </button>
      </div>
    </>
  );
};

export default AccountDetail;
