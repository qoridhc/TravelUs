import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import AccountNumberInput from "../../components/transfer/inputFields/AccountNumberInput";

interface TransferSelectBankProps {
  // Define the props for your component here
}

const TransferSelectBank = () => {
  const navigate = useNavigate();

  const [accountNo, setAccountNo] = useState<string>("");
  const [isValidation, setIsValidation] = useState<boolean>(false);

  const handleAccountNoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAccountNo(e.target.value);
  };

  useEffect(() => {
    if (accountNo.length > 0) {
      setIsValidation(true);
    } else {
      setIsValidation(false);
    }
  }, [accountNo]);

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="flex flex-col space-y-10">
          <IoIosArrowBack
            onClick={() => {
              navigate("/");
            }}
            className="text-2xl"
          />
          <div className="flex flex-col space-y-16">
            <p className="text-2xl font-extrabold">어디로 돈을 보낼까요?</p>

            <div className="flex flex-col space-y-10">
              <div>
                <AccountNumberInput value={accountNo} labelName="계좌 번호" onChange={handleAccountNoChange} />
              </div>

              <div>
                <p className="text-zinc-600">은행</p>
                <input
                  type="text"
                  value={"튜나뱅크"}
                  className="w-full bg-white px-1 py-2 text-lg font-bold outline-none focus:placeholder-transparent"
                  disabled={true}
                />
                <div className="h-[1.5px] bg-[#bdbdbd] w-full" />
              </div>
            </div>
          </div>
        </div>

        <div>
          <button
            onClick={() => {
              navigate("/transfer/setmoney", { state: { accountNo } });
            }}
            className={`w-full h-14 text-lg font-semibold rounded-xl tracking-wide ${
              isValidation ? "text-white bg-[#1429A0]" : "text-[#565656] bg-[#E3E4E4]"
            }`}
            disabled={!isValidation}>
            다음
          </button>
        </div>
      </div>
    </div>
  );
};

export default TransferSelectBank;
