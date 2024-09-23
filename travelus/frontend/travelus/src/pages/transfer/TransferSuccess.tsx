import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router";
import { IoIosArrowBack } from "react-icons/io";

interface TransferSuccessProps {
  // Define the props for the component here
}

const TransferSuccess: React.FC<TransferSuccessProps> = (props) => {
  const navigate = useNavigate();

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="h-full flex flex-col justify-center items-center space-y-5">
          <img src="/assets/confirmIcon.png" alt="확인아이콘" />
          <div>
            <p className="text-xl font-bold text-center">
              박예진
              <span className="font-normal"> 님에게</span>
            </p>
            <p className="text-xl font-bold text-center">2,000,000원을</p>
            <p className="text-xl font-bold text-center">보냈어요</p>
          </div>
        </div>
        <div className="flex flex-col space-y-6">
          <button
            onClick={() => {
              navigate("/");
            }}
            className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide text-white bg-[#1429A0]">
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default TransferSuccess;
