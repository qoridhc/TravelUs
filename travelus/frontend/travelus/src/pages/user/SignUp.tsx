import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router";
import { userApi } from "../../api/user";
import UserForm from "../../components/user/UserForm";
import { FaHandSparkles } from "react-icons/fa";

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
  accountPassword: string;
}

const SignUp = () => {
  const navigate = useNavigate();
  const [isFormValid, setIsFormValid] = useState(false);
  const [userId, setUserId] = useState<string | null>(null);

  const [inputs, setInputs] = useState<InputState>({
    file: null,
    email: "",
    password: "",
    confirmPassword: "",
    name: "",
    birthday: "",
    phone: "",
    address: "",
    accountPassword: "",
    verificationCode: "",
  });

  const formatBirthday = (birthday: string): string => {
    return `${birthday.slice(0, 4)}-${birthday.slice(4, 6)}-${birthday.slice(6, 8)}`;
  };

  const fetchVerifySmsCode = async () => {
    try {
      const formattedValue = inputs.phone.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
      const response = await userApi.fetchVerifySmsCode(formattedValue, inputs.verificationCode!);

      if (response.status === 200) {
        console.log("휴대폰 인증이 성공적으로 완료되었습니다!");
        return true;
      } else {
        alert("인증번호를 다시 확인해주세요");
        return false;
      }
    } catch (error) {
      console.error("Error creating user:", error);
      alert("인증번호를 다시 확인해주세요");
    }
  };

  const handleSignUp = async () => {

    // // 인증 코드 검증
    // const isCodeVerified = await fetchVerifySmsCode();
  
    // if (!isCodeVerified) 
    //   return;

    const formData = new FormData();

    // formData.append("file", inputs.file as Blob);
    formData.append("name", inputs.name);
    formData.append("email", inputs.email);
    formData.append("password", inputs.password);
    formData.append("phone", inputs.phone);
    formData.append("address", inputs.address);
    formData.append("birth", formatBirthday(inputs.birthday));
    formData.append("accountPassword", inputs.accountPassword);

    try {
      const response = await userApi.fetchSignUp(formData);
      if (response.status === 200) {
        console.log("회원가입이 성공적으로 완료되었습니다!");

        const sseUrl = `https://soltravel.shop/api/v1/notification/subscribe/${response.data}`; // response.data를 통해 사용자 ID를 가져옵니다.
        const eventSource = new EventSource(sseUrl);

        eventSource.onopen = function (event) {
          console.log("SSE connection opened:", event);
        };

        eventSource.addEventListener("all", function(event){
          console.log("all: ",event.data);
        })

        eventSource.addEventListener("Exchange", function (event) {
          const data = JSON.parse(event.data);
          console.log("Exchange notification received:", data);
          // 알림 메시지를 화면에 표시하거나, 다른 UI 업데이트를 수행
        });

        eventSource.addEventListener("Settlement", function (event) {
          const data = JSON.parse(event.data);
          console.log("Settlement notification received:", data);
          // 알림 메시지를 화면에 표시하거나, 다른 UI 업데이트를 수행
        });

        eventSource.addEventListener("Transaction", function (event) {
          const data = JSON.parse(event.data);
          console.log("Transaction notification received:", data);
          // 알림 메시지를 화면에 표시하거나, 다른 UI 업데이트를 수행
        });

        eventSource.onerror = function (event) {
          console.error("Error occurred in SSE connection:", event);
          eventSource.close(); // 오류 발생 시 SSE 연결 닫기
        };

        eventSource.close = function() {
          console.log("SSE connection closed");
          // 재연결 로직을 추가할 수 있습니다.
        };
        
        navigate("/login");
      }
    } catch (error) {
      console.error("Error creating user:", error);
      alert("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };


  return (
    <div className="w-full min-h-screen p-5 bg-[#EFEFF5]">
      <div className="flex flex-col justify-center space-y-8">
        <p className="text-xl font-bold">회원가입</p>
        <UserForm inputs={inputs} setInputs={setInputs} setIsFormValid={setIsFormValid} />
        <button
          onClick={() => {
            handleSignUp();
          }}
          className={`w-full h-12 rounded-md font-bold text-white text-sm ${
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
