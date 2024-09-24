import React from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import NameInputMui from "../../components/meetingAccount/NameInputMui";
import BirthDateInputMui from "../../components/meetingAccount/BirthDateInputMui";
import GenderInputMui from "../../components/meetingAccount/GenderInputMui";

const UserInfoOfCreateAccount = () => {
  const navigate = useNavigate();
  const handleNext = () => {
    navigate("/passwordofcreatemeetingaccount/nomeeting");
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" />
        </div>

        <div className="grid gap-10">
          <div className="text-2xl font-semibold">
            <p>이예림님의 정보를</p>
            <p>확인해주세요</p>
          </div>

          <NameInputMui />
          <BirthDateInputMui />
          <GenderInputMui />
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleNext()}>
        다음
      </button>
    </div>
  );
};

export default UserInfoOfCreateAccount;
