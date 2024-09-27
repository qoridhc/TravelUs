import React, { useEffect, useState } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import { MeetingAccountInfo } from "../../../types/account";
import { accountApi } from "../../../api/account";
import { IoIosArrowBack } from "react-icons/io";
import { IoPeopleSharp } from "react-icons/io5";
import { MdKeyboardArrowRight } from "react-icons/md";
import { IoCard } from "react-icons/io5";
import { AiFillSmile } from "react-icons/ai";

interface MeetingAccountManagementProps {
  // Define the props for your component here
}

const MeetingAccountManagement: React.FC<MeetingAccountManagementProps> = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const numId = Number(id);
  const [accountInfo, setAccountInfo] = useState<MeetingAccountInfo | null>(null);

  useEffect(() => {
    // 해당 모임 조회 API 호출
    const fetchSpecificMeetingAccount = async () => {
      try {
        const response = await accountApi.fetchSpecificMeetingAccount(numId);
        if (response.status === 200) {
          setAccountInfo(response.data);
        }
      } catch (error) {
        console.error("모임 조회 에러", error);
      }
    };

    fetchSpecificMeetingAccount();
  }, [id]);

  return (
    <div className="h-full">
      <div className="p-6 pb-8 flex flex-col space-y-8">
        <div className="grid grid-cols-3 items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate(`/meetingaccount/${numId}`);
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">관리</p>
        </div>

        <div className="flex flex-col space-y-2">
          <p className="text-lg font-semibold">{accountInfo?.groupName}</p>
          <p>계좌번호 · {accountInfo?.groupAccountNo}</p>
        </div>

        <div className="flex flex-col space-y-3">
          <div className="flex items-center space-x-4">
            <IoPeopleSharp className="text-3xl text-[#699BF7]" />
            <div className="w-full flex justify-between">
              <p className="text-zinc-500">모임원</p>
              <div
                onClick={() => {
                  navigate(`/meetingaccount/management/${accountInfo?.groupId}/groupmember`);
                }}
                className="flex items-center">
                <p>{accountInfo?.participants.length}명</p>
                <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
              </div>
            </div>
          </div>

          <div className="flex items-center space-x-4">
            <IoCard className="text-3xl text-[#1429A0]" />
            <div className="w-full flex justify-between">
              <p className="text-zinc-500">내 모임카드</p>
              <div className="flex items-center">
                <p className="text-[#0471E9]">발급하기</p>
                <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
              </div>
            </div>
          </div>

          <div className="flex items-center space-x-4">
            <AiFillSmile className="text-3xl text-[#fcd876]" />
            <div className="w-full flex justify-between">
              <p className="text-zinc-500">아이콘 · 모임통장명</p>
              <div className="flex items-center">
                <p className="text-[#0471E9]">변경하기</p>
                <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="w-full h-5 bg-[#F6F6F8]"></div>

      <div className="p-5">
        <button className="text-zinc-400">통장 해지하기</button>
      </div>
    </div>
  );
};

export default MeetingAccountManagement;
