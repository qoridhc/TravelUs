import React from 'react';
import { useNavigate } from 'react-router-dom';
import ExchangeRateInfo from '../../components/exchange/ExchangeRate';

const ExchangeRatePage = (): React.ReactElement => {
  const navigate = useNavigate();

  const handleExchange = () => {
    navigate('/exchange');
  };

  return (
    <div className="p-4 mt-5 max-w-sm mx-auto bg-white rounded-xl shadow-md">
      <h1 className="text-xl font-bold mb-4">환율 조회</h1>
      <ExchangeRateInfo onExchangeClick={handleExchange} />
    </div>
  );
};

export default ExchangeRatePage;