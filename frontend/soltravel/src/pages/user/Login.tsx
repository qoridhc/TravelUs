import React, { useState } from "react";
import { useNavigate } from "react-router";
import { userApi } from "../../api/user";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.name === "email") {
      setEmail(e.target.value);
    } else {
      setPassword(e.target.value);
    }
  };

  const handleLogin = async () => {
    try {
      const response = await userApi.fetchLogin(email, password);
      if (response.status === 200) {
        localStorage.setItem("accessToken", response.data.accessToken);
        localStorage.setItem("userId", response.data.userId.toString());
        localStorage.setItem("userName", response.data.name);
        navigate("/");
      }
    } catch (error) {
      console.error("Error creating user:", error);
      alert("로그인 에러");
    }
  };

  return (
    <div className="w-full h-full bg-[#EFEFF5]">
      <div className="w-full h-full flex flex-col justify-center items-center">
        <div className="w-full h-[50%] flex flex-col justify-between items-center">
          <div className="w-[90%] h-[78%] flex flex-col items-center justify-around rounded-xl bg-white shadow-sm">
            <p className="text-2xl font-bold">All-In-One 로그인</p>
            <div className="w-[83%] h-[50%] flex flex-col justify-around text-zinc-500">
              <p className="text-sm font-semibold">아이디</p>
              <input
                onChange={(e) => {
                  handleChange(e);
                }}
                className="focus:outline-none text-zinc-800"
                type="text"
                name="email"
                value={email}
              />
              <hr className="bg-zinc-800" />
              <p className="text-sm font-semibold">사용자 암호</p>
              <input
                onChange={(e) => {
                  handleChange(e);
                }}
                className="focus:outline-none text-zinc-800"
                type="password"
                name="password"
                value={password}
              />
              <hr className="bg-zinc-800" />
            </div>
            <button
              onClick={() => {
                handleLogin();
              }}
              className="w-[92%] h-12 rounded-md bg-[#0046FF] font-bold text-white text-sm">
              로그인
            </button>
          </div>

          <div className="w-[90%] h-[18%] flex items-center justify-around rounded-xl bg-white shadow-sm">
            <div className="flex w-[58%] justify-around items-center">
              <p className="text-sm font-bold">계정이 없으신가요?</p>
              <button
                onClick={() => {
                  navigate("/signup");
                }}
                className="text-[#0046FF] font-bold">
                가입하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
