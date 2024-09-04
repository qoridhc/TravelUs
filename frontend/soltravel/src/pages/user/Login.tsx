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
        sessionStorage.setItem("accessToken", response.data.accessToken);
        localStorage.setItem("userId", response.data.userId.toString());
        localStorage.setItem("userName", response.data.name);
        navigate("/");
      }
    } catch (error) {
      console.error("Error creating user:", error);
      alert("아이디 또는 비밀번호가 틀렸습니다.");
    }
  };

  return (
    <div className="w-full h-full bg-[#EFEFF5]">
      <div className="w-full h-full flex flex-col justify-center items-center">
        <div className="w-full h-[50%] flex flex-col justify-between items-center">
          <div className="w-[90%] h-[78%] flex flex-col items-center justify-around rounded-xl bg-white shadow-sm">
            <div className="flex items-center space-x-1">
              <img className="w-7" src="/assets/shc_symbol_ci.png" alt="" />
              <p className="text-[1.4rem] font-bold">신한은행 로그인</p>
            </div>
            <div className="w-[83%] h-[50%] flex flex-col justify-around text-zinc-500">
              <label htmlFor="email" className="text-sm font-semibold cursor-pointer">
                아이디
              </label>
              <input
                onChange={(e) => {
                  handleChange(e);
                }}
                id="email"
                className="focus:outline-none text-zinc-800"
                type="text"
                name="email"
                value={email}
              />
              <hr className="bg-zinc-800" />
              <label htmlFor="password" className="text-sm font-semibold cursor-pointer">
                사용자 암호
              </label>
              <input
                onChange={(e) => {
                  handleChange(e);
                }}
                onKeyDown={(e) => {
                  if (e.key === 'Enter') {
                    handleLogin();
                  }
                }}
                id="password"
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
