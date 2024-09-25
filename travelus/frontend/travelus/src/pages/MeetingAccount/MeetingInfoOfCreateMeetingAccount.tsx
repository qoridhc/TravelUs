import React, { useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import MeetingNameInputMui from "../../components/meetingAccount/MeetingNameInputMui";
import MeetingTypeInputMui from "../../components/meetingAccount/MeetingTypeInputMui";
import { useNavigate } from "react-router";
import { useDispatch } from "react-redux";
import { setMeetingName, setMeetingType } from "../../redux/meetingAccountSlice";

const MeetingInfoOfCreateMeetingAccount = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [menuStep, setMenuStep] = useState(1); // 1 : 모임명, 2 : 모임종류
  const guideText = [
    ["모임명을", "입력해주세요"],
    ["모임종류를", "선택해주세요"],
  ];

  const [meetingName, setMeetingName] = useState("");
  const [meetingType, setMeetingType] = useState("");

  const handleNext = () => {
    // dispatch(setMeetingName(meetingName));
    navigate("/meeting/create/password/meeting");
  };

  const handleNextStep = () => {
    if (menuStep < guideText.length) {
      setMenuStep(menuStep + 1);
    }
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" />
        </div>

        <div className="grid gap-10">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">01</p>
              <p className="font-medium">모임통장 개설</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>{guideText[menuStep - 1][0]}</p>
              <p>{guideText[menuStep - 1][1]}</p>
            </div>
          </div>

          {menuStep >= 2 ? <MeetingTypeInputMui meetingType={meetingName} setMeetingType={setMeetingName} /> : <></>}
          {menuStep >= 1 ? (
            <MeetingNameInputMui
              meetingName={meetingType}
              setMeetingName={setMeetingType}
              onInputComplete={handleNextStep}
            />
          ) : (
            <></>
          )}
        </div>
      </div>

      <button
        className={`w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0] ${
          meetingName === "" || meetingType === "" ? "text-[#565656] bg-[#E3E4E4]" : "text-white bg-[#1429A0]"
        }`}
        onClick={() => handleNext()}
        disabled={meetingName === "" || meetingType === ""}>
        다음
      </button>
    </div>
  );
};

export default MeetingInfoOfCreateMeetingAccount;
