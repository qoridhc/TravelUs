import { useState } from "react";
import { useNavigate } from "react-router";
import { userApi } from "../../api/user";
import UserForm from "../../components/user/UserForm";

interface InputState {
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

    formData.append(
      "request",
      new Blob(
        [
          JSON.stringify({
            name: inputs.name,
            email: inputs.email,
            password: inputs.password,
            phone: inputs.phone,
            address: inputs.address,
            birth: formatBirthday(inputs.birthday),
          }),
        ],
        { type: "application/json" }
      )
    );

    try {
      // 첫 번째 API 호출: 회원가입
      const signUpResponse = await userApi.fetchSignUp(formData);

      // 두 번째 API 호출: 알림 구독
      const notificationResponse = await userApi.fetchNotificationSubscribe();

      // 두 API 호출 모두 성공했는지 확인
      if (signUpResponse && notificationResponse) {
        console.log("회원가입 성공");
      } else {
        console.error("회원가입 오류: API 호출 실패");
      }
    } catch (error) {
      console.error("회원가입 오류", error);
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
          className={`w-full h-24 rounded-md font-bold text-white text-sm ${!isFormValid ? "bg-[#c5cde2]" : "bg-[#0046FF]"}`}
          disabled={!isFormValid}
        >
          가입
        </button>
        <div className="w-full h-20 flex items-center justify-center space-x-2">
          <p className="text-sm font-bold">계정이 있으신가요?</p>
          <button
            onClick={() => {
              navigate("/login");
            }}
            className="text-[#0046FF] font-bold"
          >
            로그인
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignUp;
