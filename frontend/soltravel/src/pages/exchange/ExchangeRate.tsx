import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import ExchangeRateInfo from "../../components/exchange/ExchangeRate";
import ExchangeRateChart from "../../components/exchange/ExchangeRateChart";

const ExchangeRatePage = (): React.ReactElement => {
  const navigate = useNavigate();
  const [selectedCurrency, setSelectedCurrency] = useState<string>("USD");

  const handleExchange = () => {
    navigate("/exchange");
  };

  const handleCurrencyChange = (currency: string) => {
    setSelectedCurrency(currency);
  };

  return (
    <div className="p-4 max-w-sm mx-auto bg-white rounded-xl shadow-md">
      <h1 className="text-xl font-bold mb-4">환율 조회</h1>
      <ExchangeRateInfo onExchangeClick={handleExchange} onCurrencyChange={handleCurrencyChange} />
      <div className="mt-8 bg-white rounded-lg p-4 shadow">
        <ExchangeRateChart currency={selectedCurrency} />
      </div>
    </div>
  );
};

export default ExchangeRatePage;
