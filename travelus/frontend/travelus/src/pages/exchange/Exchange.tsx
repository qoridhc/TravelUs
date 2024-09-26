import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { accountApi } from '../../api/account';
import { exchangeRateApi } from '../../api/exchange';
import { MeetingAccountInfo } from '../../types/account';
import { ExchangeRateInfo } from '../../types/exchange';
import { ChevronLeft, ChevronDown, ArrowUpDown } from 'lucide-react';
import { FcMoneyTransfer } from 'react-icons/fc';

const koreanCountryNameMapping: { [key: string]: string } = {
  EUR: "유럽",
  JPY: "일본",
  USD: "미국",
  CNY: "중국",
  KRW: "대한민국",
};

const currencyNameMapping: { [key: string]: string } = {
  EUR: "유로",
  JPY: "엔화",
  USD: "달러",
  CNY: "위안",
  KRW: "원",
};

const getFlagImagePath = (currencyCode: string) => {
  const countryName = {
    EUR: "Europe",
    JPY: "Japan",
    USD: "TheUnitedStates",
    CNY: "China",
    KRW: "Korea",
  }[currencyCode] || currencyCode;
  return `/assets/flag/flagOf${countryName}.png`;
};

const MeetingAccountExchange: React.FC = () => {
  const navigate = useNavigate();
  const [accounts, setAccounts] = useState<MeetingAccountInfo[]>([]);
  const [selectedAccount, setSelectedAccount] = useState<MeetingAccountInfo | null>(null);
  const [exchangeRates, setExchangeRates] = useState<{ [key: string]: number }>({});
  const [krwAmount, setKrwAmount] = useState<string>('');
  const [foreignAmount, setForeignAmount] = useState<string>('');
  const [isAccountMenuOpen, setIsAccountMenuOpen] = useState<boolean>(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [joined, created, rates] = await Promise.all([
          accountApi.fetchJoinedMeetingAccount(),
          accountApi.fetchCreatedMeetingAccount(),
          exchangeRateApi.getExchangeRates()
        ]);
        const allAccounts = [...joined, ...created].filter(account => 
          account.moneyBoxDtoList.some(box => box.currencyCode !== 'KRW')
        );
        setAccounts(allAccounts);
        setExchangeRates(rates.reduce((acc, rate) => ({...acc, [rate.currencyCode]: rate.exchangeRate}), {}));
        if (allAccounts.length > 0) {
          setSelectedAccount(allAccounts[0]);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, []);

  const handleAccountSelect = (account: MeetingAccountInfo) => {
    setSelectedAccount(account);
    setIsAccountMenuOpen(false);
    setKrwAmount('');
    setForeignAmount('');
  };

  const getExchangeRateDisplay = (currencyCode: string) => {
    if (currencyCode === 'JPY') {
      return `100 ${currencyCode} = ${(exchangeRates[currencyCode]).toFixed(2)}원`;
    }
    return `1 ${currencyCode} = ${exchangeRates[currencyCode].toFixed(2)}원`;
  };

  const handleKrwChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/[^0-9]/g, '');
    setKrwAmount(value);
    if (value && getForeignCurrency()) {
      const foreignCurrency = getForeignCurrency()!;
      let calculatedForeign: number;
      if (foreignCurrency.currencyCode === 'JPY') {
        calculatedForeign = (parseInt(value) / exchangeRates[foreignCurrency.currencyCode]) * 100;
      } else {
        calculatedForeign = parseInt(value) / exchangeRates[foreignCurrency.currencyCode];
      }
      setForeignAmount(calculatedForeign.toFixed(2));
    } else {
      setForeignAmount('');
    }
  };

  const handleForeignChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setForeignAmount(value);
    if (value && getForeignCurrency()) {
      const foreignCurrency = getForeignCurrency()!;
      let calculatedKRW: number;
      if (foreignCurrency.currencyCode === 'JPY') {
        calculatedKRW = parseFloat(value) * (exchangeRates[foreignCurrency.currencyCode] / 100);
      } else {
        calculatedKRW = parseFloat(value) * exchangeRates[foreignCurrency.currencyCode];
      }
      setKrwAmount(calculatedKRW.toFixed(0));
    } else {
      setKrwAmount('');
    }
  };

  const getBalance = (currencyCode: string) => {
    if (!selectedAccount) return 0;
    const moneyBox = selectedAccount.moneyBoxDtoList.find(box => box.currencyCode === currencyCode);
    return moneyBox ? moneyBox.balance : 0;
  };

  const getForeignCurrency = () => {
    if (!selectedAccount) return null;
    return selectedAccount.moneyBoxDtoList.find(box => box.currencyCode !== 'KRW');
  };

  const handleConfirm = () => {
    // Implement confirmation logic here
    console.log('Confirmed');
  };

  if (accounts.length === 0) {
    return <div>Loading...</div>;
  }

  return (
    <div className="flex flex-col h-screen">
      <div className="p-4">
        <button onClick={() => navigate(-1)} className="mb-4">
          <ChevronLeft className="w-6 h-6" />
        </button>
        <h1 className="text-xl font-bold mb-2">외화 채우기</h1>
        {getForeignCurrency() && (
          <p className="text-sm text-gray-500 mb-6">
            {getExchangeRateDisplay(getForeignCurrency()!.currencyCode)}
          </p>
        )}

        <div className="relative mb-4">
          <button
            className="w-full text-left bg-gray-100 p-3 rounded-lg flex justify-between items-center"
            onClick={() => setIsAccountMenuOpen(!isAccountMenuOpen)}>
            <span>{selectedAccount ? selectedAccount.groupName : '모임을 선택하세요'}</span>
            <ChevronDown className="w-5 h-5" />
          </button>
          {isAccountMenuOpen && (
            <div className="absolute w-full mt-1 bg-white border rounded-lg shadow-lg z-10">
              {accounts.map((account) => (
                <button
                  key={account.groupId}
                  className="w-full text-left p-3 hover:bg-gray-100"
                  onClick={() => handleAccountSelect(account)}>
                  <div>{account.groupName}</div>
                  <div className="text-sm text-gray-500">계좌번호: {account.groupAccountNo}</div>
                </button>
              ))}
            </div>
          )}
        </div>

        {selectedAccount && (
          <>
            <div className="bg-gray-100 rounded-lg p-4 mb-4">
              <div className="flex justify-between items-center mb-2">
                <div className="flex items-center">
                  <img src={getFlagImagePath("KRW")} alt="KRW Flag" className="w-6 h-4 mr-2" />
                  <span>{koreanCountryNameMapping["KRW"]} {currencyNameMapping["KRW"]}</span>
                </div>
                <div className="flex items-center">
                  <input
                    type="tel"
                    inputMode="numeric"
                    pattern="[0-9]*"
                    value={krwAmount}
                    onChange={handleKrwChange}
                    className="text-right bg-transparent w-20 mr-1"
                    placeholder="0"
                  />
                  <span>KRW</span>
                </div>
              </div>
              <p className="text-sm text-gray-500">잔액: {getBalance('KRW').toLocaleString()} {currencyNameMapping["KRW"]}</p>
            </div>

            <div className="flex justify-center items-center my-3">
              <ArrowUpDown className="w-6 h-6 text-gray-500" />
            </div>

            {getForeignCurrency() && (
              <div className="bg-gray-100 rounded-lg p-4 mb-4">
                <div className="flex justify-between items-center mb-2">
                  <div className="flex items-center">
                    <img src={getFlagImagePath(getForeignCurrency()!.currencyCode)} alt={`${getForeignCurrency()!.currencyCode} Flag`} className="w-6 h-4 mr-2" />
                    <span>{koreanCountryNameMapping[getForeignCurrency()!.currencyCode]} {currencyNameMapping[getForeignCurrency()!.currencyCode]}</span>
                  </div>
                  <div className="flex items-center">
                    <input
                      type="tel"
                      inputMode="numeric"
                      pattern="[0-9]*"
                      value={foreignAmount}
                      onChange={handleForeignChange}
                      className="text-right bg-transparent w-20 mr-1"
                      placeholder="0"
                    />
                    <span>{getForeignCurrency()!.currencyCode}</span>
                  </div>
                </div>
                <p className="text-sm text-gray-500">잔액: {getBalance(getForeignCurrency()!.currencyCode).toLocaleString()} {currencyNameMapping[getForeignCurrency()!.currencyCode]}</p>
              </div>
            )}
          </>
        )}
      </div>

      <div className="p-4 mt-auto">
        <div className="flex items-center justify-center mb-4 text-gray-600">
          <FcMoneyTransfer className="mr-2 text-xl" />
          <p className="text-[#1429A0]">수수료는 튜나뱅크가 낼게요</p>
        </div>
        <button
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={handleConfirm}
          disabled={!selectedAccount || !getForeignCurrency() || (!krwAmount && !foreignAmount) || (parseFloat(krwAmount) <= 0 && parseFloat(foreignAmount) <= 0)}>
          확인
        </button>
      </div>
    </div>
  );
};

export default MeetingAccountExchange;