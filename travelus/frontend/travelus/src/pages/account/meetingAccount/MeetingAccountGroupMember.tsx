import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { accountApi } from "../../../api/account";
import { userApi } from "../../../api/user";
import { IoIosArrowBack } from "react-icons/io";
import { MdKeyboardArrowRight } from "react-icons/md";
import { MeetingAccountInfo } from "../../../types/account";

interface Props {}

const MeetingAccountGroupMember: React.FC<Props> = (props) => {
  const navigate = useNavigate();
  const { id } = useParams();
  const numId = Number(id);
  const [accountInfo, setAccountInfo] = useState<MeetingAccountInfo | null>(null);
  const [memberList, setMemberList] = useState<any[] | null>(null);
  const [userId, setUserId] = useState<number | null>(null);

  useEffect(() => {
    // 해당 모임 조회 API 호출
    const fetchSpecificMeetingAccount = async () => {
      try {
        const response = await accountApi.fetchSpecificMeetingAccount(numId);
        if (response.status === 200) {
          setAccountInfo(response.data);
          setMemberList(response.data.participants);
          console.log(response.data.participants);
        }
      } catch (error) {
        console.error("모임 조회 에러", error);
      }
    };

    fetchSpecificMeetingAccount();
  }, [id]);

  useEffect(() => {
    // 모임 참여자가 userId로 본인인지 확인
    const fetchUser = async () => {
      try {
        const response = await userApi.fetchUser();
        if (response.status === 200) {
          const myId = response.data.userId;
          setUserId(myId);
        }
      } catch (error) {
        console.error("모임원인지 확인 에러", error);
      }
    };

    fetchUser();
  }, [id]);

  const shareKakao = () => {
    window.Kakao.Link.sendCustom({
      templateId: 112239,
      templateArgs: {
        hostName: "이예림",
        groupName: "구미 2반 D209",
      },
    });
  };

  return (
    <div className="h-full flex flex-col justify-between">
      <div>
        <div className="p-6 pb-8 flex flex-col space-y-9">
          <div className="items-end">
            <IoIosArrowBack
              onClick={() => {
                navigate(`/meetingaccount/management/${id}`);
              }}
              className="text-2xl"
            />
          </div>

          <div className="flex flex-col space-y-5">
            <p className="text-lg font-semibold">{accountInfo?.groupName}</p>
            <div>
              <p className="font-semibold">모임원</p>
              <p className="text-zinc-500">{memberList?.length}명</p>
            </div>
          </div>
        </div>

        <div className="px-6 space-y-7">
          {memberList?.map((member, index) => (
            <div key={index} className="flex items-center space-x-4">
              <img className="w-11 rounded-full border border-zinc-100" src={member.profile} alt="유저아이콘" />
              <div className="w-full flex justify-between items-center">
                <div className="flex flex-col">
                  <p className="font-bold">{member.userName}</p>
                  <div className="flex">
                    <p className="text-zinc-500">{userId === member.userId && "나"}</p>
                    <p className="text-zinc-500">{userId === member.userId && member.master && "ﾠ·ﾠ"}</p>
                    <p className="text-zinc-500">{member.master ? "모임장" : ""}</p>
                  </div>
                </div>
                <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="p-6 pb-8">
        <button
          onClick={() => shareKakao()}
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold bg-[#1429A0]">
          초대하기
        </button>
      </div>
    </div>
  );
};

export default MeetingAccountGroupMember;
