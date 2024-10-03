import React, { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import { accountApi } from "../../api/account";
import { AccountInfoNew, TransactionNew } from "../../types/account";

const TravelBoxTransaction = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const accountNo = useParams().accountNo;
  const groupId = location.state.groupId;
  const currencyCode = location.state.currencyCode;
  const [account, setAccount] = useState<AccountInfoNew>();
  const [transactionList, setTransactionList] = useState<TransactionNew[]>([]);
  const depositTransactionType = ["D", "TD", "ED", "SD"];
  const withdrawTransactionType = ["W", "TW", "EW", "SW"];

  useEffect(() => {
    const fetchData = async () => {
      if (!accountNo) return;

      try {
        const [transactionResponse, accountResponse] = await Promise.all([
          accountApi.fetchTracsactionHistory(accountNo, currencyCode, "DESC"),
          accountApi.fetchSpecificAccountInfo(accountNo),
        ]);

        if (transactionResponse.status === 200) {
          setTransactionList(transactionResponse.data.content);
        }

        if (accountResponse.status === 201) {
          setAccount(accountResponse.data);
          console.log(accountResponse.data);
        }
      } catch (error) {
        console.error("데이터 조회 실패", error);
      }
    };

    fetchData();
  }, [accountNo]);

  // 날짜별로 그룹화하여 표시
  const groupedTransactions = transactionList.reduce((acc, current) => {
    // 시간 데이터를 한국식 날짜 형식으로 변환
    const date = new Date(current.transactionDate).toLocaleDateString("ko-KR");

    // 누적 객체에 해당 날짜의 거래 내역이 없을 때
    if (!acc[date]) {
      acc[date] = [];
    }
    acc[date].push(current);

    return acc;
  }, {} as { [key: string]: typeof transactionList });

  const dateList = Object.keys(groupedTransactions);

  const formatCurrency = (amount: string) => {
    const numAmount = Number(amount);
    return new Intl.NumberFormat("ko-KR").format(numAmount);
  };

  const formatCurrencyNum = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  return (
    <div className="h-full pb-8">
      <div className="flex flex-col">
        <div className="p-5 grid grid-cols-3 items-center sticky top-0">
          <IoIosArrowBack
            onClick={() => {
              navigate(`/meetingaccount/${groupId}`, { state: { groupId } });
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">트래블박스</p>
        </div>

        {account?.moneyBoxDtos && (
          <div className="p-5">
            <div className="my-10 space-y-3">
              <p className="text-zinc-500 underline underline-offset-2">
                {currencyCode === "USD" && "USD (미국 / $)"}
                {currencyCode === "JPY" && "JPY (일본 / ¥)"}
                {currencyCode === "EUR" && "EUR (유로 / €)"}
                {currencyCode === "CNY" && "CNY (중국 / ¥)"}
              </p>
              <p className="text-3xl font-bold">
                {formatCurrencyNum(account.moneyBoxDtos[1].balance)} {currencyCode}
              </p>
            </div>

            <div className="flex justify-between">
              <button
                onClick={() => {
                  navigate("/transfer/selectbank");
                }}
                className="w-full h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
                재환전
              </button>
            </div>
          </div>
        )}
        <div className="w-full h-5 bg-[#F6F6F8]"></div>

        <div className="p-5 overflow-y-auto">
          {transactionList.length > 0 ? (
            dateList.map((date) => (
              <div className="grid gap-5" key={date}>
                <p className="text-zinc-500">
                  {new Date(date).toLocaleDateString("ko-KR", {
                    month: "long",
                    day: "numeric",
                  })}
                </p>

                {groupedTransactions[date].map((transaction, index) => (
                  <div key={index} className="flex flex-col items-center">
                    <div className="w-full flex justify-between">
                      <p className="text-lg font-semibold">{transaction.transactionSummary}</p>

                      {withdrawTransactionType.includes(transaction.transactionType) ? (
                        <p className="text-lg tracking-wider">
                          - {formatCurrency(transaction.transactionAmount)}
                          {currencyCode}
                        </p>
                      ) : (
                        <p className="text-lg text-[#026CE1] tracking-wider">
                          {formatCurrency(transaction.transactionAmount)}
                          {currencyCode}
                        </p>
                      )}
                    </div>

                    <div className="w-full flex justify-between">
                      <p className="text-sm text-zinc-500 tracking-wider">
                        {new Date(transaction.transactionDate).toLocaleTimeString("ko-KR", {
                          hour: "2-digit",
                          minute: "2-digit",
                          hour12: false,
                        })}
                      </p>

                      <p className="text-sm text-right text-zinc-500 tracking-wider">
                        {formatCurrency(transaction.transactionBalance)}
                        {currencyCode}
                      </p>
                    </div>
                  </div>
                ))}
                <hr className="mb-5" />
              </div>
            ))
          ) : (
            <div>
              <p className="text-center">거래 내역이 없습니다</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default TravelBoxTransaction;
