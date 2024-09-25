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
      if (params.type === "travelbox") {
        navigate("/meeting/create/completed/travelbox");
      } else {
        navigate("/meeting/create/password/check", { state: { originalPassword: password, type: params.type } });
      }
    }
  }, [password]);

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          {params.type === "travelbox"
            ? "모임통장의 비밀번호를"
            : params.type === "meeting"
            ? "모임통장에서 사용할"
            : "튜나뱅크에서 사용할"}
          <br />
          {params.type === "travelbox" ? "입력해주세요" : "비밀번호를 입력해주세요"}
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
