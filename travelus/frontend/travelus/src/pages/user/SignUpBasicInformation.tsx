import React from "react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { IoIosArrowBack } from "react-icons/io";
import IdInput from "../../components/user/inputField/IdInput";
import UserPasswordInput from "../../components/user/inputField/UserPasswordInput";
import UserPasswordConfirmInput from "../../components/user/inputField/UserPasswordConfirmInput";
import NameInput from "../../components/user/inputField/NameInput";
import BirthdayInput from "../../components/user/inputField/BirthdayInput";

interface SignUpBasicInformationProps {
  // Define your props here
}

interface InputState {
  id: string;
  password: string;
  confirmPassword: string;
  name: string;
  birthday: string;
}

const SignUpBasicInformation = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(0);
  const stepList = [
    "아이디를 입력해주세요",
    "비밀번호를 입력해주세요",
    "비밀번호를 확인해주세요",
    "이름을 알려주세요",
    "생년월일을 알려주세요",
    "생년월일을 알려주세요",
  ];

  const [isFormValid, setIsFormValid] = useState(false);
  const [isIdDuplicated, setIsIdDuplicated] = useState(true);

  const [inputs, setInputs] = useState<InputState>({
    id: "",
    password: "",
    confirmPassword: "",
    name: "",
    birthday: "",
  });

  const [errors, setErrors] = useState({
    id: false,
    password: false,
    confirmPassword: false,
    name: false,
    birthday: false,
  });

  const handleValidation = (id: string, value: string) => {
    let error = false;

    const hasEnglishLetters = /[a-zA-Z]/.test(value);
    const hasNumbers = /\d/.test(value);

    switch (id) {
      case "id":
        const idRegex = /^[a-z0-9]{6,13}$/;
        error = !idRegex.test(value);
        break;
      case "password":
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]).{8,15}$/;
        error = !passwordRegex.test(value);
        break;
      case "confirmPassword":
        error = value !== inputs.password; // 패스워드 확인이 일치하는지 확인
        break;
      case "name":
        error = hasEnglishLetters || hasNumbers || value.trim() === "";
        break;
      case "birthday":
        // 생년월일 유효성 검사
        const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
        error = !dateRegex.test(value);
        break;
    }
    return error;
  };

  const formatBirthday = (value: string) => {
    // Remove any non-digit characters
    const cleanedValue = value.replace(/\D/g, "");

    // Format as YYYY-MM-DD
    if (cleanedValue.length <= 4) {
      return cleanedValue;
    } else if (cleanedValue.length <= 6) {
      return `${cleanedValue.slice(0, 4)}-${cleanedValue.slice(4)}`;
    } else {
      return `${cleanedValue.slice(0, 4)}-${cleanedValue.slice(4, 6)}-${cleanedValue.slice(6, 8)}`;
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;

    const formattedValue = id === "birthday" ? formatBirthday(value) : value;

    setInputs((prev) => ({ ...prev, [id]: formattedValue }));

    // 입력이 변경될 때마다 유효성 검사를 실행하고 에러 상태 업데이트
    const error = handleValidation(id, value);
    setErrors((prev) => ({ ...prev, [id]: error }));
  };

  useEffect(() => {
    // 모든 필드가 유효한지 확인
    const allFieldsValid = Object.values(errors).every((error) => !error);
    const allFieldsFilled = Object.values(inputs).every((value) => value !== "");

    setIsFormValid(allFieldsValid && allFieldsFilled);
  }, [errors, inputs]);

  // 새로운 useEffect: 비밀번호 입력 처리
  useEffect(() => {
    if (step === 1 && inputs.password.length >= 8 && inputs.password.length <= 15 && !errors.password) {
      setStep(2);
    }
  }, [inputs.password, errors.password, step]);

  // 비밀번호 확인 처리
  useEffect(() => {
    if (step === 2 && inputs.confirmPassword === inputs.password && !errors.confirmPassword) {
      setStep(3);
    }
  }, [inputs.confirmPassword, inputs.password, errors.confirmPassword, step]);

  // 이름 입력 처리
  useEffect(() => {
    if (step === 3 && inputs.name.length >= 2 && !errors.name) {
      setStep(4);
    }
  }, [inputs.name, errors.name, step]);

  // 생년월일 입력 처리
  useEffect(() => {
    if (step === 4 && inputs.birthday.length === 10 && !errors.birthday) {
      setStep(5);
    }
  }, [inputs.birthday, errors.birthday, step]);

  const handleNext = () => {};

  const handleIsIdDuplicated = () => {
    console.log("중복확인");
    setIsIdDuplicated(false);
    setStep(1);
  };

  return (
    <div className="w-full min-h-screen p-5 bg-[#F3F4F6] flex flex-col justify-between">
      <div className="flex flex-col space-y-5">
        <div className="grid gap-8">
          <div className="mt-16 text-2xl font-semibold">
            <p>{stepList[step]}</p>
          </div>

          <div className="grid gap-3">
            <div
              className={`transition-transform duration-300 ease-in-out ${
                step > 3 ? "translate-y-[3px]" : "translate-y-0"
              }`}>
              {step > 3 && <BirthdayInput labelName="생년월일" name={inputs.birthday} onChange={handleChange} />}
            </div>

            <div
              className={`transition-transform duration-300 ease-in-out ${
                step > 2 ? "translate-y-[3px]" : "translate-y-0"
              }`}>
              {step > 2 && <NameInput labelName="이름" name={inputs.name} onChange={handleChange} />}
            </div>

            <div
              className={`transition-transform duration-300 ease-in-out ${
                step > 1 ? "translate-y-0" : "translate-y-[3px]"
              }`}>
              {step > 1 && (
                <UserPasswordConfirmInput
                  labelName="비밀번호 확인"
                  name={inputs.confirmPassword}
                  onChange={handleChange}
                />
              )}
            </div>

            <div
              className={`transition-transform duration-300 ease-in-out ${
                step > 0 ? "translate-y-0" : "translate-y-[3px]"
              }`}>
              {step > 0 && (
                <UserPasswordInput
                  labelName="비밀번호"
                  name={inputs.password}
                  error={errors.password}
                  onChange={handleChange}
                />
              )}
            </div>

            <div
              className={`transition-transform duration-300 ease-in-out ${
                step === 0 ? "translate-y-0" : "translate-y-[3px]"
              }`}>
              <IdInput
                labelName="아이디"
                name={inputs.id}
                error={errors.id}
                onChange={handleChange}
                handleIsIdDuplicated={handleIsIdDuplicated}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="py-5">
        <button
          className={`w-full py-3 text-white rounded-lg ${step !== 5 ? "bg-gray-300" : "bg-[#1429A0]"}`}
          onClick={() => setStep(5)}
          disabled={step !== 5}>
          다음
        </button>
      </div>
    </div>
  );
};

export default SignUpBasicInformation;
