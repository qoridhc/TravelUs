import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { Swiper, SwiperSlide } from "swiper/react";
import { FreeMode, Pagination } from "swiper/modules";
import "swiper/css";
import "swiper/css/free-mode";
import "swiper/css/pagination";
import { PiAirplaneTiltFill } from "react-icons/pi";
import { IoSchool } from "react-icons/io5";
import { IoIosArrowBack } from "react-icons/io";
import AccountDetail from "../../components/account/AccountDetail";
import { AccountInfo } from "../../types/account";

const MeetingAccountDetail = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [numberIndex, setNumberIndex] = useState(Number(id) + 1);

  const accountList = useSelector((state: RootState) => state.account.accountList);
  const foreignAccountList = useSelector((state: RootState) => state.account.foreignAccountList);
  const [selectedAccount, setSelectedAccount] = useState<AccountInfo | null>(null);
  const [selectedForeignAccount, setSelectedForeignAccount] = useState<AccountInfo | null>(null);

  useEffect(() => {
    if (accountList.length > 0 && !isNaN(numberIndex)) {
      // index가 범위 내에 있는지 확인
      if (numberIndex >= 0 && numberIndex < accountList.length) {
        setSelectedAccount(accountList[numberIndex]);
        setSelectedForeignAccount(foreignAccountList[numberIndex - 1]);
      } else {
        // index가 범위 외일 경우 처리 (예: 기본값 또는 에러 처리)
        setSelectedAccount(null);
        setSelectedForeignAccount(null);
      }
    }
  }, [accountList, numberIndex]); // accountList와 numberIndex에 의존

  if (selectedAccount === null) {
    return <p>계좌 정보를 불러오는 중입니다...</p>;
  }

  return (
    <>
      {accountList.length > 0 && foreignAccountList.length > 0 && (
        <div className="w-full h-full pb-16 bg-[#EFEFF5]">
          <div className="p-5 flex flex-col bg-[#c3d8eb]">
            <div className="mb-12 flex space-x-2 items-center justify-start">
              <IoIosArrowBack onClick={() => navigate(-1)} className="text-2xl cursor-pointer" />
              <p className="text-sm font-bold flex items-center">{selectedAccount.groupName}</p>
            </div>

            <div className="w-full flex flex-col items-center space-y-5">
              <div className="w-full p-2 flex justify-center space-x-2">
                <Swiper
                  slidesPerView={4.7}
                  spaceBetween={20}
                  freeMode={true}
                  modules={[FreeMode, Pagination]}
                  className="userSwiper">
                  {/* SwiperSlide 데이터는 실제로는 동적으로 제공되어야 할 부분 */}
                  {["박예진", "허동원", "박민규", "이성현", "이진주", "이예림"].map((name, index) => (
                    <SwiperSlide key={index}>
                      <div className="flex flex-col justify-center items-center space-y-1">
                        <img className="w-12" src="/assets/user/userIconSample.png" alt={name} />
                        <p className="text-xs text-zinc-800">{name}</p>
                      </div>
                    </SwiperSlide>
                  ))}
                </Swiper>
              </div>
            </div>
          </div>
          <div className="w-full p-5 flex flex-col">
            <div className="mb-3 flex items-center space-x-[9px]">
              <div className="w-6 h-6 bg-[#638ee4] rounded-full flex justify-center items-center">
                <PiAirplaneTiltFill className="text-zinc-50" />
              </div>
              <p className="text-sm text-zinc-800 font-bold">직장</p>
            </div>
            <hr className="mb-3 border-0 border-t-[0.5px] border-zinc-200" />
            <AccountDetail account={selectedAccount} foreignAccount={selectedForeignAccount} />
          </div>
        </div>
      )}
    </>
  );
};

export default MeetingAccountDetail;
