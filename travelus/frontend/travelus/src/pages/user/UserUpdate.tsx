import { useNavigate } from "react-router";
import { useEffect, useState } from "react";
import UpdateUserForm from "../../components/user/UpdateUserForm";
import { IoIosArrowBack } from "react-icons/io";

const UserUpdate = () => {
  const navigate = useNavigate();

  return (
    <div className="w-full h-full p-5 bg-white flex flex-col justify-between">
      <div className="flex flex-col space-y-10">
        <div className="mt-1 flex space-x-1">
          <IoIosArrowBack
            onClick={() => {
              navigate(-1);
            }}
            className="text-2xl"
          />
          <p className="text-lg font-bold">내 정보 수정</p>
        </div>
        <UpdateUserForm />
      </div>
      <div className="flex flex-col"></div>
      <button
        onClick={() => {
          navigate("/mypage");
        }}
        className="w-full h-12 rounded-md bg-[#1429A0] font-bold text-white text-sm">
        수정하기
      </button>
    </div>
  );
};

export default UserUpdate;
