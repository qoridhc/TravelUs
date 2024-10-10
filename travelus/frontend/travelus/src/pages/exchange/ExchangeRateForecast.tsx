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
import Loading from "../../components/loading/Loading";

interface Props {
  setExchangeRateFront: (rageFront: number) => void;
  setExchangeRateBack: (rageBack: number) => void;
}

const ExchangeRateForecast = ({ setExchangeRateFront, setExchangeRateBack }: Props) => {
  const [predictions, setPredictions] = useState<AllDetailedPredictions | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedCurrency, setSelectedCurrency] = useState<keyof AllDetailedPredictions>("USD");
  const [currency, setCurrency] = useState<string>("USD");
  const [trendInKorean, setTrendInKorean] = useState<string>("상승할");
  const [type, setType] = useState<number | null>(null);

  const [selectPercent, setSelectPercent] = useState<number | null>(null);
  const safeData = [
    {
      rate: 1345.34,
      percent: 90,
    },
    {
      rate: 1344.6,
      percent: 80,
    },
    {
      rate: 1344.44,
      percent: 70,
    },
  ];
  const cheapData = [
    {
      rate: 1344.44,
      percent: 10,
    },
    {
      rate: 1344.6,
      percent: 20,
    },
    {
      rate: 1344.8,
      percent: 30,
    },
  ];

  const navigate = useNavigate();

  // const forecastEntries = Object.entries(prediction.forecast);
  // const latestForecastRate = forecastEntries[forecastEntries.length - 1][1];
  // const earliestForecastRate = forecastEntries[0][1];
  // const currentRate = prediction.current_rate;
  // const forecastChange = latestForecastRate - earliestForecastRate;
  // const isIncreasing = forecastChange >= 0;
  const flagImagePath = `/assets/flag/flagOfUSD.png`;

  const handleNext = () => {
    // const updatedTravelboxInfo = { ...travelboxInfo, currencyCode: currency };
    // dispatch(setTravelboxInfo(updatedTravelboxInfo));
    // navigate("/travelbox/create/auto/exchange/rate", { state: { currency: currency } });
  };

  const handleSelectType = (type: number) => {
    setType(type);
  };

  const handleSelect = (rate: number, percent: number) => {
    const rateSplit = String(rate).split(".n");
    setExchangeRateFront(Number(rateSplit[0]));
    setExchangeRateBack(Number(rateSplit[1]));
    setSelectPercent(percent);
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

  return (
    <div className="grid gap-3">
      <p className="text-lg font-semibold tracking-wide">튜나뱅크가 추천하는 환율이에요</p>

      {/* <div className="flex flex-col space-y-4">
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

      <div className="my-8 h-56">그래프 자리임</div> */}
      {/* <div className="my-8 h-64"><Line data={chartData} options={chartOptions} /></div> */}

      {/* 추천 환율 */}
      {Array(2)
        .fill(null)
        .map((_, idx) => (
          <div className="w-full p-3 rounded-lg bg-[#f4f4f4] flex flex-col border-2 border-transparent" key={idx}>
            <div className="space-y-3">
              <div className="text-lg font-semibold flex space-x-1">
                <p className="text-[#1429A0]">{idx === 0 ? "안전한" : "더 저렴한"}</p>
                <p className="text-zinc-700">자동환전 추천 환율</p>
              </div>

              <div className="grid grid-cols-3 gap-3">
                {(idx === 0 ? safeData : cheapData).map((data, index) => (
                  <div className="flex flex-col items-center justify-center space-y-1" key={index}>
                    <button
                      onClick={() => handleSelect(data.rate, data.percent)}
                      className={`w-full py-2 text-sm font-semibold rounded-3xl border-2 transition-all duration-300 ease-in-out ${
                        selectPercent === data.percent
                          ? "bg-[#1429A0] text-white border-[#1429A0]"
                          : "bg-white text-zinc-600 border-[#b7c4dd]"
                      }`}>
                      {data.rate}원
                    </button>
                    <p className="text-sm text-zinc-500">백분위수 {data.percent}%</p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        ))}
    </div>
  );
};

export default ExchangeRateForecast;
