import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { useNavigate, useParams } from "react-router";
import SecurityNumberKeyboard from "../../components/common/SecurityNumberKeyboard";
import { setAccountPassword } from "../../redux/accountSlice";

const PasswordOfCreateMeetingAccount = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const params = useParams();
  const password = useSelector((state: RootState) => state.account.accountPassword);

  useEffect(() => {
    if (password.length === 4) {
      dispatch(setAccountPassword(""));
      navigate("/checkpasswordofcreatemeetingaccount", { state: { originalPassword: password, type: params.type } });
    }
  }, [password]);

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          {params.type === "meeting" ? "모임통장" : "튜나뱅크"}에서 사용할
          <br />
          비밀번호를 입력해주세요
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

export default PasswordOfCreateMeetingAccount;
