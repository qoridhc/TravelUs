import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import SecurityNumberKeyboard from "../../../components/common/SecurityNumberKeyboard";
import { accountApi } from "../../../api/account";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../types/axiosError";
import { MeetingAccountInfo, MeetingAccountDetailInfo } from "../../../types/account";
import { set } from "date-fns";

const PasswordOfCreateCard = () => {
  const navigate = useNavigate();
  const { groupId } = useParams();
  const { type } = useParams();
  const [password, setPassword] = useState("");
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [account, setAccount] = useState<MeetingAccountDetailInfo | null>(null);

  useEffect(() => {
    if (password.length === 4) {
      if (type === "meeting") {
        if (account?.accountPassword !== password) {
          setPassword("");
          alert("모임통장 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
          return;
        } else {
          setPassword("");
          navigate(`/card/${groupId}/create/password/card`);
        }
      } else if (type === "card") {
        navigate(`/card/${groupId}/create/password/check`, { state: { originalPassword: password } });
      }
    }
  }, [password]);

  useEffect(() => {
    if (groupId && type === "meeting") {
      fetchSpecificMeetingAccount();
    }
  }, [groupId]);

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
      if (response.status === 200) {
        const meetingData = response.data;

        setMeeting(meetingData);

        // 모임 조회 성공 시, 바로 통장 정보 조회
        if (meetingData?.groupAccountNo) {
          fetchSpecificAccountInfo(meetingData.groupAccountNo);
        } else {
          console.log("groupAccountNo가 없습니다.");
        }
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    }
  };

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccount(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          {type === "meeting" ? "카드를 개설할 모임통장의" : "모임 카드에서 사용할"}
          <br />
          비밀번호를 입력해주세요
        </p>

        <div className="flex space-x-3">
          {[...Array(4)].map((_, index) => (
            <div
              className={`w-4 aspect-1 ${index < password.length ? "bg-[#565656]" : "bg-[#D9D9D9]"} rounded-full`}
              key={index}></div>
          ))}
        </div>
      </div>

      <SecurityNumberKeyboard password={password} setPassword={setPassword} />
    </div>
  );
};

export default PasswordOfCreateCard;
