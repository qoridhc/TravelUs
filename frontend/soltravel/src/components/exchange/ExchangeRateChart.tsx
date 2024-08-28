import React, { useState, useEffect } from 'react';
import { LineChart } from '@mui/x-charts/LineChart';
import { exchangeApi } from '../../api/exchange';
import { currencyNames } from '../../types/exchange';
import { ExchangeRateHistoryResponse } from '../../types/exchange';

interface ExchangeRateChartProps {
  currency: string;
}

const ExchangeRateChart: React.FC<ExchangeRateChartProps> = ({ currency }) => {
  const [chartData, setChartData] = useState<ExchangeRateHistoryResponse[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchExchangeRateHistory();
  }, [currency]);

  const fetchExchangeRateHistory = async () => {
    setIsLoading(true);
    setError(null);
    const endDate = new Date().toISOString().split('T')[0];
    const startDate = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];

    try {
      const data = await exchangeApi.getExchangeRateHistory({
        currencyCode: currency || 'CAD',
        startDate,
        endDate,
      });
      if (!Array.isArray(data)) {
        setChartData([data]);
      } else {
        setChartData(data);
      }
    } catch (error) {
      console.error('Failed to fetch exchange rate history:', error);
      setError('환율 정보를 불러오는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  const renderChart = () => {
    if (isLoading) {
      return <div>데이터를 불러오는 중...</div>;
    }

    if (error) {
      return <div className="text-red-500">{error}</div>;
    }

    if (!chartData || chartData.length === 0) {
      return <div>표시할 데이터가 없습니다.</div>;
    }

    const formattedData = chartData.map((dataPoint) => ({
      date: new Date(dataPoint.postAt).toLocaleDateString(),
      rate: dataPoint.dealBasR, // 각 데이터 포인트의 환율
    }));

    return (
      <LineChart
        xAxis={[{ 
          dataKey: 'date',
          scaleType: 'band',
          tickLabelStyle: { angle: 45, textAnchor: 'start', dominantBaseline: 'hanging' }
        }]}
        series={[
          {
            dataKey: 'rate',
            area: true,
            label: `${currencyNames[currency] || currency}`,
          },
        ]}
        dataset={formattedData}
        height={300}
        margin={{ left: 50, right: 50, top: 20, bottom: 50 }}
      />
    );
  };

  return (
    <div>
      <h2 className="text-lg font-semibold mb-4">최근 30일 환율 변동</h2>
      <div className="mt-4" style={{ height: '300px', width: '100%' }}>
        {renderChart()}
      </div>
    </div>
  );
};

export default ExchangeRateChart;
