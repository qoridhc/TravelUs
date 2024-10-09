import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { RiErrorWarningFill } from "react-icons/ri";
import { RiExchangeDollarLine } from "react-icons/ri";
import { MeetingAccountDetailInfo } from "../../../types/account";
import { accountApi } from "../../../api/account";

const DeleteTravelBox = () => {
  const navigate = useNavigate();
  const { groupId } = useParams();
  const { accountNo } = useParams();
  const [account, setAccount] = useState<MeetingAccountDetailInfo | null>(null);

  useEffect(() => {
    if (!accountNo) return;

    fetchSpecificAccountInfo(accountNo);
  }, [accountNo]);

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccount(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  const handleNext = () => {
    navigate(`/meetingaccount/management/${groupId}`);
  };

  return (
    <div className="min-h-screen h-full flex flex-col">
      <div className="h-full flex flex-col justify-between">
        <div>
          <div className="p-5 flex flex-col space-y-5">
            <IoIosArrowBack
              onClick={() => {
                navigate(`/meetingaccount/management/${groupId}`);
              }}
              className="text-2xl"
            />
            <p className="font-bold text-xl">해지할 외화저금통을 확인해주세요</p>
          </div>

          <div className="p-5 space-y-5">
            <div className="">
              <p className="text-zinc-500">모임통장 정보</p>
              <p className="text-lg font-semibold">안구테러</p>
              <p className="text-zinc-600 font-semibold">100-133211-132</p>
            </div>

            <div className="">
              <p className="text-zinc-500">외화저금통 잔액</p>
              <p className="text-lg font-semibold">2,246.59$ (USD/$)</p>
            </div>

            <div className="flex items-center space-x-1">
              <RiErrorWarningFill className="text-[#F2B91E]" />
              <p className="text-xs text-red-600 font-semibold">외화저금통에 잔액이 있을 경우 해지할 수 없어요</p>
            </div>
          </div>

          <div className="w-full h-5 bg-[#F6F6F8]"></div>

          <div className="p-5 flex items-center space-x-1">
            <RiExchangeDollarLine className="text-lg text-[#27995a]" />
            <p className="font-semibold">먼저 재환전을 통해 외화저금통을 비워주세요</p>
          </div>
        </div>

        <div className="p-5 pb-8">
          <button className="w-full h-14 text-lg rounded-xl tracking-wide text-[#DD1F36] font-semibold bg-[#FFE4E5]">
            해지하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default DeleteTravelBox;
