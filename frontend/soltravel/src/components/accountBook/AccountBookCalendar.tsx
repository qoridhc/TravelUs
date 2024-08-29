import React, { useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { format } from "date-fns";
import "../../css/calendar.css";

type ValuePiece = Date | null;

type Value = ValuePiece | [ValuePiece, ValuePiece];

const AccountBookCalendar = () => {
  const [value, onChange] = useState<Value>(new Date());

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
      />
    </div>
  );
};

export default AccountBookCalendar;
