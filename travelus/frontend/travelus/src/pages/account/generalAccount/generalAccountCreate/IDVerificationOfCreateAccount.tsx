import React from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../../redux/store";
import { setMeetingAccountInfo, setTravelboxInfo } from "../../../../redux/meetingAccountSlice";
import { accountApi } from "../../../../api/account";

const IDVerificationOfCreateAccount = () => {
  const navigate = useNavigate();
  const params = useParams();
  const dispatch = useDispatch();
  const location = useLocation();
  const password = location.state.password;

  const guideText = [
    "신분증 앞면이 선명하게 보이도록 가로로 촬영해주세요.",
    "검정색 등 어두운 바탕 위에서 촬영해주세요.",
    "빛이 반사되지 않는 곳에서 촬영해주세요.",
    "훼손되지 않은 신분증을 촬영해주세요.",
  ];

  const handleNext = async () => {
    const data = {
      accountType: "I",
      accountPassword: password,
      bankId: 1,
    };

    console.log("입출금통장생성데이터", data);
    try {
      // 입출금통장 생성 통신
      const response = await accountApi.fetchCreateGeneralAccount(data);
      console.log(response.data);
      if (response.status === 201) {
        navigate(`/account/create/completed`);
      }
    } catch (error) {
      alert("입출금통장 개설에 실패했어요");
      navigate("/");
      console.log(error);
    }
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="mt-20 flex flex-col justify-center space-y-10">
        <div className="grid gap-3">
          <p className="text-2xl font-semibold">
            주민등록증 또는
            <br />
            운전면허증을 준비해주세요
          </p>
          <p className="text-[#565656]">통장을 만들기 위한 본인인증을 진행해주세요</p>
        </div>

        <img className="p-5" src="/assets/IDVerification.png" alt="" />

        <div className="px-5 grid gap-3">
          {guideText.map((text, index) => (
            <div className="flex items-center space-x-2" key={index}>
              <div className="w-5 aspect-1 bg-[#1429A0] rounded-full flex justify-center items-center">
                <p className="text-xs text-white ">{index + 1}</p>
              </div>
              <p className="text-xs text-[#565656]">{text}</p>
            </div>
          ))}
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleNext()}>
        신분증 촬영하기
      </button>
    </div>
  );
};

export default IDVerificationOfCreateAccount;
