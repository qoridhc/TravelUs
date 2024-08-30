import React, { useEffect, useState } from "react";
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

type ValuePiece = Date | null;

type Value = ValuePiece | [ValuePiece, ValuePiece];

interface Props {
  accountNo: string;
}

const AccountBookCalendar = ({ accountNo }: Props) => {
  const dispatch = useDispatch();
  const [value, onChange] = useState<Value>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [activeStartDate, setActiveStartDate] = useState("");
  const [activeEndDate, setActiveEndDate] = useState("");
  const [monthlyTransaction, setMonthlyTransaction] = useState<Array<DayHistory>>([]);

  const handleActiveDateChange = (date: any) => {
    setActiveStartDate(format(date.activeStartDate, "yyyyMMdd"));
    let last = new Date(Number(format(date.activeStartDate, "yyyy")), Number(format(date.activeStartDate, "M")), 0);
    setActiveEndDate(format(date.activeStartDate, "yyyyMM") + last.getDate());
  };

  const handleDateDetail = async (date: any) => {
    if (accountNo !== "") {
      let clickDate = format(date, "yyyyMMdd");
      try {
        const data = {
          date: clickDate,
          transactionType: "A",
        };
        const response = await accountBookApi.fetchAccountBookDayInfo(accountNo, data);
        dispatch(setDayHistoryDetailList(response.data));
      } catch (error) {
        console.log("accountBookApi의 fetchAccountBookDayInfo : ", error);
      }
    }
  };

  const getAccountBookInfo = async () => {
    try {
      setIsLoading(true);
      const data = { startDate: activeStartDate, endDate: activeEndDate, transactionType: "A", orderByType: "ASC" };
      const response = await accountBookApi.fetchAccountBookInfo(accountNo, data);
      setMonthlyTransaction(response.data.monthHistoryList);
    } catch (error) {
      console.log("accountBook의 fetchAccountBookInfo : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const initDates = () => {
      const today = new Date();
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
      const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

      setActiveStartDate(format(firstDay, "yyyyMMdd"));
      setActiveEndDate(format(lastDay, "yyyyMMdd"));
    };

    initDates(); // 컴포넌트가 처음 렌더링될 때 실행

    if (accountNo !== "") {
      getAccountBookInfo();
    }
  }, [accountNo]);

  return isLoading ? (
    <p>Loading...</p>
  ) : (
    <div className="w-full flex flex-col justify-center items-end space-y-3 relative">
      <Calendar
        className="p-3 rounded-md"
        value={value}
        onChange={onChange}
        formatDay={(locale, date) => format(date, "d")}
        calendarType="gregory" // 일요일 부터 시작
        showNeighboringMonth={false} // 전달, 다음달 날짜 숨기기
        next2Label={null} // +1년 & +10년 이동 버튼 숨기기
        prev2Label={null} // -1년 & -10년 이동 버튼 숨기기
        minDetail="year" // 10년단위 년도 숨기기
        tileContent={({ activeStartDate, date, view }) => (
          <div>
            <div className="text-xs text-left font-semibold">
              <p className="text-[#FF5F5F]">
                {monthlyTransaction[date.getDate()] !== undefined &&
                monthlyTransaction[date.getDate()].totalExpenditure !== 0
                  ? `+ ${monthlyTransaction[date.getDate()].totalExpenditure}`
                  : ""}
              </p>
              <p className="text-[#0471E9]">
                {monthlyTransaction[date.getDate()] !== undefined &&
                monthlyTransaction[date.getDate()].totalIncome !== 0
                  ? `- ${monthlyTransaction[date.getDate()].totalIncome}`
                  : ""}
              </p>
            </div>

            {/* {today === date ? <p>오늘</p> : <></>} */}
          </div>
        )}
        onActiveStartDateChange={(activeStartDate) => handleActiveDateChange(activeStartDate)}
        onClickDay={(value, event) => handleDateDetail(value)}
      />

      <AccountBookInputModal accountNo={accountNo} />
    </div>
  );
};

export default AccountBookCalendar;
