import React, { useState } from "react";
import { IoIosArrowBack, IoIosArrowDown } from "react-icons/io";
import { LuDot } from "react-icons/lu";
import { useNavigate } from "react-router";

const ExpenditureTransactionDetail = () => {
  const navigate = useNavigate();
  const [selectAmmount, setSelectAmmount] = useState(0);
  const [checkedNum, setCheckedNum] = useState(0);

  // 날짜별 거래처, 거래 금액, 시간, 지출인지 수입인지를 포함한 거래 내역 더미데이터
  const transactionList = [
    {
      type: "지출",
      time: "2024-09-14T15:00:00",
      place: "candyshopinNewYork",
      amount: 12,
      exchangeRate: 1328.55,
      transactionBalance: 26000,
    },
    {
      type: "지출",
      time: "2024-09-13T16:00:00",
      place: "인천공항면세점",
      amount: 24000,
      exchangeRate: 0,
      transactionBalance: 29000,
    },
    {
      type: "지출",
      time: "2024-09-12T16:00:00",
      place: "투썸플레이스인천공항점",
      amount: 11300,
      exchangeRate: 0,
      transactionBalance: 32000,
    },
    {
      type: "지출",
      time: "2024-09-12T12:00:00",
      place: "스타벅스구미인의점",
      amount: 10000,
      exchangeRate: 0,
      transactionBalance: 37000,
    },
    {
      type: "지출",
      time: "2024-09-12T12:00:00",
      place: "CU진평점",
      amount: 5930,
      exchangeRate: 0,
      transactionBalance: 47000,
    },
    {
      type: "지출",
      time: "2024-09-13T16:00:00",
      place: "CU인동도서관점",
      amount: 3000,
      exchangeRate: 0,
      transactionBalance: 29000,
    },
    {
      type: "지출",
      time: "2024-09-12T16:00:00",
      place: "인천공항면세점",
      amount: 5000,
      exchangeRate: 0,
      transactionBalance: 32000,
    },
    {
      type: "지출",
      time: "2024-09-14T15:00:00",
      place: "인천공항파리바게트",
      amount: 34000,
      exchangeRate: 0,
      transactionBalance: 26000,
    },
    {
      type: "지출",
      time: "2024-09-13T16:00:00",
      place: "CU인천공항점",
      amount: 7000,
      exchangeRate: 0,
      transactionBalance: 29000,
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

  // 모든 날짜(키) 목록을 배열로 반환
  const dateList = Object.keys(groupedTransactions);

  // 금액을 한국 통화 형식으로 포맷(콤마가 포함된 형태)
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const handleChecked = (state: boolean, amount: number) => {
    if (state) {
      setCheckedNum((prev) => prev + 1);
      setSelectAmmount((prev) => prev + amount);
    } else {
      setCheckedNum((prev) => prev - 1);
      setSelectAmmount((prev) => prev - amount);
    }
  };

  const handleNext = () => {
    navigate("/", { state: { selectAmmount } });
  };

  return (
    <div className="h-full pb-8">
      <div className="flex flex-col">
        <div className="p-5 bg-white grid grid-cols-[1fr_3fr_1fr] items-center sticky top-0">
          <IoIosArrowBack
            onClick={() => {
              navigate("/meetingaccount/1");
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">개별지출 정산하기</p>
        </div>

        <div className="p-5 flex items-center space-x-3">
          <p className="text-3xl font-bold">9월</p>
          <IoIosArrowDown className="text-xl text-[#555555]" />
        </div>

        <div className="w-full h-5 bg-[#F6F6F8]"></div>

        <div className="p-5 overflow-y-auto">
          {dateList.map((date) => (
            <div className="grid gap-5" key={date}>
              <p className="text-sm text-zinc-500">
                {new Date(date).toLocaleDateString("ko-KR", {
                  month: "long",
                  day: "numeric",
                })}
              </p>

              {groupedTransactions[date].map((transaction, index) => (
                <label key={index} className="flex justify-between items-center">
                  <div className="flex flex-col justify-between">
                    <div className="flex items-center space-x-1">
                      <p className="text-lg font-bold tracking-wider">
                        - {formatCurrency(transaction.amount)}
                        {transaction.exchangeRate === 0 ? "원" : "$"}
                      </p>
                      {transaction.exchangeRate === 0 ? (
                        <></>
                      ) : (
                        <p className="text-sm text-[#565656] tracking-wider">
                          = {formatCurrency(Number((transaction.amount * transaction.exchangeRate).toFixed(0)))}원 (당시
                          환율
                          {transaction.exchangeRate}원)
                        </p>
                      )}
                    </div>
                    <p className="text-sm text-[#565656] tracking-wider">{transaction.place}</p>
                  </div>

                  <input
                    type="checkbox"
                    className="w-6 h-6 appearance-none bg-[url('./assets/check/nochecked.png')] checked:bg-[url('./assets/check/checked.png')] bg-cover rounded-full"
                    onChange={(e) =>
                      handleChecked(
                        e.target.checked,
                        transaction.exchangeRate === 0
                          ? transaction.amount
                          : Number((transaction.amount * transaction.exchangeRate).toFixed(0))
                      )
                    }
                  />
                </label>
              ))}

              <hr className="mb-5" />
            </div>
          ))}
        </div>

        <div className="w-full p-5 pb-8 bg-white fixed bottom-0 z-50">
          <button
            className={`w-full h-14 text-lg rounded-xl tracking-wide ${
              checkedNum === 0 ? "text-[#565656] bg-[#E3E4E4]" : "text-white bg-[#1429A0]"
            }`}
            disabled={checkedNum === 0}
            onClick={() => handleNext()}>
            {checkedNum === 0 ? "정산금을 선택해주세요" : "정산하기"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ExpenditureTransactionDetail;
