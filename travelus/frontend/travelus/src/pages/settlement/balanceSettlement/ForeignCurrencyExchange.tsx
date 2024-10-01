import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { TiArrowUnsorted } from "react-icons/ti";
import { useLocation, useNavigate, useParams } from "react-router";
import { currencyNames, ExchangeRateInfo } from "../../../types/exchange";
import { exchangeRateApi } from "../../../api/exchange";

const ForeignCurrencyExchange = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams();

  const [foreignAmount, setForeignAmount] = useState(0);
  const [exchangeRate, setExchangeRate] = useState<ExchangeRateInfo>();

  const handleExchange = () => {
    navigate(`/settlement/balance/participants/${id}`, {
      state: {
        foreignAmount: foreignAmount,
        currencyCode: location.state.foriegnInfo.currencyCode,
      },
    });
  };

  // 금액을 한국 통화 형식으로 포맷(콤마가 포함된 형태)
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, "0");

    return `${month}월 ${day}일 ${hours}:${minutes}시 기준`;
  };

  // 환율 정보 가져오기
  const fetchExchangeRate = async () => {
    try {
      const data = await exchangeRateApi.getExchangeRate("USD");
      setExchangeRate(data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchExchangeRate();
  }, []);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="grid grid-cols-3">
          <div className="flex items-center">
            <IoIosArrowBack className="text-2xl" />
          </div>
          <p className="text-lg text-center">재환전</p>
        </div>

        <div className="grid gap-8">
          <p className="text-2xl font-semibold tracking-wide">
            남은 외화는
            <br />
            원화로 바꿔주세요
          </p>

          <div className="grid gap-3">
            <div>
              <p className="text-xs text-right text-[#9e9e9e]">
                {formatDate(exchangeRate?.created ? exchangeRate?.created : "")}
              </p>
              <div className="flex justify-between items-center">
                <p className="text-lg font-semibold">원화로 바꾸기</p>
                <p className="text-sm text-[#515151]">
                  1 {location.state.foriegnInfo.currencyCode} = {exchangeRate?.exchangeRate} 원
                </p>
              </div>
            </div>

            <div className="flex flex-col items-center">
              {/* 외화정보 */}
              <div className="p-5 bg-[#F3F4F6] rounded-xl grid grid-cols-2">
                <div className="grid gap-1">
                  <div className="flex items-center space-x-2">
                    <img
                      className="w-6 h-4 border"
                      src={`/assets/flag/flagOf${location.state.foriegnInfo.currencyCode}.png`}
                      alt=""
                    />
                    <p>{currencyNames[location.state.foriegnInfo.currencyCode]}</p>
                  </div>

                  <p className="text-sm text-[#555555]">
                    잔액 {formatCurrency(location.state.foriegnInfo.balance)} {location.state.foriegnInfo.currencyCode}
                  </p>
                </div>

                <div className="flex justify-end items-center space-x-2">
                  <input
                    type="number"
                    className="w-2/3 text-right bg-[#F3F4F6] outline-none placeholder:text-black"
                    placeholder="0"
                    onChange={(e) => setForeignAmount(parseFloat(e.target.value))}
                  />
                  <p>{location.state.foriegnInfo.currencyCode}</p>
                </div>
              </div>

              {/* 양방향 화살표 아이콘 */}
              <div className="w-8 h-8 bg-white shadow-md border-[0.5px] border-[#cccccc] rounded-full flex justify-center items-center">
                <TiArrowUnsorted className="text-sm text-[#999999]" />
              </div>

              {/* 원화정보 */}
              <div className="p-5 bg-[#F3F4F6] rounded-xl grid grid-cols-2">
                <div className="flex items-center space-x-2">
                  <img className="w-6 h-4 border" src="/assets/flag/flagOfKRW.png" alt="" />
                  <p>대한민국 원</p>
                </div>

                <div className="text-[#949494] flex justify-end items-center space-x-2">
                  <input
                    type="number"
                    className="w-2/3 text-right bg-[#F3F4F6] outline-none"
                    value={exchangeRate ? Math.ceil(exchangeRate.exchangeRate * foreignAmount) : 0}
                  />
                  <p>KRW</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <button
        className={`w-full h-14 text-lg rounded-xl tracking-wide ${
          exchangeRate?.exchangeMin && foreignAmount < exchangeRate?.exchangeMin
            ? "text-[#565656] bg-[#E3E4E4]"
            : "text-white bg-[#1429A0]"
        }`}
        onClick={() => handleExchange()}
        disabled={exchangeRate?.exchangeMin !== undefined && foreignAmount < exchangeRate?.exchangeMin}>
        재환전
      </button>
    </div>
  );
};

export default ForeignCurrencyExchange;
