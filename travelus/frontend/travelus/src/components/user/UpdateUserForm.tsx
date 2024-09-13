import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import InputAdornment from "@mui/material/InputAdornment";
import { IoIosArrowForward } from "react-icons/io";

interface InputState {
  id: string;
  password: string;
  confirmPassword: string;
  name: string;
  birthday: string;
  phone: string;
  address: string;
  verificationCode?: string;
}

const UpdateUserForm = () => {
  const userData = useSelector((state: RootState) => state.userInformation.UserInfo);
  const navigate = useNavigate();

  const [inputs, setInputs] = useState<InputState>({
    id: userData.email,
    password: "",
    confirmPassword: "",
    name: userData.name,
    birthday: userData.birth,
    phone: userData.phone,
    address: userData.address,
    verificationCode: "",
  });

  const [errors, setErrors] = useState({
    id: false,
    password: false,
    confirmPassword: false,
    name: false,
    birthday: false,
    phone: false,
    address: false,
    verificationCode: false,
  });

  const [showVerificationInput, setShowVerificationInput] = useState(false);

  const handleValidation = (id: string, value: string) => {
    let error = false;

    const hasEnglishLetters = /[a-zA-Z]/.test(value);
    const hasNumbers = /\d/.test(value);

    switch (id) {
      case "id":
        error = !value.includes("@");
        break;
      case "password":
        error = value.length < 6;
        break;
      case "confirmPassword":
        error = value !== inputs.password; // 패스워드 확인이 일치하는지 확인
        break;
      case "name":
        error = hasEnglishLetters || hasNumbers || value.trim() === "";
        break;
      case "birthday":
        // 생년월일은 YYYYMMDD 형식이어야 하며, 날짜 유효성 검사
        const dateRegex = /^\d{8}$/;
        const isValidDateFormat = dateRegex.test(value);
        break;
      case "phone":
        // 휴대폰 번호는 11자리 숫자만 허용
        const phoneRegex = /^\d{11}$/;
        error = !phoneRegex.test(value);
        break;
      case "verificationCode":
        // 인증번호는 6자리 숫자만 허용
        const codeRegex = /^\d{6}$/;
        error = !codeRegex.test(value);
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
      id: handleValidation("id", inputs.id),
      password: handleValidation("password", inputs.password),
      confirmPassword: handleValidation("confirmPassword", inputs.confirmPassword),
      name: handleValidation("name", inputs.name),
      birthday: handleValidation("birthday", inputs.birthday),
      phone: handleValidation("phone", inputs.phone),
      address: handleValidation("address", inputs.address),
      verificationCode: handleValidation("verificationCode", inputs.verificationCode || ""),
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
    <>
      <Box
        className="w-full flex flex-col justify-center items-center space-y-5"
        component="form"
        noValidate
        autoComplete="off"
        onSubmit={handleSubmit}>
        <TextField
          required
          className="w-full h-14"
          id="id"
          label="아이디"
          variant="outlined"
          value={inputs.id}
          onChange={handleChange}
          error={errors.id}
          helperText={errors.id ? "유효한 이메일을 입력하세요." : ""}
          disabled={true}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem", // 도움말 텍스트 크기 조정 (예: 12px)
              marginTop: "2px", // 도움말 텍스트의 위쪽 마진 조정
            },
          }}
        />
        <TextField
          required
          className="w-full h-14"
          id="name"
          label="성명"
          variant="outlined"
          value={inputs.name}
          onChange={handleChange}
          error={errors.name}
          helperText={errors.name ? "성함은 한글로 입력해주세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem", // 도움말 텍스트 크기 조정 (예: 12px)
              marginTop: "2px", // 도움말 텍스트의 위쪽 마진 조정
            },
          }}
        />

        <TextField
          required
          className="w-full h-14"
          id="birthday"
          label="생년월일"
          variant="outlined"
          value={inputs.birthday}
          onChange={handleChange}
          error={errors.birthday}
          helperText={errors.birthday ? "YYYYMMDD 형식으로 입력해주세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem", // 도움말 텍스트 크기 조정 (예: 12px)
              marginTop: "2px", // 도움말 텍스트의 위쪽 마진 조정
            },
          }}
        />

        <TextField
          required
          className="w-full h-14"
          id="phone"
          label="휴대폰 번호"
          variant="outlined"
          value={inputs.phone}
          onClick={() => {
            navigate("/userupdate/phone");
          }}
          onChange={handleChange}
          error={errors.phone}
          helperText={errors.phone ? "숫자만 입력해주세요." : ""}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IoIosArrowForward style={{ cursor: "pointer" }} />
              </InputAdornment>
            ),
          }}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
              cursor: "pointer",
            },
            "& input": {
              cursor: "pointer", // 입력 필드에 커서 스타일을 적용
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem", // 도움말 텍스트 크기 조정 (예: 12px)
              marginTop: "2px", // 도움말 텍스트의 위쪽 마진 조정
            },
          }}
        />

        <TextField
          required
          className="w-full h-14"
          id="address"
          label="주소"
          variant="outlined"
          value={inputs.address}
          onClick={() => {
            navigate("/");
          }}
          onChange={handleChange}
          error={errors.address}
          helperText={errors.address ? "주소를 입력해주세요." : ""}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IoIosArrowForward style={{ cursor: "pointer" }} />
              </InputAdornment>
            ),
          }}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
              cursor: "pointer",
            },
            "& input": {
              cursor: "pointer", // 입력 필드에 커서 스타일을 적용
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
            },
          }}
        />
        <button className="w-full px-5 text-[#1429A0] flex justify-end">비밀번호 변경</button>
      </Box>
    </>
  );
};

export default UpdateUserForm;
