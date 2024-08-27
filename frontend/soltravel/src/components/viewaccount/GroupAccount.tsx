import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { accountApi } from '../../api/account';
import { AccountInfo, Transaction } from '../../types/account';
import AccountDetails from './AccountDetails';
import TransactionHistory from './TransactionHistory';

// 계좌 정보를 불러오기
const GroupAccount = (): React.ReactElement => {
  const [accountInfo, setAccountInfo] = useState<AccountInfo | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [showBalance, setShowBalance] = useState(true);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();
  const { userId } = useParams<{ userId: string }>();

  useEffect(() => {
    const loadAccounData = async () => {
      if (userId) {
        try {
          setIsLoading(true);
          const info = await accountApi.fetchAccountInfo(userId);
          setAccountInfo(info);
          console.log(info)
          // const history = await accountApi.fetchTransactionHistory(info.accountNo);
          // setTransactions(history);
        } catch (error) {
          setError('계좌 정보를 불러오는데 실패했습니다.');
          console.error('Error fetching account data:', error);
        } finally {
          setIsLoading(false);
        }
      }
    };
    loadAccounData();
  }, [userId]);

  const handleNavigate = () => {
    navigate('/detail');
  };

  if (!isLoading) return <div>Loading...</div>
  if (error) return <div>{error}</div>
  if (!accountInfo) return <div>계좌 정보를 찾을 수 없습니다.</div>

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

      {/* <TransactionHistory 
        transactions={transactions} 
        showBalance={showBalance}
        setShowBalance={setShowBalance}
      /> */}
    </div>
  );
};

export default GroupAccount;