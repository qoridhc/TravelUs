import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { fetchAccountInfo, fetchTransactionHistory } from '../api/account';
import { AccountInfo, Transaction } from '../types/account';
import AccountDetails from './AccountDetails';
import TransactionHistory from './TransactionHistory';

const GroupAccount = (): JSX.Element => {
  const [accountInfo, setAccountInfo] = useState<AccountInfo | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [showBalance, setShowBalance] = useState(true);
  const navigate = useNavigate();
  const { userId } = useParams<{ userId: string }>();

  useEffect(() => {
    const loadAccountData = async () => {
      if (userId) {
        try {
          const info = await fetchAccountInfo(userId);
          setAccountInfo(info);
          const history = await fetchTransactionHistory(info.accountNo);
          setTransactions(history as Transaction[]);
        } catch (error) {
          console.error('Error fetching account data:', error);
        }
      }
    };
    loadAccountData();
  }, [userId]);

  if (!accountInfo) return <div>Loading...</div>;

  const handleNavigate = () => {
    navigate('/detail');
  };

  return (
    <div className='p-4 max-w-md mx-auto bg-gray-100 min-h-screen'>
      <AccountDetails accountInfo={accountInfo} />

      <div className="mb-4 flex space-x-2">
        <button className='py-2 flex-1 bg-[#0046FF] text-white rounded'>입금</button>
        <button
          className='py-2 flex-1 bg-[#0046FF] text-white rounded'
          onClick={handleNavigate}
        >
          계좌 관리
        </button>
      </div>

      <TransactionHistory 
        transactions={transactions} 
        showBalance={showBalance}
        setShowBalance={setShowBalance}
      />
    </div>
  );
};

export default GroupAccount;