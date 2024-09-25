import React, { useState } from "react";
import { IoIosArrowBack, IoIosCheckmarkCircle, IoIosCheckmarkCircleOutline } from "react-icons/io";
import { LuDot } from "react-icons/lu";
import { useNavigate } from "react-router";

const SelectSettlementAmount = () => {
  const navigate = useNavigate();
  const [isChecked, setIsChecked] = useState([false, false]);
  const checkInfo = [
    { text: "원화", coin: "KRW", amount: "20,500 원" },
    { text: "외화", coin: "USD", amount: "119.67 $" },
  ];
  const guideTextList = [
    "남은 원화는 일반 모임통장의 잔액이에요.",
    "남은 외화는 외화 모임통장의 잔액이에요.",
    "원화와 외화 함께 정산 시, 외화는 현재 환율을 적용하여 원화로 환전한 후 정산돼요.",
  ];

  const setChecked = (index: number) => {
    let temp = [...isChecked];
    temp[index] = !temp[index];
    setIsChecked(temp);
  };

  const handleSettlement = () => {
    navigate("/settlementforeigncurrencyexchange", { state: { isChecked: isChecked } });
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="grid grid-cols-3">
          <div className="flex items-center">
            <IoIosArrowBack className="text-2xl" />
          </div>
          <p className="text-lg text-center">정산하기</p>
        </div>

        <div className="grid gap-8">
          <p className="text-2xl font-semibold tracking-wide">
            정산할 금액을
            <br />
            선택해주세요
          </p>

          <div className="grid gap-2">
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
      </div>

      <div className="grid gap-5">
        <div className="grid gap-3">
          <p className="text-[#333D4B] font-semibold">정산 안내</p>

          {guideTextList.map((text, index) => (
            <div className="flex" key={index}>
              <LuDot className="text-lg text-[#8B95A1]" />
              <p className="w-[90%] text-sm text-[#4E5968] break-keep">{text}</p>
            </div>
          ))}
        </div>

        <button
          className={`w-full h-14 text-lg rounded-xl tracking-wide ${
            isChecked[0] || isChecked[1] ? "text-white bg-[#1429A0] " : "text-[#565656] bg-[#E3E4E4]"
          }`}
          onClick={() => handleSettlement()}
          disabled={!(isChecked[0] || isChecked[1])}>
          {isChecked[0] || isChecked[1] ? "정산하기" : "정산금을 선택해주세요"}
        </button>
      </div>
    </div>
  );
};

export default SelectSettlementAmount;
