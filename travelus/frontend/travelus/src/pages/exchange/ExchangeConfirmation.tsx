import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { useNavigate, useLocation } from "react-router";
import SecurityNumberKeyboard from "../../components/common/SecurityNumberKeyboard";
import { setAccountPassword } from "../../redux/accountSlice";
import { exchangeRateApi } from "../../api/exchange";
import { ExchangeRequest, ExchangeResponse } from "../../types/exchange";
import { ChevronLeft } from "lucide-react";

interface LocationState {
  accountNo: string;
  sourceCurrencyCode: string;
  targetCurrencyCode: string;
  transactionBalance: string;
}

const AccountPasswordInput = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();
  const [stateData, setStateData] = useState<LocationState | null>(null);
  const [transactionList, setTransactionList] = useState<ExchangeResponse[]>([]);

  const password = useSelector((state: RootState) => state.account.accountPassword);

  useEffect(() => {
    if (location.state) {
      setStateData(location.state as LocationState);
    } else {
      console.log("No State received");
      navigate(-1);
    }

    return () => {
      dispatch(setAccountPassword(""));
    };
  }, [location, dispatch, navigate]);

  useEffect(() => {
    if (password.length === 4 && stateData) {
      handleExchange();
    }
  }, [password, stateData]);

  const handleExchange = async () => {
    if (!stateData) return;

    const exchangeRequest: ExchangeRequest = {
      transferType: "M",
      accountNo: stateData.accountNo,
      accountPassword: password,
      sourceCurrencyCode: stateData.sourceCurrencyCode,
      targetCurrencyCode: stateData.targetCurrencyCode,
      transactionBalance: stateData.transactionBalance,
    };

    try {
      const response: ExchangeResponse[] = await exchangeRateApi.requestExchange(exchangeRequest);
      setTransactionList(response);

      const sourceTransaction = response.find((t) => t.transactionType === "EW");
      const targetTransaction = response.find((t) => t.transactionType === "ED");

      if (!sourceTransaction || !targetTransaction) {
        throw new Error("Missing transaction information");
      }

      navigate("/exchange/exchange-completion", {
        state: {
          sourceCurrencyCode: stateData.sourceCurrencyCode,
          targetCurrencyCode: stateData.targetCurrencyCode,
          sourceAmount: stateData.transactionBalance,
          targetAmount: targetTransaction.transactionAmount,
          transactionSummary: sourceTransaction.transactionSummary,
          fullTransactionList: response,
        },
      });
    } catch (error) {
      console.log("Exchange failed:", error);
    } finally {
      dispatch(setAccountPassword(""));
    }
  };

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="p-4">
        {/* <button onClick={() => navigate(-1)} className="mb-4">
          <ChevronLeft className="w-6 h-6" />
        </button> */}
      </div>
      <div className="flex-grow flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          환전을 위한
          <br />
          계좌 비밀번호를 입력해주세요
        </p>

        <div className="flex space-x-3">
          {[...Array(4)].map((_, index) => (
            <div
              className={`w-4 aspect-1 ${index < password.length ? "bg-[#565656]" : "bg-[#d9d9d9]"} rounded-full`}
              key={index}></div>
          ))}
        </div>
      </div>
      <SecurityNumberKeyboard />
    </div>
  );
};

export default AccountPasswordInput;
