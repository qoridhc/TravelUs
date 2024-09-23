import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router";
import { IoIosArrowBack } from "react-icons/io";

interface TransferConfirmProps {
  // Define the props for the component here
}

const TransferConfirm: React.FC<TransferConfirmProps> = (props) => {
  const navigate = useNavigate();
  const [isValidation, setIsValidation] = useState<boolean>(false);

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div>
          <IoIosArrowBack
            onClick={() => {
              navigate("/transfer/setmoney");
            }}
            className="text-2xl"
          />
        </div>
        <div className="mb-16 flex flex-col items-center">
          <p className="text-2xl font-bold">
            박예진
            <span className="font-normal"> 님에게</span>
          </p>
          <p className="text-2xl font-bold">2,000,000원</p>
          <p className="text-2xl font-bold">보낼까요?</p>
        </div>
        <div className="flex flex-col space-y-6">
          <div className="flex flex-col space-y-2">
            <div className="flex justify-between">
              <p className="text-zinc-500">받는 분에게 표시</p>
              <p>이예림</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">출금 계좌</p>
              <p>002-82791231-123</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">입금 계좌</p>
              <p>002-82789741-123</p>
            </div>
          </div>
          <div className="flex flex-col space-y-3">
            <button
              onClick={() => {
                navigate("/transfer/success");
              }}
              className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide text-white bg-[#1429A0]">
              보내기
            </button>
            <button
              onClick={() => {
                navigate("/transfer/setmoney");
              }}
              className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide">
              취소
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TransferConfirm;
