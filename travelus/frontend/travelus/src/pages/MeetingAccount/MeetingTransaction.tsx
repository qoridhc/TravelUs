import React from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";

const MeetingTransaction = () => {
  const navigate = useNavigate();
  const balance = 29000; // 잔액

  // 날짜별 거래처, 거래 금액, 시간, 지출인지 수입인지를 포함한 거래 내역 더미데이터
  const transactionList = [
    {
      type: "수입",
      time: "2024-09-14T15:00:00",
      place: "프렌즈체크카드캐시백",
      amount: 3000,
      transactionBalance: 26000,
    },
    {
      type: "지출",
      time: "2024-09-13T16:00:00",
      place: "CU인동도서관점",
      amount: 3000,
      transactionBalance: 29000,
    },
    {
      type: "지출",
      time: "2024-09-12T16:00:00",
      place: "GS25진평대로점",
      amount: 5000,
      transactionBalance: 32000,
    },
    {
      type: "지출",
      time: "2024-09-12T12:00:00",
      place: "스타벅스구미인의점",
      amount: 10000,
      transactionBalance: 37000,
    },
    {
      type: "수입",
      time: "2024-09-12T12:00:00",
      place: "CU진평점",
      amount: 1000,
      transactionBalance: 47000,
    },
  ];

  // 날짜별로 그룹화하여 표시
  const groupedTransactions = transactionList.reduce((acc, current) => {
    // 시간 데이터를 한국식 날짜 형식으로 변환
    const date = new Date(current.time).toLocaleDateString("ko-KR");

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
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  return (
    <div className="h-full pb-8">
      <div className="flex flex-col">
        <div className="p-5 grid grid-cols-3 items-center sticky top-0">
          <IoIosArrowBack
            onClick={() => {
              navigate("/meetingaccount/1");
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">모임통장</p>
        </div>

        <div className="p-5">
          <div className="my-10 space-y-3">
            <p className="text-zinc-500 underline underline-offset-2">110-455-247307</p>
            <p className="text-3xl font-bold">{formatCurrency(balance)} 원</p>
          </div>

          <div className="flex justify-between">
            <button className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">채우기</button>
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

              {groupedTransactions[date].map((transaction, index) => (
                <div key={index} className="flex flex-col items-center">
                  <div className="w-full flex justify-between">
                    <p className="text-lg font-semibold">{transaction.place}</p>

                    {transaction.type === "지출" ? (
                      <p className="text-lg tracking-wider">- {formatCurrency(transaction.amount)}원</p>
                    ) : (
                      <p className="text-lg text-[#026CE1] tracking-wider">{formatCurrency(transaction.amount)}원</p>
                    )}
                  </div>

                  <div className="w-full flex justify-between">
                    <p className="text-sm text-zinc-500 tracking-wider">
                      {new Date(transaction.time).toLocaleTimeString("ko-KR", {
                        hour: "2-digit",
                        minute: "2-digit",
                        hour12: false,
                      })}
                    </p>

                    {transaction.type === "지출" ? (
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
