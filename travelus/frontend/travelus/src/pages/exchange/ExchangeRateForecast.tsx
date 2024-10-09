import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Line } from "react-chartjs-2";
import { ChartData } from "chart.js";
import { IoIosArrowBack } from "react-icons/io";
import { exchangeApi } from "../../api/exchange";
import { AllDetailedPredictions, PredictionCurrencyData } from "../../types/exchange";
import { forecastChartSetup } from "../../utils/forecastChartSetup";
import { formatExchangeRate } from "../../utils/currencyUtils";
import { IoCaretUpOutline } from "react-icons/io5";
import { IoCaretDownOutline } from "react-icons/io5";

const ExchangeRateForecast: React.FC = () => {
  const [predictions, setPredictions] = useState<AllDetailedPredictions | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedCurrency, setSelectedCurrency] = useState<keyof AllDetailedPredictions>("USD");
  const [currency, setCurrency] = useState<string>("USD");
  const [trendInKorean, setTrendInKorean] = useState<string>("상승할");

  const navigate = useNavigate();

  useEffect(() => {
    const loadPredictions = async () => {
      try {
        const data = await exchangeApi.getPrediction();
        setPredictions(data);
        setLoading(false);
      } catch (err) {
        setError("Failed to load predictions");
        setLoading(false);
      }
    };

    loadPredictions();
  }, []);

  // const forecastEntries = Object.entries(prediction.forecast);
  // const latestForecastRate = forecastEntries[forecastEntries.length - 1][1];
  // const earliestForecastRate = forecastEntries[0][1];
  // const currentRate = prediction.current_rate;
  // const forecastChange = latestForecastRate - earliestForecastRate;
  // const isIncreasing = forecastChange >= 0;
  const flagImagePath = `/assets/flag/flagOfUSD.png`;

  const [type, setType] = useState<number | null>(null);

  const handleNext = () => {
    // const updatedTravelboxInfo = { ...travelboxInfo, currencyCode: currency };
    // dispatch(setTravelboxInfo(updatedTravelboxInfo));
    // navigate("/autocurrencyexchangeofcreatetravelbox", { state: { currency: currency } });
  };

  const handleSelectType = (type: number) => {
    setType(type);
  };

  // const trendInKorean = getTrendInKorean(prediction.trend);

  // 추천 희망 환율 계산 함수
  // const getRecommendedRates = () => {
  //   const forecastRates = Object.values(prediction.forecast);
  //   const stats = prediction.forecast_stats;

  //   // 1. 최소한의 이득을 원하는 사람
  //   const minProfitRate = forecastRates.find((rate) => rate < currentRate) || currentRate;

  //   // 2. 최대의 이득을 원하는 사람
  //   const maxProfitRate = {
  //     min: stats.min,
  //     avg: stats.average,
  //     max: stats.max,
  //   };

  //   // 3. 추천 희망 환율 (10% ~ 90% 사이)
  //   const recommendedRate = {
  //     p10: stats.p10,
  //     p90: stats.p90,
  //   };

  //   return { minProfitRate, maxProfitRate, recommendedRate };
  // };

  // const recommendedRates = getRecommendedRates();

  // const chartData: ChartData<"line"> = {
  //   labels: Object.keys(prediction.forecast),
  //   datasets: [
  //     {
  //       label: `${currency} 예측`,
  //       data: Object.values(prediction.forecast),
  //       borderColor: prediction.trend === "UPWARD" ? "rgb(221, 82, 87)" : "rgb(72, 128, 238)",
  //       tension: 0.1,
  //     },
  //   ],
  // };

  // const chartOptions = forecastChartSetup(currency, formatExchangeRate, prediction.trend);

  return (
    <div className="min-h-screen p-5 pb-8">
      <button onClick={() => navigate(-1)} className="flex items-center mb-6">
        <IoIosArrowBack className="text-2xl" />
      </button>

      <div className="flex flex-col space-y-3">
        <div className="flex flex-col space-y-2">
          <div>
            <p className="text-2xl font-semibold">자동환전 희망 환율 설정</p>
            {/* <p className="text-2xl font-semibold">희망 환율을 선택해주세요</p> */}
          </div>
          <p className="text-sm text-zinc-500">환율 예측을 토대로 자동환전 희망 환율을 추천해요</p>
        </div>

        <div className="flex flex-col space-y-4">
          <hr />
          <div className="ml-1 flex items-center">
            <img src={flagImagePath} alt={`${currency} Flag`} className="w-6 h-5 mr-2 rounded-sm" />
            <h1 className="text-xl font-semibold">미국 달러</h1>
          </div>

          <div className="flex justify-between items-center">
            <div className="space-y-1">
              <p className="text-sm text-zinc-500">실시간 환율</p>
              <p className="text-xl font-semibold">1,346.14원</p>
            </div>
            <div className="text-sm flex items-center space-x-1">
              <p className="text-zinc-400">이후 2주동안</p>
              <IoCaretUpOutline className="text-red-500" />
              <p className="text-red-500">예측</p>
            </div>
          </div>
        </div>

        <div className="my-8 h-56">그래프 자리임</div>
        {/* <div className="my-8 h-64"><Line data={chartData} options={chartOptions} /></div> */}

        {/* 추천 환율 */}
        <div>
          <div className="flex flex-col space-y-5">

            <div
              className={`w-full p-3 rounded-lg bg-[#f4f4f4] flex flex-col border-2 border-transparent
                `}>
              <div className="space-y-2">
                <div className="text-zinc-700 flex space-x-1">
                  <p className="text-[#1429A0] font-semibold">안전한</p>
                  <p className="font-semibold">자동환전 추천 환율</p>
                </div>
                <div className="grid grid-cols-3">
                  <div className="flex flex-col items-center justify-center space-y-1">
                    <button
                      onClick={() => handleSelectType(1)}
                      className={`py-2 px-3 text-sm font-semibold rounded-3xl border-2 transition-all duration-300 ease-in-out ${
                        type === 1
                          ? "bg-[#1429A0] text-white border-[#1429A0]"
                          : "bg-white text-zinc-600 border-[#b7c4dd]"
                      }`}>
                      1345.34원
                    </button>
                    <p className="text-xs text-zinc-500">하위 10%</p>
                  </div>
                  <div className="flex flex-col items-center justify-center space-y-1">
                    <button
                      onClick={() => handleSelectType(2)}
                      className={`py-2 px-3 text-sm font-semibold rounded-3xl border-2 transition-all duration-300 ease-in-out ${
                        type === 2
                          ? "bg-[#1429A0] text-white border-[#1429A0]"
                          : "bg-white text-zinc-600 border-[#b7c4dd]"
                      }`}>
                      1345.20원
                    </button>
                    <p className="text-xs text-zinc-500">하위 20%</p>
                  </div>
                  <div className="flex flex-col items-center justify-center space-y-1">
                    <button
                      onClick={() => handleSelectType(3)}
                      className={`py-2 px-3 text-sm font-semibold rounded-3xl border-2 transition-all duration-300 ease-in-out ${
                        type === 3
                          ? "bg-[#1429A0] text-white border-[#1429A0]"
                          : "bg-white text-zinc-600 border-[#b7c4dd]"
                      }`}>
                      1345.10원
                    </button>
                    <p className="text-xs text-zinc-500">하위 30%</p>
                  </div>
                </div>
              </div>
            </div>

            <div
              className={`w-full p-3 rounded-lg bg-[#f4f4f4] flex flex-col border-2 border-transparent
                `}>
              <div className="space-y-2">
                <div className="text-zinc-700 flex space-x-1">
                  <p className="text-[#1429A0] font-semibold">더 저렴한</p>
                  <p className="font-semibold">자동환전 추천 환율</p>
                </div>
                <div className="grid grid-cols-3">
                  <div className="flex flex-col items-center justify-center space-y-1">
                    <button
                      onClick={() => handleSelectType(4)}
                      className={`py-2 px-3 text-sm font-semibold rounded-3xl border-2 transition-all duration-300 ease-in-out ${
                        type === 4
                          ? "bg-[#1429A0] text-white border-[#1429A0]"
                          : "bg-white text-zinc-600 border-[#b7c4dd]"
                      }`}>
                      1344.80원
                    </button>
                    <p className="text-xs text-zinc-500">상위 30%</p>
                  </div>
                  <div className="flex flex-col items-center justify-center space-y-1">
                    <button
                      onClick={() => handleSelectType(5)}
                      className={`py-2 px-3 text-sm font-semibold rounded-3xl border-2 transition-all duration-300 ease-in-out ${
                        type === 5
                          ? "bg-[#1429A0] text-white border-[#1429A0]"
                          : "bg-white text-zinc-600 border-[#b7c4dd]"
                      }`}>
                      1344.60원
                    </button>
                    <p className="text-xs text-zinc-500">상위 20%</p>
                  </div>
                  <div className="flex flex-col items-center justify-center space-y-1">
                    <button
                      onClick={() => handleSelectType(6)}
                      className={`py-2 px-3 text-sm font-semibold rounded-3xl border-2 transition-all duration-300 ease-in-out ${
                        type === 6
                          ? "bg-[#1429A0] text-white border-[#1429A0]"
                          : "bg-white text-zinc-600 border-[#b7c4dd]"
                      }`}>
                      1344.44원
                    </button>
                    <p className="text-xs text-zinc-500">상위 10%</p>
                  </div>
                </div>
              </div>
            </div>

            <div
              onClick={() => handleSelectType(7)}
              className={`w-full p-3 rounded-lg bg-[#f4f4f4] flex flex-col transition-all duration-300 ease-in-out ${
                type === 7 ? "border-2 border-[#1429A0] shadow-lg" : "border-2 border-transparent"
              }`}>
              <div className="space-y-3">
                <div className="text-zinc-700 font-semibold flex space-x-1">
                  <p className="text-[#1429A0]">직접</p>
                  <p className="">자동환전 희망 환율</p>
                  <p>선택할래요</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ExchangeRateForecast;
