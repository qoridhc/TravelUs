import { useState } from "react";
import { useNavigate } from "react-router";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";

interface InputState {
  email: string;
  password: string;
  name: string;
  birthday: string;
  phone: string;
  address: string;
}

const SignUp = () => {
  const navigate = useNavigate();

  const [inputs, setInputs] = useState<InputState>({
    email: "",
    password: "",
    name: "",
    birthday: "",
    phone: "",
    address: "",
  });

  const [errors, setErrors] = useState({
    email: false,
    password: false,
    name: false,
    birthday: false,
    phone: false,
    address: false,
  });

  const handleValidation = (id: string, value: string) => {
    let error = false;
    switch (id) {
      case "email":
        error = !value.includes("@");
        break;
      case "password":
        error = value.length < 6;
        break;
      case "name":
        error = value.trim() === "";
        break;
      case "birthday":
        error = value.trim() === "";
        break;
      case "phone":
        error = value.trim() === "";
        break;
      case "address":
        error = value.trim() === "";
        break;
      default:
        break;
    }
    return error;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;
    setInputs((prev) => ({ ...prev, [id]: value }));

    // 입력이 변경될 때마다 유효성 검사를 실행하고 에러 상태 업데이트
    const error = handleValidation(id, value);
    setErrors((prev) => ({ ...prev, [id]: error }));
  };

  const validateInputs = () => {
    const newErrors = {
      email: handleValidation("email", inputs.email),
      password: handleValidation("password", inputs.password),
      name: handleValidation("name", inputs.name),
      birthday: handleValidation("birthday", inputs.birthday),
      phone: handleValidation("phone", inputs.phone),
      address: handleValidation("address", inputs.address),
    };
    setErrors(newErrors);
    return !Object.values(newErrors).some((error) => error);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validateInputs()) {
      // 유효성 검사 통과 시 처리 로직
      console.log("Form submitted:", inputs);
      // navigate("/next-page"); // 예: 회원가입 완료 페이지로 이동
    }
  };

  return (
    <div className="w-full h-screen p-5 bg-[#EFEFF5]">
      <div className="h-full flex flex-col justify-center space-y-6">
        <p className="text-2xl font-bold">All-In-One 뱅크 회원가입</p>
        <Box
          className="w-full flex flex-col justify-center items-center space-y-7"
          component="form"
          noValidate
          autoComplete="off"
          onSubmit={handleSubmit}>
          <TextField
            required
            className="w-full bg-white"
            id="email"
            label="이메일"
            variant="outlined"
            value={inputs.email}
            onChange={handleChange}
            error={errors.email}
            helperText={errors.email ? "유효한 이메일을 입력하세요." : ""}
            sx={{
              "& .MuiFormHelperText-root": {
                backgroundColor: "black !important",
              },
            }}
          />
          <TextField
            required
            className="w-full bg-white"
            id="outlined-basic"
            type="password"
            label="사용자 암호"
            variant="outlined"
          />
          <TextField required className="w-full bg-white" id="outlined-basic" label="성명" variant="outlined" />
          <TextField required className="w-full bg-white" id="outlined-basic" label="생년월일" variant="outlined" />
          <TextField required className="w-full bg-white" id="outlined-basic" label="휴대폰 번호" variant="outlined" />
          <TextField required className="w-full bg-white" id="outlined-basic" label="주소" variant="outlined" />
          <button className="w-full h-12 rounded-md bg-[#0046FF] font-bold text-white text-sm">가입</button>
          <div className="w-full h-20 bg-white rounded-lg shadow-sm flex items-center justify-center space-x-2">
            <p className="text-sm font-bold">계정이 있으신가요?</p>
            <button
              onClick={() => {
                navigate("/login");
              }}
              className="text-[#0046FF] font-bold">
              로그인
            </button>
          </div>
        </Box>
      </div>
    </div>
  );
};

export default SignUp;
