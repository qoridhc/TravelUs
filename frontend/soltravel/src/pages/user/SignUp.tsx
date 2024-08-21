import { useState } from "react";
interface InputState {
  email: string;
  password: string;
  name: string;
  birthday: string;
  phone: string;
  address: string;
}

const SignUp = () => {
  const [inputValues, setInputValues] = useState<InputState>({
    email: "",
    password: "",
    name: "",
    birthday: "",
    phone: "",
    address: "",
  });

  const handleInputChange = (field: keyof InputState, value: string) => {
    setInputValues((prev) => ({ ...prev, [field]: value }));
  };

  return (
    <div className="w-full h-full p-5 bg-[#EFEFF5]">
      <p className="text-2xl font-bold">All-In-One 뱅크 회원가입</p>
      <div className="w-full h-full flex flex-col justify-center items-center space-y-2">
        {/* Email Field */}
        <div className="relative w-full h-16 flex justify-center items-center rounded-xl bg-white border border-gray-300 p-2 text-zinc-800">
          <input
            className={`w-full h-full placeholder:text-zinc-500 focus:outline-none transition-transform duration-150 ${
              inputValues.email ? "transform translate-y-2" : ""
            }`}
            type="email"
            value={inputValues.email}
            onChange={(e) => handleInputChange("email", e.target.value)}
            placeholder="이메일"
          />
          <label
            className={`absolute left-2 transition-all duration-150 ${
              inputValues.email ? "top-2 text-xs" : "top-4 text-zinc-500"
            }`}>
            {inputValues.email && "이메일"}
          </label>
        </div>

        {/* Password Field */}
        <div className="relative w-full h-16 flex justify-center items-center rounded-xl bg-white border border-gray-300 p-2 text-zinc-800">
          <input
            className={`w-full h-full placeholder:text-zinc-500 focus:outline-none transition-transform duration-150 ${
              inputValues.password ? "transform translate-y-2" : ""
            }`}
            type="password"
            value={inputValues.password}
            onChange={(e) => handleInputChange("password", e.target.value)}
            placeholder="사용자 암호"
          />
          <label
            className={`absolute left-2 transition-all duration-150 ${
              inputValues.password ? "top-2 text-xs" : "top-4 text-zinc-500"
            }`}>
            {inputValues.password && "사용자 암호"}
          </label>
        </div>

        {/* Name Field */}
        <div className="relative w-full h-16 flex justify-center items-center rounded-xl bg-white border border-gray-300 p-2 text-zinc-800">
          <input
            className={`w-full h-full placeholder:text-zinc-500 focus:outline-none transition-transform duration-150 ${
              inputValues.name ? "transform translate-y-2" : ""
            }`}
            type="text"
            value={inputValues.name}
            onChange={(e) => handleInputChange("name", e.target.value)}
            placeholder="성명"
          />
          <label
            className={`absolute left-2 transition-all duration-150 ${
              inputValues.name ? "top-2 text-xs" : "top-4 text-zinc-500"
            }`}>
            {inputValues.name && "성명"}
          </label>
        </div>

        {/* Birthday Field */}
        <div className="relative w-full h-16 flex justify-center items-center rounded-xl bg-white border border-gray-300 p-2 text-zinc-800">
          <input
            className={`w-full h-full placeholder:text-zinc-500 focus:outline-none transition-transform duration-150 ${
              inputValues.birthday ? "transform translate-y-2" : ""
            }`}
            type="text"
            value={inputValues.birthday}
            onChange={(e) => handleInputChange("birthday", e.target.value)}
            placeholder="생년월일"
          />
          <label
            className={`absolute left-2 transition-all duration-150 ${
              inputValues.birthday ? "top-2 text-xs" : "top-4 text-zinc-500"
            }`}>
            {inputValues.birthday && "생년월일"}
          </label>
        </div>

        {/* Phone Field */}
        <div className="relative w-full h-16 flex justify-center items-center rounded-xl bg-white border border-gray-300 p-2 text-zinc-800">
          <input
            className={`w-full h-full placeholder:text-zinc-500 focus:outline-none transition-transform duration-150 ${
              inputValues.phone ? "transform translate-y-2" : ""
            }`}
            type="text"
            value={inputValues.phone}
            onChange={(e) => handleInputChange("phone", e.target.value)}
            placeholder="휴대폰 번호"
          />
          <label
            className={`absolute left-2 transition-all duration-150 ${
              inputValues.phone ? "top-2 text-xs" : "top-4 text-zinc-500"
            }`}>
            {inputValues.phone && "휴대폰 번호"}
          </label>
        </div>

        {/* Address Field */}
        <div className="relative w-full h-16 flex justify-center items-center rounded-xl bg-white border border-gray-300 p-2 text-zinc-800">
          <input
            className={`w-full h-full placeholder:text-zinc-500 focus:outline-none transition-transform duration-150 ${
              inputValues.address ? "transform translate-y-2" : ""
            }`}
            type="text"
            value={inputValues.address}
            onChange={(e) => handleInputChange("address", e.target.value)}
            placeholder="주소"
          />
          <label
            className={`absolute left-2 transition-all duration-150 ${
              inputValues.address ? "top-2 text-xs" : "top-4 text-zinc-500"
            }`}>
            {inputValues.address && "주소"}
          </label>
        </div>

        <button className="w-full h-12 rounded-md bg-[#0046FF] font-bold text-white text-sm">가입</button>

        <div className="flex items-center space-x-2">
          <p className="text-sm font-bold">계정이 있으신가요?</p>
          <button className="text-[#0046FF] font-bold">로그인</button>
        </div>
      </div>
    </div>
  );
};

export default SignUp;
