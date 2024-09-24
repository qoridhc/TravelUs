import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { ChevronLeft } from "lucide-react";
import { exchangeApi } from '../../api/exchange';
import { ExchangeRateInfo, currencyNames } from '../../types/exchange';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const countryNameMapping: { [key: string]: string } = {
  EUR: 'Europe',
  JPY: 'Japan',
  USD: 'TheUnitedStates',
  CNY: 'China'
};

const getFlagImagePath = (currencyCode: string) => {
  const countryName = countryNameMapping[currencyCode] || currencyCode;
  return `/assets/flag/flagOf${countryName}.png`;
};

const ExchangeDetail: React.FC = () => {
  const { currencyCode } = useParams<{ currencyCode: string }>();
  const navigate = useNavigate();
  const [exchangeData, setExchangeData] = useState<ExchangeRateInfo | null>(null);
  const [historicalData, setHistoricalData] = useState<{date: string, rate: number}[]>([]);
  const [selectedPeriod, setSelectedPeriod] = useState<string>('1일');
  const [activeTab, setActiveTab] = useState<'exchange' | 'prediction'>('exchange');

  useEffect(() => {
    const fetchData = async () => {
      if (currencyCode) {
        try {
          const rates = await exchangeApi.getExchangeRates();
          const currentRate = rates.find(rate => rate.currencyCode === currencyCode);
          setExchangeData(currentRate || null);

          // Temporary mock data
          setHistoricalData([
            { date: '05-01', rate: 1340 },
            { date: '05-02', rate: 1345 },
            { date: '05-03', rate: 1342 },
            { date: '05-04', rate: 1348 },
            { date: '05-05', rate: 1350 },
          ]);
        } catch (error) {
          console.error('Failed to fetch exchange rate data:', error);
        }
      }
    };

    fetchData();
  }, [currencyCode, selectedPeriod]);

  const handlePeriodChange = (newPeriod: string) => {
    setSelectedPeriod(newPeriod);
  };

  const chartData = {
    labels: historicalData.map(data => data.date),
    datasets: [
      {
        label: '환율',
        data: historicalData.map(data => data.rate),
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1
      }
    ]
  };

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        display: false,
      },
      title: {
        display: false,
      },
    },
    scales: {
      x: {
        grid: {
          display: false,
        },
      },
      y: {
        grid: {
          display: false,
        },
      },
    },
  };

  if (!exchangeData) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  return (
    <div className="h-full p-5 pb-8">
      <div className="container mx-auto max-w-md px-4 py-8">
        <div className="bg-white rounded-lg shadow-md p-6">
          <button 
            onClick={() => navigate(-1)} 
            className="flex items-center text-blue-600 mb-4"
          >
            <ChevronLeft className="w-5 h-5 mr-1" />
          </button>
          <div className="flex items-center mb-4">
            <img 
              src={getFlagImagePath(currencyCode || '')} 
              alt={`${currencyNames[currencyCode || '']} Flag`} 
              className="w-8 h-6 mr-2"
            />
            <h1 className="text-2xl font-bold">
              {currencyNames[currencyCode || ''] || '알 수 없는 통화'}
            </h1>
          </div>
          <div className="flex mb-6">
            <button
              onClick={() => setActiveTab('exchange')}
              className={`flex-1 py-2 text-center ${
                activeTab === 'exchange'
                  ? 'bg-white text-[#353535] font-bold border-b-2 border-[#026CE1]'
                  : 'bg-gray-100 text-gray-600'
              }`}
            >
              환율
            </button>
            <button
              onClick={() => setActiveTab('prediction')}
              className={`flex-1 py-2 text-center ${
                activeTab === 'prediction'
                  ? 'bg-white text-[#353535] font-bold border-b-2 border-[#026CE1]'
                  : 'bg-gray-100 text-gray-600'
              }`}
            >
              환율 예측
            </button>
          </div>
          {activeTab === 'exchange' && (
            <>
              <div className="bg-gray-100 rounded-md p-4 mb-6">
                <h2 className="text-2xl font-semibold mb-2">
                  {exchangeData.exchangeRate.toFixed(2)}원
                </h2>
                <p className="text-red-500">
                  전일대비 {(exchangeData.exchangeRate - 1340).toFixed(2)}원 ▲
                </p>
              </div>
              <div className="flex justify-between mb-6">
                <div className="text-gray-600">
                  <div>최고 1,346.64원</div>
                  <div>최저 1,339.22원</div>
                </div>
                <div className="flex">
                  {['1일', '1주', '3달'].map((period) => (
                    <button
                      key={period}
                      onClick={() => handlePeriodChange(period)}
                      className={`ml-2 px-3 py-1 rounded-md ${
                        selectedPeriod === period
                          ? 'bg-white text-[#353535] font-bold'
                          : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                      }`}
                    >
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
          {activeTab === 'prediction' && (
            <div className="text-center py-8">
              <p>환율 예측 기능은 준비 중입니다.</p>
            </div>
          )}
          <div className="flex justify-between">
            <button className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
              원화로 바꾸기
            </button>
            <button className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
              외화 채우기
            </button>
          </div>h
        </div>
      </div>
    </div>
  );
};

export default ExchangeDetail;