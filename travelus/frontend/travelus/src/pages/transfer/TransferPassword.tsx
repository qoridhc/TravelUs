import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { useNavigate, useLocation } from "react-router";
import { AccountInfoNew } from "../../types/account";
import SecurityNumberKeyboard from "../../components/common/SecurityNumberKeyboard";
import { setAccountPassword } from "../../redux/accountSlice";
import { set } from "date-fns";

interface TransferPasswordProps {
  // Define the props for your component here
}

const TransferPassword: React.FC<TransferPasswordProps> = (props) => {
  const navigate = useNavigate();
  const location = useLocation();

  const [password, setPassword] = useState("");
  const { accountNo } = location.state as { accountNo: string };
  const { transferAmount } = location.state as { transferAmount: string };
  const { depositAccount } = location.state as { depositAccount: AccountInfoNew };

  useEffect(() => {
    if (password.length === 4) {
      navigate("/transfer/confirm", { state: { accountNo, transferAmount, password, depositAccount } });
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

      <SecurityNumberKeyboard password={password} setPassword={setPassword} />
    </div>
  );
};

export default TransferPassword;
