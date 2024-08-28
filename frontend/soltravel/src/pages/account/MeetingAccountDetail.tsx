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
    <div className="w-full h-full p-5 pb-16 bg-[#EFEFF5]">
      <div className="mb-5 flex space-x-2 items-center justify-start">
        <IoIosArrowBack
          onClick={() => {
            navigate(-1);
          }}
          className="text-2xl"
        />
        {/* {meetingAccountList[numberId - 1].MeetingAccountIcon === "airplane" ? (
            <div className="w-6 h-6 bg-[#638ee4] rounded-full flex justify-center items-center">
              <PiAirplaneTiltFill className="text-zinc-50" />
            </div>
          ) : (
            <div className="w-6 h-6 bg-[#61bb9d] rounded-full flex justify-center items-center">
              <IoSchool className="text-zinc-50" />
            </div>
          )} */}
        <p className="font-bold flex items-center">{meetingAccountList[numberId - 1].MeetingAccountName}</p>
      </div>

      <div className="w-full flex flex-col items-center space-y-5">
        <div className="w-full flex flex-col space-y-1">
          <p className="text-sm font-bold">참여중인 모임원</p>
          <div className="w-full p-2 flex justify-start space-x-4">
            <Swiper
              slidesPerView={4.7}
              spaceBetween={30}
              freeMode={true}
              modules={[FreeMode, Pagination]}
              className="userSwiper">
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-16" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-sm">박예진</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-16" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-sm">허동원</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-16" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-sm">박민규</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-16" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-sm">이성현</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-16" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-sm">이진주</p>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="flex flex-col justify-center items-center space-y-1">
                  <img className="w-16" src="/assets/user/userIconSample.png" alt="" />
                  <p className="text-sm">이예림</p>
                </div>
              </SwiperSlide>
            </Swiper>
          </div>
          <hr className="border-zinc-200" />
        </div>
        <div className="w-full flex flex-col space-y-2">
          <AccountDetail />
        </div>
      </div>
    </div>
  );
};

export default MeetingAccountDetail;
