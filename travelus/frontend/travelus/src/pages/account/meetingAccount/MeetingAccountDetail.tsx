import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { accountApi } from "../../../api/account";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/free-mode";
import "swiper/css/pagination";

import { IoHome } from "react-icons/io5";
import { PiAirplaneTiltFill } from "react-icons/pi";
import { FaUserFriends, FaBriefcase, FaHeart } from "react-icons/fa";

import { GoHome } from "react-icons/go";
import { MeetingAccountInfo, MeetingAccountDetailInfo } from "../../../types/account";

const MeetingAccountDetail = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [account, setAccount] = useState<MeetingAccountDetailInfo | null>(null);

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(id));
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
      console.log("Fetching account info for groupAccountNo:", groupAccountNo); // 통장 조회 로그 추가
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccount(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  // id가 변경될 때마다 모임 조회 실행
  useEffect(() => {
    if (id) {
      fetchSpecificMeetingAccount();
    }
  }, [id]);

  const [loading, setLoading] = useState<boolean>(true);

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const getAccountTypeFromIconName = (iconName: string) => {
    switch (iconName) {
      case "airPlane":
        return "여행";
      case "friend":
        return "친구";
      case "family":
        return "가족";
      case "lover":
        return "연인";
      case "job":
        return "직장";
      default:
        return "여행";
    }
  };

  const getIcon = (iconName: string) => {
    // 아이콘별 배경색을 정의하는 객체
    const iconBackgrounds: Record<string, string> = {
      airPlane: "bg-[#638ee4]",
      friend: "bg-[#F5E198]",
      family: "bg-[#FFB555]",
      lover: "bg-[#EB8CA2]",
      job: "bg-[#95DBC1]",
      default: "bg-[#638ee4]", // 기본 배경색
    };

    // 해당 아이콘의 배경색을 가져오고, 없으면 기본값 사용
    const backgroundClass = iconBackgrounds[iconName] || iconBackgrounds.default;

    const containerClasses = `w-9 h-9 ${backgroundClass} rounded-full flex justify-center items-center text-white`;
    const iconClasses = "w-6 h-6"; // 아이콘 자체 크기를 줄이기 위한 클래스

    let IconComponent;

    switch (iconName) {
      case "airPlane":
        IconComponent = <PiAirplaneTiltFill className={iconClasses} />;
        break;
      case "friend":
        IconComponent = <FaUserFriends className={iconClasses} />;
        break;
      case "family":
        IconComponent = <IoHome className={iconClasses} />;
        break;
      case "lover":
        IconComponent = <FaHeart className={iconClasses} />;
        break;
      case "job":
        IconComponent = <FaBriefcase className={iconClasses} />;
        break;
      default:
        IconComponent = <PiAirplaneTiltFill className={iconClasses} />;
        break;
    }

    return <span className={containerClasses}>{IconComponent}</span>;
  };

  useEffect(() => {
    // const getParticipants = async () => {
    //   try {
    //     if (selectedAccount === null) return;

    //     setLoading(true); // 데이터 로딩 시작
    //     const response = await accountApi.fetchParticipantInfo(selectedAccount.id);
    //     const participantNames = response.participants.map((participant: any) => participant.userInfo.username);
    //     setMemberList(participantNames);
    //   } catch (error) {
    //     console.error("Error fetching data:", error);
    //     alert("참여자 정보 조회에 실패했습니다.");
    //   } finally {
    //     setLoading(false); // 데이터 로딩 완료
    //   }
    // };

    // getParticipants();

    // 참여자 정보 조회 더미데이터
    const participants = ["John Doe", "Jane Doe", "Alice", "Bob", "Charlie"];
    setLoading(false); // 데이터 로딩 완료
  }, []);

  if (account === null) {
    return <p>계좌 정보를 불러오는 중입니다...</p>;
  }

  return (
    account &&
    meeting && (
      <div className="h-full bg-[#EBF1FF] grid grid-rows-[35%_65%]">
        <div className="h-full p-5 pb-8">
          <div className="h-full flex flex-col justify-between">
            <div className="flex justify-between items-center">
              <GoHome
                onClick={() => {
                  navigate("/");
                }}
                className="text-2xl text-zinc-600 cursor-pointer"
              />
              <button
                onClick={() => {
                  navigate(`/meetingaccount/management/${meeting.groupId}`);
                }}
                className="w-14 h-8 bg-zinc-500 opacity-40 rounded-3xl text-zinc-50 font-semibold flex justify-center items-center">
                관리
              </button>
            </div>

            <div className="flex flex-col items-center space-y-5">
              <p className="text-lg font-semibold text-zinc-500">{meeting.groupName}</p>
              <div className="flex flex-col justify-center items-center space-y-1">
                <p>{getIcon(meeting.icon)}</p>
                <p className="text-sm">{getAccountTypeFromIconName(meeting.icon)}</p>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-t-3xl flex flex-col justify-between">
          <div>
            <div
              onClick={() => {
                navigate(`/meetingtransaction/${meeting.groupId}`);
              }}
              className="mt-6 p-5 flex flex-col space-y-5">
              <div className="flex flex-col space-y-1">
                <p>모임통장</p>
                <p className="text-2xl font-bold">{formatCurrency(account.moneyBoxDtos[0].balance)}원</p>
              </div>
              {account.moneyBoxDtos.length > 1 ? (
                <div className="flex justify-between">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate("/transfer/selectbank");
                    }}
                    className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
                    채우기
                  </button>
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate("/exchange");
                    }}
                    className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
                    환전
                  </button>
                </div>
              ) : (
                <div className="flex justify-between">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate("/transfer/selectbank");
                    }}
                    className="w-full h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
                    채우기
                  </button>
                </div>
              )}
            </div>

            <div className="w-full h-4 bg-[#F6F6F8]"></div>

            <div
              onClick={() => {
                navigate("/transaction/detail/travelbox/1");
              }}
              className="p-5 flex flex-col space-y-5">
              <div className="flex flex-col space-y-1">
                {account.moneyBoxDtos.length > 1 ? (
                  <>
                    <p>트래블박스</p>
                    <p className="text-2xl font-bold">
                      {formatCurrency(account.moneyBoxDtos[1].balance)}
                      <span>{account.moneyBoxDtos[1].currencyCode}</span>
                    </p>
                    <div className="flex justify-between">
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          navigate("/exchangekrw");
                        }}
                        className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
                        재환전
                      </button>
                      <button className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
                        내역
                      </button>
                    </div>
                  </>
                ) : (
                  <></>
                )}
              </div>
            </div>
          </div>

          {account.moneyBoxDtos.length > 1 ? (
            <div className="p-5 pb-8 flex flex-col items-center justify-center space-y-5">
              <button
                onClick={() => {
                  navigate(`/settlement/balance/amount/${id}`);
                }}
                className="w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold bg-[#1429A0]">
                정산하기
              </button>
              <button
                className="text-sm text-[#1429A0] font-semibold"
                onClick={() => navigate("/settlement/expenditure/transaction/detail/1")}>
                개별지출 정산하기
              </button>
            </div>
          ) : (
            <div className="p-5 pb-8 flex flex-col items-center justify-center space-y-5">
              <button className="w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold bg-[#1429A0]">
                트래블박스 개설하기
              </button>
            </div>
          )}
        </div>
      </div>
    )
  );
};

export default MeetingAccountDetail;
