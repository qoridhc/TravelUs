import React from "react";
import { useParams } from "react-router";
import { useNavigate } from "react-router";
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

const MeetingAccountDetail = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const numberId = Number(id);

  const meetingAccountList = useSelector((state: RootState) => state.account.meetingAccountList);

  return (
    <div className="w-full h-full pb-16 bg-[#EFEFF5]">
      <div className="p-5 flex flex-col bg-[#c3d8eb]">
        <div className="mb-12 flex space-x-2 items-center justify-start">
          <IoIosArrowBack
            onClick={() => {
              navigate(-1);
            }}
            className="text-2xl"
          />
          <p className="text-sm font-bold flex items-center">{meetingAccountList[numberId - 1].meetingAccountName}</p>
        </div>

        <div className="w-full flex flex-col items-center space-y-5">
          <div className="w-full p-2 flex justify-center space-x-2">
            <Swiper slidesPerView={4.7} spaceBetween={20} freeMode={true} modules={[FreeMode, Pagination]} className="userSwiper">
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-10" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-xs text-zinc-800">박예진</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-10" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-xs text-zinc-800">허동원</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-10" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-xs text-zinc-800">박민규</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-10" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-xs text-zinc-800">이성현</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-10" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-xs text-zinc-800">이진주</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-10" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-xs text-zinc-800">이예림</p>
                </div>
              </SwiperSlide>
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
        <AccountDetail />
      </div>
    </div>
  );
};

export default MeetingAccountDetail;
