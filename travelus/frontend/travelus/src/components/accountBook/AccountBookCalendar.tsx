import React, { useEffect, useState, useCallback } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { format } from "date-fns";
import "../../css/calendar.css";
import api from "../../lib/axios";
import { accountBookApi } from "../../api/accountBook";
import { BuyItemInfo, DayHistory } from "../../types/accountBook";
import { useDispatch } from "react-redux";
import { setDayHistoryDetailList } from "../../redux/accountBookSlice";
import AccountBookInputModal from "./AccountBookInputModal";
import AccountBookDetailModal from "./AccountBookDetailModal";

type ValuePiece = Date | null;

type Value = ValuePiece | [ValuePiece, ValuePiece];

interface Props {
  accountNo: string;
}

const AccountBookCalendar = ({ accountNo }: Props) => {
  const dispatch = useDispatch();
  const [value, onChange] = useState<Value>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [activeStartDate, setActiveStartDate] = useState("");
  const [activeEndDate, setActiveEndDate] = useState("");
  const [monthlyTransaction, setMonthlyTransaction] = useState<Array<any>>([]);
  const [selectedDate, setSelectedDate] = useState<ValuePiece>(null);

  const handleActiveDateChange = useCallback((date: any) => {
    setActiveStartDate(format(date.activeStartDate, "yyyyMMdd"));
    const last = new Date(Number(format(date.activeStartDate, "yyyy")), Number(format(date.activeStartDate, "M")), 0);
    setActiveEndDate(format(date.activeStartDate, "yyyyMM") + last.getDate());
  }, []);

  const handleDateDetail = useCallback(
    async (date: any) => {
      if (accountNo !== "") {
        const clickDate = format(date, "yyyyMMdd");
        try {
          const data = {
            date: clickDate,
            transactionType: "A",
          };
          const response = await accountBookApi.fetchAccountBookDayInfo(accountNo, data);
          dispatch(setDayHistoryDetailList(response.data));
          setIsDetailModalOpen(true);
          setSelectedDate(date);
        } catch (error) {
          console.error("accountBookApi의 fetchAccountBookDayInfo:", error);
        }
      }
    },
    [accountNo, dispatch]
  );

  const getAccountBookInfo = useCallback(async () => {
    try {
      setIsLoading(true);
      const data = {
        startDate: activeStartDate,
        endDate: activeEndDate,
        transactionType: "A",
        orderByType: "ASC",
      };
      const response = await accountBookApi.fetchAccountBookInfo(accountNo, data);
      setMonthlyTransaction(response.data.monthHistoryList);
    } catch (error) {
      console.error("accountBook의 fetchAccountBookInfo:", error);
    } finally {
      setIsLoading(false);
    }
  }, [activeStartDate, activeEndDate, accountNo]);

  useEffect(() => {
    const initDates = () => {
      const today = new Date();
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
      const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
      setActiveStartDate(format(firstDay, "yyyyMMdd"));
      setActiveEndDate(format(lastDay, "yyyyMMdd"));
    };

    initDates();

    if (accountNo !== "") {
      getAccountBookInfo();
    }
  }, [accountNo, getAccountBookInfo]);

  return isLoading ? (
    <p>Loading...</p>
  ) : (
    <div className="w-full flex flex-col justify-center items-end space-y-3 relative">
      <Calendar
        className="p-3 rounded-md"
        value={value}
        onChange={onChange}
        formatDay={(locale, date) => format(date, "d")}
        calendarType="gregory"
        showNeighboringMonth={false}
        next2Label={null}
        prev2Label={null}
        minDetail="year"
        tileContent={({ date }) => (
          <div>
            <div className="text-xs text-left font-semibold">
              <p className="text-[#FF5F5F]">
                {monthlyTransaction[date.getDate()] && monthlyTransaction[date.getDate()].totalExpenditure !== 0
                  ? `- ${monthlyTransaction[date.getDate()].totalExpenditure.toFixed(1)}`
                  : ""}
              </p>
              <p className="text-[#0471E9]">
                {monthlyTransaction[date.getDate()] && monthlyTransaction[date.getDate()].totalIncome !== 0
                  ? `+ ${monthlyTransaction[date.getDate()].totalIncome.toFixed(1)}`
                  : ""}
              </p>
            </div>
          </div>
        )}
        onActiveStartDateChange={handleActiveDateChange}
        onClickDay={handleDateDetail}
      />

      <AccountBookInputModal
        accountNo={accountNo}
        isModalOpen={isModalOpen}
        setIsModalOpen={setIsModalOpen}
        getAccountBookInfo={getAccountBookInfo}
      />

      <AccountBookDetailModal
        accountNo={accountNo}
        isModalOpen={isDetailModalOpen}
        setIsModalOpen={setIsDetailModalOpen}
        selectedDate={selectedDate}
      />
    </div>
  );
};

export default AccountBookCalendar;