import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router";
import { userApi } from "../../api/user";
import UserForm from "../../components/user/UserForm";

interface InputState {
  file: File | null;
  email: string;
  password: string;
  confirmPassword: string;
  name: string;
  birthday: string;
  phone: string;
  address: string;
  verificationCode?: string;
}

const SignUp = () => {
  const navigate = useNavigate();
  const [isFormValid, setIsFormValid] = useState(false);

  const [inputs, setInputs] = useState<InputState>({
    file: null,
    email: "",
    password: "",
    confirmPassword: "",
    name: "",
    birthday: "",
    phone: "",
    address: "",
    // verificationCode: "",
  });

  const formatBirthday = (birthday: string): string => {
    return `${birthday.slice(0, 4)}-${birthday.slice(4, 6)}-${birthday.slice(6, 8)}`;
  };

  const handleSignUp = async () => {
    const formData = new FormData();

    // formData.append("file", inputs.file as Blob);
    formData.append("name", inputs.name);
    formData.append("email", inputs.email);
    formData.append("password", inputs.password);
    formData.append("phone", inputs.phone);
    formData.append("address", inputs.address);
    formData.append("birth", formatBirthday(inputs.birthday));

    try {
      const response = await userApi.fetchSignUp(formData);
      // 두 번째 API 호출: 알림 구독 (백에서 시도한답니다)
      // const notificationResponse = await userApi.fetchNotificationSubscribe();
      if (response.status === 200) {
        console.log("회원가입이 성공적으로 완료되었습니다!");
        navigate("/login");
      }
    } catch (error) {
      console.error("Error creating user:", error);
      alert("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  return (
    <div className="w-full h-screen p-5 bg-[#EFEFF5]">
      <div className="h-full flex flex-col justify-center space-y-8">
        <p className="text-xl font-bold">회원가입</p>
        <UserForm inputs={inputs} setInputs={setInputs} setIsFormValid={setIsFormValid} />
        <button
          onClick={() => {
            handleSignUp();
          }}
          className={`w-full h-24 rounded-md font-bold text-white text-sm ${
            !isFormValid ? "bg-[#c5cde2]" : "bg-[#0046FF]"
          }`}
          disabled={!isFormValid}>
          가입
        </button>
        <div className="w-full h-20 flex items-center justify-center space-x-2">
          <p className="text-sm font-bold">계정이 있으신가요?</p>
          <button
            onClick={() => {
              navigate("/login");
            }}
            className="text-[#0046FF] font-bold">
            로그인
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignUp;
