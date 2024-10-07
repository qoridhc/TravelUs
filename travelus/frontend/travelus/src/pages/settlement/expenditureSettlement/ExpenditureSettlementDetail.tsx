import React from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";

const ExpenditureSettlementDetail = () => {
  const navigate = useNavigate();
  const list = [
    {
      name: "이예림",
      amount: 100,
      status: true,
    },
    {
      name: "박민규",
      amount: 100,
      status: false,
    },
  ];

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-20">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" onClick={() => navigate("/settlement/expenditure/list/NOT_COMPLETED")} />
        </div>

        <div className="grid gap-8">
          <div>
            <p className="">총 {formatCurrency(200)}원 중</p>
            <p className="text-3xl font-bold">{formatCurrency(100)}원 완료</p>
          </div>

          <div className="grid gap-3">
            <hr />
            <p className="text-sm font-light text-[#8F8F8F]">요청일 2024년 10월 4일 15:43</p>
          </div>

          <div className="grid gap-5">
            {list.map((item, index) => (
              <div className="flex justify-between items-center" key={index}>
                <div className="text-lg flex items-center space-x-3">
                  <img className="w-10 h-10" src="/assets/user/userIconSample.png" alt="" />
                  <p>{item.name}</p>
                </div>
                <div className={`text-lg ${item.status ? "" : "text-[#8F8F8F]"} flex space-x-3`}>
                  <p>{formatCurrency(item.amount)}원</p>
                  <p className={`w-14 text-right `}>{item.status ? "완료" : "미완료"}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <button className="w-full h-14  text-white bg-[#1429A0] rounded-xl">{formatCurrency(100)}원 보내기</button>
    </div>
  );
};

export default ExpenditureSettlementDetail;
