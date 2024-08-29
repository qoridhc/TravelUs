import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { useNavigate } from "react-router";
import { RiHome5Line } from "react-icons/ri";
import TypeSelect from "../../components/account/inputField/TypeSelect";
import { currencyTypeList } from "../../types/exchange";
import ExchangeRateInput from "../../components/account/inputField/ExchangeRateInput";
import TravleDateRangePicker from "../../components/account/inputField/TravelDateRangePicker";
import { Dayjs } from "dayjs";

const ForeignMeetingAccountCreate = () => {
  // const {} = useSelector((state: RootState) => state.account);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [step, setStep] = useState(0);
  const stepList = ["통화를", "희망 환율을", "여행 일정을"];
  const [currencyType, setCurrencyType] = useState("");
  const [exchangeRate, setExchangeRate] = useState(0);
  const [travelSchedule, setTravelSchedule] = useState<{ startDate: Dayjs | null; endDate: Dayjs | null }>({
    startDate: null,
    endDate: null,
  });

  const handleCurrencyTypeChange = (currencyType: string) => {
    setCurrencyType(currencyType);
    if (currencyType !== "") {
      setStep(1);
      console.log(step);
    }
  };

  const handleExchangeRateChange = (exchangeRate: number) => {
    setExchangeRate(exchangeRate);
    if (exchangeRate > 0) {
      setStep(2);
    }
  };

  const handleTravelStartDate = (startDate: Dayjs) => {
    setTravelSchedule((prevSchedule) => ({
      ...prevSchedule,
      startDate,
    }));
  };

  const handleTravelEndDate = (endDate: Dayjs) => {
    setTravelSchedule((prevSchedule) => ({
      ...prevSchedule,
      endDate,
    }));
  };

  const handleNext = () => {
    // dispatch(
    //   editGeneralMeetingAccountList({
    //     generalMeetingAccountName: meetingName,
    //     generalMeetingAccountIcon: meetingType,
    //     generalMeetingAccountUserName: name,
    //     generalMeetingAccountUserResidentNumber: residentNumber,
    //     generalMeetingAccountPassword: accountPassword,
    //     generalMeetingAccountMemberList: memberList,
    //   })
    // );

    navigate("/meetingaccountcreatecomplete");
  };

  return (
    <div className="h-full flex flex-col justify-between">
      <div className="flex flex-col space-y-5">
        <div className="p-5 grid grid-cols-[1fr_8fr_1fr]">
          <div className="flex items-center">
            <RiHome5Line className="text-2xl" />
          </div>
          <p className="text-xl text-center font-semibold">외화모임통장 가입정보</p>
        </div>

        <div className="p-5 grid gap-8">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">02</p>
              <p className="font-semibold">외화모임통장 계좌개설</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>{stepList[step]}</p>
              <p>입력해주세요</p>
            </div>
          </div>

          <div className="grid gap-3">
            <div
              className={`transition-transform duration-300 ease-in-out ${
                step > 1 ? "translate-y-0" : "translate-y-[3px]"
              }`}>
              {step > 1 && (
                <TravleDateRangePicker
                  schedule={travelSchedule}
                  onStartChange={handleTravelStartDate}
                  onEndChange={handleTravelEndDate}
                />
              )}
            </div>

            <div
              className={`transition-transform duration-300 ease-in-out ${
                step > 0 ? "translate-y-0" : "translate-y-[3px]"
              }`}>
              {step > 0 && (
                <ExchangeRateInput
                  currencyType={currencyType}
                  exchangeRate={exchangeRate}
                  onChange={handleExchangeRateChange}
                />
              )}
            </div>

            <div
              className={`transition-transform duration-300 ease-in-out ${
                step === 0 ? "translate-y-0" : "translate-y-[3px]"
              }`}>
              <TypeSelect
                label="통화"
                menuList={currencyTypeList}
                selectedType={currencyType}
                onChange={handleCurrencyTypeChange}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="px-5 py-10">
        <button
          className={`w-full py-3 text-white bg-[#0471E9] rounded-lg ${
            step !== 2 ||
            currencyType === "" ||
            exchangeRate === 0 ||
            travelSchedule.startDate === null ||
            travelSchedule.endDate === null
              ? "opacity-40"
              : ""
          }`}
          onClick={() => handleNext()}
          disabled={
            step !== 2 ||
            currencyType === "" ||
            exchangeRate === 0 ||
            travelSchedule.startDate === null ||
            travelSchedule.endDate === null
          }>
          완료
        </button>
      </div>
    </div>
  );
};

export default ForeignMeetingAccountCreate;
