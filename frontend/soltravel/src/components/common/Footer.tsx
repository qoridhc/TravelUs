import React, { useState } from "react";
import { RiHome5Line } from "react-icons/ri";
import { MdCurrencyExchange } from "react-icons/md";
import { MdOutlineEventNote } from "react-icons/md";
import { BsThreeDots } from "react-icons/bs";

const Footer = () => {
  const [currentMenu, setCurrentMenu] = useState("홈");

  const handleMenuHome = () => {
    setCurrentMenu("홈");
  };

  const handleMenuExchange = () => {
    setCurrentMenu("환전");
  };

  const handleMenuAccountDiary = () => {
    setCurrentMenu("가계부");
  };

  const handleMenuConversation = () => {
    setCurrentMenu("전체");
  };

  return (
    <div className="w-full h-16 p-3 bg-white border-t fixed bottom-0 flex justify-around items-center">
      <button
        className={`flex flex-col items-center ${currentMenu === "홈" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuHome()}>
        <RiHome5Line className="text-2xl" />
        <p className="text-sm font-medium">홈</p>
      </button>
      <button
        className={`flex flex-col items-center ${currentMenu === "환전" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuExchange()}>
        <MdCurrencyExchange className="text-2xl" />
        <p className="text-sm font-medium">환전</p>
      </button>
      <button
        className={`flex flex-col items-center ${currentMenu === "가계부" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuAccountDiary()}>
        <MdOutlineEventNote className="text-2xl" />
        <p className="text-sm font-medium">가계부</p>
      </button>
      <button
        className={`flex flex-col items-center ${currentMenu === "전체" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuConversation()}>
        <BsThreeDots className="text-2xl" />
        <p className="text-sm font-medium">전체</p>
      </button>
    </div>
  );
};

export default Footer;