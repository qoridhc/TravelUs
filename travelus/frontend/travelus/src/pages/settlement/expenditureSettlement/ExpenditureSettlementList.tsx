import React, { useEffect, useState } from "react";
import { IoIosArrowBack, IoIosArrowForward } from "react-icons/io";
import { settlementApi } from "../../../api/settle";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../types/axiosError";
import { useNavigate } from "react-router";
import { SettlementPersonalInfo } from "../../../types/settlement";
import Lottie from "lottie-react";
import loadingAnimation from "../../../lottie/loadingAnimation.json";

const ExpenditureSettlementList = () => {
  const navigate = useNavigate();
  const [isTab, setIsTab] = useState("NOT_COMPLETED"); // 진행 중 : NOT_COMPLETED, 완료 : COMPLETED
  const [isEmpty, setIsEmpty] = useState(false);
  const [settlementList, setSettlementList] = useState<{ [date: string]: SettlementPersonalInfo[] }>({});
  const [dateList, setDateList] = useState<string[]>([]);

  // 금액을 한국 통화 형식으로 포맷(콤마가 포함된 형태)
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  // 날짜 형식 변환 함수
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("ko-KR", {
      month: "long",
      day: "numeric",
    });
  };

  const handleNext = (settlement: SettlementPersonalInfo) => {
    const data = {
      personalSettlementId: settlement.personalSettlementId,
      participantId: settlement.participantId,
      remainingAmount: settlement.remainingAmount,
      groupName: settlement.groupName,
      groupId: settlement.groupId,
    };
    navigate("/settlement/expenditure/transfer/setMoney", { state: { data } });
  };

  const fetchSettlementList = async (settlementStatus: string) => {
    try {
      const response = await settlementApi.fetchSettlementPersonalList(settlementStatus);
      console.log(response.data);

      // 거래내역을 날짜별로 그룹화하여 병합
      const temp = response.data.reduce(
        (acc: { [date: string]: SettlementPersonalInfo[] }, cur: SettlementPersonalInfo) => {
          const dateKey = formatDate(cur.settlementRequestTime);
          if (acc[dateKey]) {
            acc[dateKey].push(cur);
          } else {
            acc[dateKey] = [cur];
          }
          return acc;
        },
        {} as { [date: string]: SettlementPersonalInfo[] }
      );

      console.log(temp);

      setSettlementList(temp);
      setIsEmpty(false);
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "PERSONAL_SETTLEMENT_HISTORY_NOT_FOUND") {
          setIsEmpty(true);
        }
      }
      console.log("settlementApi의 fetchSettlementPersonalList : ", error);
    }
  };

  useEffect(() => {
    setDateList([]);
    setSettlementList({});
    fetchSettlementList(isTab);
  }, [isTab]);

  useEffect(() => {
    setDateList(Object.keys(settlementList));
  }, [settlementList]);

  return (
    <div className="h-full p-5">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" onClick={() => navigate("/")} />
        </div>

        <div className="grid gap-10">
          <p className="text-2xl font-semibold">
            여행 동안의 개인 지출을
            <br />
            정산해주세요
          </p>

          <div className="text-center text-lg font-semibold grid grid-cols-2">
            <p
              className={`pb-1 ${isTab === "NOT_COMPLETED" ? "border-b-[3px] border-[#565656]" : "text-[#9e9e9e]"}`}
              onClick={() => setIsTab("NOT_COMPLETED")}>
              진행 중
            </p>
            <p
              className={`pb-1 ${isTab === "COMPLETED" ? "border-b-[3px] border-[#565656]" : "text-[#9e9e9e]"}`}
              onClick={() => setIsTab("COMPLETED")}>
              완료
            </p>
          </div>

          {isEmpty ? (
            <></>
          ) : dateList.length === 0 ? (
            <div className="flex flex-col justify-center items-center">
              <Lottie animationData={loadingAnimation} />
            </div>
          ) : (
            dateList.map((date, index) => (
              <div className="grid gap-5" key={index}>
                <p className="text-[#565656]">{date}</p>
                <div className="grid gap-8">
                  {settlementList[date].map((settlement, index) => (
                    <div
                      className={`grid ${
                        isTab === "NOT_COMPLETED" ? "grid-rows-2" : "s"
                      } grid-cols-[1fr_5fr_1fr] gap-y-3`}
                      key={index}>
                      <div className="flex items-center">
                        <img className="w-10 h-10" src="/assets/user/userIconSample.png" alt="" />
                      </div>
                      <div>
                        <p className="text-xl font-bold tracking-tight">{formatCurrency(settlement.amount)}원</p>
                        <p className="text-[#565656]">
                          {isTab === "NOT_COMPLETED"
                            ? `${settlement.groupName}의 총 ${settlement.participantCount}명`
                            : `${formatCurrency(settlement.amount)}원 보내기 완료`}
                        </p>
                      </div>

                      <div className="flex justify-end items-center">
                        <IoIosArrowForward className="text-lg" />
                      </div>

                      <div className="col-start-2 col-span-2 flex items-center">
                        {isTab === "NOT_COMPLETED" ? (
                          <button
                            className="w-full p-3 text-white bg-[#1429A0] rounded-xl"
                            onClick={() => handleNext(settlement)}>
                            {formatCurrency(settlement.remainingAmount)}원 보내기
                          </button>
                        ) : (
                          <></>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default ExpenditureSettlementList;
