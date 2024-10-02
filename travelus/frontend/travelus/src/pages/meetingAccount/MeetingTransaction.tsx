import React, { useEffect, useState } from "react";
import { useLocation } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import { accountApi } from "../../api/account";
import { MeetingAccountInfo, TransactionNew } from "../../types/account";

const MeetingTransaction = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { account } = location.state as { account: MeetingAccountInfo };
  const [transactionList, setTransactionList] = useState<TransactionNew[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await accountApi.fetchTracsactionHistory(account.groupAccountNo, "KRW", "ASC");
        if (response.status === 200) {
          setTransactionList(response.data.content);
          console.log("모임통장 거래 내역 조회 성공", response.data.content);
        }
      } catch (error) {
        console.error("모임통장 거래 내역 조회 실패", error);
      }
    };

    fetchData();
  }, [account]);

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

  // 그룹화된 날짜 목록을 내림차순으로 정렬
  const dateList = Object.keys(groupedTransactions);

  // 금액을 한국 통화 형식으로 포맷(콤마가 포함된 형태)
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
              navigate(`/meetingaccount/${account.groupId}`, { state: { account } });
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">모임통장</p>
        </div>

        <div className="p-5">
          <div className="my-10 space-y-3">
            <p className="text-zinc-500 underline underline-offset-2">{account.groupAccountNo}</p>
            <p className="text-3xl font-bold">{formatCurrencyNum(account?.moneyBoxDtoList[0].balance)} 원</p>
          </div>

          <div className="flex justify-between">
            <button
              onClick={() => {
                navigate("/transfer/selectbank");
              }}
              className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
              채우기
            </button>
            <button
              onClick={() => {
                navigate("/");
              }}
              className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
              환전
            </button>
          </div>
        </div>

        <div className="w-full h-5 bg-[#F6F6F8]"></div>

        <div className="p-5 overflow-y-auto">
          {dateList.map((date) => (
            <div className="grid gap-5" key={date}>
              <p className="text-zinc-500">
                {new Date(date).toLocaleDateString("ko-KR", {
                  month: "long",
                  day: "numeric",
                })}
              </p>

              {transactionList.map((transaction, index) => (
                <div key={index} className="flex flex-col items-center">
                  <div className="w-full flex justify-between">
                    <p className="text-lg font-semibold">{transaction.transactionSummary}</p>

                    {transaction.transactionType === ("W" || "TW" || "EW" || "SW") ? (
                      <p className="text-lg tracking-wider">- {formatCurrency(transaction.transactionAmount)}원</p>
                    ) : (
                      <p className="text-lg text-[#026CE1] tracking-wider">
                        {formatCurrency(transaction.transactionAmount)}원
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

                    {transaction.transactionType === ("D" || "TD" || "ED" || "SD") ? (
                      <p className="text-sm text-right text-zinc-500 tracking-wider">
                        {formatCurrency(transaction.transactionBalance)}원
                      </p>
                    ) : (
                      <p className="text-sm text-right text-zinc-500 tracking-wider">
                        {formatCurrency(transaction.transactionBalance)}원
                      </p>
                    )}
                  </div>
                </div>
              ))}

              <hr className="mb-5" />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MeetingTransaction;
