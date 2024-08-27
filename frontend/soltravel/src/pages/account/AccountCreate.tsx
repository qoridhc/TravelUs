import React, { useEffect, useState } from "react";
import { RiHome5Line } from "react-icons/ri";
import { GoDotFill } from "react-icons/go";
import { useSelector, useDispatch } from "react-redux";
import { setIsKeyboard, setAccountPassword } from "../../redux/accountSlice";
import { RootState } from "../../redux/store";
import SecurityKeyboard from "../../components/Account/SecurityKeyboard";
import { TextField } from "@mui/material";

const AccountCreate = () => {
  const { isKeyboard, accountPassword } = useSelector((state: RootState) => state.account);
  const dispatch = useDispatch();

  const [step, setStep] = useState(0);
  const stepList = ["이름을", "주민등록번호를", "계좌 비밀번호를"];
  const [name, setName] = useState("");
  const [residentNumber, setResidentNumber] = useState("");
  const [maskedPassword, setMaskedPassword] = useState("");

  useEffect(() => {
    if (accountPassword !== undefined) {
      setMaskedPassword("●".repeat(accountPassword.length));
    }
  }, [accountPassword]);

  const handleNameChange = (name: string) => {
    setName(name);
    if (name.length >= 3) {
      setStep(1);
    }
  };

  const handleResidentNumberChange = (number: string) => {
    if (13 - number.length < 6) {
      let temp = number.substring(0, 8);
      temp += "*".repeat(number.length - 8);
      number = temp;
    }

    let masking = number.replace(/^(\d{0,6})(\d{0,7})$/g, "$1-$2").replace(/\-{1}$/g, "");
    setResidentNumber(masking);

    if (number.length === 14) {
      // 하이픈 포함
      setStep(2);
      dispatch(setIsKeyboard(true));
    }
  };

  const handlePasswordChange = (passsword: string) => {};

  const handlePasswordKeyboard = () => {
    dispatch(setIsKeyboard(true));
  };

  return (
    <div className="h-full flex flex-col justify-between">
      <div className="flex flex-col space-y-5">
        <div className="p-5 grid grid-cols-[1fr_8fr_1fr]">
          <div className="flex items-center">
            <RiHome5Line className="text-2xl" />
          </div>
          <p className="text-xl text-center font-semibold">입출금통장 가입정보</p>
        </div>

        <div className="p-5 grid gap-8">
          <div className="text-2xl font-semibold">
            <p>{stepList[step]}</p>
            <p>입력해주세요</p>
          </div>

          <div className="grid gap-3">
            <div
              className={`transition-transform duration-300 ease-in-out ${
                step > 1 ? "translate-y-[3px]" : "translate-y-0"
              }`}
              onClick={() => handlePasswordKeyboard()}>
              {step > 1 && (
                <TextField
                  sx={{
                    width: "100%",
                    "& .MuiInputBase-root": {
                      backgroundColor: "white",
                    },
                    "& .MuiInputBase-input": {
                      backgroundColor: "white",
                      fontSize: "20px",
                      fontWeight: "bold",
                      border: "1px solid #9E9E9E",
                      borderRadius: "10px",
                    },
                    "& .MuiInputLabel-root": {
                      color: "#9E9E9E",
                      fontSize: "20px",
                    },
                    "& .MuiInputLabel-shrink": {
                      fontSize: "16px",
                    },
                    "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
                      display: "none",
                    },
                  }}
                  id="filled-basic"
                  label="계좌비밀번호"
                  variant="filled"
                  value={maskedPassword}
                  onChange={(e) => handlePasswordChange(e.target.value)}
                  inputProps={{ maxLength: 4, readOnly: true }}
                />
              )}
            </div>

            <div
              className={`transition-transform duration-300 ease-in-out ${
                step > 0 ? "translate-y-[3px]" : "translate-y-0"
              }`}>
              {step > 0 && (
                <TextField
                  sx={{
                    width: "100%",
                    "& .MuiInputBase-root": {
                      backgroundColor: "white",
                    },
                    "& .MuiInputBase-input": {
                      backgroundColor: "white",
                      fontSize: "20px",
                      fontWeight: "bold",
                      border: "1px solid #9E9E9E",
                      borderRadius: "10px",
                    },
                    "& .MuiInputLabel-root": {
                      color: "#9E9E9E",
                      fontSize: "20px",
                    },
                    "& .MuiInputLabel-shrink": {
                      fontSize: "16px",
                    },
                    "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
                      display: "none",
                    },
                  }}
                  id="filled-basic"
                  label="주민등록번호"
                  variant="filled"
                  value={residentNumber}
                  onChange={(e) => handleResidentNumberChange(e.target.value)}
                  inputProps={{ maxLength: 14 }}
                />
              )}
            </div>

            <div
              className={`transition-transform duration-300 ease-in-out ${
                step === 0 ? "translate-y-0" : "translate-y-[3px]"
              }`}>
              <TextField
                sx={{
                  width: "100%",
                  "& .MuiInputBase-root": {
                    backgroundColor: "white",
                  },
                  "& .MuiInputBase-input": {
                    backgroundColor: "white",
                    fontSize: "20px",
                    fontWeight: "bold",
                    border: "1px solid #9E9E9E",
                    borderRadius: "10px",
                  },
                  "& .MuiInputLabel-root": {
                    color: "#9E9E9E",
                    fontSize: "20px",
                  },
                  "& .MuiInputLabel-shrink": {
                    fontSize: "16px",
                  },
                  "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
                    display: "none",
                  },
                }}
                id="filled-basic"
                label="이름"
                variant="filled"
                value={name}
                onChange={(e) => handleNameChange(e.target.value)}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="px-5 py-10">
        <button
          className={`w-full py-3 text-white bg-[#0471E9] rounded-lg ${step < 3 ? "opacity-40" : ""}`}
          disabled={step < 3}>
          완료
        </button>
      </div>

      {isKeyboard ? (
        <div className="w-full fixed bottom-0">
          <SecurityKeyboard />
        </div>
      ) : (
        <></>
      )}
    </div>
  );
};

export default AccountCreate;
