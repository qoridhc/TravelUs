import React, { useEffect, useState } from "react";
import { IoIosArrowBack, IoIosCheckmarkCircle, IoIosCheckmarkCircleOutline } from "react-icons/io";
import { LuDot } from "react-icons/lu";
import { useNavigate, useParams } from "react-router";
import { accountApi } from "../../../api/account";
import { MeetingAccountDetailInfo } from "../../../types/account";
import { currencyTypeList } from "../../../types/exchange";

const SelectSettlementAmount = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isChecked, setIsChecked] = useState<boolean[]>([]);
  const [accountInfo, setAccountInfo] = useState<MeetingAccountDetailInfo | null>(null);
  const guideTextList = [
    "남은 원화는 일반 모임통장의 잔액이에요.",
    "남은 외화는 외화 모임통장의 잔액이에요.",
    "원화와 외화 함께 정산 시, 외화는 현재 환율을 적용하여 원화로 환전한 후 정산돼요.",
  ];

  const handleSettlement = () => {
    navigate("/settlementforeigncurrencyexchange", { state: { isChecked: isChecked } });
  };

  const handleCheck = (index: number, checked: boolean) => {
    const updatedChecked = [...isChecked];
    updatedChecked[index] = checked;
    setIsChecked(updatedChecked);
  };

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(id));
      if (response.status === 200) {
        // 모임 조회 성공 시, 바로 통장 정보 조회
        fetchSpecificAccountInfo(response.data.groupAccountNo);
      }
    } catch (error) {
      console.error("accountApi의 fetchSpecificMeetingAccount : ", error);
    }
  };

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      console.log("Fetching account info for groupAccountNo:", groupAccountNo);
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccountInfo(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  useEffect(() => {
    fetchSpecificMeetingAccount();
  }, []);

  return (
    <>
      {accountInfo && (
        <div className="h-full p-5 pb-8 flex flex-col justify-between">
          <div className="grid gap-14">
            <div className="grid grid-cols-3">
              <div className="flex items-center">
                <IoIosArrowBack className="text-2xl" />
              </div>
              <p className="text-lg text-center">정산하기</p>
            </div>

            <div className="grid gap-8">
              <p className="text-2xl font-semibold tracking-wide">
                정산할 금액을
                <br />
                선택해주세요
              </p>

              <div className="grid gap-3">
                {accountInfo.moneyBoxDtos.map((moneyBox, index) => (
                  <label className="font-semibold flex justify-between items-center" key={index}>
                    <div className="flex  space-x-2">
                      <input
                        type="checkbox"
                        className="w-6 aspect-1 appearance-none bg-[url('./assets/check/nochecked.png')] checked:bg-[url('./assets/check/checked.png')] bg-cover rounded-full"
                        onChange={(e) => handleCheck(index, e.target.checked)}
                      />
                      <p>
                        남은 {moneyBox.currencyCode === "KRW" ? "원화" : "외화"} / {moneyBox.currencyCode}
                      </p>
                    </div>

                    <p>
                      {moneyBox.balance}&nbsp;
                      {moneyBox.currencyCode === "KRW" ? (
                        "원"
                      ) : (
                        <>{currencyTypeList.find((item) => item.value === moneyBox.currencyCode)?.text.slice(-2, -1)}</>
                      )}
                    </p>
                  </label>
                ))}
              </div>
            </div>
          </div>

          <div className="grid gap-5">
            <div className="grid gap-3">
              <p className="text-[#333D4B] font-semibold">정산 안내</p>

              {guideTextList.map((text, index) => (
                <div className="flex" key={index}>
                  <LuDot className="text-lg text-[#8B95A1]" />
                  <p className="w-[90%] text-sm text-[#4E5968] break-keep">{text}</p>
                </div>
              ))}
            </div>

            <button
              className={`w-full h-14 text-lg rounded-xl tracking-wide ${
                isChecked[0] || isChecked[1] ? "text-white bg-[#1429A0] " : "text-[#565656] bg-[#E3E4E4]"
              }`}
              onClick={() => handleSettlement()}
              disabled={!(isChecked[0] || isChecked[1])}>
              {isChecked[0] || isChecked[1] ? "정산하기" : "정산금을 선택해주세요"}
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default SelectSettlementAmount;
