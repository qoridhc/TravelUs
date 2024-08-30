import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { accountApi } from '../../api/account';
import { transactionApi } from '../../api/transaction';
import { AccountInfo } from '../../types/account';
import { TransferRequest, TransferResponse } from '../../types/transaction';

const Transaction: React.FC = () => {
  const navigate = useNavigate();
  const [userId, setUserId] = useState<number | null>(null);
  const [groupAccounts, setGroupAccounts] = useState<AccountInfo[]>([]);
  const [individualAccounts, setIndividualAccounts] = useState<AccountInfo[]>([]);
  const [selectedGroupAccount, setSelectedGroupAccount] = useState<AccountInfo | null>(null);
  const [selectedIndividualAccount, setSelectedIndividualAccount] = useState<AccountInfo | null>(null);
  const [transferAmount, setTransferAmount] = useState<string>('');
  const [transactionSummary, setTransactionSummary] = useState<string>('');
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const storedUserId = localStorage.getItem('userId');
    if (!storedUserId) {
      setError('로그인 후 사용해 주세요');
      navigate('/login');
      return;
    }
    setUserId(parseInt(storedUserId, 10));
  }, [navigate])

  useEffect(() => {
    const fetchAccounts = async () => {
      if (!userId) return;

      try {
        const accounts = await accountApi.fetchAccountInfo(userId);
        const groupAccounts = accounts.filter(acc => acc.accountType === 'GROUP');
        const individualAccounts = accounts.filter(acc => acc.accountType === 'INDIVIDUAL');
        setGroupAccounts(groupAccounts);
        setIndividualAccounts(individualAccounts);
        if (groupAccounts.length > 0) setSelectedGroupAccount(groupAccounts[0]);
        if (individualAccounts.length > 0) setSelectedIndividualAccount(individualAccounts[0]);
      } catch (error) {
        console.log('Error fetching accounts', error);
        setError('계좌 정보를 불러오는데 실패했습니다.');
      }
    };

    fetchAccounts();
  }, [userId]);

  const handleGroupAccountChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const account = groupAccounts.find(acc => acc.id === parseInt(e.target.value));
    if (account) setSelectedGroupAccount(account);
  };

  const handleIndividualAccountChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const account = individualAccounts.find(acc => acc.id === parseInt(e.target.value));
    if (account) setSelectedIndividualAccount(account);
  };

  const handleTransfer = async () => {
    if (!selectedGroupAccount || !selectedIndividualAccount || !userId) return;

    const amount = parseFloat(transferAmount);
    if (isNaN(amount) || amount <= 0) {
      setError('입금 금액을 입력해주세요.');
      return;
    }

    if (amount > selectedIndividualAccount.balance) {
      setError('잔액이 부족합니다');
      return;
    }

    setIsLoading(true);
    try {
      const request: TransferRequest = {
        depositAccountNo: selectedGroupAccount.accountNo,
        depositTransactionSummary: transactionSummary || '모임통장 입금',
        transactionBalance: amount,
        withdrawlTransactionSummary: '개인계좌 출금'
      };
      const response: TransferResponse = await transactionApi.TransferInfo(selectedIndividualAccount.accountNo, request);
      alert(`입금이 완료되었습니다.`)
      setTransferAmount('');
      setTransactionSummary('');
      navigate('/');
    } catch (error) {
      console.log('Deposit Error:', error);
      setError('입금 처리 중 오류가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  if (error) {
    return <div className="p-4 text-center text-red-500">{error}</div>
  }

  return (
    <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
      <h1 className="mb-6 text-2xl font-bold">모임통장 입금</h1>

      <div className="mb-6 p-4 bg-white rounded-lg shadow">
        <h2 className="mb-4 text-xl font-semibold">출금 계좌 선택 (개인계좌)</h2>
        <select
          className="mb-4 p-2 w-full border rounded"
          value={selectedIndividualAccount?.id || ''}
          onChange={handleIndividualAccountChange}
        >
          {individualAccounts.map(account => (
            <option key={account.id} value={account.id}>
              {account.accountName} ({account.accountNo})
            </option>
          ))}
        </select>
        {selectedIndividualAccount && (
          <p className="mb-4 text-sm text-gray-600">
            출금계좌 잔액: {selectedIndividualAccount.balance.toLocaleString()} {selectedIndividualAccount.currency.currencyCode}
          </p>
        )}

        <h2 className="mb-4 text-xl font-semibold">입금 계좌 선택 (모임통장)</h2>
        <select
          className="mb-4 p-2 w-full border rounded"
          value={selectedGroupAccount?.id || ''}
          onChange={handleGroupAccountChange}
        >
          {groupAccounts.map(account => (
            <option key={account.id} value={account.id}>
              {account.accountName} ({account.accountNo})
            </option>
          ))}
        </select>
        {selectedGroupAccount && (
          <p className="mb-4 text-sm text-gray-600">
            입금계좌 잔액: {selectedGroupAccount.balance.toLocaleString()} {selectedGroupAccount.currency.currencyCode}
          </p>
        )}

        <input
          type="number"
          className="mb-4 p-2 w-full border rounded"
          value={transferAmount}
          onChange={(e) => setTransferAmount(e.target.value)}
          placeholder="이체 금액"
        />
        <input
          type="text"
          className="mb-4 p-2 w-full border rounded"
          value={transactionSummary}
          onChange={(e) => setTransactionSummary(e.target.value)}
          placeholder="거래 내용 (선택사항)"
        />
        <button
          className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition duration-200"
          onClick={handleTransfer}
          disabled={isLoading}
        >
          {isLoading ? '처리 중...' : '이체하기'}
        </button>
      </div>
    </div>
  );
};

export default Transaction;