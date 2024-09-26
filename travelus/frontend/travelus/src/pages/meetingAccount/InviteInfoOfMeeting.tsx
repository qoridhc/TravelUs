import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate, useParams } from "react-router";
import AccountListInputMui from "../../components/meetingAccount/AccountListInputMui";
import { AiTwotoneExclamationCircle } from "react-icons/ai";
import { LuDot } from "react-icons/lu";
import { accountApi } from "../../api/account";

const InviteInfoOfMeeting = () => {
  const navigate = useNavigate();
  const params = useParams();
  const noticeTextList = [
    "모임통장 기능에는 정산하기 기능이 있습니다.",
    "정산 시, 선택한 입출금통장으로 정산금이 입금됩니다.",
    "초대 수락 시, 입출금통장을 보유해야 합니다.",
  ];
  const [groupId, setGroupId] = useState(0);
  const [accountNo, setAccountNo] = useState("001-85903999-209");

  const getGroupId = async () => {
    if (params.code !== undefined) {
      try {
        const response = await accountApi.fetchGroupIdByInvitationCode(params.code);
        console.log("모임 아이디 : ", response);
        setGroupId(response.data.groupId);
      } catch (error) {
        console.log("account의 fetchGroupIdByInvitationCode", error);
      }
    }
  };

  const handleInvitation = async () => {
    try {
      const data = {
        groupId: groupId,
        personalAccountNo: accountNo,
      };
      const response = await accountApi.fetchCreateParticipant(data);
      console.log(response);
    } catch (error) {
      console.log("account의 fetchCreateParticipant", error);
    }
  };

  useEffect(() => {
    getGroupId();
  }, []);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" onClick={() => navigate(`/meeting/invite/${params.code}`)} />
        </div>

        <div className="grid gap-10">
          <div className="text-2xl font-semibold">
            <p>
              정산금을 받을
              <br />
              입출금통장을 선택해주세요
            </p>
          </div>

          <AccountListInputMui accountNo={accountNo} setAccountNo={setAccountNo} />

          <div className="flex flex-col space-y-2">
            <div className="flex items-center space-x-1">
              <AiTwotoneExclamationCircle />
              <p>알아두세요</p>
            </div>

            {noticeTextList.map((text, index) => (
              <div className="flex" key={index}>
                <LuDot className="text-[#565656]" />
                <p className="text-xs text-[#565656]">{text}</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      <button
        className={`w-full h-14 text-lg rounded-xl tracking-wide ${
          accountNo === "" ? "text-[#565656] bg-[#E3E4E4]" : "text-white bg-[#1429A0]"
        }`}
        onClick={() => handleInvitation()}>
        완료
      </button>
    </div>
  );
};

export default InviteInfoOfMeeting;
