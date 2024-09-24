import React from "react";
import { useNavigate } from "react-router-dom";
import { IoIosArrowBack } from "react-icons/io";

interface AccountTransactionProps {}

const AccountTransaction: React.FC<AccountTransactionProps> = (props) => {
  const navigate = useNavigate();
  const balance = 29000; // 잔액

  // 날짜별 거래처, 거래 금액, 시간, 지출인지 수입인지를 포함한 거래 내역 더미데이터
  const transactionList = [
    {
      transaction: "지출",
      time: "2024-09-12T16:00:00",
      companyName: "GS25 진평대로점",
      amount: 5000,
    },
    {
      transaction: "지출",
      time: "2024-09-12T12:00:00",
      companyName: "스타벅스 구미인의점",
      amount: 10000,
    },
    {
      transaction: "수입",
      time: "2024-09-14T15:00:00",
      companyName: "프렌즈체크카드캐시백",
      amount: 3000,
    },
    {
      transaction: "지출",
      time: "2024-09-13T16:00:00",
      companyName: "CU 인동도서관점",
      amount: 3000,
    },
  ];

  // 시간 기준으로 거래 내역 정렬 (내림차순)
  const sortedTransactionList = transactionList.sort((a, b) => {
    return new Date(b.time).getTime() - new Date(a.time).getTime();
  });

  // 날짜별로 그룹화하여 표시
  const groupedTransactions = sortedTransactionList.reduce((acc, current) => {
    const date = new Date(current.time).toLocaleDateString("ko-KR"); // 날짜만 추출
    if (!acc[date]) {
      acc[date] = [];
    }
    acc[date].push(current);
    return acc;
  }, {} as { [key: string]: typeof transactionList });

  // 그룹화된 날짜 목록을 내림차순으로 정렬
  const sortedDates = Object.keys(groupedTransactions).sort((a, b) => {
    return new Date(b).getTime() - new Date(a).getTime();
  });

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  return (
    <div className="h-full pb-8">
      <div className="flex flex-col">
        <div className="p-5 grid grid-cols-3 items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate("/meetingaccount/1");
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">입출금통장</p>
        </div>

        <div className="p-5">
          <div className="my-10 space-y-3">
            <p className="text-zinc-500 underline">110-455-247307</p>
            <p className="text-3xl font-bold">{formatCurrency(balance)} 원</p>
          </div>
          <div className="flex justify-between">
            <button className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">카드</button>
            <button
              onClick={()=>{navigate("/transfer/selectbank")}}
              className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">이체</button>
          </div>
        </div>

        <div className="w-full h-5 bg-[#F6F6F8]"></div>

        <div className="p-5">
          {sortedDates.map((date) => (
            <div key={date}>
              <p className="text-zinc-500 py-3">{date}</p>
              {groupedTransactions[date].map((transaction, index) => (
                <div key={index} className="flex justify-between items-center py-2">
                  <div className="flex flex-col">
                    <p className="text-lg font-bold">{transaction.companyName}</p>
                    <p className="text-sm text-zinc-500">
                      {new Date(transaction.time).toLocaleTimeString("ko-KR", {
                        hour: "2-digit",
                        minute: "2-digit",
                        hour12: false,
                      })}
                    </p>
                  </div>
                  <div className="flex flex-col">
                    {transaction.transaction === "지출" ? (
                      <p className="text-lg">- {transaction.amount} 원</p>
                    ) : (
                      <p className="text-lg text-[#026CE1]">{transaction.amount} 원</p>
                    )}
                  </div>
                </div>
              ))}
              <hr className="my-3" />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AccountTransaction;
