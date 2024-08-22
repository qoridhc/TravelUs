import React from "react";
import { IoIosArrowForward } from "react-icons/io";

const MainPage = () => {
  return (
    <div className="w-full h-full bg-[#EFEFF5]">
      <div className="w-full p-5 flex flex-col items-center space-y-4">
        <div className="w-full p-5 flex flex-col space-y-5 rounded-xl bg-white shadow-md">
          <div className="flex justify-between items-center">
            <div className="flex flex-col space-y-2">
              <p className="text-sm">모임통장과 외화통장을 한 번에</p>
              <div>
                <p className="text-lg font-bold">더 편한 환전</p>
                <p className="text-lg font-bold">해외여행 올인원 모임통장</p>
              </div>
            </div>

            <div>
              <img className="w-20" src="/assets/bankBookIcon.png" alt="올인원모임통장" />
            </div>
          </div>
          <button className="h-10 rounded-md bg-[#0046FF] font-bold text-white text-sm">신청하기</button>{" "}
        </div>

        <div className="w-full grid grid-cols-2 gap-5">
          <div className="w-full h-40 p-5 rounded-xl bg-white shadow-md flex flex-col justify-between items-start space-y-8">
            <img className="w-12" src="/assets/exchangeMoneyIcon.png" alt="환전아이콘" />
            <div>
              <p className="font-bold">수수료 없는</p>
              <p className="font-bold">환전하기</p>
            </div>
          </div>

          <div className="w-full h-40 p-5 rounded-xl bg-white shadow-md flex flex-col items-start space-y-8">
            <img className="w-12" src="/assets/creditCardIcon.png" alt="카드아이콘" />
            <div>
              <p className="font-bold">카드 신청하기</p>
            </div>
          </div>
        </div>

        <div className="w-full p-5 flex flex-col rounded-xl bg-white shadow-md">
          <div className="flex items-center space-x-1">
            <p className="text-lg font-bold flex justify-start">환율</p>
            <IoIosArrowForward className="text-[#565656]" />
          </div>
          <div className="flex justify-end">
            <p className="text-sm text-zinc-400">매매기준율 2024.08.22 17:20:00</p>
          </div>
          <div className="flex justify-between">
            <div className="p-3 flex flex-col justify-center space-y-2">
              <div className="flex justify-center items-center space-x-1">
                <img className="w-6 h-5 rounded-sm" src="/assets/flag/flagOfTheUnitedStates.png" alt="미국" />
                <p>USD</p>
              </div>
              <p className="text-lg font-semibold">1,335.90</p>
            </div>
            <div className="p-3 flex flex-col justify-center space-y-2">
              <div className="flex justify-center items-center space-x-1">
                <img className="w-6 h-5 rounded-sm border" src="/assets/flag/flagOfJapan.png" alt="미국" />
                <p>JPY</p>
              </div>
              <p className="text-lg font-semibold">918.65</p>
            </div>
            <div className="p-3 flex flex-col justify-center space-y-2">
              <div className="flex justify-center items-center space-x-1">
                <img className="w-6 h-5 rounded-sm" src="/assets/flag/flagOfEurope.png" alt="미국" />
                <p>EUR</p>
              </div>
              <p className="text-lg font-semibold">1,488.99</p>
            </div>
          </div>
          <button className="h-10 rounded-md bg-[#EAEAEA] font-bold text-sm">환전신청</button>{" "}
        </div>

        {/* <div className="w-full h-40 p-5 rounded-xl bg-white shadow-md flex flex-col items-start space-y-8">
            <img className="w-12" src="/assets/exchangeRateIcon.png" alt="환율아이콘" />
            <div>
              <p className="font-bold">환율 조회</p>
            </div>
          </div>

          <div className="w-full h-40 p-5 rounded-xl bg-white shadow-md flex flex-col items-start space-y-8">
            <img className="w-12" src="/assets/budgetIcon.png" alt="가계부아이콘" />
            <div>
              <p className="font-bold">가계부 조회</p>
            </div>
          </div> */}
      </div>
    </div>
  );
};

export default MainPage;
