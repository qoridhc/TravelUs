import React, { useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import Lottie from "lottie-react";
import invitationAnimation from "../../lottie/invitationAnimation.json";
import { useNavigate, useParams } from "react-router";

const InvitationOfMeeting = () => {
  const navigate = useNavigate();
  const params = useParams();
  const [groupLeader, setGroupLeader] = useState("이예림");

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="mt-32 flex flex-col items-center space-y-3">
        <Lottie className="w-36" animationData={invitationAnimation} />
        <div className="text-2xl text-center font-semibold">
          <p>{groupLeader}님의 모임통장</p>
          <p>초대장 도착!</p>
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => navigate("/login", { state: { type: "invite", code: params.code } })}>
        로그인
      </button>
    </div>
  );
};

export default InvitationOfMeeting;
