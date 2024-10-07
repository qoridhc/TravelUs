import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router";
import { accountApi } from "../../../../api/account";
import { userApi } from "../../../../api/user";
import { IoIosArrowBack } from "react-icons/io";
import Lottie from "lottie-react";
import loadingAnimation from "../../../../lottie/loadingAnimation.json";

const SettlementTransferConfirm = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [withdrawalAccountNo, setWithdrawalAccountNo] = useState<string>("");
  const [userName, setUserName] = useState<string>("");

  // 금액에 콤마 추가
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const handleTransfer = () => {
    const settlement = location.state.data;
    const data = {
      personalSettlementId: settlement.personalSettlementId,
      participantId: settlement.participantId,
      depositAccountNo: settlement.depositAccountNo,
      withdrawalAccountNo,
      transactionBalance: settlement.remainingAmount,
      withdrawalTransactionSummary: userName,
      depositTransactionSummary: settlement.groupName,
    };
    navigate("/settlement/expenditure/transfer/password", { state: { data } });
  };

  const fetchData = async () => {
    try {
      // 출금 계좌 번호를 위한 API 호출
      const accountResponse = await accountApi.fetchAllAccountInfo("I");
      setWithdrawalAccountNo(accountResponse.data[0].accountNo);

      // 사용자 이름을 위한 API 호출
      const userResponse = await userApi.fetchUser();
      setUserName(userResponse.data.name);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (withdrawalAccountNo === "" || userName === "") {
    return (
      <div className="h-full flex flex-col justify-center items-center">
        <Lottie animationData={loadingAnimation} />
      </div>
    );
  }

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div>
          <IoIosArrowBack
            onClick={() => {
              navigate("/transfer/selectbank");
            }}
            className="text-2xl"
          />
        </div>
        <div className="mb-16 flex flex-col items-center">
          <p className="text-2xl font-bold">
            {location.state.data.groupName}
            <span className="font-normal"> 님에게</span>
          </p>
          <p className="text-2xl font-bold">{formatCurrency(parseInt(location.state.data.remainingAmount))}원</p>
          <p className="text-2xl">보낼까요?</p>
        </div>
        <div className="flex flex-col space-y-6">
          <div className="flex flex-col space-y-2">
            <div className="flex justify-between">
              <p className="text-zinc-500">받는 분에게 표시</p>
              <p>{userName}</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">출금 계좌</p>
              <p>{withdrawalAccountNo}</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">입금 계좌</p>
              <p>{location.state.data.depositAccountNo}</p>
            </div>
          </div>
          <div className="flex flex-col space-y-3">
            <button
              onClick={() => {
                handleTransfer();
              }}
              className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide text-white bg-[#1429A0]">
              보내기
            </button>
            <button
              onClick={() => {
                navigate("/transfer/selectbank");
              }}
              className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide">
              취소
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SettlementTransferConfirm;
