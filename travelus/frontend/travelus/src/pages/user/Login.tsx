import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router";
import { userApi } from "../../api/user";
import { initializeFcmAndRegisterToken } from "../../utils/notificationUtils";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const location = useLocation();

  // 페이지 로딩 시 토큰 확인
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      console.log("token: ", token);
      navigate("/");
    }
  }, []);

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
        sessionStorage.setItem("accessToken", response.data.accessToken);
        localStorage.setItem("userId", response.data.userId.toString());
        localStorage.setItem("userName", response.data.name);

        // 로그인 성공 시 FCM 초기설정 & 토큰 발급 및 저장
        initializeFcmAndRegisterToken();

        if (location.state && location.state.type !== null) {
          // 모임 초대장에서 넘어온 로그인일 경우, 초대장으로 연결
          navigate(
            `/meeting/invite/${location.state.code}?groupLeader=${location.state.groupInfo.groupLeader}&groupName=${location.state.groupInfo.groupName}`
          );
        } else {
          // 그냥 로그인일 경우, 메인으로 연결
          navigate("/");
        }
      }
    } catch (error) {
      console.error("Error creating user:", error);
      alert("아이디 또는 비밀번호가 틀렸습니다.");
    }
  };

  return (
    <div className="h-full p-5 bg-[#F3F4F6] flex flex-col justify-center items-center">
      <div className="w-full p-8 bg-white rounded-lg flex flex-col justify-between items-center space-y-8">
        <div className="flex items-center space-x-1">
          <p className="text-2xl font-bold">TravelUs 로그인</p>
        </div>

        <div className="w-full grid gap-5">
          <div className="w-full text-[#565656]">
            <label htmlFor="id" className="w-full font-semibold cursor-pointer">
              아이디
            </label>
            <input
              onChange={(e) => {
                handleChange(e);
              }}
              id="id"
              className="py-2 text-lg outline-none border-b border-[#cccccc]"
              type="text"
              name="email"
              value={email}
            />
          </div>

          <div className="w-full text-[#565656]">
            <label htmlFor="password" className="w-full font-semibold cursor-pointer">
              비밀번호
            </label>
            <input
              onChange={(e) => {
                handleChange(e);
              }}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  handleLogin();
                }
              }}
              id="password"
              className="py-2 text-lg outline-none border-b border-[#cccccc]"
              type="password"
              name="password"
              value={password}
            />
          </div>
        </div>

        <button
          onClick={() => {
            handleLogin();
          }}
          className="w-full h-12 text-lg rounded-md tracking-wide text-white bg-[#1429A0]">
          로그인
        </button>

        <div className="bg-white rounded-xl flex space-x-3">
          <p className="font-semibold">계정이 없으신가요?</p>
          <button
            onClick={() => {
              navigate("/signup");
            }}
            className="text-[#0046FF] font-semibold">
            가입하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;
