import React from "react";
import { IoMdCheckmark } from "react-icons/io";
import { useNavigate } from "react-router";

const MeetingAccountCreateComplete = () => {
  const navigate = useNavigate();

  return (
    <div className="h-full p-5 grid grid-rows-3">
      <div className="row-start-2 flex flex-col justify-center items-center space-y-5">
        <div className="p-3 bg-[#0471E9] rounded-full">
          <IoMdCheckmark className="text-4xl text-white" />
        </div>

        <div className="text-lg text-center">
          <p className="font-semibold">해외여행 올인원 모임통장</p>
          <p>계좌 개설 완료되었습니다.</p>
        </div>

        <div className="w-full p-5 bg-[#EFEFF5] rounded-md flex justify-between">
          <p className="text-sm text-[#565656]">계좌번호</p>
          <p className="text-sm font-semibold">{"372849-382-1979387"}</p>
        </div>
      </div>

      <div className="w-full font-semibold row-start-3 flex flex-col justify-end space-y-3">
        <button className="w-full py-3 text-[#565656] border-2 rounded-lg" onClick={() => navigate("/")}>
          홈으로
        </button>
      </div>
    </div>
  );
};

export default MeetingAccountCreateComplete;
