import React, { useState, useEffect } from 'react';

interface AccountInfo {
  bankName: string;
  accountType: string;
  accountNumber: string;
  balance: number;
  currency: string;
}

interface Transaction {
  date: string;
  time: string;
  description: string;
  amount: number;
  balance: number;
  type: 'deposit' | 'withdrawal';
}

const ForeignAccount: React.FC = () => {
  const [accountInfo, setAccountInfo] = useState<AccountInfo | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [showBalance, setShowBalance] = useState(true);

  useEffect(() => {
    // API 호출 시뮬레이션
    setAccountInfo({
      bankName: '신한은행',
      accountType: '트래블 박스',
      accountNumber: '110-455-247307',
      balance: 2000000,
      currency: '￥'
    });
    setTransactions([
      { date: '2024.07.15', time: '08:10', description: '자동 환전', amount: 100000, balance: 2000000, type: 'deposit' },
      { date: '2024.07.15', time: '08:10', description: '자동 환전', amount: 100000, balance: 1900000, type: 'deposit' },
      { date: '2024.07.14', time: '18:30', description: '환전하기', amount: 100000, balance: 1800000, type: 'deposit' },
      { date: '2024.07.14', time: '08:10', description: '자동 환전', amount: 100000, balance: 1700000, type: 'deposit' },
    ]);
  }, []);

  if (!accountInfo) return <div>Loading...</div>;

  const groupedTransactions = transactions.reduce((groups, transaction) => {
    const date = transaction.date;
    if (!groups[date]) {
      groups[date] = [];
    }
    groups[date].push(transaction);
    return groups;
  }, {} as Record<string, Transaction[]>);

  return (
    <div className='p-4 max-w-md mx-auto bg-gray-100 min-h-screen'>
      {/* 계좌 정보 섹션 */}
      <div className='p-4 mb-4 bg-white rounded-lg shadow'>
        <div className='mb-2 flex items-center'>
          <div className='w-8 h-8 mr-2 bg-[#0046FF] rounded-full'></div>
          <div className='ml-3'>
            <p className='font-semibold'>{accountInfo.bankName}</p>
            <p className='font-semibold'>{accountInfo.accountType}</p>
            <p className='text-sm text-gray-500'>{accountInfo.accountNumber}</p>
          </div>
        </div>
        <p className='text-2xl font-bold text-right'>{accountInfo.balance.toLocaleString()} {accountInfo.currency}</p>
      </div>

      {/* 버튼 섹션 */}
      <div className="mb-4 flex space-x-2">
        <button className='py-2 flex-1 bg-[#0046FF] text-white rounded'>환전하기</button>
      </div>

      {/* 거래 내역 섹션 */}
      <div className='bg-white rounded-lg p-4'>
        <div className='flex items-center justify-between mb-4'>
          <span className='text-lg font-semibold'>최신순</span>
          <div className='flex items-center'>
            <span className='mr-2'>잔액보기</span>
            <button
              className={`w-12 h-6 rounded-full ${showBalance ? 'bg-blue-500' : 'bg-gray-300'}`}
              onClick={() => setShowBalance(!showBalance)}
            >
              <div className={`w-5 h-5 rounded-full bg-white transform transition-transform ${showBalance ? 'translate-x-6' : 'translate-x-1'}`} />
            </button>
          </div>
        </div>

        {Object.entries(groupedTransactions).map(([date, dayTransactions], groupIndex, groupArray) => (
          <div key={date} className={`mb-6 ${groupIndex !== groupArray.length - 1 ? 'border-b border-gray-200 pb-6' : ''}`}>
            <h3 className='text-lg font-semibold mb-4'>{date}</h3>
            {dayTransactions.map((transaction, index) => (
              <div key={index} className='mb-4 last:mb-0'>
                <div className='flex justify-between items-start'>
                  <div>
                    <p className='font-semibold'>{transaction.time}</p>
                    <p className='text-gray-600'>{transaction.description}</p>
                  </div>
                  <div className='text-right'>
                    <p className={`font-semibold ${transaction.type === 'deposit' ? 'text-[#0046FF]' : 'text-red-500'}`}>
                      {transaction.type === 'deposit' ? '+' : '-'}{transaction.amount.toLocaleString()} {accountInfo.currency}
                    </p>
                    {showBalance && (
                      <p className='text-sm text-gray-500'>잔액 {transaction.balance.toLocaleString()} {accountInfo.currency}</p>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ForeignAccount;