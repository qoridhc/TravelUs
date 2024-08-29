import React, { useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { format } from "date-fns";
import "../../css/calendar.css";

type ValuePiece = Date | null;

type Value = ValuePiece | [ValuePiece, ValuePiece];

const AccountBookCalendar = () => {
  const [value, onChange] = useState<Value>(null);
  const [activeYear, setActiveYear] = useState("");
  const [activeMonth, setActiveMonth] = useState("");

  const monthlyTransaction = [
    { totalExpenditure: 0, totalIncome: 0 },
    { totalExpenditure: 45.12, totalIncome: 0 },
    { totalExpenditure: 30.5, totalIncome: 100.0 },
    { totalExpenditure: 23.89, totalIncome: 100.0 },
    { totalExpenditure: 0, totalIncome: 150.0 },
    { totalExpenditure: 75.34, totalIncome: 0 },
    { totalExpenditure: 60.12, totalIncome: 50.0 },
    { totalExpenditure: 90.75, totalIncome: 0 },
    { totalExpenditure: 12.49, totalIncome: 0 },
    { totalExpenditure: 109.99, totalIncome: 200.0 },
    { totalExpenditure: 27.5, totalIncome: 0 },
    { totalExpenditure: 35.25, totalIncome: 0 },
    { totalExpenditure: 0, totalIncome: 300.0 },
    { totalExpenditure: 80.6, totalIncome: 0 },
    { totalExpenditure: 44.99, totalIncome: 50.0 },
    { totalExpenditure: 98.3, totalIncome: 0 },
    { totalExpenditure: 56.79, totalIncome: 0 },
    { totalExpenditure: 150.1, totalIncome: 0 },
    { totalExpenditure: 39.99, totalIncome: 100.0 },
    { totalExpenditure: 24.75, totalIncome: 0 },
    { totalExpenditure: 0, totalIncome: 200.0 },
    { totalExpenditure: 77.4, totalIncome: 0 },
    { totalExpenditure: 65.89, totalIncome: 0 },
    { totalExpenditure: 123.99, totalIncome: 250.0 },
    { totalExpenditure: 84.67, totalIncome: 0 },
    { totalExpenditure: 46.5, totalIncome: 0 },
    { totalExpenditure: 0, totalIncome: 150.0 },
    { totalExpenditure: 38.29, totalIncome: 0 },
    { totalExpenditure: 102.34, totalIncome: 50.0 },
    { totalExpenditure: 57.48, totalIncome: 0 },
    { totalExpenditure: 85.5, totalIncome: 0 },
    { totalExpenditure: 74.99, totalIncome: 0 },
  ];

  const handleActiveDateChange = (date: any) => {
    setActiveYear(date.activeStartDate.getFullYear());
    setActiveMonth(date.activeStartDate.getMonth() + 1);
  };

  return (
    <div className="w-full flex justify-center relative">
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
          <>
            <p className="text-xs text-[#FF5F5F] font-semibold">
              {monthlyTransaction[date.getDate()].totalExpenditure !== 0
                ? `+ ${monthlyTransaction[date.getDate()].totalExpenditure}`
                : ""}
            </p>
            <p className="text-xs text-[#0471E9] font-semibold">
              {monthlyTransaction[date.getDate()].totalIncome !== 0
                ? `- ${monthlyTransaction[date.getDate()].totalIncome}`
                : ""}
            </p>
          </>
        )}
        onActiveStartDateChange={(activeStartDate) => handleActiveDateChange(activeStartDate)}
      />
    </div>
  );
};

export default AccountBookCalendar;
