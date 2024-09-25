import React, { useEffect } from "react";
import SecurityNumberKeyboard from "../../components/common/SecurityNumberKeyboard";
import { useLocation, useNavigate } from "react-router";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";

const CheckPasswordOfCreateMeetingAccount = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const password = useSelector((state: RootState) => state.account.accountPassword);

  useEffect(() => {
    if (password.length === 4) {
      if (location.state.originalPassword === password) {
        navigate(`/meeting/create/idverificationo/${location.state.type}`);
      } else {
        alert("비밀번호가 일치하지 않습니다.");
        navigate(`/meeting/create/password/${location.state.type}`);
      }
    }
  }, [password]);

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          비밀번호를
          <br />한 번 더 입력해주세요
        </p>

        <div className="flex space-x-3">
          {[...Array(4)].map((_, index) => (
            <div
              className={`w-4 aspect-1 ${
                index < password.length ? "bg-[#565656]" : "bg-[#D9D9D9]"
              } rounded-full`}></div>
          ))}
        </div>
      </div>

      <SecurityNumberKeyboard />
    </div>
  );
};

export default CheckPasswordOfCreateMeetingAccount;
