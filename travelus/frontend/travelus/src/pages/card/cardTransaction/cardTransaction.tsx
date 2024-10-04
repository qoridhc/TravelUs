import React, { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import { accountApi } from "../../../api/account";
import { AccountInfoNew, TransactionNew, MeetingAccountInfo } from "../../../types/account";

const CardTransaction = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const groupId = useParams().groupId;
  const [account, setAccount] = useState<AccountInfoNew>();
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [transactionList, setTransactionList] = useState<TransactionNew[]>([]);
  const [domesticTotal, setDomesticTotal] = useState(0); // 국내 금액 누적 합계
  const [foreignTotal, setForeignTotal] = useState(0); // 해외 금액 누적 합계

  const currencySymbols: { [key: string]: string } = {
    KRW: "원",
    USD: "$",
    EUR: "€",
    CNY: "¥",
    JPY: "¥",
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
        if (response.status === 200) {
          const meetingData = response.data;
          setMeeting(meetingData);
          fetchTransactionHistory(meetingData.groupAccountNo);
          console.log("모임 조회 성공", meetingData);
        }
      } catch (error) {
        console.error("모임 조회 에러", error);
      }
    };

    fetchData();
  }, [groupId]);

  const fetchTransactionHistory = async (accountNo: string) => {
    try {
      const data = {
        accountNo: accountNo,
        orderByType: "DESC",
        transactionType: "CW",
      };

      console.log(data);
      const response = await accountApi.fetchTracsactionHistory(data);

      if (response.status === 200) {
        setTransactionList(response.data.content);
        console.log("카드 내역 조회 성공", response.data.content);
        fetchSpecificAccountInfo(accountNo);
        calculateTotals(response.data.content);
      }
    } catch (error) {
      console.error("카드 내역 조회 실패", error);
    }
  };

  const fetchSpecificAccountInfo = async (accountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(accountNo);
      if (response.status === 201) {
        setAccount(response.data);
        console.log("계좌 조회 성공", response.data);
      }
    } catch (error) {
      console.error("계좌 조회 에러", error);
    }
  };

  const formatCardNumber = (cardNumber: string) => {
    // 카드 번호의 앞 4자리와 마지막 4자리를 추출
    const firstFourDigits = cardNumber.slice(0, 4);
    const middleDigits = cardNumber.slice(5, 7);
    const lastFourDigits = cardNumber.slice(-4);

    // 중간 6자리를 '*'로 변환
    const maskedSection = "** ****";

    // 전체 포맷팅된 카드 번호 생성
    return `${firstFourDigits} ${middleDigits}${maskedSection} ${lastFourDigits}`;
  };

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

  const calculateTotals = (transactions: TransactionNew[]) => {
    let domesticSum = 0;
    let foreignSum = 0;

    transactions.forEach((transaction) => {
      if (transaction.currencyCode === "KRW") {
        domesticSum += Number(transaction.transactionAmount);
      } else {
        foreignSum += Number(transaction.transactionAmount);
      }
    });

    setDomesticTotal(domesticSum);
    setForeignTotal(foreignSum);
  };

  return (
    <div className="h-full pb-8">
      <div className="flex flex-col">
        <div className="p-5 grid grid-cols-3 items-center sticky top-0">
          <IoIosArrowBack
            onClick={() => {
              navigate(`/card/${groupId}`, { state: { groupId } });
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">모임카드</p>
        </div>

        {meeting && account && (
          <div className="p-5">
            <div className="mt-8 mb-5 space-y-7">
              {/* <div>
                <p className="text-sm text-zinc-500 underline underline-offset-2">{formatCardNumber(meeting?.cardNumber)}</p>
              </div> */}
              <div className="space-y-5">
                <p className="text-zinc-700">누적 이용 금액</p>
                <div className="space-y-2">
                  <div>
                    <p className="text-sm text-zinc-600">국내</p>
                    <p className="text-2xl font-bold">
                      {formatCurrencyNum(domesticTotal)} {currencySymbols[account?.moneyBoxDtos[0].currencyCode]}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-zinc-600">해외</p>
                    <p className="text-2xl font-bold">
                      {formatCurrencyNum(foreignTotal)} {currencySymbols[account?.moneyBoxDtos[1].currencyCode]}
                    </p>
                  </div>
                </div>
              </div>
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
                      <p className="text-lg font-semibold">{transaction.payeeName}</p>

                      <p className="text-lg tracking-wider">
                        {formatCurrency(transaction.transactionAmount)}
                        {currencySymbols[transaction.currencyCode]}
                      </p>
                    </div>

                    <div className="w-full flex justify-between">
                      <p className="text-sm text-zinc-500 tracking-wider">
                        {new Date(transaction.transactionDate).toLocaleTimeString("ko-KR", {
                          hour: "2-digit",
                          minute: "2-digit",
                          hour12: false,
                        })}
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

export default CardTransaction;
