import React from "react";
import { useNavigate } from "react-router";

const CompletedOfCreateMeetingAccount = () => {
  const navigate = useNavigate();

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
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="h-full mt-32 flex flex-col items-center space-y-5">
          <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />

          <div className="text-2xl flex flex-col justify-center">
            <div className="flex">
              <p>튜나뱅크&nbsp;</p>
              <p className="text-[#1429A0] font-semibold">모임통장</p>
              <p>이</p>
            </div>
            <p className="text-center">개설되었어요</p>
          </div>
        </div>

        <div className="flex flex-col space-y-3">
          <button
            className="w-full h-14 text-[#565656] bg-[#FAE100] rounded-lg flex justify-center items-center space-x-2"
            onClick={() => shareKakao()}>
            <img
              className="w-6 aspect-1"
              src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png"
              alt="카카오링크 보내기 버튼"
            />
            <p>카카오톡으로 초대장 공유하기</p>
          </button>

          <button
            onClick={() => {
              navigate("/");
            }}
            className="w-full h-14 text-lg rounded-xl tracking-wide">
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default CompletedOfCreateMeetingAccount;
