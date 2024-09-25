import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronLeft, ChevronDown } from "lucide-react";
import { FcMoneyTransfer } from "react-icons/fc";

interface Account {
  id: string;
  name: string;
  krwBalance: number;
  usdBalance: number;
}

const accounts: Account[] = [
  { id: '1', name: '사랑스러운 박씨네', krwBalance: 0, usdBalance: 1000 },
  { id: '2', name: '우리 가족 모임', krwBalance: 0, usdBalance: 500 },
  { id: '3', name: '회사 동료 모임', krwBalance: 0, usdBalance: 200 },
];

const ExchangeKRWFlow: React.FC = () => {
  const navigate = useNavigate();
  const [selectedAccount, setSelectedAccount] = useState<Account>(accounts[0]);
  const [isAccountMenuOpen, setIsAccountMenuOpen] = useState(false);
  const [inputAmount, setInputAmount] = useState('');
  const [calculatedKRW, setCalculatedKRW] = useState('0');
  const [isExchangeComplete, setIsExchangeComplete] = useState(false);
  const exchangeRate = 1343.98;

  useEffect(() => {
    const numericAmount = parseFloat(inputAmount);
    if (inputAmount === '' || isNaN(numericAmount) || numericAmount === 0) {
      setCalculatedKRW('0');
    } else {
      const calculated = (numericAmount * exchangeRate).toFixed(2);
      setCalculatedKRW(calculated);
    }
  }, [inputAmount, exchangeRate]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/[^0-9.]/g, '');
    setInputAmount(value);
  };

  const handleConfirm = () => {
    const numericAmount = parseFloat(inputAmount);
    if (inputAmount && !isNaN(numericAmount) && numericAmount > 0) {
      setIsExchangeComplete(true);
    }
  };

  const handleAccountSelect = (account: Account) => {
    setSelectedAccount(account);
    setIsAccountMenuOpen(false);
  };

  const handleClose = () => {
    navigate('/');
  };

  if (isExchangeComplete) {
    return (
      <div className="h-full p-5 pb-8 flex flex-col">
        <div className="flex-grow flex flex-col items-center justify-center space-y-5">
          <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />
          <div className="text-2xl font-semibold text-center">
            <p>
              <span className="font-normal">원화 </span>
              {calculatedKRW} KRW
            </p>
            <p className="font-normal">채우기 완료</p>
          </div>
        </div>
        
        <div className="mb-6">
          <div className="mb-4 flex justify-between">
            <p className="text-gray-500 text-sm">환전 금액</p>
            <p className="text-lg">{inputAmount} USD → {calculatedKRW} KRW</p>
          </div>
          <div className="flex justify-between">
            <p className="text-gray-500 text-sm">적용 환율</p>
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
  }

  return (
    <div className="flex flex-col h-screen">
      <div className="p-4 flex-grow overflow-auto">
        <button className="mb-4" onClick={() => navigate(-1)}>
          <ChevronLeft className="w-6 h-6" />
        </button>
        <h1 className="text-xl font-bold mb-2">원화로 바꾸기</h1>
        <p className="text-sm text-gray-500 mb-6">1 USD = {exchangeRate.toFixed(2)}원</p>
        
        <div className="relative mb-4">
          <button 
            className="w-full text-left bg-gray-100 p-3 rounded-lg flex justify-between items-center"
            onClick={() => setIsAccountMenuOpen(!isAccountMenuOpen)}
          >
            <span>{selectedAccount.name}</span>
            <ChevronDown className="w-5 h-5" />
          </button>
          {isAccountMenuOpen && (
            <div className="absolute w-full mt-1 bg-white border rounded-lg shadow-lg z-10">
              {accounts.map(account => (
                <button
                  key={account.id}
                  className="w-full text-left p-3 hover:bg-gray-100"
                  onClick={() => handleAccountSelect(account)}
                >
                  {account.name}
                </button>
              ))}
            </div>
          )}
        </div>

        <div className="bg-gray-100 rounded-lg p-4 mb-4">
          <div className="flex justify-between items-center mb-2">
            <div className="flex items-center">
              <img src="/assets/flag/flagOfKorea.png" alt="KRW Flag" className="w-6 h-4 mr-2" />
              <span>대한민국 원</span>
            </div>
            <div>{parseFloat(calculatedKRW).toLocaleString()} KRW</div>
          </div>
          <p className="text-sm text-gray-500">
            현재 잔액: {selectedAccount.krwBalance.toLocaleString()} KRW
          </p>
        </div>

        <div className="bg-gray-100 rounded-lg p-4">
          <div className="flex justify-between items-center mb-2">
            <div className="flex items-center">
              <img src="/assets/flag/flagOfTheUnitedStates.png" alt="USD Flag" className="w-6 h-4 mr-2" />
              <span>미국 달러</span>
            </div>
            <div className="flex items-center">
              <input
                type="text"
                inputMode="decimal"
                value={inputAmount}
                onChange={handleInputChange}
                className="text-right bg-transparent w-20 mr-1"
                placeholder="0"
              />
              <span>USD</span>
            </div>
          </div>
          <p className="text-sm text-gray-500">
            현재 잔액: {selectedAccount.usdBalance.toLocaleString()} USD
          </p>
        </div>
      </div>

      <div className="p-4 mt-auto">
        <div className="flex items-center justify-center mb-4 text-gray-600">
          <FcMoneyTransfer className="mr-2 text-xl text-[#1429A0]" />
          <p>수수료는 튜나뱅크가 낼게요</p>
        </div>
        <button 
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={handleConfirm}
          disabled={!inputAmount || parseFloat(inputAmount) <= 0}
        >
          확인
        </button>
      </div>
    </div>
  );
};

export default ExchangeKRWFlow;