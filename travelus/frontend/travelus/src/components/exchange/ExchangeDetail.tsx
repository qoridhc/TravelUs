import React, { useState, useEffect } from "react";
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
} from "chart.js";
import { ChevronLeft } from "lucide-react";
import { exchangeApi } from "../../api/exchange";
import { ExchangeRateInfo2, currencyNames, CurrencyPrediction, RecentRates } from "../../types/exchange";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const countryNameMapping: { [key: string]: string } = {
  EUR: "Europe",
  JPY: "Japan",
  USD: "TheUnitedStates",
  CNY: "China",
};

const getFlagImagePath = (currencyCode: string) => {
  const countryName = countryNameMapping[currencyCode] || currencyCode;
  return `/assets/flag/flagOf${countryName}.png`;
};

// 타입 가드 함수들
function isCurrencyPrediction(data: any): data is CurrencyPrediction {
  return data && "forecast" in data && "recent_rates" in data;
}

function isRecentRatesOnly(data: any): data is { recent_rates: RecentRates } {
  return data && "recent_rates" in data && !("forecast" in data);
}

type PeriodKey = "1주" | "1달" | "3달";

const formatCurrency = (value: number, currencyCode: string) => {
  const formatter = new Intl.NumberFormat("ko-KR", {
    style: "currency",
    currency: currencyCode,
    minimumFractionDigits: currencyCode === "KRW" ? 0 : 2,
    maximumFractionDigits: currencyCode === "KRW" ? 0 : 2,
  });
  return formatter.format(value);
};

