import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router";
import { RiHome5Line } from "react-icons/ri";
import { IoPeople } from "react-icons/io5";
import { MdOutlineEventNote } from "react-icons/md";
import { BsThreeDots } from "react-icons/bs";

const Footer = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [currentMenu, setCurrentMenu] = useState("홈");

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);

  const handleMenuHome = () => {
    navigate("/");
    setCurrentMenu("홈");
  };

  const handleMenuExchange = () => {
    navigate("/meetingaccountlist");
    setCurrentMenu("모임통장");
  };

  const handleMenuAccountDiary = () => {
    navigate("/accountbookdetail");
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
        className={`flex flex-col items-center ${currentMenu === "모임통장" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuExchange()}>
        <IoPeople className="text-2xl" />
        <p className="text-sm font-medium">모임통장</p>
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
