import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { useNavigate, useLocation } from "react-router";
import SecurityNumberKeyboard from "../../components/common/SecurityNumberKeyboard";
import { setAccountPassword } from "../../redux/accountSlice";
import { set } from "date-fns";

interface TransferPasswordProps {
  // Define the props for your component here
}

const TransferPassword: React.FC<TransferPasswordProps> = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const password = useSelector((state: RootState) => state.account.accountPassword);
  const { accountNo } = location.state as { accountNo: string };
  const { transferAmount } = location.state as { transferAmount: string };

  useEffect(() => {
    if (password.length === 4) {
      navigate("/transfer/confirm", { state: { accountNo, transferAmount, password } });
      dispatch(setAccountPassword(""));
    }
  }, [password]);

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
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

export default TransferPassword;
