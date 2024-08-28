// components/ExchangeRateChart.tsx
import React, { useState, useEffect } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { exchangeApi } from '../../api/exchange';
import DateRangeSelector from './DateRangeSelector';
import CustomXAxis from './CustomXAxis';

interface ExchangeRateChartProps {
  currency: string;
}

const ExchangeRateChart: React.FC<ExchangeRateChartProps> = ({ currency }) => {
  const [dateRange, setDateRange] = useState<number>(7);
  const [chartData, setChartData] = useState<any[]>([]);

  useEffect(() => {
    fetchExchangeRateHistory();
  }, [currency, dateRange]);

  const fetchExchangeRateHistory = async () => {
    const endDate = new Date().toISOString().split('T')[0];
    const startDate = new Date(Date.now() - dateRange * 24 * 60 * 60 * 1000).toISOString().split('T')[0];

    try {
      const data = await exchangeApi.getExchangeRateHistory({
        currency,
        startDate,
        endDate,
      });
      setChartData(data);
    } catch (error) {
      console.error('Failed to fetch exchange rate history:', error);
    }
  };

  return (
    <div>
      <DateRangeSelector onSelect={setDateRange} selectedRange={dateRange} />
      <div className="mt-4" style={{ height: '300px' }}>
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" />
            <CustomXAxis dataKey="postAt" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="dealBasR" stroke="#8884d8" />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default ExchangeRateChart;