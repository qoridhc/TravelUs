import React from "react";
import { useNavigate } from "react-router";
import { IoIosArrowForward } from "react-icons/io";
import { PiAirplaneTiltFill } from "react-icons/pi";

const MainPage = () => {
  const navigate = useNavigate();

  const userDetail = [
    {
      userId: 1,
      userName: "허동원",
      userSavingAccount: {
        accountNumber: "217-473928-13289",
        accountMoney: "3,481,900",
      },
      usermeetingAccount: {
        accountNumber: "217-879928-13289",
        accountMoney: "4,734,910",
        travelBox: {
          boxMoney: "113,890",
          currencyType: "￥",
        },
      },
    },
  ];

  return (
    <div className="w-full pb-16 bg-[#EFEFF5]">
      <div className="w-full p-5 flex flex-col items-center space-y-4">
        {/* 모임통장 신청 */}
        <div className="w-full p-6 flex flex-col space-y-5 rounded-xl bg-white shadow-md">
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
          <button
            className="h-10 rounded-md bg-[#0046FF] font-bold text-white text-sm"
            onClick={() => {
              navigate("/accountcreate");
            }}>
            신청하기
          </button>{" "}
        </div>

        {/* 입출금 통장 있을 시 표시 */}
        {userDetail[0].userSavingAccount && (
          <div
            onClick={() => {
              navigate("/myaccount");
            }}
            className="w-full py-5 px-5 flex flex-col rounded-xl bg-white shadow-md">
            <div className="flex flex-col space-y-3">
              <div className="flex justify-between items-center">
                <div className="flex flex-col">
                  <p className="font-bold">올인원머니통장</p>
                  <p className="text-sm text-zinc-500">입출금 {userDetail[0].userSavingAccount.accountNumber}</p>
                </div>
              </div>
              <div className="flex items-center">
                <p className="text-[1.3rem] font-semibold">{userDetail[0].userSavingAccount.accountMoney}</p>
                <p className="text-[1rem]">원</p>
              </div>
              <hr />
            </div>
            <div className="flex justify-end mt-3">
              <button className="h-8 w-14 rounded-3xl bg-[#0046FF] font-bold text-white text-sm">이체</button>
            </div>
          </div>
        )}

        {/* 모임 통장 있을 시 표시 */}
        {userDetail[0].usermeetingAccount && (
          <div
            onClick={() => {
              navigate("/generalaccount");
            }}
            className="w-full py-5 px-5 flex flex-col rounded-xl bg-[rgb(186,203,238)] shadow-md">
            <div className="flex flex-col space-y-3">
              <div className="flex justify-between items-center">
                <div className="flex flex-col">
                  <div className="flex items-center space-x-1">
                    <div className="w-6 h-6 bg-[#8da5d6] rounded-full flex justify-center items-center">
                      <PiAirplaneTiltFill className="text-zinc-50" />
                    </div>
                    <p className="font-bold">해외여행 올인원모임통장</p>
                  </div>
                  <p className="text-sm text-zinc-500">{userDetail[0].usermeetingAccount.accountNumber}</p>
                </div>
              </div>
              <div className="flex items-center">
                <p className="text-[1.3rem] font-semibold">{userDetail[0].usermeetingAccount.accountMoney}</p>
                <p className="text-[1rem]">원</p>
              </div>
              <div className="flex justify-end">
                <button className="h-8 w-14 rounded-3xl bg-[#a4b6db] font-bold text-zinc-700 text-sm">이체</button>
              </div>
              <hr className="bg-[#0e1b38]" />
              <div
                onClick={(e) => {
                  e.stopPropagation();
                  navigate("/foreignaccount");
                }}
                className="flex justify-between items-center">
                <p className="text-sm font-bold">트래블월렛</p>
                {/* 트래블박스 가입했을 시 */}
                {userDetail[0].usermeetingAccount.travelBox ? (
                  <div className="flex items-center">
                    <p className="font-semibold">{userDetail[0].usermeetingAccount.travelBox.boxMoney}</p>
                    <p>{userDetail[0].usermeetingAccount.travelBox.currencyType}</p>
                  </div>
                ) : (
                  <div>
                    <p className="text-sm font-bold text-zinc-500">시작하기</p>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}

        {/* 환율 표시 */}
        <div className="w-full p-6 flex flex-col space-y-2 rounded-xl bg-white shadow-md">
          <div className="flex items-center space-x-1">
            <p className="text-md font-bold flex justify-start">환율</p>
            <IoIosArrowForward className="text-[#565656]" />
          </div>
          <div className="flex justify-end">
            <p className="text-sm text-zinc-400">매매기준율 2024.08.22 17:20:00</p>
          </div>
          <div className="flex justify-between items-center">
            <div className="w-24 p-1 flex flex-col justify-center items-center space-y-2">
              <div className="flex justify-center items-center space-x-1">
                <img className="w-6 h-5 rounded-sm" src="/assets/flag/flagOfTheUnitedStates.png" alt="미국" />
                <p>USD</p>
              </div>
              <p className="text-lg font-semibold">1,335.90</p>
            </div>
            <div className="w-[0.8px] h-14 bg-gray-300"></div>
            <div className="w-24 p-1 flex flex-col justify-center items-center space-y-2">
              <div className="flex justify-center items-center space-x-1">
                <img className="w-6 h-5 rounded-sm border" src="/assets/flag/flagOfJapan.png" alt="미국" />
                <p>JPY</p>
              </div>
              <p className="text-lg font-semibold">918.65</p>
            </div>
            <div className="w-[0.8px] h-14 bg-gray-300"></div>
            <div className="w-24 p-1 flex flex-col justify-center items-center space-y-2">
              <div className="flex justify-center items-center space-x-1">
                <img className="w-6 h-5 rounded-sm" src="/assets/flag/flagOfEurope.png" alt="미국" />
                <p>EUR</p>
              </div>
              <p className="text-lg font-semibold">1,488.99</p>
            </div>
          </div>
          <button
            onClick={(e) => {
              navigate("/exchange");
              e.stopPropagation();
            }}
            className="h-10 rounded-md bg-[#EAEAEA] font-bold text-sm">
            환전신청
          </button>{" "}
        </div>

        {/* 가계부 */}
        <div className="w-full p-6 flex flex-col space-y-3 rounded-xl bg-blue-500 shadow-md">
          <div className="flex items-center space-x-1">
            <p className="text-md text-white font-bold flex justify-start">가계부</p>
            <IoIosArrowForward className="text-white" />
          </div>
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-8">
              <img className="w-12" src="/assets/budgetIcon.png" alt="가계부아이콘" />
              <div className="flex flex-col">
                <p className="text-zinc-200 font-semibold text-sm">이번 여행</p>
                <p className="text-zinc-200 font-semibold text-sm">가계부 확인하기</p>
              </div>
            </div>
            <IoIosArrowForward className="text-white" />
          </div>
        </div>

        {/* 환전, 카드 신청 */}
        <div className="w-full grid grid-cols-2 gap-5">
          <div
            onClick={() => {
              navigate("/exchange");
            }}
            className="w-full h-40 p-5 rounded-xl bg-white shadow-md flex flex-col justify-between items-start space-y-8">
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
      </div>
    </div>
  );
};

export default MainPage;
