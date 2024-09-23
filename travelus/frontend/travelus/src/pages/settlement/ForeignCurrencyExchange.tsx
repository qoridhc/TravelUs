import React, { useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { TiArrowUnsorted } from "react-icons/ti";
import { useNavigate } from "react-router";

const ForeignCurrencyExchange = () => {
  const navigate = useNavigate();

  const [usdAmount, setUsdAmount] = useState(0);

  const handleExchange = () => {
    navigate("/settlement");
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="grid grid-cols-3">
          <div className="flex items-center">
            <IoIosArrowBack className="text-2xl" />
          </div>
          <p className="text-lg text-center">재환전</p>
        </div>

        <div className="grid gap-8">
          <p className="text-2xl font-semibold tracking-wide">
            남은 외화는
            <br />
            원화로 바꿔주세요
          </p>

          <div className="grid gap-3">
            <div className="flex justify-between">
              <p className="text-lg font-semibold">원화로 바꾸기</p>
              <p className="text-sm text-[#515151]">1 USD = 1343.98 원</p>
            </div>

            <div className="flex flex-col items-center">
              {/* 외화정보 */}
              <div className="p-5 bg-[#F3F4F6] rounded-xl grid grid-cols-2">
                <div className="grid gap-1">
                  <div className="flex items-center space-x-2">
                    <img className="w-6 h-4 border" src="/assets/flag/flagOfTheUnitedStates.png" alt="" />
                    <p>미국 달러</p>
                  </div>

                  <p className="text-sm text-[#555555]">잔액 119.67 USD</p>
                </div>

                <div className="flex justify-end items-center space-x-2">
                  <input
                    type="number"
                    className="w-2/3 text-right bg-[#F3F4F6] outline-none placeholder:text-black"
                    placeholder="0"
                    onChange={(e) => setUsdAmount(parseFloat(e.target.value))}
                  />
                  <p>USD</p>
                </div>
              </div>

              {/* 양방향 화살표 아이콘 */}
              <div className="w-8 h-8 bg-white shadow-md border-[0.5px] border-[#cccccc] rounded-full flex justify-center items-center">
                <TiArrowUnsorted className="text-sm text-[#999999]" />
              </div>

              {/* 원화정보 */}
              <div className="p-5 bg-[#F3F4F6] rounded-xl grid grid-cols-2">
                <div className="flex items-center space-x-2">
                  <img className="w-6 h-4 border" src="/assets/flag/flagOfKorea.jpg" alt="" />
                  <p>대한민국 원</p>
                </div>

                <div className="text-[#949494] flex justify-end items-center space-x-2">
                  <input
                    type="number"
                    className="w-2/3 text-right bg-[#F3F4F6] outline-none"
                    value={Math.floor(1343.98 * usdAmount)}
                  />
                  <p>KRW</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleExchange()}>
        재환전
      </button>
    </div>
  );
};

export default ForeignCurrencyExchange;
