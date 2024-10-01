import React from "react";
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { userApi } from "../../../api/user";
import LastNameInputMui from "../../../components/card/LastNameInputMui";
import FirstNameInputMui from "../../../components/card/FirstNameInputMui";

const EnglishNameOfCreateCard = () => {
  const navigate = useNavigate();
  const { groupId } = useParams();
  const [isLoading, setIsLoading] = useState(false);
  const [lastname, setLastName] = useState("");
  const [firstname, setFirstName] = useState("");

  const handleNext = () => {
    navigate(`/card/${groupId}/create/password/meeting`);
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate(`/meetingaccount/${groupId}`);
            }}
            className="text-2xl"
          />
        </div>

        <div className="grid gap-6">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">01</p>
              <p className="font-medium">모임카드 신청</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>영문 이름을 입력해주세요</p>
            </div>
          </div>

          <LastNameInputMui name={lastname} setName={setLastName} />
          <FirstNameInputMui name={firstname} setName={setFirstName} />
          <p className="text-sm text-zinc-400">해외에서 쓰려면 여권 이름과 같아야 해요.</p>
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

export default EnglishNameOfCreateCard;
