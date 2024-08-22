import React, { useState, useEffect } from 'react';

// 백에 맞게 수정 필요
interface AccountInfo {
  bankName: string;
  accountType: string;
  accountNumber: string;
  balance: number;
}

// 백에 맞게 수정 필요
interface Transaction {
  date: string;
  time: string;
  description: string;
  amount: number;
  balance: number;
  type: 'deposit' | 'withdrawl';
}

const PersonalAccount = () => {
  const [accountInfo, setAccountInfo] = useState<AccountInfo | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  useEffect(() => {
    // API 호출 필요
    setAccountInfo({
      bankName: '신한은행',
      accountType: '개인 입출금 통장',
      accountNumber: '110-455-247307',
      balance: 3000000
  });
  setTransactions([
    { date: '2024.07.15', time: '08:10', description: '편의점', amount: -100000, balance: 3000000, type: 'withdrawl' },
    { date: '2024.07.15', time: '08:10', description: '이디야 커피', amount: -100000, balance: 2900000, type: 'withdrawl' },
    { date: '2024.07.14', time: '18:30', description: '월급', amount: 2000000, balance: 3000000, type: 'deposit' },
    { date: '2024.07.15', time: '08:10', description: '가스비', amount: -100000, balance: 1000000, type: 'withdrawl' },
  ]);
}, []);

if (!accountInfo) return <div>Loading...</div>

  return (
    <div className='p-4 max-w-md mx-auto bg-gray-100'>
      <div className='p-4 mb-4 rounded-lg bg-white '>
        <div className='mb-2 flex items-center'>
          <div className='w-8 h-8 mr-2 bg-blue-500 rounded-full'></div>
          <div className='ml-3'>
            <p className='font-semibold'>{accountInfo.bankName}</p>
            <p className='font-semibold'>{accountInfo.accountType}</p>
            <p className='text-sm text gray=500'>{accountInfo.accountNumber}</p>
          </div>
        </div>
        <p className='text-2xl font-bold text-right'>{accountInfo.balance.toLocaleString()} 원</p>
      </div>

      <div className="mb-4 flex space-x-2">
        <button className='py-2 flex-1 bg-blue-500 text-white rounded'>이체</button>
        <button className='py-2 flex-1 bg-blue-500 text-white rounded'>계좌 관리</button>
      </div>

      <div className='p-4 bg-white rounded-lg'>
        <select className='p-2 mb-4 w-full border rounded'>
          <option value="">최근</option>
          <option value="">전체</option>
        </select>

        {transactions.map((transaction, index) => (
          <div key={index} className='py-2 border-b last:border-b-0'>
            <div className='flex justify-between'>
              <div>
                <p className='font-semibold'>{transaction.date}</p>
                <p>{transaction.time}</p>
                <p>{transaction.description}</p>
              </div>
              <div className='text-right'>
                <p className={`font-semibold ${transaction.type === 'deposit' ? 'text-blue-500' : 'text-red-500'}`}>
                  {transaction.type ===  'deposit' ? '+' : '-'}{Math.abs(transaction.amount).toLocaleString()} 원
                </p>
                <p className='text-sm text-gray-500'>잔액 {transaction.balance.toLocaleString()} 원</p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default PersonalAccount;