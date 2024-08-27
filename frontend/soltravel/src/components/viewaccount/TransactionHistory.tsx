import React from 'react';
import { Transaction } from '../../types/account';

type TransactionHistoryProps = {
  transactions: Transaction[];
  showBalance: boolean;
  setShowBalance: (show: boolean) => void;
};

const TransactionHistory = ({ transactions, showBalance, setShowBalance }: TransactionHistoryProps): React.ReactElement => {
  const groupedTransactions = transactions.reduce((groups, transaction) => {
    const date = transaction.transactionDate;
    if (!groups[date]) {
      groups[date] = [];
    }
    groups[date].push(transaction);
    return groups;
  }, {} as Record<string, Transaction[]>);

  return (
    <div className='p-4 bg-white rounded-lg'>
      <div className='mb-4 flex items-center justify-between'>
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
        <div key={date} className={`mb-6 ${groupIndex !== groupArray.length - 1 ? 'pb-6 border-b border-gray-200' : ''}`}>
          <h3 className='mb-4 text-lg font-semibold'>{date}</h3>
          {dayTransactions.map((transaction, index) => (
            <div key={index} className='mb-4 last:mb-0'>
              <div className='flex justify-between items-start'>
                <div>
                  <p className='font-semibold'>{transaction.transactionTime}</p>
                  <p className='text-gray-600'>{transaction.transactionSummary}</p>
                </div>
                <div className='text-right'>
                  <p className={`font-semibold ${transaction.transactionType === '1' ? 'text-[#0046FF]' : 'text-red-500'}`}>
                    {transaction.transactionType === '1' ? '+' : '-'}{parseInt(transaction.transactionBalance).toLocaleString()}원
                  </p>
                  {showBalance && (
                    <p className='text-sm text-gray-500'>잔액 {parseInt(transaction.transactionAfterBalance).toLocaleString()}원</p>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      ))}
    </div>
  );
};

export default TransactionHistory;