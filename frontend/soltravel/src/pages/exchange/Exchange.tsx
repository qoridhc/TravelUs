import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

// 백에서 받아올 데이터를 interface로 type 설정 현재는 예시들
interface Account {
  id: string;
  name: string;
  accountNumber: string;
  balance: number;
  currency: string;
}

interface ExchangeRate {
  currency: string;
  date: string;
  time: string;
  round: string;
  sendRate: number;
  receiveRate: number;
  buyRate: number;
  sellRate: number;
  baseRate: number;
  usdConversionRate: number;
}

// 환전 요청
const Exchange: React.FC = () => {
  const { accountId } = useParams<{ accountId: string }>();
  const [account, setAccount] = useState<Account | null>(null);
  const [exchangeRate, setExchangeRate] = useState<ExchangeRate | null>(null);
  const [exchangeAmount, setExchangeAmount] = useState<string>('');
  const [expectedExchange, setExpectedExchange] = useState<string>('0');

  useEffect(() => {
    // 실제 구현에서는 API 호출로 대체해야 합니다.
    const fetchAccountInfo = async () => {
      // 예시 데이터
      const mockAccount: Account = {
        id: accountId || '',
        name: '신한은행 올인원머니통장',
        accountNumber: '신한 110-455-247307',
        balance: 3000000,
        currency: 'USD', // 예시로 USD 사용
      };
      setAccount(mockAccount);

      // 예시 환율 데이터
      const mockExchangeRate: ExchangeRate = {
        currency: 'USD',
        date: '2024.07.30',
        time: '23:02:23',
        round: '194',
        sendRate: 1398.20,
        receiveRate: 1371.80,
        buyRate: 1409.23,
        sellRate: 1360.77,
        baseRate: 1383.00,
        usdConversionRate: 1.0000,
      };
      setExchangeRate(mockExchangeRate);
    };

    fetchAccountInfo();
  }, [accountId]);

  // 환전할 돈을 입력한 후 취소 했을 때, NaN이 뜨지 않도록 하기
  const handleExchangeAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const amount = e.target.value;
    setExchangeAmount(amount);
    if (account && exchangeRate) {
      const numAmount = parseFloat(amount);
      if (!isNaN(numAmount) && numAmount > 0) {
        const exchanged = (numAmount / exchangeRate.baseRate).toFixed(2);
        setExpectedExchange(exchanged);
      } else {
        setExpectedExchange('0');
      }
    }
  };

  // 환전하기 버튼 (백으로 요청 보내는 로직 필요) : 여기서 계산을 하는건지, 백에서 계산을 한 값을 던져주는 건지?
  const handleExchange = () => {
    // 실제 환전 로직 구현 필요
    console.log('환전 실행:', accountId, exchangeAmount, expectedExchange);
  };

  if (!account || !exchangeRate) {
    return <div>Loading...</div>;
  }

  return (
    <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
      <h1 className="text-xl font-bold mb-4">환전하기</h1>
      <h2 className="font-bold mb-2">환전할 금액 입력</h2>
      <div className="bg-white rounded-lg p-4 shadow mb-4">
        <div className="flex items-center mb-2">
          <div className="w-8 h-8 bg-[#0046FF] rounded-full mr-2"></div>
          <div>
            <p className="font-semibold">{account.name}</p>
            <p className="text-sm text-gray-500">{account.accountNumber}</p>
          </div>
        </div>
        <input
          type="number"
          className="w-full text-right text-2xl font-bold p-2 border rounded"
          value={exchangeAmount}
          onChange={handleExchangeAmountChange}
          placeholder="0"
        />
        <p className="text-right text-sm text-gray-500">최대 {account.balance.toLocaleString()} 원 가능</p>
      </div>
      <div className="bg-white rounded-lg p-4 shadow mb-4">
        <p className="font-bold mb-2">예상 환전 금액</p>
        <p className="text-2xl font-bold">
          {parseFloat(expectedExchange) > 0 ? parseFloat(expectedExchange).toLocaleString() : '0'} {account.currency}
        </p>
      </div>
      <div className="bg-white rounded-lg p-4 shadow">
        <h3 className="font-bold mb-2">{exchangeRate.currency}</h3>
        <div className="grid grid-cols-2 gap-4 text-sm">
          <p>기준일</p>
          <p className="text-right">{exchangeRate.date}</p>
          <p>고시시간/회차</p>
          <p className="text-right">{exchangeRate.time} / {exchangeRate.round}회차</p>
          <p>송금 보낼 때</p>
          <p className="text-right">{exchangeRate.sendRate.toFixed(2)} 원</p>
          <p>송금 받을 때</p>
          <p className="text-right">{exchangeRate.receiveRate.toFixed(2)} 원</p>
          <p>현찰 살 때</p>
          <p className="text-right">{exchangeRate.buyRate.toFixed(2)} 원</p>
          <p>현찰 팔 때</p>
          <p className="text-right">{exchangeRate.sellRate.toFixed(2)} 원</p>
          <p>매매기준율</p>
          <p className="text-right">{exchangeRate.baseRate.toFixed(2)} 원</p>
          <p>대미환산율</p>
          <p className="text-right">{exchangeRate.usdConversionRate.toFixed(4)}</p>
        </div>
      </div>
      <button
        className="w-full bg-[#0046FF] text-white py-3 rounded-lg mt-4"
        onClick={handleExchange}
      >
        환전하기
      </button>
    </div>
  );
};

export default Exchange;