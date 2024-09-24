import React from "react";
import { useNavigate } from "react-router-dom";
import { IoIosArrowBack } from "react-icons/io";
import { MdKeyboardArrowRight } from "react-icons/md";

interface Props {}

const MeetingAccountGroupMember: React.FC<Props> = (props) => {
  const navigate = useNavigate();
  const myId = 1;

  // 이름, 모임장 여부, 프로필 이미지를 가진 더미 데이터
  const memberList = [
    {
      userId: 1,
      name: "김철수",
      isLeader: true,
      profileImage: "",
    },
    {
      userId: 2,
      name: "이영희",
      isLeader: false,
      profileImage: "",
    },
  ];

  return (
    <div className="h-full flex flex-col justify-between">
      <div>
        <div className="p-6 pb-8 flex flex-col space-y-9">
          <div className="items-end">
            <IoIosArrowBack
              onClick={() => {
                navigate("/meetingaccount/management/1");
              }}
              className="text-2xl"
            />
          </div>

          <div className="flex flex-col space-y-5">
            <p className="text-lg font-semibold">사랑스러운 박씨네</p>
            <div>
              <p className="font-semibold">모임원</p>
              <p className="text-zinc-500">2명</p>
            </div>
          </div>
        </div>

        <div className="px-6 space-y-7">
          {memberList.map((member, index) => (
            <div key={index} className="flex items-center space-x-4">
              <img className="w-11" src="/assets/user/userIconSample.png" alt="유저아이콘" />
              <div className="w-full flex justify-between items-center">
                <div className="flex flex-col">
                  <p className="font-bold">{member.name}</p>
                  <div className="flex">
                    <p className="text-zinc-500">{myId === member.userId && "나"}</p>
                    <p className="text-zinc-500">{myId === member.userId && member.isLeader && "ﾠ·ﾠ"}</p>
                    <p className="text-zinc-500">{member.isLeader ? "모임장" : ""}</p>
                  </div>
                </div>
                <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="p-6 pb-8">
        <button className="w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold bg-[#1429A0]">초대하기</button>
      </div>
    </div>
  );
};

export default MeetingAccountGroupMember;
