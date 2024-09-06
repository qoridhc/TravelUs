import React, { useState, useEffect, useRef } from 'react';
import { ScatterChart } from '@mui/x-charts/ScatterChart';
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
  const containerRef = useRef<HTMLDivElement>(null);

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
        currencyCode: currency || 'USD',
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

    const formattedData = chartData.map((dataPoint, index) => ({
      id: index,
      x: index,
      y: dataPoint.dealBasR,
      info: {
        date: new Date(dataPoint.postAt).toLocaleDateString(),
        rate: dataPoint.dealBasR
      }
    }));

    const minRate = Math.min(...formattedData.map(d => d.y));
    const maxRate = Math.max(...formattedData.map(d => d.y));
    const yAxisMin = Math.max(500, Math.floor(minRate / 100) * 100);
    const yAxisMax = Math.ceil(maxRate / 100) * 100;

    // 차트의 너비를 데이터 포인트 수에 따라 동적으로 설정
    const chartWidth = Math.max(600, formattedData.length * 20); // 최소 600px, 각 데이터 포인트당 20px

    return (
      <ScatterChart
        width={chartWidth}
        height={300}
        series={[
          {
            data: formattedData,
            label: `${currencyNames[currency] || currency}`,
            valueFormatter: (v: any) => `${v.info.date}: ${v.info.rate.toFixed(2)}`,
          },
        ]}
        xAxis={[
          { 
            label: '날짜',
            valueFormatter: (v) => formattedData[v]?.info.date || '',
          }
        ]}
        yAxis={[
          { 
            label: '환율',
            min: yAxisMin,
            max: yAxisMax,
          }
        ]}
        margin={{ left: 70, right: 20, top: 20, bottom: 50 }}
      />
    );
  };

  return (
    <div>
      <h2 className="text-lg font-semibold mb-4">최근 30일 환율 변동</h2>
      <div 
        ref={containerRef}
        className="mt-4 overflow-x-auto" 
        style={{ 
          height: '350px', // 차트 높이 + 여유 공간
          width: '100%',
        }}
      >
        <div style={{ minWidth: '600px', height: '300px' }}>
          {renderChart()}
        </div>
      </div>
    </div>
  );
};

export default ExchangeRateChart;