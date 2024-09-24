import React, { useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import ExchangeRateInputMui from "../../components/travelBox/ExchangeRateInputMui";
import ExchangeAmmountInput from "../../components/travelBox/ExchangeAmmountInput";

const AutoCurrencyExchangeOfCreateTravelBox = () => {
  const navigate = useNavigate();

  const handleNext = () => {
    navigate("/passwordofcreatemeetingaccount/travelbox");
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between relative">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" />
        </div>

        <div className="grid gap-10">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">02</p>
              <p className="font-medium">트래블박스 개설</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>희망 환율이 되면</p>
              <p>자동으로 환전할게요</p>
            </div>
          </div>

          <ExchangeRateInputMui />
          <ExchangeAmmountInput />
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleNext()}>
        다음
      </button>
    </div>
  );
};

export default AutoCurrencyExchangeOfCreateTravelBox;
