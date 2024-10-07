import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../redux/store";
import { useNavigate, useParams } from "react-router";
import SecurityNumberKeyboard from "../../../components/common/SecurityNumberKeyboard";
import { accountApi } from "../../../api/account";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../types/axiosError";
import { exchangeRateApi } from "../../../api/exchange";

const PasswordOfCreateMeetingAccount = () => {
  const navigate = useNavigate();
  const params = useParams();

  const [password, setPassword] = useState("");
  const [isTravelboxCreated, setIsTravelboxCreated] = useState(false);
  const [isTargetRateCreated, setIsTargetRateCreated] = useState(false);
  const travelboxInfo = useSelector((state: RootState) => state.meetingAccount.travelboxInfo);
  const exchangeTargetInfo = useSelector((state: RootState) => state.meetingAccount.exchangeTargetInfo);

  const createTravelbox = async () => {
    const travelboxData = {
      accountPassword: password,
      accountNo: travelboxInfo.accountNo,
      currencyCode: travelboxInfo.currencyCode,
    };
    try {
      const response = await accountApi.fetchCreateTravelBox(travelboxData);
      if (response.status === 201) {
        setIsTravelboxCreated(true);
      }
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "ACCOUNT_PASSWORD_INVALID") {
          setPassword("");
          alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
      }
      console.log("accountApi의 fetchCreateTravelBox : ", error);
    }
  };

  const createTargetRate = async () => {
    const targetRate = {
      accountNo: travelboxInfo.accountNo,
      currencyCode: travelboxInfo.currencyCode,
      transactionBalance: exchangeTargetInfo.transactionBalance,
      targetRate: exchangeTargetInfo.targetRate,
    };
    try {
      const response = await exchangeRateApi.postExchangeTargetRate(targetRate);
      if (response.status === 200) {
        setIsTargetRateCreated(true);
      }
    } catch (error) {
      console.log("exchangeRateApi의 postExchangeTargetRate : ", error);
    }
  };

  useEffect(() => {
    if (password.length === 4) {
      if (params.type === "travelbox") {
        createTravelbox();
        createTargetRate();
      } else {
        navigate("/meeting/create/password/check", { state: { originalPassword: password, type: params.type } });
      }
    }
  }, [password]);

  useEffect(() => {
    if (isTravelboxCreated && isTargetRateCreated) {
      navigate("/meeting/create/completed/travelbox");
    }
  }, [isTravelboxCreated, isTargetRateCreated]);

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          {params.type === "travelbox"
            ? "모임통장의 비밀번호를"
            : params.type === "meeting"
            ? "모임통장에서 사용할"
            : "튜나뱅크에서 사용할"}
          <br />
          {params.type === "travelbox" ? "입력해주세요" : "비밀번호를 입력해주세요"}
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

export default PasswordOfCreateMeetingAccount;