const ExchangeDetail: React.FC = () => {
  const { currencyCode } = useParams<{ currencyCode: string }>();
  const navigate = useNavigate();
  const [exchangeData2, setExchangeData2] = useState<ExchangeRateInfo2 | null>(null);
  const [historicalData, setHistoricalData] = useState<{ date: string; rate: number }[]>([]);
  const [predictionData, setPredictionData] = useState<{ date: string; rate: number }[]>([]);
  const [selectedPeriod, setSelectedPeriod] = useState<PeriodKey>("1주");
  const [activeTab, setActiveTab] = useState<"exchange" | "prediction">("exchange");

  const chartRef = React.useRef<ChartJS>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (currencyCode) {
        try {
          const response = await exchangeApi.getPrediction();
          const currencyData = response[currencyCode as keyof typeof response];

          if (typeof currencyData === "string") {
            console.error("Unexpected string data for currency:", currencyCode);
            return;
          }

          if (isCurrencyPrediction(currencyData) || isRecentRatesOnly(currencyData)) {
            const latestDate = Object.keys(currencyData.recent_rates["1_week"]).pop();

            if (latestDate) {
              setExchangeData2({
                currencyCode,
                exchangeRate: currencyData.recent_rates["1_week"][latestDate],
                lastUpdated: response.last_updated,
              });

              const historicalRates: Record<PeriodKey, { [date: string]: number }> = {
                "1주": currencyData.recent_rates["1_week"],
                "1달": currencyData.recent_rates["1_month"],
                "3달": currencyData.recent_rates["3_months"],
              };

              setHistoricalData(
                Object.entries(historicalRates[selectedPeriod]).map(([date, rate]) => ({ date, rate }))
              );

              if (isCurrencyPrediction(currencyData)) {
                setPredictionData(Object.entries(currencyData.forecast).map(([date, rate]) => ({ date, rate })));
              }
            }
          } else {
            console.error("Unexpected data structure for currency:", currencyCode);
          }
        } catch (error) {
          console.error("Failed to fetch exchange rate data:", error);
        }
      }
    };
    fetchData();
  }, [currencyCode, selectedPeriod]);

  const handlePeriodChange = (newPeriod: PeriodKey) => {
    setSelectedPeriod(newPeriod);
  };

  const chartData = {
    labels: (activeTab === "exchange" ? historicalData : predictionData).map((data) => data.date),
    datasets: [
      {
        label: activeTab === "exchange" ? "실제 환율" : "예측 환율",
        data: (activeTab === "exchange" ? historicalData : predictionData).map((data) => data.rate),
        fill: false,
        borderColor: activeTab === "exchange" ? "rgb(75, 192, 192)" : "rgb(255, 99, 132)",
        tension: 0.1,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      title: {
        display: false,
      },
      tooltip: {
        enabled: false,
      },
    },
    scales: {
      x: {
        display: false,
      },
      y: {
        display: false,
      },
    },
    // 최고값과 최저값을 표시하기 위한 플러그인
    plugins: [
      {
        afterDraw: (chart: ChartJS) => {
          const ctx = chart.ctx;
          const yAxis = chart.scales["y"];
          const xAxis = chart.scales["x"];
          const dataset = chart.data.datasets[0];
          const data = dataset.data as number[];

          const max = Math.max(...data);
          const min = Math.min(...data);

          data.forEach((value, index) => {
            if (value === max || value === min) {
              const x = xAxis.getPixelForValue(index);
              const y = yAxis.getPixelForValue(value);
              ctx.fillStyle = "black";
              ctx.textAlign = "center";
              ctx.fillText(formatCurrency(value, currencyCode || "KRW"), x, y - 10);
            }
          });
        },
      },
    ],
  };

  if (!exchangeData2) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  return (
    <div className="container mx-auto max-w-md h-full p-5 pb-8">
      <button onClick={() => navigate(-1)} className="flex items-center text-blue-600 mb-4">
        <ChevronLeft className="w-5 h-5 mr-1" />
      </button>
      <div className="flex items-center mb-4">
        <img
          src={getFlagImagePath(currencyCode || "")}
          alt={`${currencyNames[currencyCode || ""]} Flag`}
          className="w-8 h-6 mr-2"
        />
        <h1 className="text-2xl font-bold">{currencyNames[currencyCode || ""] || "알 수 없는 통화"}</h1>
      </div>
      <div className="flex mb-6">
        <button
          onClick={() => setActiveTab("exchange")}
          className={`flex-1 py-2 text-center ${
            activeTab === "exchange"
              ? "bg-white text-[#353535] font-bold border-b-2 border-[#353535]"
              : "bg-gray-100 text-gray-600"
          }`}>
          환율
        </button>
        <button
          onClick={() => setActiveTab("prediction")}
          className={`flex-1 py-2 text-center ${
            activeTab === "prediction"
              ? "bg-white text-[#353535] font-bold border-b-2 border-[#353535]"
              : "bg-gray-100 text-gray-600"
          }`}>
          환율 예측
        </button>
      </div>
      {activeTab === "exchange" && (
        <>
          <div className="bg-gray-100 rounded-md p-4 mb-6">
            <h2 className="text-2xl font-semibold mb-2">
              {formatCurrency(exchangeData?.exchangeRate || 0, currencyCode || "KRW")}
            </h2>
            <p
              className={`${
                historicalData[0]?.rate < historicalData[historicalData.length - 1]?.rate
                  ? "text-red-500"
                  : "text-blue-500"
              }`}>
              전일대비{" "}
              {formatCurrency(
                Math.abs(historicalData[historicalData.length - 1]?.rate - historicalData[0]?.rate),
                currencyCode || "KRW"
              )}
              {historicalData[0]?.rate < historicalData[historicalData.length - 1]?.rate ? "▲" : "▼"}
            </p>
          </div>
          <div className="flex justify-between mb-6">
            <div className="text-gray-600">
              <div>최고 {Math.max(...historicalData.map((d) => d.rate)).toFixed(2)}원</div>
              <div>최저 {Math.min(...historicalData.map((d) => d.rate)).toFixed(2)}원</div>
            </div>
            <div className="flex">
              {(["1주", "1달", "3달"] as const).map((period) => (
                <button
                  key={period}
                  onClick={() => handlePeriodChange(period)}
                  className={`ml-2 px-3 py-1 rounded-md ${
                    selectedPeriod === period
                      ? "bg-white text-[#353535] font-bold"
                      : "bg-gray-200 text-gray-700 hover:bg-gray-300"
                  }`}>
                  {period}
                </button>
              ))}
            </div>
          </div>
          <div className="mb-6 h-48">
            <Line data={chartData} options={chartOptions} />
          </div>
        </>
      )}
      {activeTab === "prediction" && (
        <>
          <div className="bg-gray-100 rounded-md p-4 mb-6">
            <h2 className="text-2xl font-semibold mb-2">
              예측 평균: {predictionData.reduce((sum, data) => sum + data.rate, 0) / predictionData.length}원
            </h2>
            <p
              className={`${
                predictionData[0]?.rate < predictionData[predictionData.length - 1]?.rate
                  ? "text-red-500"
                  : "text-blue-500"
              }`}>
              예측 변화: {(predictionData[predictionData.length - 1]?.rate - predictionData[0]?.rate).toFixed(2)}원
              {predictionData[0]?.rate < predictionData[predictionData.length - 1]?.rate ? "▲" : "▼"}
            </p>
          </div>
          <div className="mb-6 h-48">
            <Line data={chartData} options={chartOptions} />
          </div>
        </>
      )}
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
