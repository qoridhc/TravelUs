import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { ChevronRight, ChevronRightCircle } from "lucide-react";
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
  const currencyName = currencyNames[currencyCode] || '알 수 없는 통화';
  const country = countryNameMapping[currencyCode] || '알 수 없는 국가';

  const flagImagePath = getFlagImagePath(currencyCode);

  return (
    <div className="m-3 flex items-center justify-between p-4 border-b">
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
  // 임시 데이터 (백엔드 데이터 필요)
  const exchangeRates: ExchangeRateInfo[] =[
    { currencyCode: "USD", exchangeRate: 1331.86, exchangeMin: 0, created: new Date().toISOString() },
    { currencyCode: "JPY", exchangeRate: 926.78, exchangeMin: 0, created: new Date().toISOString() },
    { currencyCode: "EUR", exchangeRate: 1488.45, exchangeMin: 0, created: new Date().toISOString() },
    { currencyCode: "CNY", exchangeRate: 189.11, exchangeMin: 0, created: new Date().toISOString() }
  ];

  return (
    <div className="m-3 bg-white rounded-lg shadow">
      <h2 className="p-4 text-lg font-bold">실시간 환율</h2>
      {exchangeRates.map((rate, index) => (
        <ExchangeRateItem key={index} {...rate} />
      ))}
    </div>
  );
};

export default ExchangeRateList;