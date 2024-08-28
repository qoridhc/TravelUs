import React, { useState, useEffect } from 'react';
import { exchangeApi } from '../../api/exchange';
import { ExchangeRateInfo as ExchangeRateInfoType } from '../../types/exchange';

export const currencyNames: { [key: string]: string } = {
  USD: '미국 달러',
  JPY: '일본 엔',
  EUR: '유로',
  GBP: '영국 파운드',
  CHF: '스위스 프랑',
  CAD: '캐나다 달러',
  CNY: '중국 위안',
};

interface ExchangeRateInfoProps {
  onExchangeClick?: () => void;
}

const ExchangeRateInfo = ({ onExchangeClick }: ExchangeRateInfoProps): React.ReactElement => {
  const [currencies, setCurrencies] = useState<ExchangeRateInfoType[]>([]);
  const [selectedCurrency, setSelectedCurrency] = useState<ExchangeRateInfoType | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchExchangeRates();
  }, []);

  const fetchExchangeRates = async () => {
    setError(null);
    try {
      const data = await exchangeApi.getExchangeRates();
      setCurrencies(data);
      if (data.length > 0) {
        setSelectedCurrency(data[0]);
      }
    } catch (error) {
      setError('환율 정보를 가져오는 데 실패했습니다. 다시 시도해 주세요.');
    }
  };

  const getCurrencyName = (code: string) => {
    return currencyNames[code] || code;
  };

  return (
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

      {onExchangeClick && (
        <button 
          className="w-full py-2 bg-[#0046FF] text-white font-bold rounded"
          onClick={onExchangeClick}
        >
          환전하기
        </button>
      )}
    </div>
  );
};

export default ExchangeRateInfo;