import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { ChevronRight } from "lucide-react";
import { exchangeApi } from '../../api/exchange';
import { ExchangeRateInfo, currencyNames } from '../../types/exchange';

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

const ExchangeRateItem: React.FC<ExchangeRateInfo> = ({ currencyCode, exchangeRate }) => {
  const navigate = useNavigate();
  const currencyName = currencyNames[currencyCode] || '알 수 없는 통화';
  const country = countryNameMapping[currencyCode] || '알 수 없는 국가';

  const flagImagePath = getFlagImagePath(currencyCode);

  const handleClick = () => {
    navigate(`/exchangerate/${currencyCode}`);
  }

  return (
    <div 
      className="m-3 flex items-center justify-between p-4 border-b cursor-pointer hover:bg-gray-100"
      onClick={handleClick}
    >
      <div className="flex items-center">
        <img
         src={flagImagePath}
         alt={`${country} flag`}
         className="w-8 h-6 mr-2 object-cover rounded"
        />
        <div className="m-3">
          <p className='font-bold'>{currencyName}</p>
        </div>
      </div>
      <div className="text-right">
        <p className="font-bold">{exchangeRate.toFixed(2)}원</p>
      </div>
      <ChevronRight className="ml-2 text-gray-400" />
    </div>
  );
};

const ExchangeRateList: React.FC = () => {
  const [exchangeRates, setExchangeRates] = useState<ExchangeRateInfo[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchExchangeRates = async () => {
      try {
        const data = await exchangeApi.getExchangeRates();
        const filteredRates = data.filter(rate => 
        ['USD', 'JPY', 'EUR', 'CNY'].includes(rate.currencyCode)
        );
        setExchangeRates(filteredRates);
        setIsLoading(false);
      } catch (err) {
        console.log("Failed to fetch exchange rates:", err);
        setError("Failed to load exchange rates. Please try again later.");
        setIsLoading(false);
      }
    };

    fetchExchangeRates();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>
  }

  if (error) {
    return <div>{error}</div>
  }

  return (
    <div className="h-full p-5 pb-8">
      <div className="m-3">
        <div className="bg-white rounded-lg shadow mb-4">
          <h2 className="p-4 text-lg font-bold">실시간 환율</h2>
          {exchangeRates.map((rate, index) => (
            <ExchangeRateItem key={index} {...rate} />
          ))}
        </div>
        <div className="bg-gray-100 rounded-lg p-4 text-sm text-gray-600">
          <h3 className="font-bold mb-2">튜나뱅크 환율 안내</h3>
          <ul className="list-disc pl-5 space-y-1">
            <li>위 환율은 매매기준율로 1시간마다 업데이트돼요.</li>
            <li>위 환율은 튜나뱅크 외화 모임 통장으로 환전될 때 적용돼요.</li>
            <li>위 환율의 변동 정보는 어제 오전 10시 기준으로 계산돼요.</li>
            <li>위 환율과 환전할 때 적용되는 환율은 거래 시점에 따라 다를 수 있어요.</li>
          </ul>
        </div>
      </div>
    </div>
    
  );
};

export default ExchangeRateList;