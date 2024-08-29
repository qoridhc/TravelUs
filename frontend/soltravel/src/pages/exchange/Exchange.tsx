import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { accountApi } from '../../api/account';
import { exchangeApi } from '../../api/exchange';
import { AccountInfo } from '../../types/account';
import { ExchangeRateInfo, ExchangeRequest, ExchangeResponse } from '../../types/exchange';

const Exchange: React.FC = () => {
  // const { userId } = useParams<{ userId: string }>();
  const userId = 2
  const [accounts, setAccounts] = useState<AccountInfo[]>([]);
  const [selectedAccount, setSelectedAccount] = useState<AccountInfo | null>(null);
  const [exchangeRates, setExchangeRates] = useState<ExchangeRateInfo[]>([]);
  const [exchangeAmount, setExchangeAmount] = useState<string>('');
  const [selectedCurrency, setSelectedCurrency] = useState<string>('USD');
  const [expectedExchange, setExpectedExchange] = useState<string>('0');
  const [isLoading, setIsLoading] = useState<boolean>(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [fetchedAccounts, rates] = await Promise.all([
          accountApi.fetchAccountInfo(userId),
          exchangeApi.getExchangeRates()
        ]);
        const groupAccounts = fetchedAccounts.filter(account => account.accountType === 'GROUP');
        setAccounts(groupAccounts);
        if (groupAccounts.length > 0) {
          setSelectedAccount(groupAccounts[0]);
        }
        setExchangeRates(rates);
        if (rates.length > 0) {
          setSelectedCurrency(rates[0].currencyCode);
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        alert('데이터를 불러오는 데 실패했습니다.');
      }
    };

    fetchData();
  }, [userId]);

  useEffect(() => {
    if (exchangeAmount && exchangeRates.length > 0) {
      const rate = exchangeRates.find(r => r.currencyCode === selectedCurrency)?.exchangeRate;
      if (rate) {
        const expected = (parseFloat(exchangeAmount) / rate).toFixed(2);
        setExpectedExchange(expected);
      }
    } else {
      setExpectedExchange('0');
    }
  }, [exchangeAmount, selectedCurrency, exchangeRates]);

  // 가지고 있는 모임 통장 전체 보이게
  const handleAccountChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const account = accounts.find(acc => acc.accountNo === e.target.value);
    if (account) {
      setSelectedAccount(account);
    }
  };

  const handleExchangeAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setExchangeAmount(e.target.value);
  };

  const handleCurrencyChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedCurrency(e.target.value);
  };

  const handleExchange = async () => {
    if (!selectedAccount) return;

    const numAmount = parseFloat(exchangeAmount);
    if (isNaN(numAmount) || numAmount <= 0) {
      alert('유효한 환전 금액을 입력해주세요.');
      return;
    }

    // if (numAmount > selectedAccount.accountBalance) {
    //   alert('계좌 잔액이 부족합니다.');
    //   return;
    // }

    const selectedRate = exchangeRates.find(r => r.currencyCode === selectedCurrency);
    if (!selectedRate) {
      alert('선택한 통화의 환율 정보를 찾을 수 없습니다.');
      return;
    }

    if (numAmount < selectedRate.exchangeMin) {
      alert(`최소 환전 금액은 ${selectedRate.exchangeMin} KRW입니다.`);
      return;
    }

    const exchangeRequest: ExchangeRequest = {
      accountId: selectedAccount.id,
      accountNo: selectedAccount.accountNo,
      currencyCode: selectedCurrency,
      exchangeAmount: numAmount,
      exchangeRate: selectedRate.exchangeRate
    };

    setIsLoading(true);
    try {
      const response: ExchangeResponse = await exchangeApi.requestExchange(exchangeRequest);
      console.log('환전 완료:', response);
      // alert(`환전이 완료되었습니다. 환전된 금액: ${response.exchangeCurrencyDto.amount} ${response.exchangeCurrencyDto.currencyCode}`);
      // 환전 후 계좌 정보 업데이트
      setAccounts(accounts.map(acc => 
        acc.accountNo === selectedAccount.accountNo 
          ? { ...acc, accountBalance: response.accountInfoDto.balance }
          : acc
      ));
      setSelectedAccount({
        ...selectedAccount,
        // accountBalance: response.accountInfoDto.balance,
      });
      setExchangeAmount('');
      setExpectedExchange('0');
    } catch (error) {
      console.error('환전 중 오류 발생:', error);
      alert('환전 중 오류가 발생했습니다. 다시 시도해주세요.');
    } finally {
      setIsLoading(false);
    }
  };

  if (accounts.length === 0 || !selectedAccount || exchangeRates.length === 0) {
    return <div>Loading...</div>;
  }

  return (
    <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
      <h1 className="text-xl font-bold mb-4">환전하기</h1>
      <div className="bg-white rounded-lg p-4 shadow mb-4">
        <label htmlFor="account-select" className="block text-sm font-medium text-gray-700 mb-2">
          계좌 선택
        </label>
        <select
          id="account-select"
          className="w-full p-2 border rounded mb-2"
          value={selectedAccount.accountNo}
          onChange={handleAccountChange}
        >
          {accounts.map(account => (
            <option key={account.accountNo} value={account.accountNo}>
              {account.accountName} ({account.accountNo})
            </option>
          ))}
        </select>
      </div>
      <h2 className="font-bold mb-2">환전할 금액 입력</h2>
      <div className="bg-white rounded-lg p-4 shadow mb-4">
        <div className="flex items-center mb-2">
          <div className="w-8 h-8 bg-[#0046FF] rounded-full mr-2"></div>
          <div>
            <p className="font-semibold">{selectedAccount.accountName}</p>
            <p className="font-semibold">{selectedAccount.accountNo}</p>
            {/* <p className="text-sm text-gray-500">{selectedAccount.bankName} {selectedAccount.accountNo}</p> */}
          </div>
        </div>
        <input
          type="number"
          className="w-full text-right text-2xl font-bold p-2 border rounded mb-2"
          value={exchangeAmount}
          onChange={handleExchangeAmountChange}
          placeholder="0"
        />
        <select
          className="w-full p-2 border rounded mb-2"
          value={selectedCurrency}
          onChange={handleCurrencyChange}
        >
          {exchangeRates.map(rate => (
            <option key={rate.currencyCode} value={rate.currencyCode}>{rate.currencyCode}</option>
          ))}
        </select>
        <p className="text-right text-sm text-gray-500">
          최소 {exchangeRates.find(r => r.currencyCode === selectedCurrency)?.exchangeMin.toLocaleString()} {selectedCurrency} 이상
        </p>
      </div>
      <div className="bg-white rounded-lg p-4 shadow mb-4">
        <p className="font-bold mb-2">예상 환전 금액</p>
        <p className="text-2xl font-bold">
          {parseFloat(expectedExchange) > 0 ? parseFloat(expectedExchange).toLocaleString() : '0'} {selectedCurrency}
        </p>
        <p className="text-sm text-gray-500">
          적용 환율: {exchangeRates.find(r => r.currencyCode === selectedCurrency)?.exchangeRate.toFixed(2)} {selectedCurrency} = 1 {selectedAccount.currency.currencyCode}
        </p>
      </div>
      <button
        className={`w-full bg-[#0046FF] text-white py-3 rounded-lg mt-4 ${isLoading ? 'opacity-50 cursor-not-allowed' : ''}`}
        onClick={handleExchange}
        disabled={isLoading}
      >
        {isLoading ? '환전 중...' : '환전하기'}
      </button>
    </div>
  );
};

export default Exchange;