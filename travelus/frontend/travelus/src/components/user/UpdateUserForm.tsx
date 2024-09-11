import { useState } from "react";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";

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

const UpdateUserForm = () => {
  const [inputs, setInputs] = useState<InputState>({
    email: "shinhan@gmail.com",
    password: "",
    confirmPassword: "",
    name: "허동원",
    birthday: "19990000",
    phone: "01012341234",
    address: "42112 대구광역시 달서구 용산서로 102, 1130동 807호 (용산1동, 도레미아파트)",
    verificationCode: "",
  });

  const [errors, setErrors] = useState({
    email: false,
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
      case "email":
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
        if (isValidDateFormat) {
          const year = parseInt(value.substring(0, 4), 10);
          const month = parseInt(value.substring(4, 6), 10);
          const day = parseInt(value.substring(6, 8), 10);
          // 간단한 날짜 유효성 검사
          const isValidDate = month >= 1 && month <= 12 && day >= 1 && day <= 31;
          error = !isValidDate;
        } else {
          error = true;
        }
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
      email: handleValidation("email", inputs.email),
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
          id="email"
          label="이메일"
          variant="outlined"
          value={inputs.email}
          onChange={handleChange}
          error={errors.email}
          helperText={errors.email ? "유효한 이메일을 입력하세요." : ""}
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
          id="password"
          label="사용자 암호"
          variant="outlined"
          type="password"
          value={inputs.password}
          onChange={handleChange}
          error={errors.password}
          helperText={errors.password ? "암호는 최소 6자 이상이어야 합니다." : ""}
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
          id="confirmPassword"
          label="사용자 암호 확인"
          variant="outlined"
          type="password"
          value={inputs.confirmPassword}
          onChange={handleChange}
          error={errors.confirmPassword}
          helperText={errors.confirmPassword ? "암호가 일치하지 않습니다." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
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
          onChange={handleChange}
          error={errors.phone}
          helperText={errors.phone ? "숫자만 입력해주세요." : ""}
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

        <div className="w-full h-16 pl-2 flex items-start justify-between">
          <TextField
            className="w-[70%] h-14"
            id="phone"
            label="인증 번호"
            variant="standard"
            sx={{
              "& .MuiOutlinedInput-root": {
                backgroundColor: "white",
                height: "50px",
              },
            }}
          />
          <Button
            className="h-14 w-20 flex flex-col"
            variant="contained"
            sx={{
              backgroundColor: "#1A73E8", // 버튼의 배경색
              color: "white", // 텍스트 색상
              padding: "4px",
              // "&:hover": {
              //   backgroundColor: "#0036D4", // hover 상태에서의 배경색
              // },
            }}>
            <p className="text-xs font-semibold">인증번호</p>
            <p className="text-xs font-semibold">발송</p>
          </Button>
        </div>

        <TextField
          required
          className="w-full h-14"
          id="address"
          label="주소"
          variant="outlined"
          value={inputs.address}
          onChange={handleChange}
          error={errors.address}
          helperText={errors.address ? "주소를 입력해주세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
            },
          }}
        />
      </Box>
    </>
  );
};

export default UpdateUserForm;