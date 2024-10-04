import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate, useLocation } from "react-router";
import { MdKeyboardArrowRight } from "react-icons/md";
import { MeetingAccountInfo } from "../../../types/account";
import { accountApi } from "../../../api/account";

const CardDetail: React.FC = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const groupId = location.state.groupId;
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
      if (response.status === 200) {
        const meetingData = response.data;
        setMeeting(meetingData);
        console.log(meetingData);
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    }
  };

  useEffect(() => {
    fetchSpecificMeetingAccount();
  }, [groupId]);

  const formatCardNumber = (cardNumber: string) => {
    // 카드 번호의 앞 4자리와 마지막 4자리를 추출
    const firstFourDigits = cardNumber.slice(0, 4);
    const middleDigits = cardNumber.slice(5, 7);
    const lastFourDigits = cardNumber.slice(-4);

    // 중간 6자리를 '*'로 변환
    const maskedSection = "** ****";

    // 전체 포맷팅된 카드 번호 생성
    return `${firstFourDigits} ${middleDigits}${maskedSection} ${lastFourDigits}`;
  };

  return (
    <>
      {meeting && (
        <div className="h-full p-5 pb-8 bg-[#F3F4F6]">
          <div className="flex flex-col space-y-5">
            <div className="grid grid-cols-3 items-center">
              <IoIosArrowBack
                onClick={() => {
                  navigate(`/meetingaccount/${groupId}`);
                }}
                className="text-2xl"
              />
              <p className="text-lg text-center">모임 카드</p>
            </div>

            <div className="w-full p-5 bg-white rounded-3xl space-y-4">
              <div>
                <p className="text-sm text-zinc-500">모임카드</p>
                <p className="text-lg font-semibold">발급 완료</p>
              </div>

              <div className="flex items-center space-x-3">
                <img className="w-10 h-10" src="/assets/card.png" alt="card" />
                <div>
                  <p className="text-zinc-500">카드 정보</p>
                  <p className="text-zinc-600 text-lg font-semibold">{formatCardNumber(meeting.cardNumber)}</p>
                </div>
              </div>
            </div>

            <div className="w-full bg-white rounded-3xl">
              <div className="p-5 space-y-5">
                <div className="space-y-1">
                  <p className="text-zinc-500">이번 여행 쓴 금액</p>
                  <p className="text-2xl font-semibold">0원</p>
                </div>
                <div className="flex justify-between">
                  <p className="text-zinc-600">카드 신청 끝</p>
                  <p className="text-blue-500 text-sm">국내 / 해외 결제 가능</p>
                </div>
              </div>

              <div className="w-full border border-zinc-100"></div>

              <div className="p-4">
                <p className="text-center text-zinc-500">내역 더 보기</p>
              </div>
            </div>

            <div className="w-full p-5 bg-white rounded-3xl">
              <div className="space-y-5">
                <p className="font-semibold">카드 관리</p>
                <div className="flex justify-between items-center">
                  <p className="text-zinc-500">결제 일시정지</p>
                  <MdKeyboardArrowRight className="text-2xl text-zinc-400" />
                </div>
                <div className="flex justify-between items-center">
                  <p className="text-zinc-500">카드 비밀번호 변경</p>
                  <MdKeyboardArrowRight className="text-2xl text-zinc-400" />
                </div>
                <div className="flex justify-between items-center">
                  <p className="text-zinc-500">결제 한도 안내</p>
                  <MdKeyboardArrowRight className="text-2xl text-zinc-400" />
                </div>
              </div>
            </div>

            <div className="w-full p-5 bg-white rounded-3xl">
              <div className="space-y-5">
                <p className="font-semibold">연결 정보</p>
                <div className="flex justify-between items-center">
                  <p className="text-zinc-500">연결 계좌</p>
                  <div
                    onClick={() => {navigate(`/meetingaccount/${meeting.groupId}`)}}
                    className="flex items-center space-x-2">
                    <p className="text-zinc-600">{meeting.groupName}</p>
                    <MdKeyboardArrowRight className="text-2xl text-zinc-400" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default CardDetail;
