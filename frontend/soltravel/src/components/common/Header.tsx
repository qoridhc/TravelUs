import React from "react";
import { RxHamburgerMenu } from "react-icons/rx";
import { IoNotificationsOutline } from "react-icons/io5";

const Header = () => {
  return (
    <div className="h-16 p-3 bg-[#EFEFF5] sticky top-0 flex justify-between items-center">
      <img className="w-1/3" src="/assets/logo.png" alt="" />
      <div className="flex space-x-3">
        <IoNotificationsOutline className="text-2xl" />
        <RxHamburgerMenu className="text-2xl" />
      </div>
    </div>
  );
};

export default Header;
