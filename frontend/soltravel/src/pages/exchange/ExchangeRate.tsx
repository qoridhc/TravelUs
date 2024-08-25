import React, { useState, useEffect } from 'react';
import { Calendar, ChevronDown, Clock } from 'lucide-react';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';
import { DayPicker } from 'react-day-picker';
import 'react-day-picker/dist/style.css';
import { useNavigate } from 'react-router-dom';

// 환율 관련 정보 백에서 받아올 준비 interface로 type 설정
interface Currency {
  code: string;
  name: string;
  amount: number;
}

interface TimeSlot {
  id: number;
  name: string;
}

// 선택 박스 설정 (이것도 백에서 주는거 확인 해야 함)
const currencies: Currency[] = [
  { code: 'JPY', name: '일본 엔', amount: 100 },
  { code: 'USD', name: '미국 달러', amount: 1 },
  { code: 'EUR', name: '유럽 유로', amount: 1 },
  { code: 'CNY', name: '중국 위안', amount: 1 },
  { code: 'GBP', name: '영국 파운드', amount: 1 },
  { code: 'CHF', name: '스위스 프랑', amount: 1 },
  { code: 'CAD', name: '캐나다 달러', amount: 1 },
];

const timeSlots: TimeSlot[] = [
  { id: 1, name: '9:00 (1회차)' },
  { id: 2, name: '11:00 (2회차)' },
  { id: 3, name: '15:30 (3회차)' },
];

const ExchangeRateComponent: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [showCalendar, setShowCalendar] = useState<boolean>(false);
  const [exchangeRate, setExchangeRate] = useState<number | null>(null);
  const [selectedCurrency, setSelectedCurrency] = useState<Currency>(currencies[0]);
  const [showCurrencyDropdown, setShowCurrencyDropdown] = useState<boolean>(false);
  const [selectedTimeSlot, setSelectedTimeSlot] = useState<TimeSlot>(timeSlots[0]);
  const [showTimeSlotDropdown, setShowTimeSlotDropdown] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  useEffect(() => {
    fetchExchangeRate();
  }, [selectedCurrency]);

  // 달력 선택
  const handleDateSelect = (date: Date | undefined) => {
    if (date) {
      setSelectedDate(date);
      setShowCalendar(false);
    }
  };

  // 통화 선택
  const handleCurrencySelect = (currency: Currency) => {
    setSelectedCurrency(currency);
    setShowCurrencyDropdown(false);
  };

  // 회차 선택
  const handleTimeSlotSelect = (timeSlot: TimeSlot) => {
    setSelectedTimeSlot(timeSlot);
    setShowTimeSlotDropdown(false);
  };

  // 데이터 받아오는 로직 (백으로 요청)
  const fetchExchangeRate = async () => {
    setIsLoading(true);
    setError(null);
    try {
      // 실제 API 호출로 대체해야 합니다.
      // const response = await fetch(`your-api-endpoint?currency=${selectedCurrency.code}`);
      // const data = await response.json();
      // setExchangeRate(data.rate);

      // 임시로 랜덤한 환율을 생성합니다.
      const mockRate = Math.random() * 1000 + 500;
      setExchangeRate(mockRate);
    } catch (err) {
      setError('환율 정보를 가져오는 데 실패했습니다. 다시 시도해 주세요.');
    } finally {
      setIsLoading(false);
    }
  };

  // 환전하기 페이지로 이동
  const handleExchange = () => {
    navigate('/exchange');
  };

  return (
    <div className="p-4 mt-5 max-w-sm mx-auto bg-white rounded-xl shadow-md space-y-4">
      <h1 className="text-xl font-bold">환율 조회</h1>
      <div className="space-y-4">
        <div>
          <h2 className="text-sm font-semibold mb-1">통화</h2>
          <div className="relative">
            <button
              className="w-full flex items-center justify-between border rounded p-2"
              onClick={() => setShowCurrencyDropdown(!showCurrencyDropdown)}
            >
              <span>{`${selectedCurrency.name} (${selectedCurrency.amount} ${selectedCurrency.code})`}</span>
              <ChevronDown className="w-5 h-5" />
            </button>
            {showCurrencyDropdown && (
              <div className="absolute z-20 w-full mt-1 bg-white border rounded shadow-lg max-h-60 overflow-auto">
                {currencies.map((currency) => (
                  <button
                    key={currency.code}
                    className="w-full text-left px-4 py-2 hover:bg-gray-100"
                    onClick={() => handleCurrencySelect(currency)}
                  >
                    {`${currency.name} (${currency.amount} ${currency.code})`}
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>
        
        {exchangeRate !== null && (
          <div className="border rounded p-2">
            <div className="text-sm text-gray-500">
              현재 매매기준율
            </div>
            <div className="flex justify-between items-center">
              <span>{`${selectedCurrency.amount} ${selectedCurrency.code}`}</span>
              <span className="text-[#0046FF] font-bold">{exchangeRate.toFixed(2)}원</span>
            </div>
          </div>
        )}
        
        <div>
          <h2 className="text-sm font-semibold mb-1">날짜</h2>
          <div className="relative">
            <button
              className="w-full flex items-center justify-between border rounded p-2"
              onClick={() => setShowCalendar(!showCalendar)}
            >
              <span>{format(selectedDate, 'yyyy.MM.dd', { locale: ko })}</span>
              <Calendar className="w-5 h-5" />
            </button>
            {showCalendar && (
              <div className="absolute z-10 mt-1 bg-white border rounded shadow-lg">
                <DayPicker
                  mode="single"
                  selected={selectedDate}
                  onSelect={handleDateSelect}
                  locale={ko}
                />
              </div>
            )}
          </div>
        </div>
        
        <div>
          <h2 className="text-sm font-semibold mb-1">회차</h2>
          <div className="relative">
            <button
              className="w-full flex items-center justify-between border rounded p-2"
              onClick={() => setShowTimeSlotDropdown(!showTimeSlotDropdown)}
            >
              <span>{selectedTimeSlot.name}</span>
              <Clock className="w-5 h-5" />
            </button>
            {showTimeSlotDropdown && (
              <div className="absolute z-20 w-full mt-1 bg-white border rounded shadow-lg">
                {timeSlots.map((timeSlot) => (
                  <button
                    key={timeSlot.id}
                    className="w-full text-left px-4 py-2 hover:bg-gray-100"
                    onClick={() => handleTimeSlotSelect(timeSlot)}
                  >
                    {timeSlot.name}
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>
        
        {error && <div className="text-red-500">{error}</div>}
      </div>
      <div className="flex space-x-2">
        <button 
          className="flex-1 bg-[#0046FF] text-white rounded py-2 disabled:bg-blue-300"
          onClick={fetchExchangeRate}
          disabled={isLoading}
        >
          {isLoading ? '조회 중...' : '조회하기'}
        </button>
        <button 
          className="flex-1 bg-gray-200 rounded py-2"
          onClick={handleExchange}
        >
          환전하기
        </button>
      </div>
    </div>
  );
};

export default ExchangeRateComponent;