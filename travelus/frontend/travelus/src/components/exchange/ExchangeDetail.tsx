import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  ChartData,
  ChartOptions,
} from "chart.js";
import { ChevronLeft } from "lucide-react";
import { exchangeApi } from "../../api/exchange";
import { ExchangeRateInfo2, CurrencyPrediction, RecentRates } from "../../types/exchange";
import { setupChart } from "../../utils/chartSetup";
import { calculateChange, formatCurrency, getLatestRate } from "../../utils/currencyUtils";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

// 국가 이름 매핑
const countryNameMapping: { [key: string]: string } = {
  EUR: "Europe",
  JPY: "Japan",
  USD: "TheUnitedStates",
  CNY: "China",
};

const getFlagImagePath = (currencyCode: string): string => {
  const countryName = countryNameMapping[currencyCode] || currencyCode;
  return `/assets/flag/flagOf${countryName}.png`;
};

// Types
type PeriodKey = "1주" | "1달" | "3달";
type TabType = "exchange" | "prediction";

// Utility functions
const isCurrencyPrediction = (data: any): data is CurrencyPrediction => {
  return data && "forecast" in data && "recent_rates" in data;
};

const isRecentRatesOnly = (data: any): data is { recent_rates: RecentRates } => {
  return data && "recent_rates" in data && !("forecast" in data);
};

