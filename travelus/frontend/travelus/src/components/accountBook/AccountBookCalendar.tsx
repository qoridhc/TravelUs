import React, { useCallback, useEffect, useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { endOfMonth, format, startOfMonth } from "date-fns";
import "../../css/calendar.css";
import AccountBookInputModal from "./AccountBookInputModal";
import AccountBookDetailModal from "./AccountBookDetailModal";
import Lottie from "lottie-react";
import loadingAnimation from "../../lottie/loadingAnimation.json";
import { accountBookApi } from "../../api/accountBook";
import { DayHistory } from "../../types/accountBook";

interface Props {
  accountNo: string;
}

const AccountBookCalendar = ({ accountNo }: Props) => {
  const [isLoading, setIsLoading] = useState(false);
  const [firstDate, setFirstDate] = useState<Date | null>(null);
  const [lastDate, setLastDate] = useState<Date | null>(null);
  const [monthlyTransaction, setMonthlyTransaction] = useState<DayHistory[]>([]);

  const handleActiveStartDateChange = (activeStartDate: Date) => {
    const firstDay = startOfMonth(activeStartDate);
    const lastDay = endOfMonth(activeStartDate);
    setFirstDate(firstDay);
    setLastDate(lastDay);
  };

  const fetchAccountBookInfo = async () => {
    if (!firstDate || !lastDate) return;

    const data = {
      startDate: format(firstDate, "yyyy-MM-dd"),
      endDate: format(lastDate, "yyyy-MM-dd"),
      transactionType: "A",
      orderByType: "ASC",
    };

    try {
      setIsLoading(true);
      const response = await accountBookApi.fetchAccountBookInfo(accountNo, data);
      console.log(response);
      setMonthlyTransaction(response.data.monthHistoryList);
    } catch (error) {
      console.log("accountBookApi의 fetchAccountBookInfo : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const today = new Date();
    const firstDay = startOfMonth(today);
    const lastDay = endOfMonth(today);
    setFirstDate(firstDay);
    setLastDate(lastDay);
  }, []);

  useEffect(() => {
    if (accountNo === "") return;

    fetchAccountBookInfo();
  }, [accountNo]);

  if (isLoading || !monthlyTransaction) {
    return (
      <div className="h-full flex flex-col justify-center items-center">
        <Lottie animationData={loadingAnimation} />
      </div>
    );
  }

  console.log(monthlyTransaction);

  return (
    <div className="w-full flex flex-col justify-center items-end space-y-3 relative">
      <Calendar
        className="p-3 rounded-md"
        formatDay={(_, date) => format(date, "d")}
        calendarType="gregory"
        showNeighboringMonth={false}
        next2Label={null}
        prev2Label={null}
        minDetail="year"
        onActiveStartDateChange={({ activeStartDate }) =>
          activeStartDate && handleActiveStartDateChange(activeStartDate)
        }
        tileContent={({ date }) => {
          if (!monthlyTransaction[date.getDate()]) return;
          const day = date.getDate();

          return (
            <div className="h-3 flex justify-center items-center space-x-1">
              {/* 지출 유무 */}
              {monthlyTransaction[day].totalExpenditureForeign + monthlyTransaction[day].totalExpenditureKRW !== 0 && (
                <div className="w-2 h-2 bg-[#DD5257] rounded-full"></div>
              )}

              {/* 수입 유무 */}
              {monthlyTransaction[day].totalIncomeForeign + monthlyTransaction[day].totalIncomeKRW !== 0 && (
                <div className="w-2 h-2 bg-[#4880EE] rounded-full"></div>
              )}
            </div>
          );
        }}
      />

      {/* <AccountBookInputModal
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
      /> */}
    </div>
  );
};

export default AccountBookCalendar;
