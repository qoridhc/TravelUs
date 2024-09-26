import React, { useEffect } from "react";
import { useState } from "react";
import { useNavigate, useLocation } from "react-router";
import { accountApi } from "../../api/account";
import { userApi } from "../../api/user";
import { transactionApi } from "../../api/transaction";
import { AccountInfoNew } from "../../types/account";
import { IoIosArrowBack } from "react-icons/io";

interface TransferConfirmProps {
  // Define the props for the component here
}

const TransferConfirm: React.FC<TransferConfirmProps> = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isValidation, setIsValidation] = useState<boolean>(false);
  const [withdrawalAccountNo, setWithdrawalAccountNo] = useState<string>("");
  const [userName, setUserName] = useState<string>("");
  const { accountNo, transferAmount, password, depositAccount } = location.state as {
    accountNo: string;
    transferAmount: string;
    password: string;
    depositAccount: AccountInfoNew;
  };

  const formatAccountNumber = (accountNo: string) => {
    if (accountNo.length !== 14) {
      return accountNo;
    }
    // 계좌번호를 3-8-2 형식으로 변환
    return `${accountNo.slice(0, 3)}-${accountNo.slice(3, 11)}-${accountNo.slice(11)}`;
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 출금 계좌 번호를 위한 API 호출
        const accountResponse = await accountApi.fetchAllAccountInfo("I");
        setWithdrawalAccountNo(accountResponse[0].accountNo);

        // 사용자 이름을 위한 API 호출
        const userResponse = await userApi.fetchUser();
        setUserName(userResponse.data.name);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, []);

  const handleTransfer = async () => {
    const data = {
      transferType: "G",
      withdrawalAccountNo: formatAccountNumber(withdrawalAccountNo),
      accountPassword: password,
      depositAccountNo: formatAccountNumber(accountNo),
      transactionBalance: parseInt(transferAmount),
      withdrawalTransactionSummary: userName,
      depositTransactionSummary: userName,
    };

    console.log("이체 데이터", data);

    try {
      const response = await transactionApi.Transfer(data);
      console.log("이체 성공");
      navigate("/transfer/success", { state: { transferAmount } });
    } catch (error) {
      console.error("이체 에러", error);
    }
  };

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
            {depositAccount?.userName}
            <span className="font-normal"> 님에게</span>
          </p>
          <p className="text-2xl font-bold">{formatCurrency(parseInt(transferAmount))}원</p>
          <p className="text-2xl font-bold">보낼까요?</p>
        </div>
        <div className="flex flex-col space-y-6">
          <div className="flex flex-col space-y-2">
            <div className="flex justify-between">
              <p className="text-zinc-500">받는 분에게 표시</p>
              <p>{userName}</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">출금 계좌</p>
              <p>{formatAccountNumber(withdrawalAccountNo)}</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">입금 계좌</p>
              <p>{formatAccountNumber(accountNo)}</p>
            </div>
          </div>
          <div className="flex flex-col space-y-3">
            <button
              onClick={handleTransfer}
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

export default TransferConfirm;
