import { useState } from "react";
import { useNavigate } from "react-router";
import UserForm from "../../components/user/UserForm";


const SignUp = () => {
  const navigate = useNavigate();

  return (
    <div className="w-full h-screen p-5 bg-[#EFEFF5]">
      <div className="h-full flex flex-col justify-center space-y-8">
        <p className="text-2xl font-bold">All-In-One 뱅크 회원가입</p>
        <UserForm />
        <button
          onClick={() => {
            navigate("/");
          }}
          className="w-full h-24 rounded-md bg-[#0046FF] font-bold text-white text-sm">
          가입
        </button>
        <div className="w-full h-20 flex items-center justify-center space-x-2">
          <p className="text-sm font-bold">계정이 있으신가요?</p>
          <button
            onClick={() => {
              navigate("/login");
            }}
            className="text-[#0046FF] font-bold">
            로그인
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignUp;