import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { accountApi } from "../../api/account";
import { IoIosArrowBack } from "react-icons/io";
import { AccountInfoNew, TransactionNew } from "../../types/account";

interface AccountTransactionProps {}

const AccountTransaction: React.FC<AccountTransactionProps> = (props) => {
  const navigate = useNavigate();
  const accountNo = useParams().accountNo;
  const [account, setAccount] = useState<AccountInfoNew>();
  const [transactionList, setTransactionList] = useState<TransactionNew[]>([]);
  const depositTransactionType = ["D", "TD", "ED", "SD", "CD"];
  const withdrawTransactionType = ["W", "TW", "EW", "SW"];

  useEffect(() => {
    // 병렬로 데이터를 불러오는 함수
    const fetchData = async () => {
      if (!accountNo) return;

      try {
        // 두 API를 병렬로 호출
        const [transactionResponse, accountResponse] = await Promise.all([
          accountApi.fetchTracsactionHistory(accountNo, "KRW", "DESC"),
          accountApi.fetchSpecificAccountInfo(accountNo),
        ]);

        // 거래 내역 조회가 성공했을 경우 처리
        if (transactionResponse.status === 200) {
          setTransactionList(transactionResponse.data.content);
        }

        // 계좌 정보 조회가 성공했을 경우 처리
        if (accountResponse.status === 201) {
          setAccount(accountResponse.data);
        }

        console.log("데이터 조회 성공", transactionResponse.data.content);
      } catch (error) {
        console.error("데이터 조회 실패", error);
      }
    };

    fetchData();
  }, [accountNo]);

  const formatCurrency = (amount: string) => {
    const numAmount = Number(amount);
    return new Intl.NumberFormat("ko-KR").format(numAmount);
  };

  const formatCurrencyNum = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
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

  // 그룹화된 날짜 목록을 내림차순으로 정렬
  const dateList = Object.keys(groupedTransactions);

  return (
    <div className="h-full pb-8">
      <div className="flex flex-col">
        <div className="p-5 grid grid-cols-3 items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate("/");
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">입출금통장</p>
        </div>

        <div className="p-5">
          <div className="my-10 space-y-3">
            <p className="text-zinc-500 underline">{accountNo}</p>
            {account && <p className="text-3xl font-bold">{formatCurrencyNum(account.moneyBoxDtos[0].balance)} 원</p>}
          </div>
          <div className="flex justify-between">
            <button className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">카드</button>
            <button
              onClick={() => {
                navigate("/transfer/selectbank");
              }}
              className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
              이체
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
                    <p className="text-lg font-semibold">{transaction.transactionSummary}</p>

                    {withdrawTransactionType.includes(transaction.transactionType) ? (
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

                    {depositTransactionType.includes(transaction.transactionType) ? (
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

export default AccountTransaction;
