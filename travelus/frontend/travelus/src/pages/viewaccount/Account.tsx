import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

interface Account {
  type: string;
  name: string;
  accountNumber: string;
  balance: number;
  currency?: string; // 통화 정보 추가
}

const AccountCard: React.FC<{ account: Account }> = ({ account }) => {
  const isAllInOne = account.type === '올인원 모임 통장';
  const navigate = useNavigate();

  const handleDetailView = () => {
    if (account.type === '올인원 모임 통장') {
      navigate('/generalaccount');
    } else if (account.type === '올인원 외화 모임 통장') {
      navigate('/foreignaccount');
    }
  };

  // 통화에 따른 금액 표시 함수
  const formatCurrency = (amount: number, currency: string = 'KRW') => {
    switch (currency) {
      case 'USD':
        return `$${amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
      case 'JPY':
        return `¥${amount.toLocaleString('ja-JP', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}`;
      case 'EUR':
        return `€${amount.toLocaleString('de-DE', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
      default:
        return `${amount.toLocaleString()} 원`;
    }
  };

  return (
    <div className={`p-4 mb-4 bg-white rounded-lg shadow ${isAllInOne ? 'py-12 px-5' : ''}`}>
      <div className="mb-2 flex items-center">
        <div className="w-8 h-8 mr-2 bg-[#0046FF] rounded-full"></div>
        <div>
          <p className="font-semibold">{account.name}</p>
          <p className="text-xs text-gray-500">{account.accountNumber}</p>
        </div>
      </div>
      <p className="mb-2 text-xl text-right font-bold">
        {formatCurrency(account.balance, account.currency)}
      </p>
      <div className="flex space-x-2">
        {isAllInOne && (
          <button className="flex-1 bg-[#0046FF] text-white py-2 rounded text-sm">입금</button>
        )}
        <button className="flex-1 bg-[#0046FF] text-white py-2 rounded text-sm" onClick={handleDetailView}>
          상세 보기
        </button>
      </div>
    </div>
  );
};

const ViewAccount: React.FC = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    const tempAccounts: Account[] = [
      {
        type: '올인원 모임 통장',
        name: '올인원 모임 통장',
        accountNumber: '110-455-247307',
        balance: 3000000,
        currency: 'KRW',
      },
      {
        type: '올인원 외화 모임 통장',
        name: '트래블 박스',
        accountNumber: '110-455-247307',
        balance: 2000000,
        currency: 'JPY', // 예시로 USD 설정
      },
    ];

    const timer = setTimeout(() => {
      setAccounts(tempAccounts);
      setIsLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, []);

  if (isLoading) {
    return <div className='py-4 text-center'>Loading...</div>
  }

  return (
    <div className='p-4 max-w-md mx-auto bg-gray-100 h-screen'>
      {accounts.map((account, index) => (
        <AccountCard key={index} account={account} />
      ))}
    </div>
  )
}

export default ViewAccount;