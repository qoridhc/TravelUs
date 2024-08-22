import React, { useState, useEffect, } from 'react';
import { useNavigate } from 'react-router';

// 백에 맞게 수정 필요
interface Account {
  type: string;
  name: string;
  accountNumber: string;
  balance: number;
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

  return (
    <div className={`p-4 mb-4 bg-white rounded-lg shadow ${isAllInOne ? 'py-12 px-5' : ''}`}>
      <div className="mb-2 flex items-center">
        <div className="w-8 h-8 mr-2 bg-blue-500 rounded-full"></div>
        <div>
          <p className="font-semibold">{account.name}</p>
          <p className="text-xs text-gray-500">{account.accountNumber}</p>
        </div>
      </div>
      <p className="mb-2 text-xl text-right font-bold">{account.balance.toLocaleString()} 원</p>
      <div className="flex space-x-2">
        {isAllInOne && (
          <button className="flex-1 bg-blue-500 text-white py-2 rounded text-sm">입금</button>
        )}
        <button className="flex-1 bg-blue-500 text-white py-2 rounded text-sm" onClick={handleDetailView}>
          상세 보기
        </button>
      </div>
    </div>
  );
};

const ViewAccount = () => {
  const [accounts, setAccounts] = useState<(Account[])>([]);
  const [isLoading, setIsLoading] = useState<(boolean)>(true);
  // const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // 백에서 데이터 요청하기
    // const fetchAccounts = async () => {
    //   setIsLoading(true);
    //   setError(null);

    //   const response = await fetch('/api/accounts').catch(() => null);

    //   if (!response || !response.ok) {
    //     setError('계좌 정보를 불러오는데 실패했습니다.');
    //     setIsLoading(false);
    //     return;
    //   }

    //   const data = await response.json().catch(() => null);

    //   if (!data) {
    //     setError('데이터를 불러오는데 실패했습니다.');
    //     setIsLoading(false);
    //     return;
    //   }

    //   setAccounts(data);
    //   setIsLoading(false);
    const tempAccounts: Account[] = [
      {
        type: '올인원 모임 통장',
        name: '올인원 모임 통장',
        accountNumber: '110-455-247307',
        balance: 3000000,
      },
      {
        type: '올인원 외화 모임 통장',
        name: '트래블 박스',
        accountNumber: '110-455-247307',
        balance: 3000000,
      },
    ];

    const timer = setTimeout(() => {
      setAccounts(tempAccounts);
      setIsLoading(false);
    }, 500)

    return () => clearTimeout(timer);
  }, []);

  //   fetchAccounts();
  // }, []);

  if (isLoading) {
    return <div className='py-4 text-center'>Loading...</div>
  }

  // if (error) {
  //   return <div className='py-4 text-center text-red-500'>{error}</div>
  // }

  return (
    <div className='p-4 max-w-md mx-auto bg-gray-100 h-screen'>
      {accounts.map((account, index) => (
        <AccountCard key={index} account={account} />
      ))}
    </div>
  )
}

export default ViewAccount;