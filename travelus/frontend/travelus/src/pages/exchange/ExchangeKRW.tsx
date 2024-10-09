import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { accountApi } from "../../api/account";
import { exchangeRateApi } from "../../api/exchange";
import { MeetingAccountInfo } from "../../types/account";
import { currencyTypeList, ExchangeRateInfo } from "../../types/exchange";
import { IoIosArrowBack, IoIosArrowDown } from "react-icons/io";
import { FcMoneyTransfer } from "react-icons/fc";
import { TiArrowUnsorted } from "react-icons/ti";
import { formatCurrency } from "../../utils/currencyUtils";

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

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

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
    const rawValue = e.target.value.replace(/[^0-9]/g, "");
    const value = Number(rawValue).toLocaleString();
    setKrwAmount(value);
    if (rawValue && getForeignCurrency()) {
      const foreignCurrency = getForeignCurrency()!;
      const rateInfo = exchangeRates.find((rate) => rate.currencyCode === foreignCurrency.currencyCode);
      if (rateInfo) {
        let calculatedForeign: number;
        if (foreignCurrency.currencyCode === "JPY") {
          calculatedForeign = (parseInt(rawValue) / rateInfo.exchangeRate) * 100;
        } else {
          calculatedForeign = parseInt(rawValue) / rateInfo.exchangeRate;
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

    navigate("/exchange/account-password-input", {
      state: {
        accountNo: selectedAccount.groupAccountNo,
        sourceCurrencyCode: foreignCurrency.currencyCode,
        targetCurrencyCode: "KRW",
        transactionBalance: foreignAmount,
      },
    });
  };

  // 머니박스가 연결된 그룹통장이 없거나 하나 일때
  const noAccount = accounts.length === 0;
  const oneAccount = accounts.length === 1;

  const isButtonDisabled = noAccount || oneAccount;

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <button onClick={() => navigate(-1)}>
          <IoIosArrowBack className="w-6 h-6" />
        </button>

        <div className="grid gap-8">
          <div className="grid gap-3">
            <p className="text-2xl font-semibold">원화 채우기</p>

            {getForeignCurrency() && (
              <p className="text-sm text-right text-[#565656]">
                현재 환율 : {getExchangeRateDisplay(getForeignCurrency()!.currencyCode)}
              </p>
            )}

            <div className="relative">
              <button
                className="w-full p-3 text-left border rounded-lg flex justify-between items-center"
                onClick={() => !noAccount && setIsAccountMenuOpen(!isAccountMenuOpen)}
                disabled={isButtonDisabled}>
                <span>
                  {noAccount ? (
                    "트래블박스가 없습니다."
                  ) : selectedAccount ? (
                    <>
                      <div>{selectedAccount.groupName}</div>
                      <div className="text-sm text-gray-500">{selectedAccount.groupAccountNo}</div>
                    </>
                  ) : (
                    "모임을 선택하세요"
                  )}
                </span>
                {!noAccount && <IoIosArrowDown className="w-5 h-5" />}
              </button>

              {isAccountMenuOpen && !noAccount && (
                <div className="w-full bg-white border rounded-lg shadow-lg absolute top-20 z-50">
                  {accounts.map((account) =>
                    account.groupId === selectedAccount?.groupId ? (
                      <></>
                    ) : (
                      <button
                        key={account.groupId}
                        className="w-full text-left p-3 hover:bg-gray-100"
                        onClick={() => handleAccountSelect(account)}>
                        <div>{account.groupName}</div>
                        <div className="text-sm text-gray-500">{account.groupAccountNo}</div>
                      </button>
                    )
                  )}
                </div>
              )}
            </div>
          </div>

          <div className="grid gap-3">
            <div className="relative">
              {getForeignCurrency() && (
                <div className="p-4 bg-[#F3F4F6] rounded-lg grid grid-cols-2">
                  <div className="flex flex-col space-y-2">
                    <div className="flex items-center">
                      <img src={getFlagImagePath("KRW")} alt="KRW Flag" className="w-6 h-4 mr-2 border" />
                      <span>
                        {koreanCountryNameMapping["KRW"]} {currencyNameMapping["KRW"]}
                      </span>
                    </div>
                    <p className="text-sm text-gray-500">
                      잔액 {getBalance("KRW").toLocaleString()}
                      {currencyNameMapping["KRW"]}
                    </p>
                  </div>

                  <div className="flex justify-end items-center">
                    <input
                      type="text"
                      inputMode="numeric"
                      pattern="[0-9]*"
                      value={krwAmount}
                      onChange={handleKrwChange}
                      className="text-right bg-transparent w-28 mr-1 outline-none"
                      placeholder="0"
                    />
                    <span>KRW</span>
                  </div>
                </div>
              )}

              {/* 양방향 화살표 아이콘 */}
              {getForeignCurrency() && (
                <div className="w-8 h-8 bg-white shadow-md border-[0.5px] border-[#cccccc] rounded-full flex justify-center items-center absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
                  <TiArrowUnsorted className="text-sm text-[#999999]" />
                </div>
              )}

              <div className="h-5 " />

              {getForeignCurrency() && (
                <div className="p-4 bg-[#F3F4F6] rounded-lg grid grid-cols-2">
                  <div className="flex flex-col space-y-2">
                    <div className="flex items-center">
                      <img
                        src={getFlagImagePath(getForeignCurrency()!.currencyCode)}
                        alt={`${getForeignCurrency()!.currencyCode} Flag`}
                        className="w-6 h-4 mr-2 border"
                      />
                      <span>
                        {koreanCountryNameMapping[getForeignCurrency()!.currencyCode]}&nbsp;
                        {currencyNameMapping[getForeignCurrency()!.currencyCode]}
                      </span>
                    </div>

                    <p className="text-sm text-gray-500">
                      잔액: {getBalance(getForeignCurrency()!.currencyCode).toLocaleString()}{" "}
                      {currencyTypeList
                        .find((item) => item.value === getForeignCurrency()!.currencyCode)
                        ?.text.slice(-2, -1)}
                    </p>
                  </div>
                  <div className="flex justify-end items-center">
                    <input
                      type="text"
                      inputMode="decimal"
                      pattern="[0-9]*"
                      value={foreignAmount}
                      onChange={handleForeignChange}
                      className="text-right bg-transparent w-28 mr-1 outline-none"
                      placeholder="0"
                    />
                    <span>{getForeignCurrency()!.currencyCode}</span>
                  </div>
                </div>
              )}
            </div>

            {/* {getForeignCurrency() && (
              <p className="text-sm text-right text-[#565656]">
                최소 환전 금액&nbsp;
                {formatCurrency(
                  Number(
                    exchangeRates.find((rate) => rate.currencyCode === getForeignCurrency()!.currencyCode)?.exchangeMin
                  )
                )}
                {getForeignCurrency()!.currencyCode}
              </p>
            )} */}
          </div>
        </div>
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
          원화 채우기
        </button>

        {/* 머니박스 연결된 모임통장 없을시 보이게 */}
        {/* {noAccount && (
          <Link
            to="/meeting/create/prepare" // 모임통장 생성 페이지의 경로를 여기에 입력하세요
            className="block text-center mt-4 text-[#1429A0] underline">
            머니박스 개설하러 가기
          </Link>
        )} */}
      </div>
    </div>
  );
};

export default MeetingAccountExchange;
