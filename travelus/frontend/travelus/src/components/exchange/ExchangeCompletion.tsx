import React from 'react';
import { useNavigate } from 'react-router-dom';

interface ExchangeCompletionProps {
  amount: string;
  exchangeRate: number;
}

const ExchangeCompletion: React.FC<ExchangeCompletionProps> = ({ amount, exchangeRate }) => {
  const navigate = useNavigate();
  const amountInKRW = (parseFloat(amount) * exchangeRate).toFixed(2);

  const handleClose = () => {
    navigate('/');
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col">
      <div className="flex-grow flex flex-col items-center justify-center space-y-5">
        <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />
        <div className="text-2xl font-semibold text-center">
          <p>
            {amount} USD
          </p>
          <p className="font-normal">채우기 완료</p>
        </div>
      </div>
      
      <div className="mb-6">
        <div className="mb-4 flex justify-between">
          <p className="text-gray-500 text-sm">환전 금액</p>
          <p className="text-lg">{amountInKRW} 원 → {amount} USD</p>
        </div>
        <div className="flex justify-between">
          <p className="text-gray-500 text-sm mb-1">적용 환율</p>
          <p className="text-lg">{exchangeRate.toFixed(2)} 원</p>
        </div>
      </div>
      
      <button
        onClick={handleClose}
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
      >
        닫기
      </button>
    </div>
  );
};

export default ExchangeCompletion;