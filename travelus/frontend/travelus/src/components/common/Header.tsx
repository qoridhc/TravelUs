import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { IoNotificationsOutline } from "react-icons/io5";
import { IoIosArrowForward } from "react-icons/io";
import { userApi } from "../../api/user";

const Header = () => {
  const navigate = useNavigate();
  const [userName, setUserName] = useState("");

  useEffect(() => {
    // 유정 정보 가져오는 API 호출
    const fetchUser = async () => {
      try {
        const response = await userApi.fetchUser();
        if (response.status === 200) {
          setUserName(response.data.name);
        }
      } catch (error) {
        console.error(error);
      }
    }

    fetchUser();
  }, []);


  return (
    <div className="h-16 p-5 bg-[#F3F4F6] sticky top-0 flex justify-between items-center z-50">
      <button
        onClick={() => {
          navigate("/mypage");
        }}
        className="text-xl flex items-center space-x-2">
        <p className="text-[#565656] font-semibold">{userName} 님</p>
        <IoIosArrowForward className="text-[#565656]" />
      </button>
      <button
        onClick={() => {navigate("/notification")}}>
        <IoNotificationsOutline className="text-2xl" />
      </button>
    </div>
  );
};

export default Header;
