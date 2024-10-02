import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { accountApi } from "../../api/account";
import { exchangeRateApi } from "../../api/exchange";
import { MeetingAccountInfo } from "../../types/account";
import { ExchangeRateInfo } from "../../types/exchange";
import { ChevronLeft, ChevronDown } from "lucide-react";
import { FcMoneyTransfer } from "react-icons/fc";
import { TiArrowUnsorted } from "react-icons/ti";

const koreanCountryNameMapping: { [key: string]: string } = {
  EUR: "유럽",
  JPY: "일본",
  USD: "미국",
  TWD: "대만",
  KRW: "대한민국",
};

const currencyNameMapping: { [key: string]: string } = {
  EUR: "유로",
  JPY: "엔화",
  USD: "달러",
  TWD: "달러",
  KRW: "원",
};

const getFlagImagePath = (currencyCode: string) => {
  return `/assets/flag/flagOf${currencyCode}.png`;
};

const MeetingAccountExchange: React.FC = () => {
  const navigate = useNavigate();
  const [accounts, setAccounts] = useState<MeetingAccountInfo[]>([]);
  const [selectedAccount, setSelectedAccount] = useState<MeetingAccountInfo | null>(null);
  const [exchangeRates, setExchangeRates] = useState<ExchangeRateInfo[]>([]);
  const [krwAmount, setKrwAmount] = useState<string>("");
  const [foreignAmount, setForeignAmount] = useState<string>("");
  const [isAccountMenuOpen, setIsAccountMenuOpen] = useState<boolean>(false);

  const [adjustedKrwAmount, setAdjustedKrwAmount] = useState<string>("");

  const [isAmountValid, setIsAmountValid] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  // const validateAmount = (amount: string, currencyCode: string) => {
  //   const rateInfo = exchangeRates.find((rate) => rate.currencyCode === currencyCode);
  //   if (rateInfo && parseFloat(amount) < parseFloat(rateInfo.exchangeMin)) {
  //     setIsAmountValid(false);
  //     setErrorMessage(`최소 환전 금액은 ${rateInfo.exchangeMin} ${currencyCode}입니다.`);
  //   } else {
  //     setIsAmountValid(true);
  //     setErrorMessage("");
  //   }
  // };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [joined, created, rates] = await Promise.all([
          accountApi.fetchJoinedMeetingAccount(),
          accountApi.fetchCreatedMeetingAccount(),
          exchangeRateApi.getExchangeRates(),
        ]);
        const allAccounts = [...joined, ...created].filter((account) =>
          account.moneyBoxDtoList.some((box) => box.currencyCode !== "KRW")
        );
        setAccounts(allAccounts);
        setExchangeRates(rates);
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
    setKrwAmount("");
    setForeignAmount("");
  };

  const getExchangeRateDisplay = (currencyCode: string) => {
    const rateInfo = exchangeRates.find((rate) => rate.currencyCode === currencyCode);
    if (!rateInfo) return "";
    if (currencyCode === "JPY") {
      return `100 ${currencyCode} = ${rateInfo.exchangeRate.toFixed(2)}원`;
    }
    return `1 ${currencyCode} = ${rateInfo.exchangeRate.toFixed(2)}원`;
  };

  const handleKrwChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    // 사용자가 입력하는 input 값
    const rawValue = e.target.value.replace(/[^0-9]/g, "");
    const value = Number(rawValue).toLocaleString();
    setKrwAmount(value);

    // 실제로 계산기 되는 값
    const adjustedValue = Math.floor(parseInt(rawValue) / 10) * 10;
    setAdjustedKrwAmount(adjustedValue.toLocaleString());

    if (rawValue && getForeignCurrency()) {
      const foreignCurrency = getForeignCurrency()!;
      const rateInfo = exchangeRates.find((rate) => rate.currencyCode === foreignCurrency.currencyCode);
      if (rateInfo) {
        let calculatedForeign: number;
        if (foreignCurrency.currencyCode === "JPY") {
          calculatedForeign = (adjustedValue / rateInfo.exchangeRate) * 100;
        } else {
          calculatedForeign = adjustedValue / rateInfo.exchangeRate;
        }
        setForeignAmount(calculatedForeign.toFixed(2));
      }
    } else {
      setForeignAmount("");
    }
  };

  const handleForeignChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const rawValue = e.target.value.replace(/[^0-9.]/g, "");
    const value = rawValue.includes(".") ? rawValue : Number(rawValue).toLocaleString();
    setForeignAmount(value);

    if (rawValue && getForeignCurrency()) {
      const foreignCurrency = getForeignCurrency()!;
      const rateInfo = exchangeRates.find((rate) => rate.currencyCode === foreignCurrency.currencyCode);
      if (rateInfo) {
        let calculatedKRW: number;
        if (foreignCurrency.currencyCode === "JPY") {
          calculatedKRW = parseFloat(rawValue) * (rateInfo.exchangeRate / 100);
        } else {
          calculatedKRW = parseFloat(rawValue) * rateInfo.exchangeRate;
        }
        setKrwAmount(Math.round(calculatedKRW).toLocaleString());
      }
    } else {
      setKrwAmount("");
    }
  };

  const getBalance = (currencyCode: string) => {
    if (!selectedAccount) return 0;
    const moneyBox = selectedAccount.moneyBoxDtoList.find((box) => box.currencyCode === currencyCode);
    return moneyBox ? moneyBox.balance : 0;
  };

  const getForeignCurrency = () => {
    if (!selectedAccount) return null;
    return selectedAccount.moneyBoxDtoList.find((box) => box.currencyCode !== "KRW");
  };

  const handleConfirm = async () => {
    if (!selectedAccount || !getForeignCurrency() || !krwAmount) return;

    const foreignCurrency = getForeignCurrency()!;
    const rateInfo = exchangeRates.find((rate) => rate.currencyCode === foreignCurrency.currencyCode);

    if (rateInfo && parseFloat(foreignAmount) < parseFloat(rateInfo.exchangeMin)) {
      alert(`최소 환전 금액은 ${rateInfo.exchangeMin} ${foreignCurrency.currencyCode}입니다.`);
      return;
    }

    const cleanedKrwAmount = adjustedKrwAmount.replace(/,/g, "");

    navigate("/exchange/account-password-input", {
      state: {
        accountNo: selectedAccount.groupAccountNo,
        sourceCurrencyCode: "KRW",
        targetCurrencyCode: foreignCurrency.currencyCode,
        transactionBalance: cleanedKrwAmount,
      },
    });
    console.log(foreignCurrency);

    if (accounts.length === 0) {
      return <div>Loading...</div>;
    }
  };

  // 머니박스가 연결된 그룹통장이 없을 때
  const noAccount = accounts.length === 0;

  return (
    <div className="flex flex-col h-full p-5 pb-8">
      <div>
        <button onClick={() => navigate(-1)} className="mb-7">
          <ChevronLeft className="w-6 h-6" />
        </button>
        <h1 className="text-xl font-bold mb-2">외화 채우기</h1>
        {getForeignCurrency() && (
          <p className="text-sm text-gray-500 mb-6">{getExchangeRateDisplay(getForeignCurrency()!.currencyCode)}</p>
        )}
        <div className="relative mb-4">
          <button
            className="w-full text-left bg-gray-100 p-3 rounded-lg flex justify-between items-center"
            onClick={() => !noAccount && setIsAccountMenuOpen(!isAccountMenuOpen)}
            disabled={noAccount}>
            <span>
              {noAccount
                ? "모임통장에 연결된 머니박스가 없어요..."
                : selectedAccount
                ? selectedAccount.groupName
                : "모임을 선택하세요"}
            </span>
            {!noAccount && <ChevronDown className="w-5 h-5" />}
          </button>
          {isAccountMenuOpen && !noAccount && (
            <div className="absolute w-full mt-1 bg-gray-100 border rounded-lg shadow-lg z-10">
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
          <div className="relative">
            <div className="bg-gray-300 rounded-lg p-4">
              <div className="flex justify-between items-center mb-2">
                <div className="flex items-center">
                  <img src={getFlagImagePath("KRW")} alt="KRW Flag" className="w-6 h-4 mr-2" />
                  <span>
                    {koreanCountryNameMapping["KRW"]} {currencyNameMapping["KRW"]}
                  </span>
                </div>
                <div className="flex items-center">
                  <input
                    type="text"
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
              <p className="text-sm text-gray-500">
                잔액: {getBalance("KRW").toLocaleString()} {currencyNameMapping["KRW"]}
              </p>
            </div>
            {/* 양방향 화살표 아이콘 */}
            <div className="w-8 h-8 bg-white shadow-md border-[0.5px] border-[#cccccc] rounded-full flex justify-center items-center absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
              <TiArrowUnsorted className="text-sm text-[#999999]" />
            </div>
            <div className="h-5 " />
            {getForeignCurrency() && (
              <div className="bg-gray-300 rounded-lg p-4">
                <div className="flex justify-between items-center mb-2">
                  <div className="flex items-center">
                    <img
                      src={getFlagImagePath(getForeignCurrency()!.currencyCode)}
                      alt={`${getForeignCurrency()!.currencyCode} Flag`}
                      className="w-6 h-4 mr-2"
                    />
                    <span>
                      {koreanCountryNameMapping[getForeignCurrency()!.currencyCode]}{" "}
                      {currencyNameMapping[getForeignCurrency()!.currencyCode]}
                    </span>
                  </div>
                  <div className="flex items-center">
                    <input
                      type="text"
                      inputMode="decimal"
                      pattern="[0-9]*"
                      value={foreignAmount}
                      onChange={handleForeignChange}
                      className={`text-right bg-transparent w-20 mr-1 ${
                        !isAmountValid ? "border-red-500 border-2" : ""
                      }`}
                      placeholder="0"
                    />
                    <span>{getForeignCurrency()!.currencyCode}</span>
                  </div>
                </div>
                <p className="text-sm text-gray-500">
                  잔액: {getBalance(getForeignCurrency()!.currencyCode).toLocaleString()}{" "}
                  {currencyNameMapping[getForeignCurrency()!.currencyCode]}
                </p>
              </div>
            )}
            {/* {!isAmountValid && <p className="text-red-500 text-sm mt-1">{errorMessage}</p>} */}
            {getForeignCurrency() && (
              <p className="float-right text-gray-500 mt-2 mr-2">
                최소 환전 가능 금액:{" "}
                {exchangeRates.find((rate) => rate.currencyCode === getForeignCurrency()!.currencyCode)?.exchangeMin}{" "}
                {getForeignCurrency()!.currencyCode}
              </p>
            )}
          </div>
        )}
      </div>

      <div className="mt-auto">
        <div className="flex items-center justify-center mb-4 text-gray-600">
          <FcMoneyTransfer className="mr-2 text-xl" />
          <p className="text-[#1429A0]">수수료는 튜나뱅크가 낼게요</p>
        </div>
        <button
          className={`w-full h-14 text-lg rounded-xl tracking-wide text-white ${
            noAccount || !selectedAccount || !getForeignCurrency() || !krwAmount || parseFloat(krwAmount) <= 0
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-[#1429A0]"
          }`}
          onClick={handleConfirm}
          disabled={noAccount || !selectedAccount || !getForeignCurrency() || !krwAmount || parseFloat(krwAmount) <= 0}>
          외화 채우기
        </button>

        {/* 머니박스 연결된 모임통장 없을시 보이게 */}
        {noAccount ? (
          <Link to="/meeting/create/prepare" className="block text-center mt-4 text-[#1429A0] underline">
            머니박스 개설하러 가기
          </Link>
        ) : (
          <p className="text-sm text-gray-500 text-center mt-4">외화 환전 금액은 10원 단위로 자동 조정됩니다.</p>
        )}
      </div>
    </div>
  );
};

export default MeetingAccountExchange;
