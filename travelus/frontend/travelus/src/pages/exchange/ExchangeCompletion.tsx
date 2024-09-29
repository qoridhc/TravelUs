import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { FcMoneyTransfer } from "react-icons/fc";
import { ExchangeResponse } from "../../types/exchange";

interface LocationState {
  sourceCurrencyCode: string;
  targetCurrencyCode: string;
  sourceAmount: string;
  targetAmount: string;
  exchangeRate?: number;
  transactionSummary: string;
}

const currencyNameMapping: { [key: string]: string } = {
  KRW: "원",
  USD: "달러",
  JPY: "엔",
  CNY: "위안",
  EUR: "유로",
};

const ExchangeCompletion: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const state = location.state as LocationState;

  if (!state) {
    // state가 없으면 홈으로 리다이렉트
    navigate("/");
    return null;
  }

  const { sourceCurrencyCode, targetCurrencyCode, sourceAmount, targetAmount, transactionSummary } = state;

  // transactionSummary에서 적용 환율 숫자만 받아오기
  const extractExchangeRate = (summary: string): string => {
    const match = summary.match(/(\d+(\.\d+)?)/);
    return match ? match[1] : "N/A";
  };

  const extractRate = extractExchangeRate(transactionSummary);

  // 통화 이름 가져오기
  const getLocalCurrencyName = (currencyCode: string): string => {
    return currencyNameMapping[currencyCode] || currencyCode;
  };

  const handleClose = () => {
    navigate("/");
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col">
      <div className="flex-grow flex flex-col items-center justify-center space-y-5">
        <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />
        <div className="text-2xl font-semibold text-center">
          <p>
            {targetAmount} {getLocalCurrencyName(targetCurrencyCode)}
          </p>
          <p className="font-normal">채우기 완료</p>
        </div>
      </div>

      <div className="mb-6">
        <div className="mb-4 flex justify-between">
          <p className="text-gray-500 text-sm">환전 금액</p>
          <p className="text-lg">
            {sourceAmount} {getLocalCurrencyName(sourceCurrencyCode)} → {targetAmount}{" "}
            {getLocalCurrencyName(targetCurrencyCode)}
          </p>
        </div>

        <div className="flex justify-between">
          <p className="text-gray-500 text-sm mb-1">적용 환율</p>
          <p className="text-lg">
            {extractRate} {getLocalCurrencyName(targetCurrencyCode)}
          </p>
        </div>
      </div>

      <button onClick={handleClose} className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]">
        닫기
      </button>
    </div>
  );
};

export default ExchangeCompletion;
