import React from "react";
import { useNavigate } from "react-router";
import { IoNotificationsOutline } from "react-icons/io5";
import { IoIosArrowForward } from "react-icons/io";

const Header = () => {
  const navigate = useNavigate()
  const userName = "허동원";


  return (
    <div className="h-16 p-5 bg-[#EFEFF5] sticky top-0 flex justify-between items-center">
      <button
        onClick={() => {navigate("/mypage")}}
        className="text-xl flex items-center space-x-2">
        <p className="text-[#565656] font-semibold">{userName} 님</p>
        <IoIosArrowForward className="text-[#565656]" />
      </button>
      <button>
        <IoNotificationsOutline className="text-2xl" />
      </button>
    </div>
  );
};

export default Header;
