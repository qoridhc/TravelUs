import React, { useState } from "react";
import { IoIosArrowBack, IoIosCheckmarkCircle, IoIosCheckmarkCircleOutline } from "react-icons/io";
import { LuDot } from "react-icons/lu";

const SelectSettlementAmount = () => {
  const [isChecked, setIsChecked] = useState([false, false]);
  const checkInfo = [
    { text: "원화", coin: "KRW", amount: "20,500 원" },
    { text: "외화", coin: "USD", amount: "119.67 $" },
  ];
  const guideText = [
    "남은 원화는 일반 모임통장의 잔액이에요.",
    "남은 외화는 외화 모임통장의 잔액이에요.",
    "원화와 외화 함께 정산 시, 외화는 현재 환율을 적용하여 원화로 환전한 후 정산돼요.",
  ];

  const setChecked = (index: number) => {
    let temp = [...isChecked];
    temp[index] = !temp[index];
    setIsChecked(temp);
  };

  return (
    <div className="h-full p-5 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="grid grid-cols-3">
          <div className="flex items-center">
            <IoIosArrowBack className="text-2xl" />
          </div>
          <p className="text-lg text-center">정산하기</p>
        </div>

        <div className="grid gap-5">
          <p className="text-2xl font-semibold tracking-wide">
            정산할 금액을
            <br />
            선택해주세요
          </p>

          {checkInfo.map((info, index) => (
            <div className="flex justify-between items-center">
              <div className="flex items-center space-x-2">
                <button onClick={() => setChecked(index)}>
                  {isChecked[index] ? (
                    <IoIosCheckmarkCircle className="text-3xl text-[#1429A0]" />
                  ) : (
                    <IoIosCheckmarkCircleOutline className="text-3xl text-[#cccccc]" />
                  )}
                </button>
                <p>
                  남은 {info.text} / {info.coin}
                </p>
              </div>

              <p>{info.amount}</p>
            </div>
          ))}
        </div>
      </div>

      <div className="grid gap-3">
        <div className="text-[#9e9e9e] grid gap-1">
          <p className="text-sm">튜나뱅크 정산 안내</p>

          {guideText.map((text) => (
            <p className="text-xs flex">
              <LuDot />
              {text}
            </p>
          ))}
        </div>

        <button
          className={`w-full h-16 rounded-xl tracking-wider ${
            isChecked[0] || isChecked[1] ? "text-white bg-[#1429A0] " : "text-[#565656] bg-[#E3E4E4]"
          }`}>
          {isChecked[0] || isChecked[1] ? "정산하기" : "정산금을 선택해주세요"}
        </button>
      </div>
    </div>
  );
};

export default SelectSettlementAmount;
