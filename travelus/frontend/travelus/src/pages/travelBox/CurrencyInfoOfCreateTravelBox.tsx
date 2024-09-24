import React from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import CurrencyInputMui from "../../components/travelBox/CurrencyInputMui";

const CurrencyInfoOfCreateTravelBox = () => {
  const navigate = useNavigate();
  const handleNext = () => {
    navigate("/meetinginfoofcreatemeetingaccount");
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
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
              <p>통화를</p>
              <p>선택해주세요</p>
            </div>
          </div>

          <CurrencyInputMui />
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

export default CurrencyInfoOfCreateTravelBox;