// Component
const ExchangeDetail: React.FC = () => {
  const { currencyCode = "" } = useParams<{ currencyCode: string }>();
  const navigate = useNavigate();
  const chartRef = useRef<ChartJS<"line">>(null);

  const [exchangeData, setExchangeData] = useState<ExchangeRateInfo2 | null>(null);
  const [historicalData, setHistoricalData] = useState<{ date: string; rate: number }[]>([]);
  const [predictionData, setPredictionData] = useState<{ date: string; rate: number }[]>([]);
  const [selectedPeriod, setSelectedPeriod] = useState<PeriodKey>("1주");
  const [activeTab, setActiveTab] = useState<TabType>("exchange");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await exchangeApi.getPrediction();
        const currencyData = response[currencyCode as keyof typeof response];

        if (typeof currencyData === "string") {
          console.error("Unexpected string data for currency:", currencyCode);
          return;
        }

        if (isCurrencyPrediction(currencyData) || isRecentRatesOnly(currencyData)) {
          const latestRate = getLatestRate(currencyData.recent_rates["1_week"]);
          setExchangeData({
            currencyCode,
            exchangeRate: latestRate,
            lastUpdated: response.last_updated,
          });

          const historicalRates: Record<PeriodKey, { [date: string]: number }> = {
            "1주": currencyData.recent_rates["1_week"],
            "1달": currencyData.recent_rates["1_month"],
            "3달": currencyData.recent_rates["3_months"],
          };

          setHistoricalData(Object.entries(historicalRates[selectedPeriod]).map(([date, rate]) => ({ date, rate })));

          if (isCurrencyPrediction(currencyData)) {
            setPredictionData(Object.entries(currencyData.forecast).map(([date, rate]) => ({ date, rate })));
          }
        } else {
          console.error("Unexpected data structure for currency:", currencyCode);
        }
      } catch (error) {
        console.error("Failed to fetch exchange rate data:", error);
      }
    };

    fetchData();
  }, [currencyCode, selectedPeriod]);

  const handlePeriodChange = (newPeriod: PeriodKey) => setSelectedPeriod(newPeriod);

  const chartData: ChartData<"line"> = {
    labels: (activeTab === "exchange" ? historicalData : predictionData).map((data) => data.date),
    datasets: [
      {
        label: activeTab === "exchange" ? "실제 환율" : "예측 환율",
        data: (activeTab === "exchange" ? historicalData : predictionData).map((data) => data.rate),
        borderColor: activeTab === "exchange" ? "rgb(75, 192, 192)" : "rgb(255, 99, 132)",
        tension: 0.1,
      },
    ],
  };

  const chartOptions: ChartOptions<"line"> = setupChart(currencyCode, formatCurrency);

  // Render functions
  const renderExchangeTab = () => (
    <>
      <div className="bg-gray-100 rounded-md p-4 mb-6">
        <h2 className="text-2xl font-semibold mb-2">{formatCurrency(exchangeData?.exchangeRate || 0, currencyCode)}</h2>
        <p
          className={`${
            historicalData[0]?.rate < historicalData[historicalData.length - 1]?.rate ? "text-red-500" : "text-blue-500"
          }`}>
          전일대비{" "}
          {formatCurrency(
            Math.abs(calculateChange(historicalData[0]?.rate, historicalData[historicalData.length - 1]?.rate)),
            currencyCode
          )}
          {historicalData[0]?.rate < historicalData[historicalData.length - 1]?.rate ? "▲" : "▼"}
        </p>
      </div>
      <div className="flex justify-between mb-6">
        {(["1주", "1달", "3달"] as const).map((period) => (
          <button
            key={period}
            onClick={() => handlePeriodChange(period)}
            className={`px-3 py-1 rounded-md ${
              selectedPeriod === period
                ? "bg-white text-[#353535] font-bold"
                : "bg-gray-200 text-gray-700 hover:bg-gray-300"
            }`}>
            {period}
          </button>
        ))}
      </div>
      {renderChart()}
    </>
  );

  const renderPredictionTab = () => (
    <>
      {["EUR", "CNY"].includes(currencyCode) ? (
        <div className="text-center py-8">
          <p>이 통화는 환율 예측을 지원하지 않습니다.</p>
        </div>
      ) : (
        <>
          <div className="bg-gray-100 rounded-md p-4 mb-6">
            <h2 className="text-2xl font-semibold mb-2">
              예측 평균:
              {formatCurrency(
                predictionData.reduce((sum, data) => sum + data.rate, 0) / predictionData.length,
                currencyCode
              )}
            </h2>
            <p
              className={`${
                predictionData[0]?.rate < predictionData[predictionData.length - 1]?.rate
                  ? "text-red-500"
                  : "text-blue-500"
              }`}>
              예측 변화:{" "}
              {formatCurrency(
                Math.abs(predictionData[predictionData.length - 1]?.rate - predictionData[0]?.rate),
                currencyCode
              )}
              {predictionData[0]?.rate < predictionData[predictionData.length - 1]?.rate ? "▲" : "▼"}
            </p>
          </div>
          {renderChart()}
        </>
      )}
    </>
  );

  const renderChart = () => (
    <div className="mb-6 h-64">
      <Line data={chartData} options={chartOptions as ChartOptions<"line">} ref={chartRef} />
    </div>
  );

  if (!exchangeData) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  return (
    <div className="container mx-auto max-w-md h-full p-5 pb-8">
      <button onClick={() => navigate(-1)} className="flex items-center text-blue-600 mb-4">
        <ChevronLeft className="w-5 h-5 mr-1" />
      </button>
      <div className="flex items-center mb-4">
        <img src={getFlagImagePath(currencyCode)} alt={`${currencyCode} Flag`} className="w-8 h-6 mr-2" />
        <h1 className="text-2xl font-bold">{currencyCode}</h1>
      </div>
      <div className="flex mb-6">
        {(["exchange", "prediction"] as const).map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`flex-1 py-2 text-center ${
              activeTab === tab
                ? "bg-white text-[#353535] font-bold border-b-2 border-[#353535]"
                : "bg-gray-100 text-gray-600"
            }`}>
            {tab === "exchange" ? "환율" : "환율 예측"}
          </button>
        ))}
      </div>
      {activeTab === "exchange" ? renderExchangeTab() : renderPredictionTab()}
      <div className="flex justify-between mt-auto">
        <button
          onClick={() => navigate("/exchangekrw")}
          className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
          원화로 바꾸기
        </button>
        <button
          onClick={() => navigate("/exchange")}
          className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
          외화 채우기
        </button>
      </div>
    </div>
  );
};

export default ExchangeDetail;
