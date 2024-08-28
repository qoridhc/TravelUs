import React, { useEffect } from "react";
import { useNavigate } from "react-router";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../redux/store";
import { editMeetingAccountList } from "../redux/accountSlice";
import { Swiper, SwiperSlide } from "swiper/react";
import { Pagination } from "swiper/modules";
import { IoIosArrowForward } from "react-icons/io";
import MainMeetingAccount from "../components/mainpage/MainMeetingAccount";
import "../css/swiper.css";
import "swiper/css/pagination";
import "swiper/css";

const MainPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const meetingAccountList = useSelector((state: RootState) => state.account.meetingAccountList);

  useEffect(() => {
    dispatch(
      editMeetingAccountList([
        {
          meetingAccountName: "모히또에서 몰디브 한 잔하는 모임",
          meetingAccountIcon: "airplane",
          normalMeetingAccount: {
            accountNumber: "217-879928-13289",
            accountMoney: "3,481,900",
          },
          foreignMeetingAccount: {
            accountNumber: "212-123428-13289",
            accountMoney: "113,890",
            currencyType: "￥",
          },
        },
        {
          meetingAccountName: "신암고 1-3반 동창회",
          meetingAccountIcon: "school",
          normalMeetingAccount: {
            accountNumber: "217-874218-12289",
            accountMoney: "481,900",
          },
          foreignMeetingAccount: {
            accountNumber: "212-123902-09281",
            accountMoney: "390",
            currencyType: "$",
          },
        },
      ])
    );
  }, [dispatch]);

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
          </button>
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

        <div className="w-full flex flex-col items-center space-y-2">
          {/* 모임 통장 있을 시 표시 */}
          {userDetail[0].usermeetingAccount && (
            <Swiper
              pagination={{
                dynamicBullets: true,
              }}
              modules={[Pagination]}
              className="mainSwiper rounded-xl">
              {meetingAccountList.map((account, index) => (
                <SwiperSlide>
                  <MainMeetingAccount account={account} />
                </SwiperSlide>
              ))}
            </Swiper>
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
