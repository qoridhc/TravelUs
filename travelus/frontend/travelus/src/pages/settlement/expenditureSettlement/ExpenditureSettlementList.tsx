import React, { useState } from "react";
import { IoIosArrowBack, IoIosArrowForward } from "react-icons/io";

const ExpenditureSettlementList = () => {
  const [isTab, setIsTab] = useState("ongoing"); // 진행 중 : ongoing, 완료 : done
  const settlementList = [
    { amount: 200, members: 3 },
    { amount: 100, members: 2 },
  ];

  return (
    <div className="h-full p-5">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" />
        </div>

        <div className="grid gap-10">
          <p className="text-2xl font-semibold">
            여행 동안의 개인 지출을
            <br />
            정산해주세요
          </p>

          <div className="text-center text-lg font-semibold grid grid-cols-2">
            <p
              className={`pb-1 ${isTab === "ongoing" ? "border-b-[3px] border-[#565656]" : "text-[#9e9e9e]"}`}
              onClick={() => setIsTab("ongoing")}>
              진행 중
            </p>
            <p
              className={`pb-1 ${isTab === "done" ? "border-b-[3px] border-[#565656]" : "text-[#9e9e9e]"}`}
              onClick={() => setIsTab("done")}>
              완료
            </p>
          </div>

          <div className="grid gap-5">
            <p className="text-[#565656] font-semibold">10월 4일</p>
            <div className="grid gap-8">
              {settlementList.map((settlement) => (
                <div className="grid grid-rows-2 grid-cols-[1fr_5fr_1fr] gap-y-3">
                  <div className="flex items-center">
                    <img className="w-10 h-10" src="/assets/user/userIconSample.png" alt="" />
                  </div>
                  <div>
                    <p className="text-xl font-bold tracking-tight">{settlement.amount}원</p>
                    <p className="font-[#565656]">발리가자의 총 {settlement.members}명</p>
                  </div>

                  <div className="flex justify-end items-center">
                    <IoIosArrowForward className="text-lg" />
                  </div>

                  <div className="col-start-2 col-span-2 flex items-center">
                    <button className="w-full p-3 text-white bg-[#1429A0] rounded-xl">100원 보내기</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ExpenditureSettlementList;
