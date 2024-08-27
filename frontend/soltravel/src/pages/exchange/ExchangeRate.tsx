import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { exchangeApi } from '../../api/exchange';
import { ExchangeRateInfo } from '../../types/exchange';

// 통화를 한글로 바꿔주는 변수
export const currencyNames: { [key: string]: string } = {
  USD: '미국 달러',
  JPY: '일본 엔',
  EUR: '유로',
  GBP: '영국 파운드',
  CHF: '스위스 프랑',
  CAD: '캐나다 달러',
  CNY: '중국 위안',
};

const ExchangeRateComponent: React.FC = () => {
  const [currencies, setCurrencies] = useState<ExchangeRateInfo[]>([]);
  const [selectedCurrency, setSelectedCurrency] = useState<ExchangeRateInfo | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  useEffect(() => {
    fetchExchangeRates();
  }, []);

  const fetchExchangeRates = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await exchangeApi.getExchangeRates();
      setCurrencies(data);
      if (data.length > 0) {
        setSelectedCurrency(data[0]);
      }
    } catch (err) {
      setError('환율 정보를 가져오는 데 실패했습니다. 다시 시도해 주세요.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleExchange = () => {
    navigate('/exchange');
  };

  const getCurrencyName = (code: string) => {
    return currencyNames[code] || code;
  };

  return (
    <div className="p-4 mt-5 max-w-sm mx-auto bg-white rounded-xl shadow-md space-y-4">
      <h1 className="text-xl font-bold">환율 조회</h1>
      <div className="space-y-4">
        <div>
          <h2 className="text-sm font-semibold mb-1">통화</h2>
          <select
            className="w-full border rounded p-2"
            onChange={(e) => setSelectedCurrency(currencies.find(c => c.currency === e.target.value) || null)}
            value={selectedCurrency?.currency || ''}
          >
            {currencies.map((currency) => (
              <option key={currency.currency} value={currency.currency}>
                {getCurrencyName(currency.currency)} ({currency.currency})
              </option>
            ))}
          </select>
        </div>

        {selectedCurrency && (
          <div className="border rounded p-2">
            <div className="text-sm text-gray-500">
              현재 매매기준율
            </div>
            <div className="flex justify-between items-center">
              <span>{`1 ${getCurrencyName(selectedCurrency.currency)} (${selectedCurrency.currency})`}</span>
              <span className="text-[#0046FF] font-bold">{selectedCurrency.exchangeRate.toFixed(2)}원</span>
            </div>
            <div className="text-sm text-gray-500 mt-1">
              최소 환전액: {selectedCurrency.exchangeMin.toLocaleString()}원
            </div>
            <div className="text-xs text-gray-400 mt-1">
              갱신 시간: {new Date(selectedCurrency.created).toLocaleString()}
            </div>
          </div>
        )}

        {error && <div className="text-red-500">{error}</div>}
      </div>
      <div className="flex space-x-2">
        {/* <button 
          className="flex-1 bg-[#0046FF] text-white rounded py-2 disabled:bg-blue-300"
          onClick={fetchExchangeRates}
          disabled={isLoading}
        >
          {isLoading ? '조회 중...' : '조회하기'}
        </button> */}
        <button 
          className="py-2 flex-1 bg-[#0046FF] text-white font-bold rounded"
          onClick={handleExchange}
        >
          환전하기
        </button>
      </div>
    </div>
  );
};

export default ExchangeRateComponent;