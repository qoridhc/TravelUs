import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useLocation, useNavigate } from "react-router";
import ExchangeRateInputMui from "../../components/travelBox/ExchangeRateInputMui";
import ExchangeAmmountInput from "../../components/travelBox/ExchangeAmmountInput";
import { useDispatch, useSelector } from "react-redux";
import { setExchangeTargetRate, setTravelboxInfo } from "../../redux/meetingAccountSlice";
import { RootState } from "../../redux/store";

const AutoCurrencyExchangeOfCreateTravelBox = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const [exchangeRate, setExchangeRate] = useState(0);
  const [exchangeRateFront, setExchangeRateFront] = useState(0);
  const [exchangeRateBack, setExchangeRateBack] = useState(0);
  const [exchangeAmount, setExchangeAmount] = useState(0);
  const travelboxInfo = useSelector((state: RootState) => state.meetingAccount.travelboxInfo);

  const handleNext = () => {
    dispatch(setExchangeTargetRate({ transactionBalance: exchangeAmount, targetRate: exchangeRate }));
    navigate("/meeting/create/password/travelbox");
  };

  useEffect(() => {
    setExchangeRate(Number(exchangeRateFront + "." + exchangeRateBack));
  }, [exchangeRateFront, exchangeRateBack]);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between relative">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" />
        </div>

        <div className="grid gap-10">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">02</p>
              <p className="font-medium">외화저금통 개설</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>희망 환율이 되면</p>
              <p>자동으로 환전할게요</p>
            </div>
          </div>

          <ExchangeRateInputMui
            currency={location.state.currency}
            exchangeRateFront={exchangeRateFront}
            setExchangeRateFront={setExchangeRateFront}
            exchangeRateBack={exchangeRateBack}
            setExchangeRateBack={setExchangeRateBack}
          />
          <ExchangeAmmountInput exchangeAmount={exchangeAmount} setExchangeAmount={setExchangeAmount} />
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleNext()}>
        다음
      </button>
    </div>
  );
};

export default AutoCurrencyExchangeOfCreateTravelBox;
