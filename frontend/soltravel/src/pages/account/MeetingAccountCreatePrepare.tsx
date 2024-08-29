import React from "react";
import { RiHome5Line } from "react-icons/ri";
import { FaMoneyCheck } from "react-icons/fa";
import { FaMoneyCheckDollar } from "react-icons/fa6";
import { AiTwotoneExclamationCircle } from "react-icons/ai";
import { LuDot } from "react-icons/lu";
import { useNavigate } from "react-router";

const MeetingAccountCreateProcess = () => {
  const navigate = useNavigate();
  const noticeTextList = [
    "개설 전, 개인 입출금통장이 존재해야합니다.",
    "일반모임통장과 외화모임통장 모두를 개설해야합니다.",
    "일반모임통장만 가입이 불가합니다.",
    "외화모임통장만 가입이 불가합니다.",
  ];

  return (
    <div className="h-full flex flex-col justify-between">
      <div className="flex flex-col space-y-5">
        <div className="p-5 grid grid-cols-[1fr_8fr_1fr]">
          <div className="flex items-center">
            <RiHome5Line className="text-2xl" />
          </div>
          <p className="text-xl text-center font-semibold">계좌 개설 안내</p>
        </div>

        <div className="p-5 grid gap-8">
          <div className="text-2xl font-semibold">
            <p>해외여행 올인원 모임통장</p>
            <p>계좌개설에 대해 알려드립니다</p>
          </div>

          <div className="grid gap-5">
            <div className="flex space-x-5">
              <FaMoneyCheck className="mx-3 text-3xl text-[#565656]" />
              <div className="flex space-x-2">
                <p className="text-[#0471E9] font-semibold">01</p>
                <div>
                  <p className="font-semibold">일반모임통장 계좌개설</p>
                  <p className="text-sm text-[#565656]">원화(KRW)로 관리되는 모임통장</p>
                </div>
              </div>
            </div>

            <div className="flex space-x-5">
              <FaMoneyCheckDollar className="mx-3 text-3xl text-[#565656]" />
              <div className="flex space-x-2">
                <p className="text-[#0471E9] font-semibold">02</p>
                <div>
                  <p className="font-semibold">외화모임통장 계좌개설</p>
                  <p className="text-sm text-[#565656]">원화는 통화로 관리되는 모임통장</p>
                </div>
              </div>
            </div>

            <div className="flex flex-col space-y-2">
              <div className="flex items-center space-x-1">
                <AiTwotoneExclamationCircle />
                <p>알아두세요</p>
              </div>

              {noticeTextList.map((text, index) => (
                <div className="flex" key={index}>
                  <LuDot className="text-[#565656]" />
                  <p className="text-xs text-[#565656]">{text}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <div className="px-5 py-10">
        <button
          className={`w-full py-3 text-white bg-[#0471E9] rounded-lg`}
          onClick={() => navigate("/generalmeetingaccountcreate")}>
          가입하기
        </button>
      </div>
    </div>
  );
};

export default MeetingAccountCreateProcess;
